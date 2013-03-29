<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<script type="text/javascript" src="<light:url value="/scripts/vendor/jquery-1.8.0.min.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/spinner/jquery.mousewheel.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/spinner/ui.spinner.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/jquery.wysiwyg.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/wysiwyg.image.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/wysiwyg.link.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/wysiwyg.table.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.orderBars.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.pie.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/excanvas.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.resize.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/tables/jquery-dataTables.min.js"/>"></script>
<script type="text/javascript"
		src="<light:url value="/scripts/vendor/plugins/tables/jquery-dataTables-fnReloadAjax.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/tables/colResizable.min.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/forms.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/autogrowtextarea.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/autotab.js"/>"></script>
<script type="text/javascript"
		src="<light:url value="/scripts/vendor/plugins/forms/jquery.validationEngine-en.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/jquery.validationEngine.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/jquery.dualListBox.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/chosen.jquery.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/jquery.maskedinput.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/jquery.inputlimiter.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/jquery.tagsinput.min.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/other/calendar.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/other/elfinder.min.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/uploader/plupload.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/uploader/plupload.html5.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/uploader/plupload.html4.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/uploader/jquery.plupload.queue.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.progress.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.jgrowl.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.tipsy.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.alerts.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.colorpicker.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wizards/jquery.form.wizard.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wizards/jquery.validate.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.core.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.datepicker.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.breadcrumbs.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.collapsible.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.ToTop.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.listnav.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.sourcerer.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.timeentry.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.prettyPhoto.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/lightadmin-search.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-renderer.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/charts/chart.js"/>"></script>

<script type="text/javascript">
	$( function () {
		$( "a.not-implemented" ).click( function () {
			jAlert( 'Sorry mate, but this function is currently in development.', 'Coming soon...' );
		} );
	} );
</script>