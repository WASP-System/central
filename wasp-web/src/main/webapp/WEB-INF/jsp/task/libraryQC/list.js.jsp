<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script>
	
	//added 1-16-15;dubin
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
							alert("<fmt:message key="task.libraryqc_validateCommentAlert.label" />");
							adjacentCommentElement.focus();
							return false;
						}
					}
				}
				else{//neither Passed nor Failed selected in the dropdown box
					if(trimmedAdjacentCommentElementValue != ""){//and there is a comment written in the adjacent textarea
						var sampleId = elementsOnForm[i].getAttribute("id").replace("qcStatus","");
						alert("Please select Passed or Failed for library with a sample ID of " + sampleId + " or completely delete the comment you have written for it.");
						elementsOnForm[i].focus();
						return false;
					}
				}
			}
		}
		if(!atLeastOneDropDownWasSelectedAsPassedOrFailed){//no select boxes were selected and no comments written, so no reason to hit the submit button
			alert("You must select Passed or Failed for at least one library");
			return false;
		}
		if(numberFailed==0){//none Failed, at least one passed
			openWaitDialog();
			return true;
		}
		else if(numberFailed>0){
			var confirmReturnValue =  confirm("Are you sure you wish to fail " + numberFailed + " library(s)?");
			if(confirmReturnValue){
				openWaitDialog();
				return true;
			}
		}		
		return false;		
	}
</script>