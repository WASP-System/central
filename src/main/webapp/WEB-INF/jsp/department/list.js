<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <script language="JavaScript">
		<!--
		function validate(){
			
			var error = false;
			var message = '<fmt:message key="department.list_missingparam.error" />';
			if(document.f.name.value == ""){
				error = true
				document.f.name.focus();
			}
			if(error){ alert(message); return false; }
  			return true;
		}

		waspOnLoad=function() {
			document.f.name.focus();
		}

		//-->
	</script>  
