<%@ include file="/WEB-INF/jsp/include.jsp" %>

<div><font color="red"><wasp:message /></font></div>

<div>
  <c:forEach items="${userrole}" var="r">
    <c:out value="${r.user.login}" />
    <c:out value="${r.user.firstName}" />
    <c:out value="${r.user.lastName}" />
    <c:out value="${r.user.email}" />
    <c:out value="${r.role.name}" />
    <a href="<c:url value="/sysrole/remove/${r.userId}/${r.role.roleName}.do" />">[REMOVE]</a>
  </c:forEach>
</div>

<div>

  /// ADD SYSTEM USER
  <form method="POST" action="<c:url value="/sysrole/add.do"/>">
    <div>
    ///ROLE///

    <select name="roleName">
    <option value="" SELECTED> -- </option>
    <c:forEach items="${role}" var="r">
      <option value="<c:out value="${r.roleName}"/>">
         <c:out value="${r.name}"/>
      </option>
    </c:forEach>
    </select>
    </div>

    ///USER///

    <div>
    <select name="userId">
    <option value="" SELECTED> -- </option>
    <c:forEach items="${user}" var="u">
      <option value="<c:out value="${u.userId}"/>">
         <c:out value="${u.firstName}"/>
         <c:out value="${u.lastName}"/>
      </option>
    </c:forEach>
    </select>
    </div>
 

    <input type="submit">
  </form>

  
</div>






