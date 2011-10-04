<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <script language="JavaScript">
		<!--
		function validate(){
			//alert("test123"); return false;
			var error = false;
			var message = '<fmt:message key="user.mypassword_missingparam.error" />';
			if(document.f.oldpassword.value == "" || document.f.newpassword1.value == ""|| document.f.newpassword2.value == "" ){
				error = true;
			}
			if(error){ alert(message); return false; }
  			return true;
		}

		waspOnLoad=function() {
			document.f.oldpassword.focus();
		}

		//-->
	</script>  
	 
  