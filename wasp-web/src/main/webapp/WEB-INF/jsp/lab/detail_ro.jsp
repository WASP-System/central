<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <p></p>

	<h1><fmt:message key="pageTitle.lab/detail_ro.label" />: ${lab.name}</h1>
	
    <table class="EditTable ui-widget ui-widget-content">
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="lab.name.label" />:</td>
        <td class="DataTD">${lab.name}</td>              
      </tr>
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="lab.primaryUserId.label"/>:</td>
        <td class="DataTD">
        <c:out value="${puserFullName}"/>
        </td>             
      </tr>
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="lab.departmentId.label"/>:</td>
        <td class="DataTD"><c:out value="${lab.department.name}"/></td>
    </tr>
          <c:set var="_area" value = "lab" scope="request"/>	
		  <c:set var="_metaList" value = "${lab.labMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
	</table>
	<br />
	<sec:authorize access="hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-${lab.departmentId}') or hasRole('pi-${lab.labId}')">
    <div class="submit">
      <a href="<wasp:relativeUrl value="lab/detail_rw/${lab.departmentId}/${lab.labId}.do" />">Edit</a>
     </div>
	</sec:authorize>

	<table class="EditTable ui-widget ui-widget-content">
    <c:forEach items="${labuser}" var="ul">
			<tr class="FormData">
      <td class="action">
      <a href="<wasp:relativeUrl value='user/detail/${ul.user.userId}.do' />">${ul.user.login}</a>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
      <span><c:out value="${ul.role.name}" /></span>
      </td>
			</tr>
    </c:forEach>
	</table>

  <c:if test="${!  empty( job ) }">   
    <h2><fmt:message key="lab.jobs.label"/></h2>
		<table class="EditTable ui-widget ui-widget-content">
    <c:forEach items="${job}" var="j">
			<tr class="FormData">
      <td class="action">
        <a href="<wasp:relativeUrl value='job/detail/${j.jobId}.do' />">
        <c:out value="${j.name}" />
        </a>
      </td>
			</tr>
    </c:forEach>
    </table>
  </c:if>

