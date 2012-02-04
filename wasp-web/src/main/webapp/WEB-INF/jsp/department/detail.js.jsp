
<link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.16.custom.min.js"></script> 


 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#adminName").keyup(function(){getAuthNames();});
     });
      
     function getAuthNames(){        
     	if( $("#adminName").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#adminName").val() }, function(data) { $("input#adminName").autocomplete(data);} );
     	}
	    }
 </script>
 <script language="JavaScript">
		<!--
		function validate(param){
			var error = false;			
			if(param == 'createAdmin' && document.f.adminName.value == ""){
				error = true;
				var message = '<fmt:message key="department.detail_missingparam.error" />';
				document.f.adminName.focus();
			}
			else if(param == 'updateDept' && document.update_form.name.value == ""){
				error = true;
				var message = '<fmt:message key="department.detail_update_missingparam.error" />';
				document.update_form.name.focus();
			}
			if(error){ alert(message); return false; }
			return true;
		}
		//-->
	</script> 