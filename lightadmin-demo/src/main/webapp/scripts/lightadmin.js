DOMAIN_TYPE_METADATA = {};

(function($) {
$.fn.serializeFormJSON = function() {
	function resolveObjectHref(attrVal, attrMetadata) {
		var href = decodeURIComponent(attrMetadata.hrefTemplate).replace('{'+attrMetadata.idAttribute+'}', attrVal);
		return {href : href};
	}
var o = {};
var a = this.serializeArray();
$.each(a, function() {
	var attrVal = this.value || '';
	var attrMetadata = DOMAIN_TYPE_METADATA[this.name];
	if (attrMetadata && attrMetadata.isAssociation) {
		var href = resolveObjectHref(attrVal, attrMetadata);
		if (attrMetadata.isMulti) {
			if (!o[this.name]){
				o[this.name] = [];
			}
			o[this.name].push(href);
		} else {
			o[this.name]= href;
		}
	} else {
		o[this.name] = attrVal;
	}
});
return o;
};
})(jQuery);

function dataTableRESTAdapter( sSource, aoData, fnCallback ) {

	//extract name/value pairs into a simpler map for use later
	var paramMap = {};
	for ( var i = 0; i < aoData.length; i++ ) {
		paramMap[aoData[i].name] = aoData[i].value;
	}

	//page calculations
	var pageSize = paramMap.iDisplayLength;
	var start = paramMap.iDisplayStart;
	var pageNum = (start == 0) ? 1 : (start / pageSize) + 1; // pageNum is 1 based

	// extract sort information
	var sortCol = paramMap.iSortCol_0;
	var sortDir = paramMap.sSortDir_0;
	var sortName = paramMap['mDataProp_' + sortCol];

	//create new json structure for parameters for REST request
	var restParams = new Array();
	restParams.push( {"name":"limit", "value":pageSize} );
	restParams.push( {"name":"page", "value":pageNum } );
	restParams.push( { "name":"sort", "value":sortName } );
	restParams.push( { "name":sortName + ".dir", "value":sortDir } );

	jQuery.ajax( {
					"dataType":'json',
					"type":"GET",
					"url":sSource,
					"data":restParams,
					"success":function ( data ) {
						data.iTotalRecords = data.page.totalElements;
						data.iTotalDisplayRecords = data.page.totalElements;

						activeScope().html(activeScopeName() + ' (' + data.iTotalRecords + ')');

						fnCallback( data );
					}
				} );
}

function renderValue( value ) {
	var strValue = extractStrValue(value);

	strValue = strValue == '' ? '&nbsp;' : strValue;

	if ( value['links'] !== undefined ) {
		var restEntityUrl = value.links[1].href;

		return "<a href='" + restEntityUrl + "'>" + strValue + "</a>";
	}

	return strValue;
}

function extractStrValue( dataValue ) {
	if ( dataValue instanceof Array ) {
		var items = '';
		for (var arrayIndex in dataValue) {
			var arrayItem = dataValue[arrayIndex];
			if ( arrayItem['stringRepresentation'] !== undefined) {
				items += arrayItem['stringRepresentation'] + '<br/>';
			}
		}
		return items;
	}

	if (typeof dataValue === 'object' && dataValue['stringRepresentation'] !== undefined) {
		return dataValue['stringRepresentation'];
	}

	return dataValue;
}

function getPrimaryKey( dataValue ) {
	for (var prop in dataValue) {
		if ((dataValue[prop]['primaryKey'] !== undefined) && (dataValue[prop]['primaryKey'] == true)) {
			return dataValue[prop]['value'];
		}
	}
	return null;
}

function quickLook( aData ) {
	var primaryKey = getPrimaryKey( aData );

	var fieldsCount = Object.keys(aData).length - 2;

	var detailsHtmlBlock = '<div id="quickView-' + primaryKey + '" class="innerDetails">';

	if (fieldsCount > 0) {
		detailsHtmlBlock += '<table cellpadding="0" cellspacing="0" width="100%" class="tableStatic mono">';
		detailsHtmlBlock += '<tbody class="quick-view-data-section">';

		var currentFieldIdx = 1;
		for (var prop in aData) {
			if ( prop != 'links' && prop != 'stringRepresentation') {
				var name = aData[prop]['name'] !== undefined ? aData[prop]['name'] : prop;
				var value = aData[prop]['value'] !== undefined ? aData[prop]['value'] : aData[prop];

				var rowClass = '';
				if ( currentFieldIdx == 1) {
					rowClass = 'noborder';
				}
				if ( currentFieldIdx == fieldsCount ) {
					rowClass = 'last';
				}

				detailsHtmlBlock += '<tr class="' + rowClass +'">';
				detailsHtmlBlock += '<td width="20%" align="right" class="qv-field-name"><strong>' + name +':</strong></td>';
				detailsHtmlBlock += '<td class="qv-field-value">' + renderValue(value) +'</td>';
				detailsHtmlBlock += '</tr">';

				currentFieldIdx++;
			}
		}

		detailsHtmlBlock += '</tbody></table>';
	}
	detailsHtmlBlock += '</div>';

	return detailsHtmlBlock;
}

/* Add event listener for opening and closing details
 * Note that the indicator for showing which row is open is not controlled by DataTables,
 * rather it is done here
 */
function bindInfoClickHandlers( tableElement, dataTable ) {
	$( 'tbody td img.quickView', $(tableElement) ).live( 'click', function () {
		var infoImg = $( this );
		var nTr = infoImg.parents( 'tr' )[0];
		if ( dataTable.fnIsOpen( nTr ) ) {
			$('div.innerDetails', $(nTr).next()[0]).slideUp('slow', function () {
				dataTable.fnClose( nTr );
				infoImg.attr('src', "../images/aNormal.png");
				infoImg.attr('title', "Click for Quick View");
			});
		} else {
			var aData = dataTable.fnGetData( nTr );
			var restEntityUrl = aData.links[0].href;
			jQuery.ajax( {
				"dataType" : 'json',
				"type" : "GET",
				"url" : restEntityUrl + '/unit/quickView',
				"success":function ( data ) {
					var nDetailsRow = dataTable.fnOpen( nTr, quickLook( data ), 'details' );
					$(nDetailsRow).addClass($(nDetailsRow).prev().attr('class'));
					$('div.innerDetails', nDetailsRow).hide();
					$('div.innerDetails', nDetailsRow).slideDown('slow', function () {
						infoImg.attr('src', "../images/aInactive.png");
						infoImg.attr('title', "Click to close Quick View");
					});
				}
			} );
		}
	} );
}

function loadDomainObjectForShowView(showViewSection, restRepoUrl) {
	$.ajax({
			type: 'GET',
			url: restRepoUrl,
			dataType : 'json',
			success : function(data) {
				for (name in data) {
					var field = showViewSection.find('[name="field-' + name + '"]');
					if (field.length > 0) {
						if ($.isPlainObject(data[name].value)) {
							field.html(renderValue(data[name].value));
						} else {
							field.html(data[name].value);
						}
					}
				}
			}
		});
}

var REST_REPO_URL;

function loadDomainObjectForFormView(form, restRepoUrl) {

	function selectOptions(editor, attrMetadata, data) {
		$.each(data, function() {
			selectOption(editor, attrMetadata, this);
		});
	}

	function selectOption(editor, attrMetadata, data) {
		var objectIdData = data[attrMetadata.idAttribute];
		var objectId = $.isPlainObject(objectIdData) ? objectIdData.value : objectIdData;
		if (objectId == null) {
			objectId = '';
		}
		editor.find('option').each(function(index, option) {
			if (option.value == objectId) {
				option.selected = true;
			}
		});
	}

	REST_REPO_URL = restRepoUrl;
	$.ajax({
		type: 'GET',
		url: restRepoUrl + '/unit/formView',
		dataType : 'json',
		success : function(data, textStatus) {
			for (var attr in data) {
				var editor = form.find('[name="' + attr + '"]');
				if (editor.length > 0) {
					var attrVal = data[attr].value;
					var attrMetadata = DOMAIN_TYPE_METADATA[attr];
					if (attrMetadata && attrMetadata.isAssociation) {
						if (attrMetadata.isMulti) {
							selectOptions(editor, attrMetadata, attrVal);
						} else {
							selectOption(editor, attrMetadata, attrVal);
						}
					} else {
						editor.val(attrVal);
					}
				}
			}
			$.uniform.update();
		}
	});
}

function removeDomainObject(entityId, restUrl, callback) {
	$.ajax({
	type: 'DELETE',
	url: restUrl + '/' + entityId,
	contentType: 'application/json',
	dataType : 'json',
	success : function() {
		callback();
	},
	statusCode : {
		409:
		function() {
			jAlert('Something bad happened!', 'Alert');
		}
	}
	});

	return false;
}

function updateDomainObject(domForm) {
	$.each($(domForm).find('[id$=-error]'), function(index, element) {
		$(element).text('');
	});
	var jsonForm = $(domForm).serializeFormJSON();
	$.ajax({
		type: 'PUT',
		url: REST_REPO_URL + '?returnBody=true',
		contentType: 'application/json',
		data: JSON.stringify(jsonForm),
		dataType : 'json',
		success : function(data, textStatus) {
			var link = $.grep(data.links, function(link) {
				return link.rel == 'selfDomainLink';
			})[0];
			window.location = link.href;
		},
		statusCode : {
			400 /* BAD_REQUEST */:
				function(jqXHR) {
					var data = $.parseJSON(jqXHR.responseText);
					var errors = data.errors;
					var errorMessages = '';
					for (var i=0; i<errors.length; i++) {
						var error = errors[i];
						var errorMessage = $('<div/>').text(error.message).html();
						if (!error.field) {
							errorMessages = errorMessages + errorMessage + '<br>';
						}
						var messageDiv = $('#' + error.field + '-error');
						if (messageDiv.length > 0) {
							messageDiv.text(errorMessage);
						}
						var controlGroup = $('#' + error.field + '-control-group');
						if (controlGroup.length > 0) {
							controlGroup.addClass('error');
						}
					}
					if (errorMessages.length > 0) {
						jAlert(errorMessages);
					}
				}
		}
	});

	return false;
}


