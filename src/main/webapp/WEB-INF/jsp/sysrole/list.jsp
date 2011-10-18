<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<div>
	 <h1><fmt:message key="pageTitle.sysrole/list.label" /></h1>
	<font color="red"><wasp:message /></font></div>

<div>
  <h3><fmt:message key="sysrole.list_current.label" /></h3>
  <c:forEach items="${userrole}" var="r">
    <c:out value="${r.user.firstName}" /> <c:out value="${r.user.lastName}" /> (<c:out value="${r.user.login}" />): role '<c:out value="${r.role.name}" />'
    <a href="<c:url value="/sysrole/remove/${r.userId}/${r.role.roleName}.do" />">[REMOVE]</a><br />
  </c:forEach>
</div>

<div>

  <h3><fmt:message key="sysrole.list_create.label" /></h3>
  <form method="POST" action="<c:url value="/sysrole/add.do"/>" onsubmit='return validate();'>
    <div>
    <fmt:message key="sysrole.list_sysuser_role.label" />: 
    <select name="roleName">
    <option value="" SELECTED><fmt:message key="wasp.default_select.label"/></option>
    <c:forEach items="${role}" var="r">
      <option value="<c:out value="${r.roleName}"/>">
         <c:out value="${r.name}"/>
      </option>
    </c:forEach>
    </select>
    </div>

    <div>
    <fmt:message key="sysrole.list_sysuser_name.label" />: <input type="text" id="userId" name="userId" />
    </div>
 

    <input type="submit" value="<fmt:message key="sysrole.list_submit.label" />" />
  </form>

  
</div>






