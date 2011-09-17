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

<hr>
 <c:forEach items="${sampleDraft}" var="sd">
   <div>
     Sample:
     <c:out value="${sd.name}" />
     <c:forEach items="${sd.sampleDraftMeta}" var="sdm">
       <c:if test="${sdm.v != ''}">
         <div>
           <fmt:message key="${sdm.k}" />:
           <c:out value="${sdm.v}" />
         </div>
       </c:if>
     </c:forEach>

     <c:if test="${! empty(sd.fileId)}">
       FILE
       <c:out value="${sd.file.filelocation}" />
     </c:if>
   </div>
   <hr>
 </c:forEach>


    <form method="POST" action="<c:url value="/jobsubmit/submit/${jobDraft.jobDraftId}.do" />">
      <input type="submit"/>
    </form>


  </body>
</html>
