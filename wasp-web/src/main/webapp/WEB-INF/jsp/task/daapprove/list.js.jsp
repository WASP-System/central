<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script>
	
	$(document).ready(function() {
		
	    $("#accordion").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
		});
	    $("#accordion2").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
		});
	 	//this is not being used
	 	$('button[id^="robbutton"]').click(function() {
		  var id = parseInt(this.id.replace("robbutton", ""));
	  	  $("#robdiv" + id).fadeToggle("fast", "linear");
	  	  if( $(this).html() == "show"){ $(this).html("hide"); }
		  else{ $(this).html("show"); }	  	  
	  	});
	 	
	}); 
	
	function validate(theform){
		if(!theform.action[0].checked && !theform.action[1].checked){
			alert("<fmt:message key="jobapprovetask.approveRejectAlert.label" />");
			return false;
		}
		var commentObj = theform.comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(theform.action[1].checked && trimmedCommentValue.length==0){
			alert("<fmt:message key="jobapprovetask.validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
			return false;
		}
		return true;
	}
	function selectedFail(formId){
		var commentObj = document.getElementById(formId).comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(trimmedCommentValue.length==0){
			alert("<fmt:message key="jobapprovetask.validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
		}
	}
	function validateLabPending(theform){
		if(!theform.action[0].checked && !theform.action[1].checked){
			alert("<fmt:message key="dapendingtask.approveRejectAlert.label" />");
			return false;
		}
		var commentObj = theform.comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(theform.action[1].checked && trimmedCommentValue.length==0){
			alert("<fmt:message key="dapendingtask.validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
			return false;
		}
		return true;  
	}
	
	
</script>
