<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 

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
		function validate(){
			
			var error = false;
			if(document.f.departmentName.value == ""){
				error = true
				var message = '<fmt:message key="department.list_missingparam.error" />';
				document.f.departmentName.focus();
			}
			else if(document.f.adminName.value == ""){
				error = true
				var message = '<fmt:message key="department.detail_missingparam.error" />';
				document.f.adminName.focus();
			}
			if(error){ alert(message); return false; }
  			return true;
		}

		waspOnLoad=function() {
			document.f.name.focus();
		}

		//-->
	</script>  
