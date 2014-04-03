<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#userHook").keyup(function(){getAuthNames();});
     });
      
     function getAuthNames(){        
     	if( $("#userHook").val().length == 1){
	        	$.getJSON("<c:url value='autocomplete/getUserNamesAndLoginForDisplay.do' />", { adminNameFragment: $("#userHook").val() }, function(data) { $("input#userHook").autocomplete(data);} );
     	}
	 }
 </script>
 <script language="JavaScript">
		<!--
		function validate(){
			var error = false;
			var message = '<fmt:message key="sysrole.list_missingparam.error" />';
			if(document.f.adminName.value == ""){
				error = true;
				document.f.userId.focus();
			}
			if(error){ alert(message); return false; }
			return true;
		}
		//-->
	</script> 