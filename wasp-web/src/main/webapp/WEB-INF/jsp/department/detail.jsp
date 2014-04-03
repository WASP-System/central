<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><c:out value="${department.name}"/></h1>
	
<sec:authorize access="hasRole('su')">
  <div>
		<div class="instructions"><fmt:message key="department.detail_update_instructions.label" /></div>
  <form name="update_form" action = "<c:url value='/department/updateDepartment.do'/>" method="POST" onsubmit="return validate('updateDept');">
		<input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>

		<table class="EditTable ui-widget ui-widget-content">
			<tr class="FormData">
				<td class="CaptionTD"><fmt:message key="department.detail_update.label" />:</td>
				<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type="text" name='name' value="<c:out value="${department.name}"></c:out>"></td>
			</tr>
			<tr class="FormData">
				<td class="DataTD">&nbsp;</td>
				<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type="radio" name="isActive" <c:out value="${department.isActive==1?'CHECKED':''}" /> value="1"> <fmt:message key="department.detail_active.label" /> &nbsp; <input class="FormElement ui-widget-content ui-corner-all" type="radio" name="isActive" <c:out value="${department.isActive==0?'CHECKED':''}" />	value="0"> <fmt:message key="department.detail_inactive.label" /></td>
			</tr>
		</table>
		<div class="submit">
			<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="department.detail_submit.label" />" /> 
		</div>
  </form>
  </div>

  <div>		
		<h1><fmt:message key="department.detail_administrators.label" /></h1>
		<div class="instructions"><fmt:message key="department.detail_update_admin.label" /></div>
		
		<form name="f" action="<c:url value='/department/user/roleAdd.do'/>" method="POST" onsubmit="return validate('createAdmin');">
		<input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>
			
		<table class="EditTable ui-widget ui-widget-content"> 
			<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="department.detail_administrator_name.label" /> /
			<fmt:message key="department.detail_createadmin.label" />:</td><td>&nbsp;</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" id="adminName" name='adminName' value='' /></td>
			</tr>
		</table>				
		<div class="submit">	
			<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="department.detail_submit.label" />" /> 
		</div>	
		</form>
	</div>
</sec:authorize>

<c:if test="${departmentuser.size() > 0}">		
<h1><fmt:message key="department.detail_existingadmin.label" /></h1>
<table class="EditTable ui-widget ui-widget-content">
<c:forEach items="${departmentuser}" var="u">
	<tr class="FormData">
	<td class="DataTD">
		<c:out value="${u.user.firstName}" />
		<c:out value="${u.user.lastName}" />
	</td>
	<sec:authorize access="hasRole('su')">
	 <c:if test="${departmentuser.size() > 1}"> <!-- cannot delete the department admin if there is only a single department admin -->
		<td class="submit">			
			<a href="<c:url value='department/user/roleRemove/${department.departmentId}/${u.user.userId}.do' />">
				<fmt:message key="department.detail_remove.label" />
			</a>
		</td>
	 </c:if>
	</sec:authorize>
	</tr>
</c:forEach>
</table>
</c:if>
<br />
		
<div>
 <%-- I do not believe this grid belongs here. --%>
 <h1><fmt:message key="department.detail_labs.label" /></h1>  
  <table id="grid_id"></table> 
 <div id="gridpager"></div>
</div>
