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

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */

function FormViewController(resourceName) {

    return {
        saveDomainEntity: function (domForm, successCallback) {
            var saveOrUpdateAction = new SaveOrUpdateDomainEntityAction(resourceName, domForm, false);

            return saveOrUpdateAction.perform('POST', successCallback);
        },
        updateDomainEntity: function (domForm, successCallback) {
            var saveOrUpdateAction = new SaveOrUpdateDomainEntityAction(resourceName, domForm, false);

            return saveOrUpdateAction.perform('PUT', successCallback);
        },
        removeDomainEntity: function (entityId, successCallback) {
            return new RemoveDomainEntityAction(resourceName).perform(entityId, successCallback);
        },
        loadDomainEntity: function (entityId, domForm) {
            return new LoadDomainEntityAction(resourceName).perform(entityId, domForm);
        }
    }
}

function RemoveDomainEntityAction(resourceName) {
    return {
        perform: function (entityId, successCallback) {
            $.ajax({
                type: 'DELETE',
                url: ApplicationConfig.getDomainEntityRestUrl(resourceName, entityId),
                contentType: 'application/json',
                dataType: 'json',
                success: successCallback,
                statusCode: {
                    409: function (jqXHR) {
                        var errorMessage = $.parseJSON(jqXHR.responseText)['message'];
                        jAlert(errorMessage, 'Remove operation failure');
                    }
                }
            });
            return false;
        }
    }
}

function SaveOrUpdateDomainEntityAction(resourceName, domForm, usePlaceholders) {

    function collectErrorMessages(domForm, errors) {
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
        return errorMessages;
    }

    function resetErrors(domForm) {
        $.each($(domForm).find('[id$=-error]'), function (index, element) {
            $(element).text('');
        });
    }

    return {
        perform: function (method, successCallback) {
            var primaryKey = ConfigurationMetadataService.getPrimaryKeyProperty(resourceName)['name'];
            var jsonForm = $(domForm).serializeFormJSON(resourceName, usePlaceholders);
            var jsonData = JSON.stringify(jsonForm);

            var url = method == 'POST' ? ApplicationConfig.getDomainEntityCollectionRestUrl(resourceName) : ApplicationConfig.getDomainEntityRestUrl(resourceName, jsonForm[primaryKey]);

            resetErrors(domForm);

            $.ajax({
                type: method,
                url: url,
                contentType: 'application/json',
                data: jsonData,
                dataType: 'json',
                success: function (data) {
                    successCallback(new DomainEntity(data));
                },
                statusCode: {
                    400: function (jqXHR) {
                        var data = $.parseJSON(jqXHR.responseText);
                        var errorMessages = collectErrorMessages(domForm, data.errors);
                        if (errorMessages.length > 0) {
                            NotificationController.showFailureMessageNote(errorMessages, $(domForm));
                        }
                    },
                    409: function (jqXHR) {
                        var data = $.parseJSON(jqXHR.responseText);
                        NotificationController.showFailureMessageNote(data.message, $(domForm));
                    }
                }
            });

            return false;
        }
    }
}

function LoadDomainEntityAction(resourceName) {

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

    function prepareForm(domainEntity, form) {
        var fields = ConfigurationMetadataService.getDynamicPropertiesAsArray(resourceName, 'formView');

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
                    var fileSelected = propertyValue['file_exists'];
                    var fileUploaderController = new FileUploaderController(resourceName, form, propertyName);
                    if (fileSelected) {
                        fileUploaderController.selectFile(domainEntity.getPrimaryKeyValue(), propertyValue);
                    }
                    var filePropertyValue = fileUploaderController.loadFile(domainEntity.getPrimaryKeyValue());
                    editor.val(filePropertyValue['value']);
                    break;
                case 'DATE_TIME':
                    var timeEditor = form.find('[name="' + propertyName + '_time"]');

                    var dateTime = new DateTime(propertyValue.toString());
                    editor.val(dateTime.getDate());
                    timeEditor.val(dateTime.getTime());

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
    }

    function collectErrorMessages(errors) {
        var errorMessages = '';
        for (var i = 0; i < errors.length; i++) {
            errorMessages += $('<div/>').text(errors[i].message).html();
        }
    }

    function getDomainEntityRestUrl(resourceName, entityId) {
        if (entityId == null || entityId == 0) {
            return ApplicationConfig.getNewDomainEntityRestUrl(resourceName);
        }
        return ApplicationConfig.getDomainEntityRestUrl(resourceName, entityId);
    }

    return {
        perform: function (entityId, form) {
            $.ajax({
                type: 'GET',
                url: getDomainEntityRestUrl(resourceName, entityId),
                dataType: 'json',
                success: function (data) {
                    prepareForm(new DomainEntity(data), form);
                },
                statusCode: {
                    400 /* BAD_REQUEST */: function (jqXHR) {
                        var data = $.parseJSON(jqXHR.responseText);
                        var errorMessages = collectErrorMessages(data.errors);
                        if (errorMessages.length > 0) {
                            NotificationController.showFailureMessageNote(errorMessages);
                        }
                    }
                }
            });
            return false;
        }
    }
}