/* Add event listener for opening and closing details
 * Note that the indicator for showing which row is open is not controlled by DataTables,
 * rather it is done here
 */
function bindInfoClickHandlers(tableElement, dataTable) {
    $('tbody td img.quickView', $(tableElement)).live('click', function () {
        var infoImg = $(this);
        var nTr = infoImg.parents('tr')[0];
        if (dataTable.fnIsOpen(nTr)) {
            $('div.innerDetails', $(nTr).next()[0]).slideUp('slow', function () {
                dataTable.fnClose(nTr);
                infoImg.attr('src', "../images/aNormal.png");
                infoImg.attr('title', "Click for Quick View");
            });
        } else {
            var aData = dataTable.fnGetData(nTr);
            var domainEntity = new DomainEntity(aData);
            jQuery.ajax({
                "dataType": 'json',
                "type": "GET",
                "url": domainEntity.getSelfRestLink(),
                "success": function (data) {
                    var domainEntity = new DomainEntity(data);
                    var nDetailsRow = dataTable.fnOpen(nTr, quickLook(domainEntity), 'details');
                    $(nDetailsRow).addClass($(nDetailsRow).prev().attr('class'));
                    $('div.innerDetails', nDetailsRow).hide();
                    $('div.innerDetails', nDetailsRow).slideDown('slow', function () {
                        infoImg.attr('src', "../images/aInactive.png");
                        infoImg.attr('title', "Click to close Quick View");
                        $("a[rel^='prettyPhoto']", $(nDetailsRow)).prettyPhoto({ social_tools: ''});
                    });
                }
            });
        }
    });
}

function quickLook(domainEntity) {
    var primaryKeyProperty = ConfigurationMetadataService.getPrimaryKeyProperty();
    var primaryKey = domainEntity.getPropertyValue(primaryKeyProperty, 'quickView');

    var fields = ConfigurationMetadataService.getDynamicPropertiesAsArray('quickView');
    var fieldsCount = fields.length;

    var detailsHtmlBlock = '<div id="quickView-' + primaryKey + '" class="innerDetails">';

    if (fieldsCount > 0) {
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
            if (currentFieldIdx == fieldsCount - 1) {
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

function dataTableRESTAdapter(sSource, aoData, fnCallback) {
    if (sSource == null || typeof sSource === 'undefined') {
        return;
    }

    //extract name/value pairs into a simpler map for use later
    var paramMap = {};
    for (var i = 0; i < aoData.length; i++) {
        paramMap[aoData[i].name] = aoData[i].value;
    }

    //page calculations
    var pageSize = paramMap.iDisplayLength;
    var start = paramMap.iDisplayStart;
//    var pageNum = (start == 0) ? 1 : (start / pageSize) + 1; // pageNum is 1 based
    var pageNum = start / pageSize; // pageNum is 1 based

    // extract sort information
    var sortCol = paramMap.iSortCol_0;
    var sortDir = paramMap.sSortDir_0;
    var sortName = paramMap['mDataProp_' + sortCol].replace('original_properties.', '');

    //create new json structure for parameters for REST request
    var restParams = [];
    restParams.push({"name": "size", "value": pageSize});
    restParams.push({"name": "page", "value": pageNum });
    restParams.push({ "name": "sort", "value": sortName + ',' + sortDir });

    jQuery.ajax({
        "dataType": 'json',
        "type": "GET",
        "url": sSource,
        "data": restParams,
        "success": function (data) {
            data.iTotalRecords = data.page.totalElements;
            data.iTotalDisplayRecords = data.page.totalElements;

            getSearcher().onSearchCompleted();

            fnCallback(data);
        }
    });
}