<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#login").keyup(function(){getAuthNames();});
     });
      
     function getAuthNames(){        
     	if( $("#login").val().length == 1){
	        	$.getJSON("<wasp:relativeUrl value='autocomplete/getUserNamesAndLoginForDisplay.do' />", { adminNameFragment: $("#login").val() }, function(data) { $("input#login").autocomplete(data);} );
     	}
	    }
 </script>
 <script language="JavaScript">
		<!--
		function validate(){
			var error = false;			
			if(document.f.login.value == ""){
				error = true;
				var message = '<fmt:message key="department.detail_missingparam.error" />';
				document.f.login.focus();
			}
			
			if(error){ alert(message); return false; }
			return true;
		}
		//-->
	</script> 