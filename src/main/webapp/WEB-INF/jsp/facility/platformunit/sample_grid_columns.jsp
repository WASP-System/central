<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text"/>
<wasp:field name="submitter" type="text"/>

_url='/wasp/facility/platformunit/listJSON.do?selId=${param.selId}';
_editurl='/wasp/facility/platformunit/updateJSON.do';
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
