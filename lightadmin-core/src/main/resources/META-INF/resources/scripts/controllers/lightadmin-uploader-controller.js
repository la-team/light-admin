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

function FileUploaderController(resourceName, form, attr) {

    var editor = form.find('[name="' + attr + '"]');
    var uploader_container = $(editor).parent('div');
    var plupload_container = $(uploader_container).parent('div');
    var uploader = $(plupload_container).data('plupload');

    var remove_button = $('span.action.remove', uploader_container);
    var add_button = $("span.action.add", uploader_container);

    var field_name = $(editor).attr('name');
    var picture_container_id = field_name + '-picture-container';

    function removeFile(entityId) {
        jConfirm('Are you sure you want to remove this file from server? It won\'t be recoverable!', 'Confirmation Dialog', function (r) {
            if (r) {
                $.ajax({
                    type: 'DELETE',
                    url: ApplicationConfig.getDomainEntityFilePropertyRestUrl(resourceName, entityId, attr),
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
    }

    function loadFilePropertyValue(resourceName, entityId, propertyName) {
        var filePropertyValue = { file_exists: false };
        if (entityId == null || entityId == 0) {
            return filePropertyValue;
        }

        $.ajax({
            url: ApplicationConfig.getDomainEntityFilePropertyValueRestUrl(resourceName, entityId, propertyName),
            dataType: 'json',
            async: false,
            success: function (rawFilePropertyValue) {
                filePropertyValue = rawFilePropertyValue;
            }
        });
        return filePropertyValue;
    }

    return {
        loadFile: function(entityId) {
            return loadFilePropertyValue(resourceName, entityId, attr);
        },
        selectFile: function (entityId, attr_value) {
            var property = ConfigurationMetadataService.getProperty(resourceName, attr, 'formView');

            var valueToRender = FieldValueRenderer.render(property['name'], attr_value, property['type'], 'formView');

            var picture_container_html = "<div id='" + picture_container_id + "' style='margin-top:10px;'>" + valueToRender + "</div>";

            $('span.filename', uploader_container).html('File selected');

            uploader_container.after(picture_container_html);

            add_button.hide();
            remove_button.show();

            remove_button.click(function () {
                removeFile(entityId);
            });
        }
    }
}