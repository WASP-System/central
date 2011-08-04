<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>User Detail</title><head>
  <body>
    
    <h1>User Detail</h1>

    <font color="blue"><wasp:message /></font>  
    <form:form commandName="user">
    
     <table>
     	  <tr><td colspan=2 align=left></br><b>User Details:</b></td></tr>
     	   <c:if  test="${user.userId == 0}">
     	   <tr>
              <td><fmt:message key="user.login.label" />:</td>
              <td><form:input path="login" /></td>
              <td><form:errors path="login"/></td>
          </tr>
          <tr>
              <td><fmt:message key="user.password.label"/>:</td>
              <td><form:password path="password" /></td>
              <td><form:errors path="password" /></td>
          </tr>     	   
     	   </c:if>	
          <tr>
              <td><fmt:message key="user.firstName.label" />:</td>
              <td><form:input path="firstName" /></td>
              <td><form:errors path="firstName"/></td>
          </tr>
          <tr>
              <td><fmt:message key="user.lastName.label"/>:</td>
              <td><form:input path="lastName" /></td>
              <td><form:errors path="lastName" /></td>
          </tr>
          <tr>
              <td><fmt:message key="user.email.label"/>:</td>
              <td><form:input path="email" /></td>
              <td><form:errors path="email" /></td>
          </tr>         
          <tr>
              <td><fmt:message key="user.locale.label"/>:</td>
              <td>
              <select name=locale>
                <option value=''>-- select --</option>
              	<option value=en_US <c:if test="${user.locale == 'en_US'}"> selected</c:if>>USA</option>
              	<option value=iw_IL <c:if test="${user.locale == 'iw_IL'}"> selected</c:if>>Hebrew</option>
              	<option value=ru_RU <c:if test="${user.locale == 'ru_RU'}"> selected</c:if>>Russian</option>
              </select>
              </td>
              <td><form:errors path="locale" /></td>
          </tr>
          <c:set var="_area" value = "user" scope="request"/>	
		  <c:set var="_metaList" value = "${user.usermeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>    
         <c:if  test="${user.userId > 0}">
         <tr><td colspan=2 align=left></br><b>Lab Users:</b></td></tr>
         <c:forEach items="${user.labUser}" var="ul">
    	  <tr>
            <td><a href="/wasp/lab/detail/<c:out value="${ul.lab.labId}" />.do"><c:out value="${ul.lab.name}" /></a></td>
            <td><span><c:out value="${ul.role.name}" /></span></td>
          </tr>
        </c:forEach>
          
		 <tr><td colspan=2 align=left></br><b>Samples:</b></td></tr>
         <c:forEach items="${user.sample}" var="sample">
    	  <tr>
            <td><a href="/wasp/sample/detail/<c:out value="${sample.sampleId}" />.do"><c:out value="${sample.name}" /></a></td>           
          </tr>
        </c:forEach>
		
         <tr><td colspan=2 align=left></br><b>Jobs:</b></td></tr>
         <c:forEach items="${user.job}" var="job">
    	  <tr>
            <td><a href="/wasp/job/detail/<c:out value="${job.jobId}" />.do"><c:out value="${job.name}" /></a></td>           
          </tr>
        </c:forEach>  
       </c:if>
     </table>
  
    </form:form>
   
 
  </body>
</html>
