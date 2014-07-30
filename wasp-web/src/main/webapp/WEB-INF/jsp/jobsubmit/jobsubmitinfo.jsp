<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<section id="jobDetails">
  <div class="info">
    <div class="item">
      <label><fmt:message key="jobDraft.info_job.label" />: </label>
      <span class="DataTD"><c:out value="${jobDraft.name}" /></span>
    </div>
    <div class="item">
      <label><fmt:message key="jobDraft.info_lab.label" />: </label>
      <span class="DataTD"><c:out value="${jobDraft.lab.name}" /></span>
    </div>
    <div class="item">
      <label><fmt:message key="jobDraft.info_workflow.label" />: </label>
      <span class="DataTD"><c:out value="${jobDraft.workflow.name}" /></span>
    </div>
  </div>

  <nav id="jobSubmit">
    <c:forEach items="${pageFlowMap}" var="entry" varStatus="status">
      <c:if test="${status.first == (1==1)}" >
      	<span style="color:red; font-weight:bold; font-size: larger"><fmt:message key="jobDraft.info_navigate.label" />: </span>
      </c:if>
      <a href="<wasp:relativeUrl value="${entry[0]}" />.do"><c:out value="${entry[1]}"/></a>
      <c:if test="${status.last != (1==1)}" >
 		<c:out value=">" /> 
      </c:if> 
    </c:forEach>
  </nav>

</section>

