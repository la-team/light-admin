/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                    var domainEntity = new DomainEntity(data);
                    var id = domainEntity.getPrimaryKeyValue();
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