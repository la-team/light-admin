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

var QuickViewController = (function () {

    function renderView(domainEntity, fields, primaryKeyProperty) {
        var primaryKey = domainEntity.getPropertyValue(primaryKeyProperty, 'quickView');
        var detailsHtmlBlock = '<div id="quickView-' + primaryKey + '" class="innerDetails">';

        if (fields.length > 0) {
            detailsHtmlBlock += '<table cellpadding="0" cellspacing="0" width="100%" class="tableStatic mono">';
            detailsHtmlBlock += '<tbody class="quick-view-data-section">';

            var currentFieldIdx = 0;
            for (var prop in fields) {
                var property = fields[prop];

                var propertyName = property['name'];
                var propertyType = property['type'];
                var propertyTitle = property['title'];
                var propertyValue = domainEntity.getPropertyValue(property, 'quickView');

                var rowClass = '';
                if (currentFieldIdx == 0) {
                    rowClass = 'noborder';
                }
                if (currentFieldIdx == fields.length - 1) {
                    rowClass = 'last';
                }

                detailsHtmlBlock += '<tr class="' + rowClass + '">';
                detailsHtmlBlock += '<td width="20%" align="right" class="qv-field-name"><strong>' + propertyTitle + ':</strong></td>';
                detailsHtmlBlock += '<td class="qv-field-value">' + FieldValueRenderer.render(propertyName, propertyValue, propertyType, 'quickView') + '</td>';
                detailsHtmlBlock += '</tr">';

                currentFieldIdx++;
            }

            detailsHtmlBlock += '</tbody></table>';
        }
        detailsHtmlBlock += '</div>';

        return detailsHtmlBlock;
    }

    return {
        handle: function (domainEntity) {
            var resourceName = ApplicationConfig.RESOURCE_NAME;
            var primaryKeyProperty = ConfigurationMetadataService.getPrimaryKeyProperty(resourceName);
            var fields = ConfigurationMetadataService.getDynamicPropertiesAsArray(resourceName, 'quickView');

            return renderView(domainEntity, fields, primaryKeyProperty);
        }
    }
}());