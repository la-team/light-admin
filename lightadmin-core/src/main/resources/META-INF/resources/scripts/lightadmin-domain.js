function DomainEntity(data) {
    this.string_representation = data['string_representation'];
    this.managed_type = data['managed_type'];
    this.primary_key = data['primary_key'];
    this.domain_link = this.managed_type ? data['domain_link']['href'] : null;
    this.links = data['_links'];
    this.original_properties = data['original_properties'];
    this.dynamic_properties = data['dynamic_properties'];

    function loadAssociationValue(domainEntity, propertyMetadata) {
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
            return loadAssociationValue(this, propertyMetadata);
        } else if (isDynamicProperty || propertyType == 'FILE') {
            return this.dynamic_properties[unitType][propertyName];
        }
        return this.original_properties[propertyName];
    }
}