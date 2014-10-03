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
function DomainEntity(data) {
    this.string_representation = data['string_representation'];
    this.managed_type = data['managed_type'];
    this.primary_key = data['primary_key'];
    this.domain_link = this.managed_type ? data['domain_link']['href'] : null;
    this.links = data['_links'];
    this.original_properties = data['original_properties'];
    this.dynamic_properties = data['dynamic_properties'];

    function getDynamicAssociationValue(domainEntity, propertyMetadata, unitType) {
        var propertyName = propertyMetadata['name'];

        return domainEntity.dynamic_properties[unitType][propertyName];
    }

    /* Too slow to be "true" :) */
    function loadAssociationValueFromServer(domainEntity, propertyMetadata) {
        var associationLink = domainEntity.getAssociationLink(propertyMetadata);
        var propertyType = propertyMetadata['type'];
        var associationValue = null;
        $.ajax({
            url: associationLink,
            dataType: 'json',
            async: false,
            success: function(data) {
                if ($.isEmptyObject(data)) {
                    return null;
                } else if (propertyType == 'ASSOC_MULTI') {
                    associationValue = data['_embedded']['persistentEntityWrappers'];
                } else {
                    associationValue = data;
                }
            }
        });
        return associationValue;
    }

    this.getAssociationLink = function(propertyMetadata) {
        return this.links[propertyMetadata['name']]['href'];
    };

    this.getSelfRestLink = function() {
        return this.links['self']['href'];
    };

    this.getStringRepresentation = function() {
        return this.string_representation;
    };

    this.isManagedType = function() {
        return this.managed_type;
    };

    this.getDomainLink = function() {
        return this.domain_link;
    };

    this.getPrimaryKey = function() {
        return this.primary_key;
    };

    this.getPrimaryKeyValue = function() {
        return this.original_properties[this.getPrimaryKey()];
    };

    this.getPropertyValue = function(propertyMetadata, unitType) {
        var isDynamicProperty = !propertyMetadata['persistable'];
        var propertyName = propertyMetadata['name'];
        var propertyType = propertyMetadata['type'];

        if (!isDynamicProperty && (propertyType == 'ASSOC' || propertyType == 'ASSOC_MULTI')) {
            return getDynamicAssociationValue(this, propertyMetadata, unitType);
        } else if (isDynamicProperty || propertyType == 'FILE') {
            return this.dynamic_properties[unitType][propertyName];
        }
        return this.original_properties[propertyName];
    }
}

function DateTime(dateTimeValue) {

    var date = '';
    var time = '';

    if (dateTimeValue != null && dateTimeValue.indexOf("T") > -1) {
        date = dateTimeValue.split('T')[0];
        time = dateTimeValue.split('T')[1].substr(0, 8);
    }

    this.getDate = function() {
        return date;
    };

    this.getTime = function() {
        return time;
    };
}