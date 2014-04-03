<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<sec:authorize access="hasRole('su') or hasRole('ga-*')">
	<div>
		<h1><fmt:message key="department.create.label" /></h1>
		<div class="instructions">
			<fmt:message key="department.create_instructions.label" />
		</div>

		<form name="f" action="<wasp:relativeUrl value='/department/create.do'/>" method="POST" >
			<table class="EditTable ui-widget ui-widget-content">
				<tr class="FormData"><td class="CaptionTD"><fmt:message key="department.list_department.label" />:</td><td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='departmentName' value=''/></td></tr>
				<tr class="FormData"><td class="CaptionTD"><fmt:message key="department.detail_administrator_name.label" />:</td><td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" id="adminName" name='adminName' value='' /></td></tr>
		 
				</table>
				<div class="submit">
					<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="department.list.data" />" />
 				</div>
			</form>
	</div>
</sec:authorize>


<h1><fmt:message key="department.list.label" /></h1>
<div class="instructions">
  <fmt:message key="department.list_instructions.label" />
</div>

<%-- 
<div class="action">
 	<c:choose>
		<c:when test='${departmentAdminPendingTasks == 0}'>
			No Pending Department Administrator Tasks
	 	</c:when>
	 	<c:otherwise>
		 	<a style="color:red" href="<wasp:relativeUrl value="/department/dapendingtasklist.do"/>"><c:out value="${departmentAdminPendingTasks}" /> Pending Department Administrator Task<c:if test='${departmentAdminPendingTasks != 1}'>s</c:if></a>
		</c:otherwise>
	</c:choose>
</div>
--%>

<table class="EditTable ui-widget ui-widget-content">
	<c:forEach items="${department}" var="d">
		<tr class="FormData">
		<td class="DataTD"><a href="<wasp:relativeUrl value='department/detail/${d.departmentId}.do' />">${d.name}</a></td>
		<td class="DataTD"><c:choose><c:when test="${d.isActive == 1}"> <fmt:message key="department.active.label" /></c:when><c:otherwise> <fmt:message key="department.inactive.label" /></c:otherwise></c:choose></td>
		</tr>
	</c:forEach>
</table>
