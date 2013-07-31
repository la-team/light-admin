<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:useAttribute name="domainTypeEntityMetadata"/>

<tiles:useAttribute name="entitySingularName"/>
<tiles:useAttribute name="entityPluralName"/>

<tiles:useAttribute name="fields"/>

<light:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeAdministrationConfiguration)}" scope="page"/>
<light:url var="domainRestBaseUrl" value="${light:domainRestBaseUrl(domainTypeAdministrationConfiguration)}" scope="page"/>

<div class="title">
    <h5><c:out value="Create ${light:capitalize(light:cutLongText(entitySingularName))}"/></h5>
</div>

<light-jsp:breadcrumb>
    <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entityPluralName))}" link="${domainBaseUrl}"/>
    <light-jsp:breadcrumb-item name="${light:capitalize(light:cutLongText(entitySingularName))}"/>
</light-jsp:breadcrumb>

<form id="editForm" onsubmit="return saveDomainObject(this)" class="mainForm">
    <div class="widget">
        <div class="head"><h5 class="iCreate">
            <c:out value="${light:capitalize(light:cutLongText(entitySingularName))}"/></h5></div>
        <fieldset>
            <c:forEach var="fieldEntry" items="${fields}" varStatus="status">
                <c:if test="${!fieldEntry.generatedValue}">
                    <div id="${fieldEntry.uuid}-control-group" class="rowElem ${status.first ? 'noborder' : ''}">
                        <label><c:out value="${light:capitalize(fieldEntry.name)}"/>:</label>

                        <div class="formRight">
                            <light-jsp:edit-control fieldMetadata="${fieldEntry}" cssClass="input-xlarge" errorCssClass="error"/>
                        </div>
                        <div class="fix"></div>
                    </div>
                </c:if>
            </c:forEach>
        </fieldset>
        <div class="wizNav">
            <input id="cancel-changes" class="basicBtn" value="Cancel" type="button" onclick="history.back();">
            <input id="save-changes" class="blueBtn" value="Save" type="submit">
        </div>
    </div>
</form>

<script type="text/javascript">
    $(function () {
        $(".chzn-select").chosen();

        $("select, input:checkbox, input:radio, input:file").uniform();

        $(".input-date").datepicker({
            autoSize: true,
            appendText: '(YYYY-MM-DD)',
            dateFormat: 'yy-mm-dd'
        });
        $(".input-date").mask("9999-99-99");

        $('.wysiwyg').wysiwyg({
            iFrameClass: "wysiwyg-input",
            controls: {
                bold: { visible: true },
                italic: { visible: true },
                underline: { visible: true },
                strikeThrough: { visible: false },

                justifyLeft: { visible: true },
                justifyCenter: { visible: true },
                justifyRight: { visible: true },
                justifyFull: { visible: true },

                indent: { visible: true },
                outdent: { visible: true },

                subscript: { visible: false },
                superscript: { visible: false },

                undo: { visible: true },
                redo: { visible: true },

                insertOrderedList: { visible: true },
                insertUnorderedList: { visible: true },
                insertHorizontalRule: { visible: false },

                h1: {
                    visible: true,
                    className: 'h1',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h1>' : 'h1',
                    tags: ['h1'],
                    tooltip: 'Header 1'
                },
                h2: {
                    visible: true,
                    className: 'h2',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h2>' : 'h2',
                    tags: ['h2'],
                    tooltip: 'Header 2'
                },
                h3: {
                    visible: true,
                    className: 'h3',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h3>' : 'h3',
                    tags: ['h3'],
                    tooltip: 'Header 3'
                },
                h4: {
                    visible: true,
                    className: 'h4',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h4>' : 'h4',
                    tags: ['h4'],
                    tooltip: 'Header 4'
                },
                h5: {
                    visible: true,
                    className: 'h5',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h5>' : 'h5',
                    tags: ['h5'],
                    tooltip: 'Header 5'
                },
                h6: {
                    visible: true,
                    className: 'h6',
                    command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
                    arguments: ($.browser.msie || $.browser.safari) ? '<h6>' : 'h6',
                    tags: ['h6'],
                    tooltip: 'Header 6'
                },

                cut: { visible: true },
                copy: { visible: true },
                paste: { visible: true },
                html: { visible: true },
                increaseFontSize: { visible: false },
                decreaseFontSize: { visible: false }
            }
        });

        DOMAIN_TYPE_METADATA = <light:domain-type-metadata-json domainTypeMetadata="${domainTypeEntityMetadata}"/>;
        REST_REPO_URL = "${domainRestBaseUrl}";
    });
</script>
