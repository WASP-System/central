<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <wasp:field name="name" object="lab" type="text"/> 
  <wasp:field name="primaryUserId" object="lab" type="select" items="${pusers}" itemValue="userId" itemLabel="firstName"/>
  <wasp:field name="departmentId" object="lab" type="select" items="${departments}" itemValue="departmentId" itemLabel="name"/>
  <wasp:field name="isActive" object="lab" type="text">
    #field.jq['edittype']='checkbox';
  	#field.jq['editoptions']={value:"1:0"}; 
  	#field.jq['formatter']='checkbox';
  	#field.jq['formatoptions']={disabled : true};
  	#field.jq['align']='center';
    #field.jq['search']=false;  
  </wasp:field>
  
  
  