<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<sec:authorize access="hasRole('god') or hasRole('ga-*')">
	<div>
	  <h1><fmt:message key="department.create.label" /></h1>
	  <font color="red"><wasp:message /></font>
    	<form name="f" action="<c:url value='/department/create.do'/>" method="POST" onsubmit='return validate();'>
    	<table cellpadding="3" cellspacing="3" border="0">
      		<tr><td><fmt:message key="department.list_department.label" />:</td><td><input type='text' name='departmentName' value=''/></td></tr>
      		 <tr><td><fmt:message key="department.detail_administrator_name.label" />:</td><td><input id="adminName" name='adminName' value='' /></td></tr>
     
      		<tr><td colspan='2'><input type="submit" value="<fmt:message key="department.list.data" />" /></td></tr>
      	</table>
    	</form>
	</div>
</sec:authorize>

	<h1><fmt:message key="department.list.label" /></h1>
	<table cellpadding="3" cellspacing="3" border="0">
	<tr><td colspan="2" style="text-align:center">    
    	<b>
 		 <c:choose>
   			<c:when test='${departmentAdminPendingTasks == 0}'>No Pending Department Administrator Tasks
   			</c:when>
   			<c:otherwise>
   			<a style="color:red" href="<c:url value="/department/dapendingtasklist.do"/>"><c:out value="${departmentAdminPendingTasks}" /> Pending Department Administrator Task<c:if test='${departmentAdminPendingTasks != 1}'>s</c:if></a>
   			</c:otherwise>
  		</c:choose>
  		</b> 
  		</br> </br> 
  	</td></tr>    
    
	<c:forEach items="${department}" var="d">
		<div>
		<tr>
		<td><a href="/wasp/department/detail/<c:out value="${d.departmentId}" />.do"><c:out value="${d.name}" /></a></td>
		<td><c:choose><c:when test="${d.isActive == 1}"> active</c:when><c:otherwise> inactive</c:otherwise></c:choose></td>
		</tr>
	</c:forEach>
</table>
