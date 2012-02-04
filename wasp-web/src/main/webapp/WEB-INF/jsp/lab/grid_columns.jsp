<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <wasp:field name="name" type="text" sortable="true"/> 
  <wasp:field name="primaryUserId" type="select" items="${pusers}" itemValue="userId" itemLabel="firstName" readOnly="true" sortable="true"/>
  <wasp:field name="departmentId"  type="select" items="${departments}" itemValue="departmentId" itemLabel="name"/>
  <wasp:field name="isActive"  type="checkbox" />
  
  
  