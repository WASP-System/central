<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text" sortable="false" searchable="false"/> 
<wasp:field name="primaryUser" type="text" sortable="false" searchable="false" showLink="true" baseLinkURL="/wasp/user/list.do" idCol="2"/>
<wasp:field name="primaryUserId" type="hidden" />
<wasp:field name="departmentId"  sortable="false" searchable="false" type="select" items="${departments}" itemValue="departmentId" itemLabel="name"/>
<wasp:field name="isActive"  sortable="false" searchable="false" type="checkbox" />

_url='/wasp/lab/listJSON.do?selId=${param.selId}';

_navAttr={edit:true,view:false,add:false,del:false,search:false,refresh:true,beforeRefresh: 
	function () { 
		var url = window.location.href; 
		if(url.indexOf('selId') != -1){<%-- url contains this string (indicating coming from jobGrid), upon refresh, change url to remove; will cause a COMPLETE refresh of page rather than a JSON call--%> 
			window.location.replace('list.do'); <%-- completely refresh the page, without the userId and labId request parameters --%>
		}
	}
}


// hide the primary user dropdown list on the edit form; odd: if I use beforeShowForm it works just the same
_editAttr['afterShowForm'] = function(formId) {
	$('#primaryUser').attr('disabled', 'disabled');
};
