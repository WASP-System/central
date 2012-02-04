<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.6.2.js"></script>
<link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.16.custom.min.js"></script> 

<script>
	function validate(theform){
				
		//alert("test"); return false;
		//var radio = getElementById(theform.receivedStatus);
		if(!theform.receivedStatus[0].checked && !theform.receivedStatus[1].checked){
			alert("Please select either Sample Received or Never Coming");
			return false;
		}	
		return true;
	}
</script>