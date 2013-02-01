<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 
<script>
function validate(){
	
	//alert("test today");return false;
	
	document.f.piEmail.value = document.f.piEmail.value.trim();
	if(document.f.piEmail.value == ""){
		alert("<fmt:message key="lab.joinAnotherLab_piEmailEmpty.error" />");
		document.f.piEmail.focus();
		return false;
	}
	var emailFormat=new RegExp("^\\s*[\\w\\-\\+_]+(\\.[\\w\\-\\+_]+)*\\@[\\w\\-\\+_]+\\.[\\w\\-\\+_]+(\\.[\\w\\-\\+_]+)*\\s*$");
	if(!emailFormat.test(document.f.piEmail.value)){
		alert("<fmt:message key="lab.joinAnotherLab_piEmailFormatError.error" />");
		document.f.piEmail.focus();
		return false;
	}
	return true;	
}
</script>