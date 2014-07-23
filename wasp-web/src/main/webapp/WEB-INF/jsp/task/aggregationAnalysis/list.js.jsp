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
	});

	function validate(theform){
		
		var elements = theform.elements;
		var startAnalysis = ""; 
		for(var i = 0; i < elements.length; i++){
			if(elements[i].type == "select-one" && elements[i].name == "startAnalysis"){
				startAnalysis = elements[i].value; 
			} 
		}
		if(startAnalysis == ""){
			alert("<fmt:message key="task.cellLibraryqc_startAnalysisNotSelectedAlert.label" />");
			return false;
		} 
		openWaitDialog();
		return true; 
	}
	
</script>
