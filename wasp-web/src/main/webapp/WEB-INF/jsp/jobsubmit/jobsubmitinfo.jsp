<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<section id="jobDetails">
  <div class="info">
    <div class="item">
      <label>Job: </label>
      <span class="value"><c:out value="${jobDraft.name}" /></span>
    </div>
    <div class="item">
      <label>Lab: </label>
      <span class="value"><c:out value="${jobDraft.lab.name}" /></span>
    </div>
    <div class="item">
      <label>Workflow: </label>
      <span class="value"><c:out value="${jobDraft.workflow.name}" /></span>
    </div>
  </div>

  <nav id="jobSubmit">
    <c:forEach items="${pageFlowMap}" var="entry" varStatus="status">
      <a href="<c:url value="${entry[0]}" />.do"><c:out value="${entry[1]}"/></a>
      <c:if test="${status.last != (1==1)}" >
 		<c:out value=">" /> 
      </c:if> 
    </c:forEach>
  </nav>

</section>

