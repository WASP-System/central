<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>New User</title><head>
  <body>

    <h1>Jobs in Draft</h1>

   <c:forEach var="jd" items="${jobdrafts}">
     <div>
     <c:out value="${jd.lab.name}" />  
     <a href="<c:url value="/jobsubmit/modify/${jd.jobDraftId}.do"/>">
       <c:out value="${jd.name}" /> 
     </a>
     <c:out value="${jd.createts}" /> 
     </div>
   </c:forEach>

    <a href="<c:url value="/jobsubmit/create.do"/>">Create a New Job</a>
  </body>
</html>
