<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<h1>Pending Lab List</h1>
<table cellpadding="0" cellspacing="0" border="0">
	<c:forEach items="${lab}" var="l">
		<!-- sec:authorize access="
     hasRole('su') or
     hasRole('da-' + ${l.departmentId})
   "-->
		<tr class="FormData">
			<td class="DataTD">
				<a href="<c:url value='lab/detail/${l.labId}.do' />">
				<c:out value="${l.name}" />
			</a></td>
		</tr>
		<!-- /sec:authorize-->
	</c:forEach>
</table>

