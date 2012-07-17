<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name" type="text" sortable="true"/> 
<wasp:field name="primaryUser" type="text" sortable="true" showLink="true" baseLinkURL="/wasp/user/list.do" idCol="2"/>
<wasp:field name="primaryUserId" type="hidden" />
<wasp:field name="departmentId"  type="select" items="${departments}" itemValue="departmentId" itemLabel="name"/>
<wasp:field name="isActive"  type="checkbox" />

_navAttr={edit:true,view:true,add:false,del:false};

// hide the primary user dropdown list on the edit form
_editAttr['afterShowForm'] = function(formId) {
	$('#primaryUser').attr('disabled', 'disabled');
};
