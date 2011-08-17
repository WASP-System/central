<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <wasp:field name="name" object="lab" />  
  <wasp:field name="primaryUserId" object="lab" />
  <wasp:field name="departmentId" object="lab" />
  <wasp:field name="isActive" object="lab" />
  
  isActive.jq['edittype']='checkbox';
  isActive.jq['editoptions']={value:"1:0"}; 
  isActive.jq['formatter']='checkbox';
  isActive.jq['formatoptions']={disabled : true};
  isActive.jq['align']='center';
  isActive.jq['search']=false;
