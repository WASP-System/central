<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<div>
	 <h1><fmt:message key="pageTitle.sysrole/list.label" /></h1>
	<font color="red"><wasp:message /></font></div>

<div>
  <h3><fmt:message key="sysrole.list_current.label" /></h3>
  <table>
  <tr>
  	<th>Name (Login Name)</th>
  	<th>Role</th>
  	<th>Action</th>
  </tr>
  <c:forEach items="${userRoleMap}" var="rs">
	  <c:forEach items="${rs.value}" var="r">
	  	<tr>
		    <td><c:out value="${rs.key}" /></td>
		    <td><c:out value="${r.role.name}" /></td>
		    <td><a href="<c:url value="/sysrole/remove/${r.userId}/${r.role.roleName}.do" />">[Remove Role]</a></td>
	    </tr>
	  </c:forEach>
	</c:forEach>
  </table>
</div>

<div>

  <h3><fmt:message key="sysrole.list_create.label" /></h3>
  <form method="POST" action="<c:url value="/sysrole/add.do"/>" onsubmit='return validate();'>
  <table>
    <tr>
    	<td><fmt:message key="sysrole.list_sysuser_role.label" />: </td>
    	<td><select name="roleName">
		    <option value="" SELECTED><fmt:message key="wasp.default_select.label"/></option>
		    <c:forEach items="${role}" var="r">
		      <option value="<c:out value="${r.roleName}"/>">
		         <c:out value="${r.name}"/>
		      </option>
		    </c:forEach>
		    </select>
		</td>
    </tr>
    <tr>
	    <td><fmt:message key="sysrole.list_sysuser_name.label" />: </td>
	    <td><input type="text" id="userHook" name="userHook" /></td>
    </tr>
 
	<tr>
		<td colspan="2"> <input type="submit" value="<fmt:message key="sysrole.list_submit.label" />" /></td>
	</tr>
  </table>
    </form>

  
</div>






