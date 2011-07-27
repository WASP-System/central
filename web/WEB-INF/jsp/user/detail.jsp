<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    
    <h1>WASP</h1>
    <p><c:out value="${now}"/></p>
    <hr> 
    <form:form commandName="user">
    
     <table>
     	  <tr><td colspan=2 align=left></br><b>User Details:</b></td></tr>	
          <tr>
              <td><fmt:message key="user.label.firstName"/>:</td>
              <td><form:input path="firstName" /></td>
              <td><form:errors path="firstName" /></td>
          </tr>
          <tr>
              <td><fmt:message key="user.label.lastName"/>:</td>
              <td><form:input path="lastName" /></td>
              <td><form:errors path="lastName" /></td>
          </tr>
          <tr>
              <td><fmt:message key="user.label.email"/>:</td>
              <td><form:input path="email" /></td>
              <td><form:errors path="email" /></td>
          </tr>         
          <c:forEach items="${user.usermeta}" var="meta" varStatus="status">
          <tr>
      
            <td><fmt:message key="user.label.${fn:replace(meta.k, \"user.\", \"\")}"/>:</td>
            <td><input name="usermeta_${meta.k}" value="${meta.v}" /></td>
            <td><form:errors path="usermeta[${status.index}].k" /> </td>
            
          </tr>
          </c:forEach>
           <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>       
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
       
     </table>
  
    </form:form>
    
   

  
    [inactivate]
    [change password]
  </body>
</html>
