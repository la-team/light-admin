var FieldValueRenderer = function () {

	function EmptyValueRenderer() {
		this.render = function () {
			return '&nbsp;';
		}
	}

	function UnknownTypeValueRenderer() {
		this.render = function ( fieldValue ) {
			return fieldValue;
		}
	}

	function StringValueRenderer() {
		this.render = function ( dataValue ) {
			if ( dataValue.length == 0 ) {
				return '&nbsp;';
			}
			return $.trim( dataValue );
		}
	}

	function NumericValueRenderer() {
		this.render = function ( dataValue ) {
			return dataValue;
		}
	}

	function BooleanValueRenderer() {
		this.render = function ( fieldValue ) {
			return fieldValue ? 'Yes' : 'No';
		}
	}

	function ArrayValueRenderer() {
		this.render = function ( fieldValue ) {
			var items = '';
			for ( var arrayIndex in fieldValue ) {
				var arrayItem = fieldValue[arrayIndex];
				if ( arrayItem['stringRepresentation'] !== undefined ) {
					items += arrayItem['stringRepresentation'] + '<br/>';
				}
			}
			return items.length != 0 ? items : '&nbsp;';
		}
	}

	function DomainObjectValueRenderer() {
		this.render = function ( dataValue ) {
			var stringRepresentation = dataValue['stringRepresentation'];

			if ( dataValue['links'] === undefined ) {
				return stringRepresentation;
			}

			return "<a href='" + dataValue.links[1].href + "'>" + stringRepresentation + "</a>";
		}
	}

	function isDomainObject( fieldValue ) {
		return typeof fieldValue === 'object' && fieldValue['stringRepresentation'] !== undefined;
	}

	function createRenderer( fieldValue ) {
		if ( fieldValue instanceof Array ) {
			return new ArrayValueRenderer();
		}

		if ( $.isPlainObject( fieldValue ) && isDomainObject( fieldValue ) ) {
			return new DomainObjectValueRenderer();
		}

		if ( typeof fieldValue == 'string' ) {
			return new StringValueRenderer();
		}

		if ( typeof fieldValue == 'number' ) {
			return new NumericValueRenderer();
		}

		if ( typeof fieldValue == 'boolean' ) {
			return new BooleanValueRenderer();
		}

		return new UnknownTypeValueRenderer();
	}

	return {
		render: function ( fieldValue ) {
			if ( fieldValue == null ) {
				return new EmptyValueRenderer().render();
			}

			return createRenderer( fieldValue ).render( fieldValue );
		}
	};
}();