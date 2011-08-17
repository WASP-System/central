<%@ include file="/WEB-INF/jsp/include.jsp" %>


<h1><fmt:message key="labuser.request.title" /></h1>

<font color="blue"><wasp:message /></font>

  <form name="f" action="<c:url value='/lab/request.do'/>" method="POST">
  <table><tr>
    <td><fmt:message key="labuser.request.primaryuser.label" /></td>
    <td><input type='text' name='primaryuseremail' value=''/></td>
  </tr></table>
  <input type="submit" value="<fmt:message key="labuser.request.submit.label" />" />
  </form>


