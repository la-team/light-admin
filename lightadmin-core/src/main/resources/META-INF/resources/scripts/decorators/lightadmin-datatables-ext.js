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
                    var nDetailsRow = dataTable.fnOpen(nTr, QuickViewController.handle(domainEntity), 'details');
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

    $(this).attr("data-loaded", false);

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