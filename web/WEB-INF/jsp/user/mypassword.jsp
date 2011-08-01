<%@ include file="/WEB-INF/jsp/include.jsp" %>

<form method="POST" action="<c:url value="/user/mypassword.do"/>">
  <div>
  Old Password
  <input type="name" name="passwordold">
  </div>
  <div>
  New Password
  <input type="password" name="password">
  </div>
  <div>
  New Password Once Again
  <input type="password" name="password2">
  </div>

  <div>
  <input type="submit">
  </div>
</form>

