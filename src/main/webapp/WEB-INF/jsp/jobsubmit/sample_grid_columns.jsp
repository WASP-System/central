<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="subtypeSampleId" type="select" items="${subtypeSamples}" itemValue="subtypeSampleId" itemLabel="name"/>
<wasp:field name="status" type="select" items="${statuses}" />
<wasp:field name="fileData" type="file" >

<%-- file upload custom stuff--%>
var lastForm=null;

_saveFormId=function(postdata, formid) {

			if (document.forms[formid[0].id].fileData) {				
				lastForm=formid;
			} else {
				lastForm=null;
			}
                          
            return [true,''];
};

function showUploadError() {
	$('#uploadError').show();
}

function uploadDone(msg) { //Function will be called when iframe is loaded
	  
	   waspFade('uploadStatus',msg);
	   
	   $("#grid_id").trigger("reloadGrid");
     
}

_uploadAfterSubmit=function(response, data)  {

			var resp=response.responseText;
			
			var sampleDraftId=resp.split('|')[0];
			var serverResponse=resp.split('|')[1];

			waspFade('statusMessage',serverResponse);

			if (!lastForm) return [true,''];		

			if (! $('#fileData').val() ) return [true,'']; 

	
            $(lastForm).attr("method","POST");
            $(lastForm).attr("action","");
            $(lastForm).attr("enctype","multipart/form-data");
           
            var _urlString='/wasp/jobsubmit/uploadFile.do?id='+sampleDraftId+'&jobdraftId=${jobdraftId}';
                                                
            $.ajaxFileUpload({
                url:_urlString,
                secureuri:false,
                fileElementId:'fileData',
                dataType: 'json',
                success: function (data, status) {
                	                    
                    if (typeof(data.error) != 'undefined') {
                        if (data.error !== '') {                                                        
                          alert('file upload error: '+data.error);                          
                        } else {
                            $("#fileData").val("");                                                                                                                                             
                        }
                    }
                },
                error: function (data, status, e) {                   
                    alert('file upload error 2:'+e);
                   
                }
            });
            
            document.getElementById('uploadStatus').innerText='${uploadStartedMessage}';
                
            return [true,''];
};



_addAttr.beforeSubmit  =  _saveFormId;
_editAttr.beforeSubmit  =  _saveFormId;

_addAttr.afterSubmit  = _uploadAfterSubmit;
_editAttr.afterSubmit = _uploadAfterSubmit;
 
 
</wasp:field>


var existingJob={
name:'jobId',
label:'Existing Job',
required:false,
error:'',
jq:{
	name:'jobId', 
	width:80, 
	align:'center',
	sortable:false,
	sorttype:'text',
	editable:true,
	hidden:true,	
    editrules:{edithidden:true},    
	editoptions:{size:20},
	edittype:'select',
	editoptions:{value:{}},
	search:false
}
};

colNames.push(existingJob.label);
colModel.push(existingJob.jq);
colErrors.push(existingJob.error);

var existingJobSampleId={
name:'jobSampleId',
label:'Existing Job Sample',
required:false,
error:'',
jq:{
	name:'jobSampleId', 
	width:80, 
	align:'center',
	sortable:false,
	sorttype:'text',
	editable:true,
	hidden:true,	
    editrules:{edithidden:true},    
	editoptions:{size:20},
	edittype:'select',
	editoptions:{value:{}},
	search:false
}
};

colNames.push(existingJobSampleId.label);
colModel.push(existingJobSampleId.jq);
colErrors.push(existingJobSampleId.error);

<wasp:delete url="/wasp/jobsubmit/deleteSampleDraftJSON.do" />

<%--   url to get list of sample drafts for jobdraftId  --%>
_url='/wasp/jobsubmit/listSampleDraftsJSON.do?jobdraftId=${jobdraftId}';

<%--   url to update sinmgle sample draft  --%>
_editurl='/wasp/jobsubmit/updateSampleDraft.do?jobdraftId=${jobdraftId}';

<%-- disable search --%>
_navAttr.search=false;

 _navAttr.view=false;
 _navAttr.edit=false;
 _navAttr.add=false;



	
  
 
