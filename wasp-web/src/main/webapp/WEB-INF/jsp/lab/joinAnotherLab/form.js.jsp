<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 
<script>
function validate(){
	
	//alert("test today");return false;
	
	document.f.piLogin.value = document.f.piLogin.value.trim();
	if(document.f.piLogin.value == ""){
		alert("<fmt:message key="lab.joinAnotherLab_piLoginEmpty.error" />");
		document.f.piLogin.focus();
		return false;
	}
	return true;	
}
</script>