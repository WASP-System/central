<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<%-- this redirect is necessary because sometimes a page is laoded into a div and a login timeout --%>
<%-- requires us to refresh the whole window not just place the login page within the contents of the div --%>
<script>
	window.location='<wasp:relativeUrl value="auth/login.do" />';
</script>

