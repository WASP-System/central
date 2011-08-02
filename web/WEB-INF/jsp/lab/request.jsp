<%@ include file="/WEB-INF/jsp/include.jsp" %>

  <form name="f" action="<c:url value='/lab/request.do'/>" method="POST">
  <table><tr>
    <td>Primary I. Email</td>
    <td><input type='text' name='primaryuseremail' value=''/></td>
  </tr></table>
  <input type="submit" value="Request Access" />
  </form>


