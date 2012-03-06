<%@ include file="/WEB-INF/jsp/include.jsp" %>

<form method="POST" action="<c:out value="${c}"/>/user/mypassword.do">
  <div>
  Old Password
  <input class="FormElement ui-widget-content ui-corner-all" type="name" name="username">
  </div>
  <div>
  New Password
  <input class="FormElement ui-widget-content ui-corner-all" type="password" name="password">
  </div>
  <div>
  New Password Once Again
  <input class="FormElement ui-widget-content ui-corner-all" type="password" name="password2">
  </div>

  <div>
  <input class="FormElement ui-widget-content ui-corner-all" type="submit">
  </div>
</form>

