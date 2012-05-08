<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18/external/jquery.cookie.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script>

<script type="text/javascript">
function verifyRemove(targetURL){
	if (confirm('<fmt:message key="jobDraft.remove_confirm.label" />')){
		window.location = targetURL;
	}
}

</script>