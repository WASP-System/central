<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script>
	function validate(theform){
		if(!theform.qcStatus[0].checked && !theform.qcStatus[1].checked){
			alert("<fmt:message key="task.sampleqc_validatePassFailAlert.label" />");
			return false;
		}
		var commentObj = theform.comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(theform.qcStatus[1].checked && trimmedCommentValue.length==0){
			alert("<fmt:message key="task.sampleqc_validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
			return false;
		}
		openWaitDialog();
		return true;
	}
	function selectedFail(formId){
		var commentObj = document.getElementById(formId).comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(trimmedCommentValue.length==0){
			alert("<fmt:message key="task.sampleqc_validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
		}


	}
	
	//added 1-15-15;dubin
	function set(formId, status){
		var elementsOnForm = document.getElementById(formId).elements;// for some reason, thisForm.getElementsByTagName("select") does not work 
		for(var i = 0; i < elementsOnForm.length; i++){
			if(elementsOnForm[i].tagName=="SELECT"){
				elementsOnForm[i].value = status;
			}
		}
	}
	function validateqcform(theform){
		var elementsOnForm = theform.elements;// for some reason, thisForm.getElementsByTagName("select") does not work
		var atLeastOneDropDownWasSelectedAsPassedOrFailed = false;
		var numberFailed = 0;		
		for(var i = 0; i < elementsOnForm.length; i++){
			if(elementsOnForm[i].tagName=="SELECT"){				
				//elementsOnForm[i] is the select box
				var adjacentCommentElementId = elementsOnForm[i].getAttribute("id").replace("qcStatus","comment"); 
				var adjacentCommentElement = document.getElementById(adjacentCommentElementId);
				var adjacentCommentElementValue = adjacentCommentElement.value;
				var trimmedAdjacentCommentElementValue = adjacentCommentElementValue.replace(/^\s+|\s+$/g, "");
				
				if(elementsOnForm[i].value=="PASSED" || elementsOnForm[i].value=="FAILED"){
					atLeastOneDropDownWasSelectedAsPassedOrFailed = true;
					if(elementsOnForm[i].value=="FAILED"){
						numberFailed++;
						if(trimmedAdjacentCommentElementValue == ""){//FAILED but no comment written
							alert("<fmt:message key="task.sampleqc_validateCommentAlert.label" />");
							adjacentCommentElement.focus();
							return false;
						}
					}
				}
				else{//neither Passed nor Failed selected in the dropdown box
					if(trimmedAdjacentCommentElementValue != ""){//and there is a comment written in the adjacent textarea
						var sampleId = elementsOnForm[i].getAttribute("id").replace("qcStatus","");
						alert("Please select Passed or Failed for sample ID " + sampleId + " or completely delete the comment you have written for it.");
						elementsOnForm[i].focus();
						return false;
					}
				}
			}
		}
		if(!atLeastOneDropDownWasSelectedAsPassedOrFailed){//no select boxes were selected and no comments written, so no reason to hit the submit button
			alert("You must select Passed or Failed for at least one sample");
			return false;
		}
		if(numberFailed==0){//none Failed, at least one passed
			openWaitDialog();
			return true;
			///////////////////////////return false;//must remove
		}
		else if(numberFailed>0){
			var confirmReturnValue =  confirm("Are you sure you wish to fail " + numberFailed + " sample(s)?");
			if(confirmReturnValue){
				openWaitDialog();
				return true;
			}
		}
		
		return false;
		
	}
</script>