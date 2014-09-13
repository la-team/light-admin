<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<script type="text/javascript" src="<light:url value="/scripts/vendor/jquery-1.8.0.min.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/spinner/jquery.mousewheel.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/spinner/ui.spinner.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.ui-1.8.0.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/jquery.wysiwyg.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/wysiwyg.image.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/wysiwyg.link.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wysiwyg/wysiwyg.table.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.js"/>"></script>
<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.orderBars.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.pie.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/excanvas.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/flot/jquery.flot.resize.js"/>"></script>

<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/tables/jquery.dataTables.js"/>"></script>
<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/tables/jquery-dataTables-fnReloadAjax.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/tables/colResizable.min.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/forms.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/autogrowtextarea.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/autotab.js"/>"></script>
<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/forms/jquery.validationEngine-en.js"/>"></script>
<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/forms/jquery.validationEngine.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/jquery.dualListBox.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/forms/chosen.jquery.min.js"/>"></script>
<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/forms/jquery.maskedinput.min.js"/>"></script>
<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/forms/jquery.inputlimiter.min.js"/>"></script>
<script type="text/javascript"
        src="<light:url value="/scripts/vendor/plugins/forms/jquery.tagsinput.min.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/other/calendar.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/other/elfinder.min.js"/>"></script>

<!-- Third party script for BrowserPlus runtime (Google Gears included in Gears runtime now) -->
<script type="text/javascript" src="http://bp.yahooapis.com/2.4.21/browserplus-min.js"></script>
<!-- Load plupload and all it's runtimes and finally the jQuery queue widget -->
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/plupload/js/moxie.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/plupload/js/plupload.dev.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.progress.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.jgrowl.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.tipsy.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.alerts.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.colorpicker.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wizards/jquery.form.wizard.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/wizards/jquery.validate.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.core.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.breadcrumbs.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.collapsible.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.ToTop.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.listnav.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.sourcerer.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.timeentry.min.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/vendor/plugins/ui/jquery.prettyPhoto.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/vendor/charts/chart.js"/>"></script>

<script type="text/javascript">
    $(function () {
        $.ajaxSetup({ cache: false });

        $("#support-ukraine").click(function() {
            document.location.href = "http://wings-phoenix.org.ua/en/donate-instructions";
        });

        $("a.not-implemented").click(function () {
            jAlert('Sorry mate, but this feature is not yet implemented.', 'Coming soon...');
        });

        $("div.custom-resource").each(function () {
            var customResourceContainer = $(this);
            var customResourceServletUrl = "<light:url value='/dynamic/custom'/>";
            var customResourceUrl = $("div.body", customResourceContainer).attr('data-resource-url');
            $.ajax({
                type: 'GET',
                url: customResourceServletUrl + "?resource=" + customResourceUrl,
                success: function (data) {
                    $("div.body", customResourceContainer).html($(data));
                }
            });
        });
    });
</script>