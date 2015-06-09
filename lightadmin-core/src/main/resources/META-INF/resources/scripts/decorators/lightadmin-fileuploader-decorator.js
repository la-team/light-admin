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
var FileUploaderDecorator = (function () {
    return {
        decorate: function (container, file_input_id, attribute_name, browse_button, file_upload_url) {
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
                    max_file_size: $('#'+container).data("limit")+'mb',
                    mime_types: [
                        {title: "Files", extensions: $('#'+container).data("extensions")}
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
                            errorMessage = 'Selected file ' + (err.file ? err.file.name + ' ' : "") + 'exceeds file size limit of '+$('#'+container).data("limit")+'MB';
                        } else if (errorCode == -601 || errorCode == -700) {
                            errorMessage = "Selected file type is not supported (" + (err.file ? err.file.name : "") + "). Please select a file with extension "+$('#'+container).data("extensions")+".";
                        }

                        jAlert(errorMessage, 'Upload file operation failure');
                        up.refresh();
                    },

                    FileUploaded: function (uploader, file, response) {
                        $('#' + file.id + " b").html("100%");

                        var result = $.parseJSON(response.response);

                        $(file_input_id).val(result['value']);
                    }
                }
            });

            uploader.init();

            return uploader;
        }
    }
}());