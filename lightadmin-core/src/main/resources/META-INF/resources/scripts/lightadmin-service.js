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

    function ResourceMetadata(rawResourceMetadata) {
        this.name = rawResourceMetadata['name'];
        this.managed_type = rawResourceMetadata['managed_type'];
        this.dynamic_properties = rawResourceMetadata['dynamic_properties'];
        this.original_properties = rawResourceMetadata['original_properties'];
    }

    function loadResourceMetadata(resourceName) {
        var resourceMetadata = {};
        $.ajax({
            url: ApplicationConfig.getDomainEntityMetadataRestUrl(resourceName),
            dataType: 'json',
            async: false,
            success: function (rawResourceMetadata) {
                resourceMetadata = new ResourceMetadata(rawResourceMetadata);
            }
        });
        return resourceMetadata;
    }

    var resourcesMetada = {};

    return {
        getResourceMetadata: function (resourceName) {
            if (resourceName in resourcesMetada) {
                return resourcesMetada[resourceName];
            }
            resourcesMetada[resourceName] = loadResourceMetadata(resourceName);
            return resourcesMetada[resourceName];
        },
        getPrimaryKeyProperty: function (resourceName) {
            var result = null;
            $.each(this.getOriginalProperties(resourceName), function (key, value) {
                if (value['primaryKey']) {
                    result = value;
                }
            });
            return result;
        },
        getDynamicProperties: function (resourceName, unitType) {
            return this.getResourceMetadata(resourceName).dynamic_properties[unitType];
        },
        getOriginalProperties: function (resourceName) {
            return this.getResourceMetadata(resourceName).original_properties;
        },
        getProperty: function (resourceName, name, unitType) {
            var original_property = this.getResourceMetadata(resourceName).original_properties[name];
            if (original_property) {
                return original_property;
            }
            return this.getResourceMetadata(resourceName).dynamic_properties[unitType][name];
        },
        getDynamicPropertiesAsArray: function (resourceName, unitType) {
            var result = [];
            $.each(this.getDynamicProperties(resourceName, unitType), function (key, value) {
                result.push(value);
            });
            return result;
        }
    }
}());