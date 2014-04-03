<%@ include file="/WEB-INF/jsp/taglib.jsp" %>



<h1><fmt:message key="pageTitle.user/mypassword.label" /></h1>
<sec:authorize access="not hasRole('ldap')">

<div class="instructions">
    <fmt:message key="user.mypassword_instructions.label" />
</div>

<form name="f" action="<wasp:relativeUrl value='/user/mypassword.do'/>" method="POST" onsubmit='return validate();'>
      <table class="EditTable ui-widget ui-widget-content">
        <tr class="FormData"><td class="CaptionTD"><fmt:message key="user.mypassword_oldpassword.label" />:</td><td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='password' name='oldpassword' value=''/></td></tr>
        <tr class="FormData"><td class="CaptionTD"><fmt:message key="user.mypassword_newpassword1.label" />:</td><td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='password' name='newpassword1' value=''/></td></tr>
        <tr class="FormData"><td class="CaptionTD"><fmt:message key="user.mypassword_newpassword2.label" />:</td><td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='password' name='newpassword2' value=''/></td></tr>
      </table>
      <div class="submit"><input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit" value="<fmt:message key="user.mypassword_submit.label" />"/></div>
</form>
</sec:authorize>

	 <sec:authorize access="hasRole('ldap')">
<div class="instructions error">
	<font color="red"><fmt:message key="user.mypassword_cannotChange.error" /></font>
</div>
	 </sec:authorize>

