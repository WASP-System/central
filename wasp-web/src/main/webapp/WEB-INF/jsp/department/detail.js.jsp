<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#adminName").keyup(function(){getAuthNames();});
     });
      
     function getAuthNames(){        
     	if( $("#adminName").val().length == 1){
	        	$.getJSON("<c:url value='autocomplete/getUserNamesAndLoginForDisplay.do' />", { adminNameFragment: $("#adminName").val() }, function(data) { $("input#adminName").autocomplete(data);} );
     	}
	    }
 </script>
 <script type="text/javascript">
		<!--
		function validate(param){
			var error = false;
			var message = "";
			if(param == 'createAdmin' && document.f.adminName.value == ""){
				error = true;
				message = '<fmt:message key="department.detail_missingparam.error" />';
				document.f.adminName.focus();
			}
			else if(param == 'updateDept' && document.update_form.name.value == ""){
				error = true;
				message = '<fmt:message key="department.detail_update_missingparam.error" />';
				document.update_form.name.focus();
			}
			if(error){ alert(message); return false; }
			return true;
		}
		//-->
	</script> 