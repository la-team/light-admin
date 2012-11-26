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

						 fnCallback( data );
					 }
				 } );
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

function quickLook( aData ) {
	var detailsHtmlBlock = '<div class="innerDetails"><dl class="dl-horizontal">';

	for (var prop in aData) {
		if ( prop != 'links' && prop != 'stringRepresentation') {
			var name = aData[prop]['name'] !== undefined ? aData[prop]['name'] : prop;
			var value = aData[prop]['value'] !== undefined ? aData[prop]['value'] : aData[prop];
			detailsHtmlBlock += '<dt>' + name + '</dt>';
			value = extractStrValue( value );
			detailsHtmlBlock += '<dd>' + ( value == '' ? '&nbsp;' : value ) + '</dd>';
		}
	}
	detailsHtmlBlock += '</dl></div>';

	return detailsHtmlBlock;
}

/* Add event listener for opening and closing details
 * Note that the indicator for showing which row is open is not controlled by DataTables,
 * rather it is done here
 */
function bindInfoClickHandlers( tableElement, dataTable ) {
	$( 'tbody td img', $(tableElement) ).live( 'click', function () {
		var infoImg = $( this );
		var nTr = infoImg.parents( 'tr' )[0];
		if ( dataTable.fnIsOpen( nTr ) ) {
			$('div.innerDetails', $(nTr).next()[0]).slideUp('slow', function () {
				dataTable.fnClose( nTr );
				infoImg.attr('src', "../images/details_open.png");
			});
		} else {
			var aData = dataTable.fnGetData( nTr );
			var restEntityUrl = aData.links[0].href;
			jQuery.ajax( {
				 "dataType" : 'json',
				 "type" : "GET",
				 "url" : restEntityUrl,
				 "success":function ( data ) {
					 var nDetailsRow = dataTable.fnOpen( nTr, quickLook( data ), 'details' );
					 $('div.innerDetails', nDetailsRow).hide();
					 $('div.innerDetails', nDetailsRow).slideDown('slow', function () {
						 infoImg.attr('src', "../images/details_close.png");
					 });
				 }
			} );
		}
	} );
}