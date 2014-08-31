(function ($) {
    $.fn.serializeFormJSON = function (usePlaceholders) {
        function resolveObjectHref(attrVal, attrMetadata) {
            if (attrVal == '') {
                if (usePlaceholders) {
                    attrVal = 'NULL';
                } else {
                    return null;
                }
            }
            var rest_link_template = attrMetadata['rest_link']['href'];
            return decodeURIComponent(rest_link_template).replace('{idPlaceholder}', attrVal);
        }

        var json = {};
        $.each(this.serializeArray(), function () {
            var attrVal = this.value || '';
            var property = ConfigurationMetadataService.getProperty(this.name, 'formView');
            var propertyType = property['type'];

            if (propertyType.indexOf('ASSOC') == 0) {
                var href = resolveObjectHref(attrVal, property);
                if (propertyType == 'ASSOC_MULTI') {
                    if (!json[this.name]) {
                        json[this.name] = [];
                    }
                    json[this.name].push(href);
                } else {
                    json[this.name] = href;
                }
            } else {
                json[this.name] = attrVal;
            }
        });

        $.each(ConfigurationMetadataService.getDynamicProperties("formView"), function (attrName, attrMetadata) {
            var attrVal = json[attrName];
            if (attrVal != undefined && attrVal != '') {
                return;
            }
            switch (attrMetadata.type) {
                case 'ASSOC_MULTI':
                    json[attrName] = [];
                    break;
                case 'BOOL':
                    json[attrName] = false;
                    break;
                case 'DATE':
                    if (usePlaceholders) {
                        json[attrName] = -377743392000001;
                    }
                    break;
            }
        });
        return json;
    };
})(jQuery);

function getPrimaryKey(dataValue) {
    for (var prop in dataValue) {
        if ((dataValue[prop]['primaryKey'] !== undefined) && (dataValue[prop]['primaryKey'] == true)) {
            return dataValue[prop]['value'];
        }
    }
    return null;
}

function loadDomainObjectForShowView(showViewSection, entityId) {
    $.ajax({
        type: 'GET',
        url: ApplicationConfig.getDomainEntityRestUrl(entityId),
        dataType: 'json',
        success: function (data) {
            var domainEntity = new DomainEntity(data);
            var fields = ConfigurationMetadataService.getDynamicPropertiesAsArray('showView');
            for (var fieldIdx in fields) {
                var property = fields[fieldIdx];

                var propertyName = property['name'];
                var propertyType = property['type'];

                var propertyValue = domainEntity.getPropertyValue(property, 'showView');

                var field = showViewSection.find('[name="field-' + propertyName + '"]');
                if (field.length > 0) {
                    field.html(FieldValueRenderer.render(propertyName, propertyValue, propertyType, 'showView'));
                }
            }
            $("a[rel^='prettyPhoto']").prettyPhoto({ social_tools: ''});
        }
    });
}

function loadDomainObjectForFormView(form, entityId) {

    function selectOptions(editor, data) {
        $.each(data, function () {
            selectOption(editor, this);
        });
    }

    function selectOption(editor, data) {
        var domainEntity = new DomainEntity(data);
        var objectIdData = domainEntity.getPrimaryKeyValue();
        var objectId = $.isPlainObject(objectIdData) ? objectIdData.value : objectIdData;
        if (objectId == null) {
            objectId = '';
        }
        editor.find('option').each(function (index, option) {
            if (option.value == objectId) {
                option.selected = true;
            }
        });
    }

    $.ajax({
        type: 'GET',
        url: ApplicationConfig.getDomainEntityRestUrl(entityId),
        dataType: 'json',
        success: function (data, textStatus) {
            var domainEntity = new DomainEntity(data);
            var fields = ConfigurationMetadataService.getDynamicPropertiesAsArray('formView');

            for (var fieldIdx in fields) {
                var property = fields[fieldIdx];
                var propertyName = property['name'];
                var propertyType = property['type'];

                var editor = form.find('[name="' + propertyName + '"]');
                if (editor.length == 0) {
                    continue;
                }

                var propertyValue = domainEntity.getPropertyValue(property, 'formView');
                if (propertyValue == null) {
                    continue;
                }

                switch (propertyType) {
                    case 'ASSOC':
                        selectOption(editor, propertyValue);
                        break;
                    case 'ASSOC_MULTI':
                        selectOptions(editor, propertyValue);
                        break;
                    case 'BOOL':
                        editor.prop('checked', propertyValue);
                        break;
                    case 'FILE':
                        var fileSelected = propertyValue.length > 0;
                        if (fileSelected) {
//                            selectFileFieldValue(form, attr, content[attr], restRepoUrl);
                        }
                        editor.val(propertyValue.toString());
                        break;
                    case 'STRING':
                        if (editor.hasClass('wysiwyg')) {
                            editor.wysiwyg("insertHtml", propertyValue);
                            break;
                        }
                    default:
                        if (editor.prop('tagName') == 'SELECT' && editor.find("option[value='" + propertyValue + "']").length <= 0) {
                            editor.append($('<option>', {
                                value: propertyValue,
                                text: propertyValue
                            }));
                        }
                        editor.val(propertyValue.toString());
                        break;
                }
            }

            $.uniform.update();
            $(".chzn-select", $(form)).trigger("liszt:updated");

            $("a[rel^='prettyPhoto']", $(form)).prettyPhoto({ social_tools: ''});
        },
        statusCode: {
            400 /* BAD_REQUEST */: function (jqXHR) {
                var data = $.parseJSON(jqXHR.responseText);
                var errors = data.errors;
                var errorMessages = '';
                for (var i = 0; i < errors.length; i++) {
                    errorMessages += $('<div/>').text(errors[i].message).html();
                }
                if (errorMessages.length > 0) {
                    showFailureMessageNote(errorMessages);
                }
            }
        }
    });
}

function selectFileFieldValue(form, attr, attr_value, restRepoUrl) {
    var editor = form.find('[name="' + attr + '"]');
    var field_name = $(editor).attr('name');
    var url = restRepoUrl + '/' + attr + '/file';

    var uploader_container = $(editor).parent('div');

    var remove_button = $('span.action.remove', uploader_container);
    var add_button = $("span.action.add", uploader_container);

    var plupload_container = $(uploader_container).parent('div');

    var uploader = $(plupload_container).data('plupload');

    var picture_container_id = field_name + '-picture-container';

    var picture_container_html = "<div id='" + picture_container_id + "' style='margin-top:10px;'>" + FieldValueRenderer.render(attr_value, 'formView') + "</div>";

    $('span.filename', uploader_container).html('File selected');

    uploader_container.after(picture_container_html);

    add_button.hide();
    remove_button.show();

    remove_button.click(function () {
        jConfirm('Are you sure you want to remove this file from server? It won\'t be recoverable!', 'Confirmation Dialog', function (r) {
            if (r) {
                $.ajax({
                    type: 'DELETE',
                    url: url,
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function () {
                        $(editor).val('');
                        $('#' + picture_container_id).remove();
                        remove_button.hide();
                        add_button.show();

                        if (uploader.files.length > 0) {
                            $.each(uploader.files, function () {
                                uploader.removeFile(this);
                            });
                            uploader.refresh();
                        }

                        $('span.filename', uploader_container).html('No file selected');
                    },
                    statusCode: {
                        409: function (jqXHR) {
                            var errorMessage = $.parseJSON(jqXHR.responseText)['message'];
                            jAlert(errorMessage, 'Remove operation failure');
                        }
                    }
                });
            }
        });
    });
}

function removeDomainObject(entityId, restUrl, callback) {
    $.ajax({
        type: 'DELETE',
        url: restUrl + '/' + entityId,
        contentType: 'application/json',
        dataType: 'json',
        success: function () {
            callback();
        },
        statusCode: {
            409: function (jqXHR) {
                var errorMessage = $.parseJSON(jqXHR.responseText)['message'];
                jAlert(errorMessage, 'Remove operation failure');
            }
        }
    });

    return false;
}

function saveDomainObject(domForm, successCallback) {
    return saveOrUpdateDomainObject(domForm, false, successCallback, 'POST');
}

function updateDomainObject(domForm, successCallback) {
    return saveOrUpdateDomainObject(domForm, false, successCallback, 'PUT');
}

function saveOrUpdateDomainObject(domForm, usePlaceholders, successCallback, method) {
    $.each($(domForm).find('[id$=-error]'), function (index, element) {
        $(element).text('');
    });

    var jsonForm = $(domForm).serializeFormJSON(usePlaceholders);
    var jsonData = JSON.stringify(jsonForm);

    var primaryKey = ConfigurationMetadataService.getPrimaryKeyProperty()['name'];

    var url = method == 'POST' ? ApplicationConfig.DOMAIN_ENTITY_BASE_REST_URL : ApplicationConfig.getDomainEntityRestUrl(jsonForm[primaryKey]);
    $.ajax({
        type: method,
        url: url,
        contentType: 'application/json',
        data: jsonData,
        dataType: 'json',
        success: function (data, textStatus) {
            successCallback(data);
        },
        statusCode: {
            400 /* BAD_REQUEST */: function (jqXHR) {
                var data = $.parseJSON(jqXHR.responseText);
                var errors = data.errors;
                var errorMessages = '';
                for (var i = 0; i < errors.length; i++) {
                    var error = errors[i];
                    var errorMessage = $('<div/>').text(error['message']).html();

                    if (!error['property']) {
                        errorMessages += errorMessage + '<br>';
                    } else {
                        var messageDiv = $('#' + error['property'] + '-error', $(domForm));
                        if (messageDiv.length > 0) {
                            messageDiv.text(errorMessage);
                        }
                        var controlGroup = $('#' + error['property'] + '-control-group', $(domForm));
                        if (controlGroup.length > 0) {
                            controlGroup.addClass('error');
                        }
                    }
                }
                if (errorMessages.length > 0) {
                    showFailureMessageNote(errorMessages, $(domForm));
                }
            },
            409: function (jqXHR) {
                var data = $.parseJSON(jqXHR.responseText);
                showFailureMessageNote(data.message, $(domForm));
            }
        }
    });

    return false;
}

function showSuccessMessageNote(message) {
    showMessageNote(message, 'nSuccess', undefined);
}

function showFailureMessageNote(message, beforeElement) {
    showMessageNote(message, 'nFailure', beforeElement);
}

function showMessageNote(message, messageTypeClass, beforeElement) {
    $(".nNote").remove();

    var noteHtml = "<div class='nNote " + messageTypeClass + "'><p>" + message + "</p></div>";

    if (typeof beforeElement == 'undefined') {
        $('.breadCrumbHolder').after(noteHtml);
    } else {
        $(beforeElement).before(noteHtml);
    }

    $('.nNote').click(function () {
        $(this).fadeTo(200, 0.00, function () {
            $(this).slideUp(300, function () {
                $(this).remove();
            });
        });
    });
}

function modelFormViewDialog(elementToBind, domainTypeName, attributeName, domainFormViewUrl) {
    function wrapDialogBlock(dialog_id, dialog_title, data) {
        return "<div id='" + dialog_id + "' title='" + dialog_title + "'>" + data + "</div>";
    }

    var dialog_url = domainFormViewUrl + "/create-dialog";
    var dialog_id = "dialog-" + attributeName;
    var dialog_selector = '#' + dialog_id;
    var form_id = domainTypeName + "-dialog-form";
    var form_selector = '#' + form_id;
    var dialog_title = "Create dialog";

    var parent_form = $(elementToBind).parents('form')[0];

    var managed_select = $("select[name='" + attributeName + "']", parent_form);

    $(elementToBind).click(function () {
        if ($(dialog_selector).length) {
            $(dialog_selector).remove();
        }

        $.get(dialog_url, function (data) {
            var html = wrapDialogBlock(dialog_id, dialog_title, data);

            $(parent_form).after($(html));

//            $('div.formRight', $(dialog_selector)).css({'margin': '0px'});

            $(dialog_selector).dialog({
                autoOpen: false,
                width: 650,
                modal: true,
                close: function (event, ui) {
                    $(dialog_selector).remove();
                }
            });

            $(":button[name='cancel-changes']", $(dialog_selector)).click(function () {
                $(dialog_selector).dialog("close");
            });

            $(form_selector, $(dialog_selector)).submit(function () {
                return saveDomainObject($(form_selector, $(dialog_selector)), function (data) {
                    var id = getPrimaryKey(data);
                    var name = data['stringRepresentation'];

                    managed_select.append("<option value='" + id + "'>" + name + "</option>");

                    managed_select.find('option').each(function (index, option) {
                        if (option.value == id) {
                            option.selected = true;
                        }
                    });

                    $.uniform.update();
                    $(".chzn-select", $(parent_form)).trigger("liszt:updated");

                    $(dialog_selector).dialog("close");
                });
            });

            $(":button[name='save-changes']", $(dialog_selector)).click(function () {
                $(form_selector, $(dialog_selector)).submit();
            });

            $(dialog_selector).dialog("open");
        });
    });
}

function decorateFileUploader(container, file_input_id, attribute_name, browse_button, file_upload_url) {
    var container_selector = '#' + container;

    var uploader = new plupload.Uploader({
        runtimes: 'html5,flash,silverlight,html4',
        url: file_upload_url,
        container: document.getElementById(container),
        browse_button: document.getElementById(browse_button),
        file_data_name: attribute_name,
        unique_names: true,
        flash_swf_url: 'vendor/plugins/plupload/js/Moxie.swf',
        silverlight_xap_url: 'vendor/plugins/plupload/js/Moxie.xap',
        filters: {
            max_file_size: '10mb',
            mime_types: [
                {title: "Image files", extensions: "jpg,jpeg,png"}
            ]
        },

        init: {
            FilesAdded: function (up, files) {
                plupload.each(files, function (file) {
                    var filesContainer = $('span.filename', $(container_selector));
                    filesContainer.html($('<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b>' + '</div>'));
                });
                up.refresh();

                uploader.start();
            },

            UploadProgress: function (up, file) {
                $('#' + file.id + " b").html(file.percent + "%");
            },

            Error: function (up, err) {
                var errorCode = err.code;
                var errorMessage = err.message + (err.file ? " File: " + err.file.name : "");

                if (errorCode == -600) {
                    errorMessage = 'Selected file ' + (err.file ? err.file.name + ' ' : "") + 'exceeds file size limit of 10MB';
                } else if (errorCode == -601 || errorCode == -700) {
                    errorMessage = "Selected file type is not supported (" + (err.file ? err.file.name : "") + "). Please select JPG or PNG file.";
                }

                jAlert(errorMessage, 'Upload file operation failure');
                up.refresh();
            },

            FileUploaded: function (uploader, file, response) {
                $('#' + file.id + " b").html("100%");

                var result = $.parseJSON(response.response);

                $(file_input_id).val(result['content']['fileContent']);
            }
        }
    });

    uploader.init();

    $(container_selector).data('plupload', uploader);
}
