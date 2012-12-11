<%@ include file="/WEB-INF/jsp/include.jsp" %>

<form method="POST" action="<c:out value="${c}"/>/user/mypassword.do">
  <div>
  <fmt:message key="lab.oldPassword.label" />
  <input class="FormElement ui-widget-content ui-corner-all" type="text" name="username">
  </div>
  <div>
  <fmt:message key="lab.newPassword.label" />
  <input class="FormElement ui-widget-content ui-corner-all" type="password" name="password">
  </div>
  <div>
  <fmt:message key="lab.newPasswordConfirm.label" />
  <input class="FormElement ui-widget-content ui-corner-all" type="password" name="password2">
  </div>

  <div>
  <input class="FormElement ui-widget-content ui-corner-all" type="submit">
  </div>
</form>

