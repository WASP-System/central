<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="<c:url value='scripts/jquery/jquery.cookie.js' />"></script>

<script type="text/javascript">

$(document).ready(function(){
	$( "#dialog-form" ).dialog({
	    autoOpen: false,
	    height: 270,
	    width: 300,
	    modal: true,
	    buttons: {
	      "Apply": function() {
	    	 // var theSelectedAdaptorset = $("#theSelectedAdaptorset").val();
	    	  //$("#validateTipForThisModalDialogForm").text("looks good");
	    	 
	    	  var theSelectedAdaptorset = $("#theSelectedAdaptorset").val();//this selected value is complete url
	    	  //if(theSelectedAdaptorset==""){ 
	    		  
	    	  //} 
	    	  //var regex = /^([0-9])+$/;
	    	  //if(regex.test(theSelectedAdaptorset)){ 
	    	 if(theSelectedAdaptorset!=""){
	    		 	
	    		 $(location).attr('href',theSelectedAdaptorset);
	    		 $("#theSelectedAdaptorset").val("");
	 	        $("#validateTipForThisModalDialogForm").text("");		        	
	 	        $( this ).dialog( "close" );
	    		 //$("#validateTipForThisModalDialogForm").text(theSelectedAdaptorset);
	    		  //var jobDraftId = '<c:out value="${ jobDraft.getJobDraftId() }"/>';
	    		  //var sampleSubtypeId = '<c:out value="${ sampleSubtype.getSampleSubtypeId() }"/>';
	    		  //var href = "<c:url value='jobsubmit/manysamples/edit/' />"+jobDraftId+"/"+sampleSubtypeId+".do?theSelectedAdaptorset="+theSelectedAdaptorset;
	    		  
	    		  
	    		//$("#theSelectedAdaptorset").val("");
	  	        //$("#validateTipForThisModalDialogForm").text("");
	  	        
	    	  	//$(".settableLibraryCost").val(costToApplyToAllSettableLibraries);
	    	  	//$("#costToApplyToAllSettableLibraries").val("");
	    	  	//$("#validateTipForThisModalDialogForm").text("");
	    	  	//$( this ).dialog( "close" );
	    	  }
	    	  else{
	    		  $("#validateTipForThisModalDialogForm").text("Select an adaptor set");
	    	  } 
	      },
	      Cancel: function() {
	    	$("#theSelectedAdaptorset").val("");
	        $("#validateTipForThisModalDialogForm").text("");		        	
	        $( this ).dialog( "close" );
	      }
	    },
	    close: function() {
	      	$("#theSelectedAdaptorset").val("");
	    	$("#validateTipForThisModalDialogForm").text("");	 
	    	$( this ).dialog( "close" );
	    }
	  });	
});

function verifyRemove(targetURL){
	if (confirm('<fmt:message key="jobDraft.remove_confirm.label" />')){
		window.location = targetURL;
	}
}

function addFileUploadRow(){
	var table = document.getElementById("fileUploadTbl");
	var rowNum = table.rows.length;
	var newRow = table.insertRow(rowNum);
	
	var td1 = newRow.insertCell(0);
	td1.className = "DataTD value-centered";
	var fileInput = document.createElement("input");
	fileInput.type="file";
	fileInput.name="file_upload";
	fileInput.onchange=function() { addFileUploadRow(); };
	td1.appendChild(fileInput);
	
	var td2 = newRow.insertCell(1);
	td2.className = "DataTD value-centered";
	var textInput = document.createElement("input");
	textInput.type="text";
	textInput.name="file_description";
	textInput.className = "FormElement ui-widget-content ui-corner-all";
	td2.appendChild(textInput);
	
	var td3 = newRow.insertCell(2);
	td3.className = "DataTD value-centered";
	td3.innerHTML = '<fmt:message key="jobDraft.file_not_applicable.label"/>';
}

function validate(thisForm){
	var fileUploads = thisForm.file_upload;
	var fileDescriptions = thisForm.file_description;
	for(var i = 0; i < fileUploads.length; i++){
		if(fileUploads[i].value != "" && fileDescriptions[i].value == ""){ 
			alert("<fmt:message key="jobDraft.file_description_missing.label"/>");
			fileDescriptions[i].focus();
			return false;
		} 
	}
	$("#wait_dialog-modal").dialog("open");
	return true;
}
</script>