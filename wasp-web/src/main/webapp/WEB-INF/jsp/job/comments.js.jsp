<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script>
function validate(){
	var comment_obj = document.getElementById("comment");
	var trimmed_value = comment_obj.value.replace(/^\s+|\s+$/g, "");  //returns comment_obj.value.trim 		  
	if(trimmed_value.length==0){
		alert("<fmt:message key="jobComment.alert.label"/>");
		if(comment_obj.value.length>0){
			comment_obj.value = "";
		}
		comment_obj.focus();
		return false;
	}
	return true;	
};
</script>