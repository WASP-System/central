<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">
function checkSampleSelected(obj){
	//alert("this is the confirmSampleSelectedTest");
	var parentDiv = obj.parentNode;
	var searchEles = parentDiv.children;
	for(var i = 0; i < searchEles.length; i++) {
	    if(searchEles[i].tagName == 'SELECT'){
	    	if(searchEles[i].value==0){
	    		alert("Please select a sample");//TODO: internationalize 
	    		return false;
	    	}
	    }
	}
	return true;
}
</script>