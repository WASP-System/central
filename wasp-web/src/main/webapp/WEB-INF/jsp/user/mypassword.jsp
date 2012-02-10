<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:message />

<h1><fmt:message key="pageTitle.user/mypassword.label" /></h1>
<sec:authorize access="not hasRole('ldap')">

<div class="instructions">
    <fmt:message key="user.mypassword_instructions.label" />
</div>

<form name="f" action="<c:url value='/user/mypassword.do'/>" method="POST" onsubmit='return validate();'>
      <table class="data">
        <tr><td class="label"><fmt:message key="user.mypassword_oldpassword.label" /></td><td class="input"><input type='password' name='oldpassword' value=''/></td></tr>
        <tr><td class="label"><fmt:message key="user.mypassword_newpassword1.label" /></td><td class="input"><input type='password' name='newpassword1' value=''/></td></tr>
        <tr><td class="label"><fmt:message key="user.mypassword_newpassword2.label" /></td><td class="input"><input type='password' name='newpassword2' value=''/></td></tr>
      </table>
      <div class="submit"><input name="submit" type="submit" value="<fmt:message key="user.mypassword_submit.label" />"/></div>
</form>
</sec:authorize>

	 <sec:authorize access="hasRole('ldap')">
<div class="instructions error">
	<font color="red"><fmt:message key="user.mypassword_cannotChange.error" /></font>
</div>
	 </sec:authorize>

