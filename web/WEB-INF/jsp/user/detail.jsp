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
     	   <c:if  test="${user.userId == 0}">
     	   <tr>
              <td><fmt:message key="user.label.login" />:</td>
              <td><form:input path="login" /></td>
              <td><form:errors path="login"/></td>
          </tr>
          <tr>
              <td><fmt:message key="user.label.password"/>:</td>
              <td><form:input path="password" /></td>
              <td><form:errors path="password" /></td>
          </tr>     	   
     	   </c:if>	
          <tr>
              <td><fmt:message key="user.label.firstName" />:</td>
              <td><form:input path="firstName" /></td>
              <td><form:errors path="firstName"/></td>
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
          <tr>
              <td><fmt:message key="user.label.locale"/>:</td>
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
          <c:forEach items="${user.usermeta}" var="meta" varStatus="status">
          <tr>
       
            <td><fmt:message key="user.label.${fn:replace(meta.k, \"user.\", \"\")}"/>:</td>
            <td>
            <c:if test="${not empty meta.property.control}">
            <select name="usermeta_${meta.k}">
                <option value=''>-- select --</option>
                <c:forEach var="option" items="${meta.property.control.options}">
                <option value="${option.value}" <c:if test="${option.value == meta.v}"> selected</c:if>><c:out value="${option.label}"/></option>
                </c:forEach>               	              
            </select>
            </c:if>
            <c:if test="${empty meta.property.control}"><input name="usermeta_${meta.k}" value="${meta.v}" /></c:if>
            </td>            	           
            <td><form:errors path="usermeta[${status.index}]" /> </td>            
          </tr>
          </c:forEach>
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
   
  
    [inactivate]
    [change password]
  </body>
</html>
