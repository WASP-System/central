<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text" sortable="true" searchable="false" columnWidth="200"/> 
<wasp:field name="primaryUser" type="text" sortable="true" searchable="false" showLink="true" baseLinkURL="<wasp:relativeUrl value='user/list.do' />" idCol="2" columnWidth="200"/>
<wasp:field name="primaryUserId" type="hidden" />
<wasp:field name="departmentId"  sortable="true" searchable="false" type="select" items="${departments}" itemValue="departmentId" itemLabel="name" columnWidth="200"/>
<wasp:field name="isActive"  sortable="false" searchable="false" type="checkbox" columnWidth="70"/>
<wasp:field name="list_users" type="text" sortable="false" searchable="false" editable="false" columnWidth="100"/>

_url='<wasp:relativeUrl value="lab/listJSON.do?selId=${param.selId}" />';

_navAttr={edit:true,view:false,add:false,del:false,search:false,refresh:true,beforeRefresh: 
	function () { 
		var url = window.location.href; 
		if(url.indexOf('selId') != -1){<%-- url contains this string (indicating coming from jobGrid), upon refresh, change url to remove; will cause a COMPLETE refresh of page rather than a JSON call--%> 
			window.location.replace('list.do'); <%-- completely refresh the page, without the userId and labId request parameters --%>
		}
		<%--http://stackoverflow.com/questions/7089643/programmatically-sorting-the-jqgrid 
		with next line, sortname is set to "" and with that sidx is also set to "" 
		so that with reload grid, sidx is not controlling anything--%>
		$("#grid_id").setGridParam({sortname:''});
	}
}


// hide the primary user dropdown list on the edit form; odd: if I use beforeShowForm it works just the same
_editAttr['afterShowForm'] = function(formId) {
	$('#primaryUser').attr('disabled', 'disabled');
};
