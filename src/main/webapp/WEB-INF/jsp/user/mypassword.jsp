<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
	  <h1><fmt:message key="pageTitle.user/mypassword.label" /></h1>
	  <sec:authorize access="not hasRole('ldap')">
	    <h4><fmt:message key="user.mypassword_instructions.label" /></h4>

     	<font color="red"><wasp:message /></font>
	    <form name="f" action="<c:url value='/user/mypassword.do'/>" method="POST" onsubmit='return validate();'>
	
	      <table>
	        <tr><td><fmt:message key="user.mypassword_oldpassword.label" /></td><td><input type='password' name='oldpassword' value=''/></td></tr>
	        <tr><td><fmt:message key="user.mypassword_newpassword1.label" /></td><td><input type='password' name='newpassword1' value=''/></td></tr>
	        <tr><td><fmt:message key="user.mypassword_newpassword2.label" /></td><td><input type='password' name='newpassword2' value=''/></td></tr>
	        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="user.mypassword_submit.label" />"/></td></tr>
	      </table>
	    </form>
	 </sec:authorize>
	 <sec:authorize access="hasRole('ldap')">
	 	<font color="red"><wasp:message /></font>
	 	<font color="red"><fmt:message key="user.mypassword_cannotChange.error" /></font>
	 </sec:authorize>

