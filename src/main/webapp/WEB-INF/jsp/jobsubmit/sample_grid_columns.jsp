<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="typeSampleId" type="select" items="${typeSamples}" itemValue="typeSampleId" itemLabel="name"/>
<wasp:field name="status" type="select" items="${statuses}" />
<wasp:field name="fileData"  type="file"/>


_url='/wasp/jobsubmit/listSampleDraftsJSON.do?selId=${param.selId}&jobdraftId=${jobdraftId}';

_editurl='/wasp/jobsubmit/updateSampleDrafts.do?jobdraftId=${jobdraftId}';

_navAttr.search=false;
_navAttr.delfunc=null;
_delAttr=
{ // Delete parameters
        ajaxDelOptions: { contentType: "application/json" },
        mtype: "DELETE",
        serializeDelData: function () {
            return ""; 
        },
        onclickSubmit: function (params, postdata) {        	
            params.url = '/wasp/jobsubmit/deleteSampleDraftJSON.do?id=' + encodeURIComponent(postdata);
        },
        afterSubmit: _afterSubmit
};

var lastForm=null;
//var waspFrameId=null;

_saveFormId=function(postdata, formid) {

			if (document.forms[formid[0].id].fileData) {				
				lastForm=formid;
			} else {
				lastForm=null;
			}
             
             
            return [true,''];
};



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
 
 
 

  
 