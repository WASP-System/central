<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>New User</title><head>
  <body>

    <h1>Jobs in Draft</h1>

   [forEach]
   [/forEach]

    <a href="<c:url value="/jobsubmit/create.do"/>">Create a New Job</a>
  </body>
</html>
