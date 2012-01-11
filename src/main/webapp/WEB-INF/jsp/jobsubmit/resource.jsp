<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    <font color="blue"><wasp:message /></font>

    <h1>Create a Job -- <c:out value="${name}" /></h1>

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

    <form method="POST">

<select name="changeResource" onchange="this.parentNode.submit()">
<option></option>
<c:forEach items="${workflowResources}" var="w">
  <option value="<c:out value="${w.resource.resourceId}" />">
    <c:out value="${w.resource.platform}" />
    <c:out value="${w.resource.name}" />
  </option>
</c:forEach>
</select>
</form>

    <form:form command="jobDraft">
<input type="hidden" name="name" value="<c:out value="${jobDraftDb.name}"/>">
<input type="hidden" name="workflowId" value="<c:out value="${jobDraftDb.workflowId}"/>">
<input type="hidden" name="labId" value="<c:out value="${jobDraftDb.labId}"/>">
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
