<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <wasp:field name="name" object="lab" type="text"/>  
  <wasp:field name="primaryUserId" object="lab" type="select" items="${pusers}" itemValue="userId" itemLabel="firstName"/>
  <wasp:field name="departmentId" object="lab" type="select" items="${departments}" itemValue="departmentId" itemLabel="name"/>
  <wasp:field name="isActive" object="lab" type="text"/>
  
  isActive.jq['edittype']='checkbox';
  isActive.jq['editoptions']={value:"1:0"}; 
  isActive.jq['formatter']='checkbox';
  isActive.jq['formatoptions']={disabled : true};
  isActive.jq['align']='center';
  isActive.jq['search']=false;
 