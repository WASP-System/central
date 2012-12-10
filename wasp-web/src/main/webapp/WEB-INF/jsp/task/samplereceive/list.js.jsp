<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 

<script>
/* no longer used
	function validate(theform){
				
		//alert("test"); return false;
		//var radio = getElementById(theform.receivedStatus);
		if(!theform.receivedStatus[0].checked && !theform.receivedStatus[1].checked){
			alert("<fmt:message key="task.samplereceive_validateAlert.label" />");
			return false;
		}	
		return true;
	}
*/ 
	function set(elementName, status){
		var selectArray = document.getElementsByName(elementName);
		var i = 0;
		for(i=0; i < selectArray.length; i++){
			selectArray[i].value = status;
		}
	}
</script>