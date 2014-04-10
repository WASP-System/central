<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<html>
  <h1><c:out value="${sample.name}"/></h1>
  <div><c:out value="${sample.sampleType.name}"/></div>


  <%-- [TODO ADD PERM]--%>
  <a href="<wasp:relativeUrl value="sampleDnaToLibrary/detail/${sample.sampleId}.do"/>"><fmt:message key="sample.detail_facManSampleToLib.label" /></a>
  <%--[/TODO ADD PERM]--%>

  <c:forEach items="${samplemeta}" var="meta">
      <p>
      <span><c:out value="${meta.k}" /></span>
      <span><c:out value="${meta.v}" /></span>
      </p>
  </c:forEach>


    <h2><fmt:message key="sample.detail_relations.label" /></h2>
    <h3><fmt:message key="sample.detail_parents.label" /></h3>
    <c:forEach items="${parentsample}" var="s">
      <a href="<wasp:relativeUrl value='sample/detail/${s.sampleId}.do' />">
        <c:out value="${s.sample.name}"/>
      </a>
      <div><c:out value="${s.sample.sampleType.name}"/></div>
    </c:forEach>
    <h3><fmt:message key="sample.detail_children.label" /></h3>
    <c:forEach items="${childsample}" var="s">
      <a href="<wasp:relativeUrl value='sample/detail/${s.sampleId}.do' />">
        <c:out value="${s.sample.name}"/>
      </a>
      <div><c:out value="${s.sample.sampleType.name}"/></div>
    </c:forEach>

    <h2><fmt:message key="sample.detail_jobs.label" /></h2>
    <c:forEach items="${jobsample}" var="j">
      <p>
      <c:out value="${s.sample.sampleType.name}"/>
      <a href="<wasp:relativeUrl value='job/detail/${j.jobId}.do' />">
        <c:out value="${j.job.name}"/>
      </a>
      </p>
    </c:forEach>

    <h2><fmt:message key="sample.detail_files.label" /></h2>
    <c:forEach items="${samplefile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="<wasp:relativeUrl value='sample/file/${f.sampleId}/${f.fileId}.do' />">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>

