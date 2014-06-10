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
function checksOnContinueToNextPage(atLeastOneReplicateSetWithSingleSample){
	if(atLeastOneReplicateSetWithSingleSample=="true"){//not permited 
		alert("You have at least one replicate set that contains a single sample. Since each replicate set must contain at least two samples, you must either add another sample to that set OR delete the single sample from its set.")
		return false;
	}
	//check that no selections have currently been made on any of the select boxes, since those selections will not be saved when going to the next page. If this selection is to be saved, the user must click the appropriate 'Add Sample To Set Button.'
	var allSelectEles = document.getElementsByTagName("SELECT");
	for(var i = 0; i < allSelectEles.length; i++) {
		if(allSelectEles[i].value!="0"){
			allSelectEles[i].focus();
    		return confirm ("One of the drop-down boxes has been selected, indicating that you want to add a sample to a replicate set. To add that sample to its replicate set, click 'Cancel' and then click the adjacent 'Add Sample To Set Button.'  If instead you want to proceed to the next page WITHOUT saving your latest reqest, simply click 'OK.'");//TODO: internationalize 
    	}
	}
	return true;
}
</script>