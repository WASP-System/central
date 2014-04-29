<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
<script>

	var STATUS_PASSED = "PASSED";
	var STATUS_FAILED = "FAILED";

	function setAll(formId, action){
		var theForm = document.getElementById(formId);
		var elements = theForm.elements;
		/* for diagnostics, please save:
		var str = '';
		for(var i = 0; i < elements.length; i++)
	    {
	        str += "<b>Type:</b>" + elements[i].type + "&nbsp&nbsp";
	        str += "<b>Name:</b>" + elements[i].name + "&nbsp;&nbsp;";
	        str += "<b>Value:</b><i>" + elements[i].value + "</i>&nbsp;&nbsp;";
	        str += "<BR>";
	        str += "<BR>";
	    }
		alert(str); 
		*/ 
		
		
		
		for(var i = 0; i < elements.length; i++){
			if(elements[i].type == "radio"){
				if(elements[i].value==action){
					elements[i].checked = true;
					if(elements[i].value==STATUS_FAILED ){
						var commentObj = elements[i+1];
						var commentValue = commentObj.value; 
						var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
						if(trimmedCommentValue.length==0){					
							commentObj.value = "<fmt:message key="task.libraryqc_failBoxDefault.label" />";
							commentObj.style.color="red";
						}
						else if(commentObj.value == "<fmt:message key="task.libraryqc_failBoxDefault.label" />"){//if it had been blackified somehow	
							commentObj.style.color="red";
						}
					}
					else if(elements[i].value==STATUS_PASSED ){
						var commentObj = elements[i+2];//note + 2 
						var commentValue = commentObj.value; 
						var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
						if(commentObj.value == "<fmt:message key="task.libraryqc_failBoxDefault.label" />"){					
							commentObj.value = "";
							commentObj.style.color="black";
						}
					}
				}
				else{
					elements[i].checked = false;
				}
			}
		}
	}
	
	function validate(theform){
		
		var elements = theform.elements;
		var commentRequired = false;
		var atLeastOneSetHasBeenProperlySelected = false; //useful only when the startAnalysis button is Not checked (which is could be), since at least one record must be checked to post the form 
		var startAnalysis = ""; 
		var totalNumberOfRadioButtons = 0;//remember there are two for each pair 
		var totalNumberOfCheckedRadioButtons = 0;//if job is ready to start analysis, assert: 2 * totalNumberOfCheckedRadioButtons == totalNumberOfRadioButtons 
		var numberOfRadioButtonsCheckedAsPass = 0;  //if job is ready to start analysis, assert: numberOfRadioButtonsCheckedAsPass must be > 0 
		var numberOfRadioButtonsCheckedAsFail = 0;
		
		for(var i = 0; i < elements.length; i++){
			if(elements[i].type == "radio" ){
				totalNumberOfRadioButtons++;
				if(elements[i].checked && elements[i].value==STATUS_FAILED ){
					totalNumberOfCheckedRadioButtons++;
					numberOfRadioButtonsCheckedAsFail++;
					var commentObj = elements[i+1];
					var commentValue = commentObj.value; 
					var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
					if(trimmedCommentValue.length==0){					
						commentObj.value = "<fmt:message key="task.libraryqc_failBoxDefault.label" />";
						commentObj.style.color="red";
						commentRequired = true;
					}
					else if(trimmedCommentValue == "<fmt:message key="task.libraryqc_failBoxDefault.label" />"){//if it somehow blackified 
						commentObj.style.color="red";
						commentRequired = true;
					}
					else{
						atLeastOneSetHasBeenProperlySelected = true; 
					}
				}
				else if(elements[i].checked && elements[i].value==STATUS_PASSED ){
					totalNumberOfCheckedRadioButtons++; 
					numberOfRadioButtonsCheckedAsPass++;
					var commentObj = elements[i+2];//note +2 here 
					var commentValue = commentObj.value; 
					var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
					if(trimmedCommentValue == "<fmt:message key="task.libraryqc_failBoxDefault.label" />"){
						commentObj.value = "";
						commentObj.style.color="black";
					}
					atLeastOneSetHasBeenProperlySelected = true;
				}
			}
		}
		if(commentRequired){
			alert("<fmt:message key="task.cellLibraryqc_validateCommentAlert.label" />");
			return false; 
		}
		else if (!atLeastOneSetHasBeenProperlySelected){
			alert("<fmt:message key="task.cellLibraryqc_noLibRunSelectedAlert.label" />");
			return false;
		} 
		openWaitDialog();
		return true; 
	}
	
	function selected(num){
		var commentId = "comment" + num;
		var qcStatusName = "qcStatus" + num;
		var commentObject = document.getElementById(commentId);
		var trimmedCommentValue = commentObject.value.replace(/^\s+|\s+$/g, "");
		var qcStatusObject = document.getElementsByName(qcStatusName);
		if(qcStatusObject[0].checked && commentObject.value == "<fmt:message key="task.libraryqc_failBoxDefault.label" />"){//qcStatusObject[0].checked means STATUS_PASSED is checked  
			commentObject.value = "";
			commentObject.style.color="black";
		}
		else if(qcStatusObject[1].checked && trimmedCommentValue.length==0){//qcStatusObject[1].checked means FAILED is checked 
			commentObject.value = "<fmt:message key="task.libraryqc_failBoxDefault.label" />";
			commentObject.style.color="red";
		}
	}
	
	function changeTextColor(object, color){
		object.style.color="black";
		if(object.value == "<fmt:message key="task.libraryqc_failBoxDefault.label" />"){
			object.value = "";
		}
	}
</script>
