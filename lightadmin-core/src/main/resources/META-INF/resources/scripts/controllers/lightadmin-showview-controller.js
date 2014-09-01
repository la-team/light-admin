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

var ShowViewController = (function () {

    function loadDomainEntity(resourceName, entityId, callback) {
        $.ajax({
            type: 'GET',
            url: ApplicationConfig.getDomainEntityRestUrl(resourceName, entityId),
            dataType: 'json',
            success: function (data) {
                callback(new DomainEntity(data));
            }
        });
    }

    return {
        handle: function (showViewSection, entityId) {
            var resourceName = ApplicationConfig.RESOURCE_NAME;

            loadDomainEntity(resourceName, entityId, function (domainEntity) {
                var fields = ConfigurationMetadataService.getDynamicPropertiesAsArray(resourceName, 'showView');
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
            });
        }
    }
}());