<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:importAttribute name="lightAdminConfiguration" ignore="true"/>

<!-- Footer -->
<div id="footer">
    <div class="wrapper">
        <span><a href="http://lightadmin.org" title="LightAdmin"><spring:message code="application.copyright"/>.</a> All rights reserved.</span>
    </div>
</div>

<c:if test="${lightAdminConfiguration.demoMode}">

<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-41784690-1']);
    _gaq.push(['_trackPageview']);

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>

<!-- Yandex.Metrika counter -->
<script type="text/javascript">(function (d, w, c) {
    (w[c] = w[c] || []).push(function () {
        try {
            w.yaCounter21714952 = new Ya.Metrika({id: 21714952, webvisor: true, clickmap: true, trackLinks: true, accurateTrackBounce: true});
        } catch (e) {
        }
    });
    var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () {
        n.parentNode.insertBefore(s, n);
    };
    s.type = "text/javascript";
    s.async = true;
    s.src = (d.location.protocol == "https:" ? "https:" : "http:") + "//mc.yandex.ru/metrika/watch.js";
    if (w.opera == "[object Opera]") {
        d.addEventListener("DOMContentLoaded", f, false);
    } else {
        f();
    }
})(document, window, "yandex_metrika_callbacks");</script>
<noscript>
    <div><img src="//mc.yandex.ru/watch/21714952" style="position:absolute; left:-9999px;" alt=""/></div>
</noscript>
<!-- /Yandex.Metrika counter -->

</c:if>