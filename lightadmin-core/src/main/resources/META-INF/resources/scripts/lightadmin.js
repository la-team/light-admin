DOMAIN_TYPE_METADATA = {};

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
            var href = decodeURIComponent(attrMetadata.hrefTemplate).replace('{' + attrMetadata.idAttribute + '}', attrVal);
            return {href: href};
        }

        var json = {};
        $.each(this.serializeArray(), function () {
            var attrVal = this.value || '';
            var attrMetadata = DOMAIN_TYPE_METADATA[this.name];
            var attrType = attrMetadata ? attrMetadata.type : 'UNKNOWN';
            if (attrType.indexOf('ASSOC') == 0) {
                var href = resolveObjectHref(attrVal, attrMetadata);
                if (attrType == 'ASSOC_MULTI') {
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
        $.each(DOMAIN_TYPE_METADATA, function (attrName, attrMetadata) {
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

function dataTableRESTAdapter(sSource, aoData, fnCallback) {
    if (sSource == null || typeof sSource === 'undefined') {
        return;
    }

    //extract name/value pairs into a simpler map for use later
    var paramMap = {};
    for (var i = 0; i < aoData.length; i++) {
        paramMap[aoData[i].name] = aoData[i].value;
    }

    //page calculations
    var pageSize = paramMap.iDisplayLength;
    var start = paramMap.iDisplayStart;
    var pageNum = (start == 0) ? 1 : (start / pageSize) + 1; // pageNum is 1 based

    // extract sort information
    var sortCol = paramMap.iSortCol_0;
    var sortDir = paramMap.sSortDir_0;
    var sortName = paramMap['mDataProp_' + sortCol];

    //create new json structure for parameters for REST request
    var restParams = [];
    restParams.push({"name": "limit", "value": pageSize});
    restParams.push({"name": "page", "value": pageNum });
    restParams.push({ "name": "sort", "value": sortName });
    restParams.push({ "name": sortName + ".dir", "value": sortDir });

    jQuery.ajax({
        "dataType": 'json',
        "type": "GET",
        "url": sSource,
        "data": restParams,
        "success": function (data) {
            data.iTotalRecords = data.page.totalElements;
            data.iTotalDisplayRecords = data.page.totalElements;

            getSearcher().onSearchCompleted();

            fnCallback(data);
        }
    });
}

function getPrimaryKey(dataValue) {
    for (var prop in dataValue) {
        if ((dataValue[prop]['primaryKey'] !== undefined) && (dataValue[prop]['primaryKey'] == true)) {
            return dataValue[prop]['value'];
        }
    }
    return null;
}

function quickLook(aData) {
    var primaryKey = getPrimaryKey(aData);

    var fieldsCount = Object.keys(aData).length - 3;

    var detailsHtmlBlock = '<div id="quickView-' + primaryKey + '" class="innerDetails">';

    if (fieldsCount > 0) {
        detailsHtmlBlock += '<table cellpadding="0" cellspacing="0" width="100%" class="tableStatic mono">';
        detailsHtmlBlock += '<tbody class="quick-view-data-section">';

        var currentFieldIdx = 1;
        for (var prop in aData) {
            if (prop != 'links' && prop != 'stringRepresentation' && prop != 'managedDomainType') {
                var rowClass = '';
                if (currentFieldIdx == 1) {
                    rowClass = 'noborder';
                }
                if (currentFieldIdx == fieldsCount) {
                    rowClass = 'last';
                }

                detailsHtmlBlock += '<tr class="' + rowClass + '">';
                detailsHtmlBlock += '<td width="20%" align="right" class="qv-field-name"><strong>' + aData[prop]['title'] + ':</strong></td>';
                detailsHtmlBlock += '<td class="qv-field-value">' + FieldValueRenderer.render(aData[prop], 'quickView') + '</td>';
                detailsHtmlBlock += '</tr">';

                currentFieldIdx++;
            }
        }

        detailsHtmlBlock += '</tbody></table>';
    }
    detailsHtmlBlock += '</div>';

    return detailsHtmlBlock;
}

/* Add event listener for opening and closing details
 * Note that the indicator for showing which row is open is not controlled by DataTables,
 * rather it is done here
 */
function bindInfoClickHandlers(tableElement, dataTable) {
    $('tbody td img.quickView', $(tableElement)).live('click', function () {
        var infoImg = $(this);
        var nTr = infoImg.parents('tr')[0];
        if (dataTable.fnIsOpen(nTr)) {
            $('div.innerDetails', $(nTr).next()[0]).slideUp('slow', function () {
                dataTable.fnClose(nTr);
                infoImg.attr('src', "../images/aNormal.png");
                infoImg.attr('title', "Click for Quick View");
            });
        } else {
            var aData = dataTable.fnGetData(nTr);
            var restEntityUrl = aData.links[0].href;
            jQuery.ajax({
                "dataType": 'json',
                "type": "GET",
                "url": restEntityUrl + '/unit/quickView',
                "success": function (data) {
                    var nDetailsRow = dataTable.fnOpen(nTr, quickLook(data), 'details');
                    $(nDetailsRow).addClass($(nDetailsRow).prev().attr('class'));
                    $('div.innerDetails', nDetailsRow).hide();
                    $('div.innerDetails', nDetailsRow).slideDown('slow', function () {
                        infoImg.attr('src', "../images/aInactive.png");
                        infoImg.attr('title', "Click to close Quick View");
                        $("a[rel^='prettyPhoto']", $(nDetailsRow)).prettyPhoto({ social_tools: ''});
                    });
                }
            });
        }
    });
}

function loadDomainObjectForShowView(showViewSection, restRepoUrl) {
    $.ajax({
        type: 'GET',
        url: restRepoUrl,
        dataType: 'json',
        success: function (data) {
            for (name in data) {
                var field = showViewSection.find('[name="field-' + name + '"]');
                if (field.length > 0) {
                    field.html(FieldValueRenderer.render(data[name], 'showView'));
                }
            }
            $("a[rel^='prettyPhoto']").prettyPhoto({ social_tools: ''});
        }
    });
}

var REST_REPO_URL;

function loadDomainObjectForFormView(form, restRepoUrl) {

    function selectOptions(editor, attrMetadata, data) {
        $.each(data, function () {
            selectOption(editor, attrMetadata, this);
        });
    }

    function selectOption(editor, attrMetadata, data) {
        var objectIdData = data[attrMetadata.idAttribute];
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

    REST_REPO_URL = restRepoUrl;
    $.ajax({
        type: 'GET',
        url: restRepoUrl + '/unit/formView',
        dataType: 'json',
        success: function (data, textStatus) {
            for (var attr in data) {
                var editor = form.find('[name="' + attr + '"]');
                if (editor.length > 0) {
                    var attrVal = data[attr].value;

                    if (attrVal == null) {
                        continue;
                    }

                    var attrMetadata = DOMAIN_TYPE_METADATA[attr];
                    var attrType = attrMetadata ? attrMetadata.type : 'UNKNOWN';

                    switch (attrType) {
                        case 'ASSOC':
                            selectOption(editor, attrMetadata, attrVal);
                            break;
                        case 'ASSOC_MULTI':
                            selectOptions(editor, attrMetadata, attrVal);
                            break;
                        case 'BOOL':
                            editor.prop('checked', attrVal);
                            break;
                        case 'FILE':
                            $(editor).parent('div').parent('div').prepend(FieldValueRenderer.render(data[attr], 'formView'));
                            if (attrVal.length > 0) {
                                $(editor).parent('div').prepend(removeFileLink(editor, restRepoUrl + '/' + attr + '/file'));
                            }

                            editor.val(attrVal.toString());
                            break;
                        case 'STRING':
                            if (editor.hasClass('wysiwyg')) {
                                editor.wysiwyg("insertHtml", attrVal);
                            } else {
                                editor.val(attrVal.toString());
                            }
                            break;
                        default:
                            editor.val(attrVal.toString());
                            break;
                    }
                }
            }
            $.uniform.update();
            $(".chzn-select").trigger("liszt:updated");

            $("a[rel^='prettyPhoto']").prettyPhoto({ social_tools: ''});
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

function removeFileLink(editor, filePropertyUrl) {
    var fieldName = $(editor).attr('name');

    var link = "<a href='#" + fieldName + "'>[Remove File]</a>";
    var jLink = $(link);
    jLink.click(function () {
        $.ajax({
            type: 'DELETE',
            url: filePropertyUrl,
            contentType: 'application/json',
            dataType: 'json',
            success: function () {
                $(editor).val('');
                $("img[name='" + fieldName + "']").remove();
                jLink.remove();
            },
            statusCode: {
                409: function (jqXHR, textStatus, errorThrown) {
                    var errorMessage = $.parseJSON(jqXHR.responseText)['message'];
                    jAlert(errorMessage, 'Remove operation failure');
                }
            }
        });
    });
    return jLink;
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
            409: function (jqXHR, textStatus, errorThrown) {
                var errorMessage = $.parseJSON(jqXHR.responseText)['message'];
                jAlert(errorMessage, 'Remove operation failure');
            }
        }
    });

    return false;
}

function saveDomainObject(domForm) {
    return saveOrUpdateDomainObject(domForm, false);
}

function updateDomainObject(domForm) {
    return saveOrUpdateDomainObject(domForm, true);
}

function saveOrUpdateDomainObject(domForm, usePlaceholders) {
    $.each($(domForm).find('[id$=-error]'), function (index, element) {
        $(element).text('');
    });
    var jsonForm = $(domForm).serializeFormJSON(usePlaceholders);
    $.ajax({
        type: 'PUT',
        url: REST_REPO_URL + '?returnBody=true',
        contentType: 'application/json',
        data: JSON.stringify(jsonForm),
        dataType: 'json',
        success: function (data, textStatus) {
            var link = $.grep(data.links, function (link) {
                return link.rel == 'selfDomainLink';
            })[0];
            window.location = link.href + '?updateSuccess=true';
        },
        statusCode: {
            400 /* BAD_REQUEST */: function (jqXHR) {
                var data = $.parseJSON(jqXHR.responseText);
                var errors = data.errors;
                var errorMessages = '';
                for (var i = 0; i < errors.length; i++) {
                    var error = errors[i];
                    var errorMessage = $('<div/>').text(error.message).html();
                    if (!error.field) {
                        errorMessages += errorMessage + '<br>';
                    }
                    var messageDiv = $('#' + error.field + '-error');
                    if (messageDiv.length > 0) {
                        messageDiv.text(errorMessage);
                    }
                    var controlGroup = $('#' + error.field + '-control-group');
                    if (controlGroup.length > 0) {
                        controlGroup.addClass('error');
                    }
                }
                if (errorMessages.length > 0) {
                    showFailureMessageNote(errorMessages);
                }
            },
            409: function (jqXHR) {
                var data = $.parseJSON(jqXHR.responseText);
                showFailureMessageNote(data.message);
            }
        }
    });

    return false;
}

function showSuccessMessageNote(message) {
    showMessageNote(message, 'nSuccess');
}

function showFailureMessageNote(message) {
    showMessageNote(message, 'nFailure');
}

function showMessageNote(message, messageTypeClass) {
    $('.nNote').remove();

    var noteHtml = "<div class='nNote " + messageTypeClass + "'><p>" + message + "</p></div>";

    $('.breadCrumbHolder').after(noteHtml);

    $('.nNote').click(function () {
        $(this).fadeTo(200, 0.00, function () {
            $(this).slideUp(300, function () {
                $(this).remove();
            });
        });
    });
}
