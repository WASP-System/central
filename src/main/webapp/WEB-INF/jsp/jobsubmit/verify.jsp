<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>New User</title><head>
  <body>

    <h1>Jobs in Draft</h1>

    <div>
    Job: <c:out value="${jobDraft.name}" />
    </div>
    <div>
    Lab: <c:out value="${jobDraft.lab.name}" />
    </div>
    <div>
    Workflow: <c:out value="${jobDraft.workflow.name}" />
    </div>

    <c:set var="_area" value = "${parentarea}" scope="request"/>
    <c:set var="_metaArea" value = "${area}" scope="request"/>
    <c:set var="_metaList" value = "${jobDraft.jobDraftMeta}" scope="request" />

    <table>
    <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
    </table>

    <form method="POST" action="<c:url value="/jobsubmit/submit/${jobDraft.jobDraftId}.do" />">
      <input type="submit"/>
    </form>


  </body>
</html>
