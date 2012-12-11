<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 

<script>

	function validate(thisForm){
		var elementsOnForm = thisForm.elements;  // for some reason, thisForm.getElementsByTagName("select") does not work 
		var atLeastOneSelectBoxOnFormIsSelected = false;
		for(var i = 0; i < elementsOnForm.length; i++){
			if(elementsOnForm[i].tagName=="SELECT"){
				if(elementsOnForm[i].value != ""){
					atLeastOneSelectBoxOnFormIsSelected = true;
					break;
				}
			}
		}
		if(atLeastOneSelectBoxOnFormIsSelected==false){
			alert("<fmt:message key="task.samplereceive_validateAlert.label" />");
			return false;
		}
		return true;
	}
	function set(formId, status){
		var elementsOnForm = document.getElementById(formId).elements;// for some reason, thisForm.getElementsByTagName("select") does not work 
		for(var i = 0; i < elementsOnForm.length; i++){
			if(elementsOnForm[i].tagName=="SELECT"){
				elementsOnForm[i].value = status;
			}
		}
	}

</script>