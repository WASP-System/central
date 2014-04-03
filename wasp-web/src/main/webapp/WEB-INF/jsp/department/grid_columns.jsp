<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:field name="name" type="text" showLink="true" baseLinkURL="<c:url value='lab/list.do' />" sortable="true"/>

<wasp:field name="primaryUserId" type="select" items="${pusers}" itemValue="userId" itemLabel="firstName" readOnly="true" showLink="true" baseLinkURL="<c:url value='user/list.do' />" idCol="2" sortable="true"/>
<wasp:field name="userId" type="hidden" />

<wasp:field name="departmentId"  type="select" items="${departments}" itemValue="departmentId" itemLabel="name" />

<wasp:field name="isActive"  type="checkbox" />

_navAttr={edit:false,view:true,add:false,del:false};
