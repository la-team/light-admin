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
        this.render = function (propertyName, propertyValue) {
            return propertyValue;
        }
    }

    function LabelRenderer() {
        this.render = function (propertyName, propertyValue) {
            return propertyValue['label'];
        }
    }

    function StringValueRenderer(targetView) {
        this.targetView = targetView;

        this.render = function (propertyName, propertyValue) {
            if (propertyValue.length == 0) {
                return '&nbsp;';
            }

            if (this.targetView == 'listView') {
                return cutLongText(propertyValue);
            }
            return $.trim(propertyValue);
        }
    }

    function NumericValueRenderer() {
        this.render = function (propertyName, propertyValue) {
            return propertyValue;
        }
    }

    function DateValueRenderer() {
        this.render = function (propertyName, propertyValue) {
            return propertyValue;
        }
    }

    function TimeValueRenderer() {
        this.render = function (propertyName, propertyValue) {
            return propertyValue;
        }
    }

    function DateTimeValueRenderer() {
        this.render = function(propertyName, propertyValue) {
            if (propertyValue != null && propertyValue.indexOf("T") > -1) {
                var date = propertyValue.split('T')[0];
                var time = propertyValue.split('T')[1];

                return date + ' ' + time.substr(0, 8);
            }
            return '';
        }
    }

    function FileValueRenderer(targetView) {
        this.targetView = targetView;

        this.render = function (propertyName, propertyValue) {
            if (!propertyValue['file_exists']) {
                return '&nbsp;';
            }

            var height = '200';
            if (this.targetView == 'listView') {
                height = '21';
            } else if (this.targetView == 'quickView') {
                height = '150';
            }

            var imageUrl = propertyValue['href'] + '?height=' + height;

            return "<a href='" + propertyValue['href'] + "' title='' rel='prettyPhoto'>" + "<img name='" + propertyName + "' src='" + imageUrl + "' height='" + height + "'/></a>";
        }
    }

    function BooleanValueRenderer() {
        this.render = function (propertyName, propertyValue) {
            return propertyValue ? 'Yes' : 'No';
        }
    }

    function ArrayValueRenderer(targetView) {

        this.targetView = targetView;

        function renderItem(propertyName, arrayItem, targetView) {
            if (isDomainObject(arrayItem)) {
                return new DomainObjectValueRenderer(targetView).render(propertyName, arrayItem);
            }
            return arrayItem.toString();
        }

        this.render = function (propertyName, propertyValue) {
            var items = '';
            for (var arrayIndex in propertyValue) {
                items += renderItem(propertyName, propertyValue[arrayIndex], this.targetView) + '<br/>';
            }
            return items.length != 0 ? items : '&nbsp;';
        }
    }

    function DomainObjectValueRenderer(targetView) {
        this.targetView = targetView;

        this.render = function (propertyName, propertyValue) {
            var domainEntity = new DomainEntity(propertyValue);

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

        if (fieldType == 'TIME') {
            return new TimeValueRenderer();
        }

        if (fieldType == 'DATE_TIME') {
            return new DateTimeValueRenderer();
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

            return createRenderer(propertyType, propertyValue, targetView).render(propertyName, propertyValue);
        }
    };
}());
