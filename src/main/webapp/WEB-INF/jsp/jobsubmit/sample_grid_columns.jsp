<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="typeSampleId" type="select" items="${typeSamples}" itemValue="typeSampleId" itemLabel="name"/>
<wasp:field name="status" type="select" items="${statuses}" />

_url='/wasp/jobsubmit/listSampleDraftsJSON.do?selId=${param.selId}&jobdraftId=${jobdraftId}';
_editurl='/wasp/jobsubmit/updateSampleDraftsJSON.do?jobdraftId=${jobdraftId}';
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
            params.url = '/wasp/jobsubmit/deleteSampleDraftJSON.do?id=' + encodeURIComponent(postdata) ;
        },
        afterSubmit: _afterSubmit
};


//