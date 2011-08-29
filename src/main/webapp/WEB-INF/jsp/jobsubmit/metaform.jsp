<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>New Job</title><head>
  <body>

    <font color="blue"><wasp:message /></font>

    <h1>Create a Job -- META</h1>

    <div>
    Job: <c:out value="${jobDraftDb.name}" />
    </div>
    <div>
    Lab: <c:out value="${jobDraftDb.lab.name}" />
    </div>
    <div>
    Workflow: <c:out value="${jobDraftDb.workflow.name}" />
    </div>

    <a href="<c:url value="/jobsubmit/modify/${jobDraft.jobDraftId}.do"/>">modify</a>


    <form:form command="jobDraft">
       <table>
          <c:set var="_area" value = "${parentarea}" scope="request"/>
          <c:set var="_metaArea" value = "${area}" scope="request"/>

          <c:set var="_metaList" value = "${jobDraft.jobDraftMeta}" scope="request" />

          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>

       </table>

    </form:form>
  </body>

</html>
