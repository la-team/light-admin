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
var ConfigurationMetadataService = (function () {

    function initialize() {
        $.ajax({
            url: ApplicationConfig.DOMAIN_ENTITY_METADATA_REST_URL,
            dataType: 'json',
            async: false,
            success: function(data) {
                name = data['name'];
                managed_type = data['managed_type'];
                dynamic_properties = data['dynamic_properties'];
                original_properties = data['original_properties'];
            }
        });
    }

    var name = '';
    var managed_type = '';
    var dynamic_properties = {};
    var original_properties = {};

    initialize();

    return {
        getName: function () {
            return name;
        },
        isManagedType: function () {
            return managed_type;
        },
        getPrimaryKeyProperty: function() {
            var result = null;
            $.each(this.getOriginalProperties(), function(key, value) {
                if (value['primaryKey']) {
                    result = value;
                }
            });
            return result;
        },
        getDynamicProperties: function(unitType) {
            return dynamic_properties[unitType];
        },
        getOriginalProperties: function() {
            return original_properties;
        },
        getProperty: function(name, unitType) {
            var original_property = original_properties[name];
            return original_property ? original_property : dynamic_properties[unitType][name];
        },
        getDynamicPropertiesAsArray: function(unitType) {
            var result = [];
            $.each(this.getDynamicProperties(unitType), function(key, value) {
                result.push(value);
            });
            return result;
        }
    }
}());