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

var ModalDialogController = (function () {

    function wrapDialogBlock(dialog_id, dialog_title, data) {
        return "<div id='" + dialog_id + "' title='" + dialog_title + "'>" + data + "</div>";
    }

    function successCallback(domainEntity, dialog_selector, managed_select, parent_form) {
        var id = domainEntity.getPrimaryKeyValue();
        var name = domainEntity.getStringRepresentation();

        managed_select.append("<option value='" + id + "'>" + name + "</option>");

        managed_select.find('option').each(function (index, option) {
            if (option.value == id) {
                option.selected = true;
            }
        });

        $.uniform.update();
        $(".chzn-select", $(parent_form)).trigger("liszt:updated");

        $(dialog_selector).dialog("close");
    };

    return {
        show: function (resourceName, attributeName, elementToBind) {
            var dialog_url = ApplicationConfig.getDomainEntityCollectionUrl(resourceName) + "/create-dialog";

            var dialog_id = "dialog-" + attributeName;
            var dialog_selector = '#' + dialog_id;
            var form_id = resourceName + "-dialog-form";
            var form_selector = '#' + form_id;
            var dialog_title = "Create dialog";

            var parent_form = $(elementToBind).parents('form')[0];

            var managed_select = $("select[name='" + attributeName + "'], select[name='" + attributeName + "-n2mall']", parent_form);

            $(elementToBind).click(function () {
                if ($(dialog_selector).length) {
                    $(dialog_selector).remove();
                }

                $.get(dialog_url, function (html_content) {
                    var html = wrapDialogBlock(dialog_id, dialog_title, html_content);

                    $(parent_form).after($(html));

                    $(dialog_selector).dialog({
                        autoOpen: false,
                        width: 650,
                        modal: true,
                        close: function (event, ui) {
                            $(dialog_selector).remove();
                        }
                    });

                    $(form_selector, $(dialog_selector)).submit(function () {
                        return new FormViewController(resourceName).saveDomainEntity($(form_selector, $(dialog_selector)), function(domainEntity) {
                            successCallback(domainEntity, dialog_selector, managed_select, parent_form);
                        });
                    });

                    $(":button[name='cancel-changes']", $(dialog_selector)).click(function () {
                        $(dialog_selector).dialog("close");
                    });

                    $(":button[name='save-changes']", $(dialog_selector)).click(function () {
                        $(form_selector, $(dialog_selector)).submit();
                    });

                    $(dialog_selector).dialog("open");
                });
            });
        }
    }
}());