
<%-- 
"columns" section of the "sample drafts" page

@author: Sasha Levchuk
--%>

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="subtypeSampleId" type="select" items="${subtypeSamples}" itemValue="subtypeSampleId" itemLabel="name"/>
<wasp:field name="status" type="select" items="${statuses}" />
<wasp:field name="fileData" type="file" >

<%-- file upload logic--%>
var lastForm=null;


<%-- save form ID so we have access to it later --%>
_saveFormId=function(postdata, formid) {

			if (document.forms[formid[0].id].fileData) {				
				lastForm=formid;
			} else {
				lastForm=null;
			}
                          
            return [true,''];
};

<%-- display file upload error --%>
function showUploadError() {
	$('#uploadError').show();
}

<%-- displays "upload done" message and reload the jqgrid --%>
function uploadDone(msg) { //Function will be called when iframe is loaded
	  
	   waspFade('uploadStatus',msg);
	   
	   $("#grid_id").trigger("reloadGrid");
     
}

<%-- starts file upload after form was submitted in a separate iframe--%>
_uploadAfterSubmit=function(response, data)  {

			var resp=response.responseText;
			
			var sampleDraftId=resp.split('|')[0];
			var serverResponse=resp.split('|')[1];

			waspFade('waspMessage',serverResponse);

			<%-- return if there was no file to upload --%>
			if (!lastForm) return [true,''];		
			if (! $('#fileData').val() ) return [true,'']; 

	
            $(lastForm).attr("method","POST");
            $(lastForm).attr("action","");
            $(lastForm).attr("enctype","multipart/form-data");
           
            var _urlString='/wasp/jobsubmit/uploadFile.do?id='+sampleDraftId+'&jobdraftId=${jobdraftId}';
                         
            <%-- starts file upload via ajaxfileupload.js plugin --%>                                    
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
            
            <%-- show 'upload started' message --%>      
            document.getElementById('uploadStatus').innerText='${uploadStartedMessage}';
                
            return [true,''];
};


_addAttr.beforeSubmit   =  _saveFormId;
_editAttr.beforeSubmit  =  _saveFormId;

_addAttr.afterSubmit  = _uploadAfterSubmit;
_editAttr.afterSubmit = _uploadAfterSubmit;
 
 
</wasp:field>

<wasp:field name="jobId" type="select" items="empty">
#field.jq.hidden=true;
#field.jq.editrules.edithidden=true;
</wasp:field>

<wasp:field name="sourceSampleId" type="select" items="empty">
#field.jq.hidden=true;
#field.jq.editrules.edithidden=true;
</wasp:field>

<wasp:field name="cloned" type="text">
#field.jq.editable=true;
</wasp:field>

<%-- define url to call when user clisck "delete" icon --%>      
<wasp:delete url="/wasp/jobsubmit/deleteSampleDraftJSON.do" />

<%--   url to get list of sample drafts for jobdraftId  --%>
_url='/wasp/jobsubmit/listSampleDraftsJSON.do?jobdraftId=${jobdraftId}';

<%--   url to update sinmgle sample draft  --%>
_editurl='/wasp/jobsubmit/updateSampleDraft.do?jobdraftId=${jobdraftId}';

<%-- disable search --%>
_navAttr.search=false;

<%-- disable view/edit/add buttons --%>      
_navAttr.view=false;
_navAttr.edit=false;
_navAttr.add=false;

