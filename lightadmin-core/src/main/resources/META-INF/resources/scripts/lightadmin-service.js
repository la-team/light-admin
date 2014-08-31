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