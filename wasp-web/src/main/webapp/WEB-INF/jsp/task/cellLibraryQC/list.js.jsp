<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
<script>
	function setAll(formId, action){
		var elements = document.getElementById(formId).elements;
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
		var atLeastOneSetHasBeenProperlySelected = false; 
		
		for(var i = 0; i < elements.length; i++){
			if(elements[i].type == "radio" && elements[i].checked ){				
				if(elements[i].value=="EXCLUDE" ){
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
				else if(elements[i].value=="INCLUDE" ){
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
		return true;
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
		if(object.value = "Provide reason for exclusion"){
			object.value = "";
		}
	}
	
</script>
