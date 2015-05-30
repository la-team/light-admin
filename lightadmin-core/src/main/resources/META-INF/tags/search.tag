<%@ tag import="com.google.common.collect.Iterables" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>

<%@ attribute name="filters" required="true" rtexprvalue="true"
              type="org.lightadmin.api.config.unit.FiltersConfigurationUnit" %>

<tiles:importAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="tag_search_filterList" value="<%= Iterables.toArray( filters, org.lightadmin.core.config.domain.filter.FilterMetadata.class ) %>"/>

<c:if test="${not empty tag_search_filterList}">

    <div class="widget">
        <div id="filter-header" class="head closed normal"><h5>Advanced Search</h5></div>
        <div class="body" style="display: none; ">

            <form name="filter-form" class="mainForm">
                <fieldset>
                    <c:forEach var="filter" items="${tag_search_filterList}" varStatus="status">
                        <div class="rowElem ${status.first ? 'noborder' : ''}">
                            <label id="filter-field-name"><strong><c:out value="${light:capitalize(filter.name)}"/>:</strong></label>

                            <div class="formRight">
                                <light-jsp:filter-control domainType="${domainTypeAdministrationConfiguration.domainType}" filter="${filter}" cssClass="input-xlarge"/>
                            </div>
                            <div class="fix"></div>
                        </div>
                    </c:forEach>
                </fieldset>
                <div class="wizNav">
                    <input id="reset-filter" class="basicBtn" type="button" value="Reset"/>
                    <input id="apply-filter" class="blueBtn" type="submit" value="Search"/>
                </div>
            </form>
        </div>
    </div>

    <script type="text/javascript">
        var FILTER_COMPONENT = new FilterComponent('filter-form', getSearcher());

        $(function () {
            $('.closed').collapsible({
                defaultOpen: '',
                cssOpen: 'inactive',
                cssClose: 'normal',
                speed: 200
            });
        });

        $(function() {
            decorateUIControls($("form[name='filter-form']"));
        });
    </script>
</c:if>