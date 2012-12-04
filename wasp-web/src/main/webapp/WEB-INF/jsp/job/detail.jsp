<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


    <h1>
      <c:out value="${job.name}"/>
    </h1>

    <p>
      <fmt:message key="job.detail_lab.label" />
      <a href="<c:url value="/lab/detail_ro/${job.lab.departmentId}/${job.lab.labId}.do"/>">
        <c:out value="${job.lab.name}"/>
      </a>
    </p>

    <p>
      <fmt:message key="job.detail_submittingUser.label" />
      <a href="<c:url value="/user/detail_ro/${job.user.userId}.do"/>">
        <c:out value="${job.user.login}"/>
      </a>
      <c:out value="${job.user.firstName}"/>
      <c:out value="${job.user.lastName}"/>
    </p>


    <fmt:message key="job.detail_jobViewer.label" />
    <c:forEach items="${jobuser}" var="u">
      <p>
      <c:if test="${u.role.roleName == 'jv'}">
        <a href="/wasp/user/detail/<c:out value="${u.user.userId}" />.do"><c:out value="${u.user.login}" /></a>
        <c:out value="${u.user.firstName}" />
        <c:out value="${u.user.lastName}" />
          <a href="/wasp/job/user/roleRemove/<c:out value="${job.labId}" />/<c:out value="${job.jobId}" />/<c:out value="${u.user.userId}" />.do">
           <fmt:message key="job.detail_remove.label" />
          </a>
      </c:if>
      </p>
    </c:forEach>
    
    <sec:authorize access="hasRole('su') or hasRole('lm-${job.lab.labId}') or hasRole('js-${job.jobId}')">
    
    <form name="f" action="<c:url value='/job/user/roleAdd.do'/>" method="POST" onsubmit="return validate();" >
      <fmt:message key="job.detail_loginName.label" />:
      <input type='hidden' name='labId' value='<c:out value="${job.lab.labId}" />'/>
      <input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
      <input class="FormElement ui-widget-content ui-corner-all" type='text' id='login' name='login' value=''/>
      <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="job.detail_addJobViewer.label" />" />
    </form>
	</sec:authorize>	

    <c:forEach items="${jobmeta}" var="meta">
      <p>
      <span><c:out value="${meta.k}" /></span>
      <span><c:out value="${meta.v}" /></span>
      </p>
    </c:forEach>


    <h2><fmt:message key="job.detail_samples.label" /></h2>
    <c:forEach items="${jobsample}" var="s">
      <p>
      <c:out value="${s.sample.sampleType.name}"/>
      <a href="/wasp/sample/detail/<c:out value="${s.sampleId}"/>.do">
        <c:out value="${s.sample.name}"/>
      </a>
      </p>
    </c:forEach>

    <h2><fmt:message key="job.detail_files.label" /></h2>
    <c:forEach items="${jobfile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="/wasp/job/file/<c:out value="${f.jobId}"/>/<c:out value="${f.fileId}"/>.do">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>
