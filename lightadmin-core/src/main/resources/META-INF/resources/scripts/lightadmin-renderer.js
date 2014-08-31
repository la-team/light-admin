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
var FieldValueRenderer = (function () {

    function cutLongText(text) {
        var strValue = $.trim(text);
        //		if ( strValue.length > 50 ) {
        //			return strValue.substr( 0, 47 ) + '...';
        //		}
        return strValue;
    }

    function EmptyValueRenderer() {
        this.render = function () {
            return '&nbsp;';
        }
    }

    function UnknownTypeValueRenderer() {
        this.render = function (field) {
            return field;
        }
    }

    function LabelRenderer() {
        this.render = function (field) {
            return field['label'];
        }
    }

    function StringValueRenderer(targetView) {
        this.targetView = targetView;

        this.render = function (field) {
            if (field.length == 0) {
                return '&nbsp;';
            }

            if (this.targetView == 'listView') {
                return cutLongText(field);
            }
            return $.trim(field);
        }
    }

    function NumericValueRenderer() {
        this.render = function (field) {
            return field;
        }
    }

    function DateValueRenderer() {
        this.render = function (field) {
            return field;
        }
    }

    function FileValueRenderer(targetView) {
        this.targetView = targetView;

        this.render = function (field) {
            if (!field['fileExists']) {
                return '&nbsp;';
            }

            var height = '200';
            if (this.targetView == 'listView') {
                height = '21';
            } else if (this.targetView == 'quickView') {
                height = '150';
            }

            var imageUrl = field['fileUrl'] + '?height=' + height;

            return "<a href='" + field['fileUrl'] + "' title='' rel='prettyPhoto'>" + "<img name='" + field['name'] + "' src='" + imageUrl + "' height='" + height + "'/></a>";
        }
    }

    function BooleanValueRenderer() {
        this.render = function (field) {
            return field ? 'Yes' : 'No';
        }
    }

    function ArrayValueRenderer(targetView) {

        this.targetView = targetView;

        function renderItem(arrayItem, targetView) {
            if (isDomainObject(arrayItem)) {
                return new DomainObjectValueRenderer(targetView).render(arrayItem);
            }
            return arrayItem.toString();
        }

        this.render = function (field) {
            var fieldValue = field;
            var items = '';
            for (var arrayIndex in fieldValue) {
                items += renderItem(fieldValue[arrayIndex], this.targetView) + '<br/>';
            }
            return items.length != 0 ? items : '&nbsp;';
        }
    }

    function DomainObjectValueRenderer(targetView) {
        this.targetView = targetView;

        this.render = function (dataValue) {
            var domainEntity = new DomainEntity(dataValue);

            var valueToRender = $.trim(domainEntity.getStringRepresentation());
            if (this.targetView == 'listView') {
                valueToRender = cutLongText(domainEntity.getStringRepresentation());
            }

            if (domainEntity.isManagedType()) {
                return "<a href='" + domainEntity.getDomainLink() + "'>" + valueToRender + "</a>";
            }

            return valueToRender;
        };
    }

    function isDomainObject(fieldValue) {
        return $.isPlainObject(fieldValue) && fieldValue['string_representation'] !== undefined;
    }

    function createRenderer(propertyType, propertyValue, targetView) {
        var fieldType = propertyType;
        var fieldValue = propertyValue;
        var fieldLabel = undefined; //TODO: ???

        if (fieldLabel != undefined) {
            return new LabelRenderer();
        }

        if (fieldValue instanceof Array) {
            return new ArrayValueRenderer(targetView);
        }

        if ($.isPlainObject(fieldValue) && isDomainObject(fieldValue)) {
            return new DomainObjectValueRenderer(targetView);
        }

        if (fieldType == 'DATE') {
            return new DateValueRenderer();
        }

        if (fieldType == 'FILE') {
            return new FileValueRenderer(targetView);
        }

        if (fieldType == 'STRING') {
            return new StringValueRenderer(targetView);
        }

        if (fieldType == 'NUMBER_INTEGER' || fieldType == 'NUMBER_FLOAT') {
            return new NumericValueRenderer();
        }

        if (fieldType == 'BOOL') {
            return new BooleanValueRenderer();
        }

        return new UnknownTypeValueRenderer();
    }

    return {
        render: function (propertyName, propertyValue, propertyType, targetView) {
            if (propertyValue == null) {
                return new EmptyValueRenderer().render();
            }

            if (propertyType == 'FILE' && !propertyValue['file_exists']) {
                return new EmptyValueRenderer().render();
            }

            return createRenderer(propertyType, propertyValue, targetView).render(propertyValue);
        }
    };
}());
