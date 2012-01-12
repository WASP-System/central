<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<br />
      
    <h1><c:out value="${department.name}"/></h1>
    
	<font color="red"><wasp:message /></font>
	
	<sec:authorize access="hasRole('god')">
	 <form name="update_form" action = "<c:url value='/department/updateDepartment.do'/>" method="POST" onsubmit="return validate('updateDept');">
	  Update Department: <input type="text" name='name' value="<c:out value="${department.name}"></c:out>">
	  &nbsp; <input type="radio" name="isActive" <c:out value="${department.isActive==1?'CHECKED':''}" /> value="1">Active &nbsp; <input type="radio" name="isActive" <c:out value="${department.isActive==0?'CHECKED':''}" />  value="0">Inactive
	  <input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>
	  <input type="submit" value="<fmt:message key="department.detail_submit.label" />" /> 
	 </form>
    
    
     <h2><fmt:message key="department.detail_administrators.label" /></h2>
    
    <div>
    <h3><fmt:message key="department.detail_createadmin.label" /></h3>
    
    <form name="f" action="<c:url value='/department/user/roleAdd.do'/>" method="POST" onsubmit="return validate('createAdmin');">
       
      <input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>
      <fmt:message key="department.detail_administrator_name.label" />: <input id="adminName" name='adminName' value='' />
            
      <input type="submit" value="<fmt:message key="department.detail_submit.label" />" /> 
    </form>
    </div>
    
    </sec:authorize>
    
    <h3><fmt:message key="department.detail_existingadmin.label" /></h3>
    <c:forEach items="${departmentuser}" var="u">
      <p>
  <!-- <a href="/wasp/user/detail_ro/<c:out value="${u.user.userId}" />.do"><c:out value="${u.user.login}" /></a>  -->
        <c:out value="${u.user.firstName}" />
        <c:out value="${u.user.lastName}" />
        <sec:authorize access="hasRole('god')">
         <c:if test="${departmentuser.size() > 1}">
          <a href="/wasp/department/user/roleRemove/<c:out value="${department.departmentId}" />/<c:out value="${u.user.userId}" />.do">
          <fmt:message key="department.detail_remove.label" />
          </a>
        </c:if>
        </sec:authorize>
      </p>
    </c:forEach>

    

    <h2><fmt:message key="department.detail_labs.label" /></h2>
    <table id="grid_id"></table> 
	<div id="gridpager"></div>
	<!-- <script>
	$("#grid_id").jqGrid('setGridParam', { rowNum: 50,height: '300'}).trigger("reloadGrid");    
	</script> -->

   <!--   <h2><fmt:message key="department.detail_pendinglabs.label" /></h2>
    <c:forEach items="${labpending}" var="lp">
      <div>
      	<a href="/wasp/lab/pending/detail_ro/<c:out value="${department.departmentId}" />/<c:out value="${lp.labPendingId}" />.do">
      	<c:out value="${lp.name}" />
      	</a>
      </div>
    </c:forEach>  -->
