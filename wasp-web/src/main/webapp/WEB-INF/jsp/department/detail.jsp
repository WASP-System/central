<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<font color="red"><wasp:message /></font>
<h1><c:out value="${department.name}"/></h1>
	
<sec:authorize access="hasRole('su')">
  <div>
		<div class="instructions"> <%@ include file="/WEB-INF/jsp/lorem.jsp" %> </div>
  <form name="update_form" action = "<c:url value='/department/updateDepartment.do'/>" method="POST" onsubmit="return validate('updateDept');">
		<input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>

		<table>
		<tr>
		<td class="label">Update Department</td>
		<td class="input"><input type="text" name='name' value="<c:out value="${department.name}"></c:out>"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="radio" name="isActive" <c:out value="${department.isActive==1?'CHECKED':''}" /> value="1">Active &nbsp; <input type="radio" name="isActive" <c:out value="${department.isActive==0?'CHECKED':''}" />	value="0">Inactive</td>
		<tr>
		</table>
		<div class="submit">
			<input type="submit" value="<fmt:message key="department.detail_submit.label" />" /> 
		</div>
  </form>
  </div>

  <div>		
		<h1><fmt:message key="department.detail_administrators.label" /></h1>
		<div class="instructions"> <%@ include file="/WEB-INF/jsp/lorem.jsp" %> </div>
		
		<form name="f" action="<c:url value='/department/user/roleAdd.do'/>" method="POST" onsubmit="return validate('createAdmin');">
		<input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>
			
		<table class="data"> 
			<tr>
			<td class="label"><fmt:message key="department.detail_administrator_name.label" /> /
			<fmt:message key="department.detail_createadmin.label" /></td><td>&nbsp;</td>
			<td class="input"><input id="adminName" name='adminName' value='' /></td>
			</tr>
		</table>				
		<div class="submit">	
			<input type="submit" value="<fmt:message key="department.detail_submit.label" />" /> 
		</div>	
		</form>
	</div>
</sec:authorize>

<c:if test="${departmentuser}.size() > 0 ">		
<h1><fmt:message key="department.detail_existingadmin.label" /></h1>
<table class="data tab-data">
<c:forEach items="${departmentuser}" var="u">
	<tr>
	<td class="value">
		<c:out value="${u.user.firstName}" />
		<c:out value="${u.user.lastName}" />
	</td>
	<sec:authorize access="hasRole('su')">
		<td class="action">
			<c:if test="${departmentuser.size() > 1}">
				<a href="/wasp/department/user/roleRemove/<c:out value="${department.departmentId}" />/<c:out value="${u.user.userId}" />.do">
					<fmt:message key="department.detail_remove.label" />
				</a>
			</c:if>
		</sec:authorize>
	</tr>
</c:forEach>
<table>
</c:if>

		
<div>
<!-- <h1><fmt:message key="department.detail_labs.label" /></h1> -->
<table id="grid_id"></table> 
<div id="gridpager"></div>
</div>