<%@ tag import="com.google.common.collect.Iterables" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" tagdir="/WEB-INF/tags" %>

<%@ attribute name="filters" required="true" rtexprvalue="true" type="org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit"%>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="tag_search_filterList" value="<%= Iterables.toArray( filters, org.lightadmin.core.config.domain.filter.FilterMetadata.class ) %>"/>

<c:if test="${not empty tag_search_filterList}">
	<div class="widget">
		<div id="filter-header" class="head closed normal"><h5>Advanced Search</h5></div>
		<div class="body" style="display: none; ">

			<form name="filter-form" class="mainForm">
					<fieldset>
						<c:forEach var="filter" items="${tag_search_filterList}" varStatus="status">
							<div class="rowElem ${status.first ? 'noborder' : ''}">
								<label><c:out value="${light:capitalize(filter.attributeMetadata.name)}"/>:</label>
								<div class="formRight">
									<form:edit-control attributeMetadata="${filter.attributeMetadata}" cssClass="input-xlarge" errorCssClass="error"/>
								</div>
								<div class="fix"></div>
							</div>
						</c:forEach>
					</fieldset>
					<div class="wizNav">
						<input id="reset-filter" class="basicBtn" type="reset" value="Reset"/>
						<input id="apply-filter" class="blueBtn" type="submit" value="Search"/>
					</div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			$('.closed').collapsible({
				 defaultOpen: '',
				 cssOpen: 'inactive',
				 cssClose: 'normal',
				 speed: 200
			});

			$("#reset-filter" ).click(function() {
				$.uniform.update();
				searchScope(activeScopeName());
			});

			$("form[name='filter-form']").submit(function(event) {
				event.preventDefault();
				var filter_criteria = $("form[name='filter-form']" ).serialize();
				searchScope(activeScopeName(), filter_criteria );
			});
		});
	</script>
</c:if>