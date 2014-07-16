<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<script>
	
	$(document).ready(function() {
	    $("#accordion").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
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
		openWaitDialog();
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
	
</script>
