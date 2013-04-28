var FieldValueRenderer = function () {

	function EmptyValueRenderer() {
		this.render = function () {
			return '&nbsp;';
		}
	}

	function UnknownTypeValueRenderer() {
		this.render = function ( field ) {
			return field['value'];
		}
	}

	function StringValueRenderer() {
		this.render = function ( field ) {
			if ( field['value'].length == 0 ) {
				return '&nbsp;';
			}
			return $.trim( field['value'] );
		}
	}

	function NumericValueRenderer() {
		this.render = function ( field ) {
			return field['value'];
		}
	}

	function DateValueRenderer() {
		this.render = function ( field ) {
			return field['value'];
		}
	}

	function FileValueRenderer( targetView ) {
		this.targetView = targetView;

		this.render = function ( field ) {
			if ( !field['fileExists'] ) {
				return '&nbsp;';
			}

			var height = '200';
			if ( this.targetView == 'listView' ) {
				height = '21';
			} else if ( this.targetView == 'quickView' ) {
				height = '150';
			}

			var imageUrl = field['fileUrl'] + '?height=' + height;

			return "<a href='" + field['fileUrl'] + "' title='' rel='prettyPhoto'>" + "<img name='" + field['name'] + "' src='" + imageUrl + "'/></a>";
		}
	}

	function BooleanValueRenderer() {
		this.render = function ( field ) {
			return field['value'] ? 'Yes' : 'No';
		}
	}

	function ArrayValueRenderer() {

		function renderItem( arrayItem ) {
			if ( isDomainObject( arrayItem ) ) {
				return new DomainObjectValueRenderer().renderValue( arrayItem );
			}
			return arrayItem.toString();
		}

		this.render = function ( field ) {
			var fieldValue = field['value'];
			var items = '';
			for ( var arrayIndex in fieldValue ) {
				items += renderItem( fieldValue[arrayIndex] ) + '<br/>';
			}
			return items.length != 0 ? items : '&nbsp;';
		}
	}

	function DomainObjectValueRenderer() {
		this.renderValue = function ( dataValue ) {
			var stringRepresentation = dataValue['stringRepresentation'];
			if ( dataValue['managedDomainType'] ) {
				return "<a href='" + dataValue.links[1].href + "'>" + stringRepresentation + "</a>";
			}
			return stringRepresentation;
		};
		this.render = function ( field ) {
			return this.renderValue( field['value'] );
		}
	}

	function isDomainObject( fieldValue ) {
		return typeof fieldValue === 'object' && fieldValue['stringRepresentation'] !== undefined;
	}

	function createRenderer( field, targetView ) {
		var fieldType = field['type'];
		var fieldValue = field['value'];

		if ( fieldValue instanceof Array ) {
			return new ArrayValueRenderer();
		}

		if ( $.isPlainObject( fieldValue ) && isDomainObject( fieldValue ) ) {
			return new DomainObjectValueRenderer();
		}

		if ( fieldType == 'DATE' ) {
			return new DateValueRenderer();
		}

		if ( fieldType == 'FILE' ) {
			return new FileValueRenderer( targetView );
		}

		if ( fieldType == 'STRING' ) {
			return new StringValueRenderer();
		}

		if ( fieldType == 'NUMBER_INTEGER' || fieldType == 'NUMBER_FLOAT' ) {
			return new NumericValueRenderer();
		}

		if ( fieldType == 'BOOL' ) {
			return new BooleanValueRenderer();
		}

		return new UnknownTypeValueRenderer();
	}

	return {
		render: function ( field, targetView ) {
			if ( field['value'] == null && !field['fileExists'] ) {
				return new EmptyValueRenderer().render();
			}

			return createRenderer( field, targetView ).render( field );
		}
	};
}();