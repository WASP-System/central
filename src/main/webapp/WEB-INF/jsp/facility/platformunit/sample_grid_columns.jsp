<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>


_url='/wasp/facility/platformunit/listJson.do?selId=${param.selId}';
_editurl='/wasp/facility/platformunit/updateJson.do';
_navAttr.search=false;

/*
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
*/

//
