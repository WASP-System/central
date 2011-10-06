<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.6.2.js"></script>
<link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.16.custom.min.js"></script> 

<!-- link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/-->
<!-- script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script-->
<!-- script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script-->

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
			var message = '<fmt:message key="department.detail_missingparam.error" />';
			if(document.f.adminName.value == ""){
				error = true;
				document.f.adminName.focus();
			}
			if(error){ alert(message); return false; }
			return true;
		}
		//-->
	</script> 