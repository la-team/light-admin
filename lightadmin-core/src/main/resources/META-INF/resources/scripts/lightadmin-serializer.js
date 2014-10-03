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
(function ($) {
    $.fn.serializeFormJSON = function (resourceName, usePlaceholders) {

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

        function isPartialPropertyField(name) {
            return name.indexOf("_time") > -1;
        }

        var json = {};
        $.each(this.serializeArray(), function () {
            var attrVal = this.value || '';

            var property = ConfigurationMetadataService.getProperty(resourceName, this.name, 'formView');

            if (property == null) {
                if ( isPartialPropertyField(this.name) && attrVal.length != 0) {
                    var main_property_name = this.name.substr(0, this.name.indexOf("_time"));
                    if (json[main_property_name] != null && json[main_property_name].length != 0) {
                        json[main_property_name] = json[main_property_name] + 'T' + attrVal;
                    }
                }
                return;
            }

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

        $.each(ConfigurationMetadataService.getDynamicProperties(resourceName, "formView"), function (attrName, attrMetadata) {
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