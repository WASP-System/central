<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
<script>

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
					if(elements[i].value=="EXCLUDE" ){
						var commentObj = elements[i+1];
						var commentValue = commentObj.value; 
						var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
						if(trimmedCommentValue.length==0){					
							commentObj.value = "Provide reason for exclusion";
							commentObj.style.color="red";
						}
						else if(commentObj.value == "Provide reason for exclusion"){//if it had been blackified somehow	
							commentObj.style.color="red";
						}
					}
					else if(elements[i].value=="INCLUDE" ){
						var commentObj = elements[i+2];//note + 2 
						var commentValue = commentObj.value; 
						var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
						if(commentObj.value == "Provide reason for exclusion"){					
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
		var startAnalysis = false; //is the startAnalysis checkbox checked on this form 
		var totalNumberOfRadioButtons = 0;
		var totalNumberOfCheckedRadioButtons = 0;//if job is ready to start analysis, assert: 2 * totalNumberOfCheckedRadioButtons == totalNumberOfRadioButtons 
		var numberOfRadioButtonsCheckedAsInclude = 0;  //if job is ready to start analysis, assert: numberOfRadioButtonsCheckedAsInclude must be > 0 
		
		for(var i = 0; i < elements.length; i++){
			if(elements[i].type == "checkbox" && elements[i].name == "startAnalysis" && elements[i].checked){
				startAnalysis = true;  
			}
			else if(elements[i].type == "radio" ){
				totalNumberOfRadioButtons++;
				if(elements[i].checked && elements[i].value=="EXCLUDE" ){
					totalNumberOfCheckedRadioButtons++;
					var commentObj = elements[i+1];
					var commentValue = commentObj.value; 
					var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
					if(trimmedCommentValue.length==0){					
						commentObj.value = "Provide reason for exclusion";
						commentObj.style.color="red";
						commentRequired = true;
					}
					else if(trimmedCommentValue == "Provide reason for exclusion"){//if it somehow blackified 
						commentObj.style.color="red";
						commentRequired = true;
					}
					else{
						atLeastOneSetHasBeenProperlySelected = true; 
					}
				}
				else if(elements[i].checked && elements[i].value=="INCLUDE" ){
					totalNumberOfCheckedRadioButtons++; 
					numberOfRadioButtonsCheckedAsInclude++;
					var commentObj = elements[i+2];//note +2 here 
					var commentValue = commentObj.value; 
					var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
					if(trimmedCommentValue == "Provide reason for exclusion"){
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
		if(startAnalysis){//the startAnalysis checkbox is checked 
			if( totalNumberOfRadioButtons != 2 * totalNumberOfCheckedRadioButtons){//not every entry has been recorded as either include or exclude 
				alert("<fmt:message key="task.cellLibraryqc_startAnalysisRequestedWithoutRecordingEachRecord.label" />To start this job's analysis, you must select Include or Exclude for each entry.");
				return false;
			}
			if(numberOfRadioButtonsCheckedAsInclude <= 0){//since no entries were selected as include, you may not start analysis 
				alert("<fmt:message key="task.cellLibraryqc_startAnalysisRequestedWithoutAtLeastOneInclude.label" />");
				return false; 
			}
		}
		
		if(startAnalysis){
			alert("start the analysis");
		}else{alert("start analysis is NOT checked");}
		
		return false;
	}
	
	function selected(num){
		var commentId = "comment" + num;
		var qcStatusName = "qcStatus" + num;
		var commentObject = document.getElementById(commentId);
		var trimmedCommentValue = commentObject.value.replace(/^\s+|\s+$/g, "");
		var qcStatusObject = document.getElementsByName(qcStatusName);
		if(qcStatusObject[0].checked && commentObject.value == "Provide reason for exclusion"){//qcStatusObject[0].checked means INCLUDE is checked  
			commentObject.value = "";
			commentObject.style.color="black";
		}
		else if(qcStatusObject[1].checked && trimmedCommentValue.length==0){//qcStatusObject[1].checked means EXCLUDE is checked 
			commentObject.value = "Provide reason for exclusion";
			commentObject.style.color="red";
		}
	}
	
	function changeTextColor(object, color){
		object.style.color="black";
		if(object.value == "Provide reason for exclusion"){
			object.value = "";
		}
	}
	/* this functionality was incorporated into validate() 
	function isJobReadyToStartAnalysis(theForm){
		
		var elements = theForm.elements;
		var totalNumberOfRadioButtons = 0;
		var totalNumberOfCheckedRadioButtons = 0;//if job is ready for analysis, 2 * totalNumberOfCheckedRadioButtons == totalNumberOfRadioButtons 
		var numberOfRadioButtonsCheckedAsInclude = 0;  //if job is ready for analysis, numberOfRadioButtonsCheckedAsInclude must be > 0 
		for(var i = 0; i < elements.length; i++){
			if(elements[i].type == "radio" ){
				totalNumberOfRadioButtons++; 
				if(elements[i].checked && elements[i].value=="EXCLUDE" ){
					totalNumberOfCheckedRadioButtons++; 
					var commentObj = elements[i+1];
					var commentValue = commentObj.value; 
					var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
					if(trimmedCommentValue.length==0 || trimmedCommentValue == "Provide reason for exclusion"){					
						return false;  //not ready to start analysis 
					}
				}
				else if(elements[i].checked && elements[i].value=="INCLUDE" ){
					totalNumberOfCheckedRadioButtons++; 
					numberOfRadioButtonsCheckedAsInclude++; 
					var commentObj = elements[i+2];//note +2 here 
					var commentValue = commentObj.value; 
					var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
					if(trimmedCommentValue == "Provide reason for exclusion"){
						commentObj.value = "";
					}
				}
			}
		} 
		if( (totalNumberOfRadioButtons == 2 * totalNumberOfCheckedRadioButtons) && numberOfRadioButtonsCheckedAsInclude > 0){
			return true;
		}
		return false;
	}
	*/
</script>
