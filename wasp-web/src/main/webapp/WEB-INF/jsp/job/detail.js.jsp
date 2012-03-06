<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 

<!-- link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/-->
<!-- script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script-->
<!-- script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script-->

 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#login").keyup(function(){getAuthNames();});
     });
      
     function getAuthNames(){        
     	if( $("#login").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#login").val() }, function(data) { $("input#login").autocomplete(data);} );
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