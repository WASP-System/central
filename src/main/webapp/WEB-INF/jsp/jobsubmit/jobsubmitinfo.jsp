<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<section id="jobDetails">
  <div class="info">
    <div class="item">
      <label>Job: </label>
      <span class="value"><c:out value="${jobDraftDb.name}" /></span>
    </div>
    <div class="item">
      <label>Lab: </label>
      <span class="value"><c:out value="${jobDraftDb.lab.name}" /></span>
    </div>
    <div class="item">
      <label>Workflow: </label>
      <span class="value"><c:out value="${jobDraftDb.workflow.name}" /></span>
    </div>
  </div>

  <nav id="jobSubmit">
    <a href="<c:url value="/jobsubmit/modify/${jobDraftDb.jobDraftId}.do"/>">modify</a>
    <a href="<c:url value="/jobsubmit/cells/${jobDraftDb.jobDraftId}.do"/>">next</a>
[TODO make dynamic]
  </nav>

</section>

