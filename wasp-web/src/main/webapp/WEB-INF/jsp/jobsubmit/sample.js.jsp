<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="<wasp:relativeUrl value='scripts/jquery/jquery.cookie.js' />"></script>

<script type="text/javascript">

$(document).ready(function(){
	$( "#dialog-form2" ).dialog({
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
	    		  //var href = "<wasp:relativeUrl value='jobsubmit/manysamples/edit/' />"+jobDraftId+"/"+sampleSubtypeId+".do?theSelectedAdaptorset="+theSelectedAdaptorset;
	    		  
	    		  
	    		//$("#theSelectedAdaptorset").val("");
	  	        //$("#validateTipForThisModalDialogForm").text("");
	  	        
	    	  	//$(".settableLibraryCost").val(costToApplyToAllSettableLibraries);
	    	  	//$("#costToApplyToAllSettableLibraries").val("");
	    	  	//$("#validateTipForThisModalDialogForm").text("");
	    	  	//$( this ).dialog( "close" );
	    	  }
	    	  else{
	    		  $("#validateTipForThisModalDialogForm").text("<fmt:message key="jobsubmitSample.SelectAnAdaptorSet.label" />");
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
	
	
	$( "#urlForAddMoreSamplesOfType" ).change(function(){
		//alert("dubin testing new ADD SAMPLES method of change");
		var href = $( this ).val();
		//alert("value of href = " + href);
		if(href == ""){//--select-- was selected; should not happen 
			return false;
		}
		$(location).attr('href',href);
		return;
	});
	
	$( "#urlForEditYourSamplesOfType" ).change(function(){
		//alert("dubin testing new EDIT EDIT EDIT method of change");
		var numberOfAdaptorSetsUsedOnThisJobDraftAndSampleTypeAndHref = $( this ).val();
		if(numberOfAdaptorSetsUsedOnThisJobDraftAndSampleTypeAndHref==""){//--select-- was selected; should not happen 
			return false;
		}
		//alert("value of numberOfAdaptorSetsUsedOnThisJobDraftAndSampleTypeAndHref = " + numberOfAdaptorSetsUsedOnThisJobDraftAndSampleTypeAndHref);
		var splitArray = numberOfAdaptorSetsUsedOnThisJobDraftAndSampleTypeAndHref.split("::",3);
		var numberOfAdaptorSetsUsedOnThisJobDraft = splitArray[0];
		var sampleType = splitArray[1];
		var href = splitArray[2];
		if(numberOfAdaptorSetsUsedOnThisJobDraft == null || numberOfAdaptorSetsUsedOnThisJobDraft == "" || sampleType == null || sampleType == "" || href == null || href == ""){
			alert("Unexpected Error; should not occur");
			return false;
		}
		//alert("numberOfAdaptorSetsUsedOnThisJobDraft = " + numberOfAdaptorSetsUsedOnThisJobDraft);
		//alert("value of href = " + href);
		if(sampleType.toLowerCase() != "library"){
			//alert("sampleType is NOT library, so can go to next page");
			$(location).attr('href',href);
			return;
		}
		else if(sampleType.toLowerCase() == "library" && numberOfAdaptorSetsUsedOnThisJobDraft == "0"){
			alert("Unexpected Error (should not occur): sampleType is a library, but number of adaptors used on this job draft is 0");
			return;
		}
		else if(sampleType.toLowerCase() == "library" && numberOfAdaptorSetsUsedOnThisJobDraft == "1"){
			//alert("sampleType is a library AND number of adaptors used on this job draft is 1, and it's in the url, so go to next page");
			$(location).attr('href',href);
			return;
		}		
		else if(sampleType.toLowerCase() == "library" && numberOfAdaptorSetsUsedOnThisJobDraft != "0" && numberOfAdaptorSetsUsedOnThisJobDraft != "1"){
			//alert("sampleType is a library AND number of adaptors used on this job draft is > 1 so MUST DISPLAY DIALOG-FORM2");
			//delegate (pull href and assign to location) to dialog-form2
			$( "#dialog-form2" ).dialog( "open" );//if a url is selected, this will go to next page via code in dialog-form2
			$( this ).val("");//in case the user selects cancel on the dialog-from, I want to reset this select box back to choice: --select--  
			return;
		}	
		else{
			alert("Unexpected Error (should never occur)");
			return false;
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