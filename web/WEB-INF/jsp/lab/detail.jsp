<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
  <form:form commandName="lab">
   <table>
     	  <tr><td colspan=2 align=left></br><b>Lab Details:</b></td></tr>
           <tr>
              <td><fmt:message key="lab.name.label" />:</td>
              <td><form:input path="name" /></td>
              <td><form:errors path="name"/></td>
          </tr>
           <tr>
              <td><fmt:message key="lab.primaryuser.label" />:</td>
              <td>${primaryuser.lastName}, ${primaryuser.firstName}</td>              
          </tr> 
          <tr>
              <td><fmt:message key="lab.departmentId.label"/>:</td>
              <td>
              <select name=departmentId>
                <option value=''>-- select --</option>
              	<c:forEach var="dept" items="${departments}">
                	<option value="${dept.departmentId}" <c:if test="${dept.departmentId == lab.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
               	</c:forEach>     
              </select>
              </td>
              <td><form:errors path="departmentId" /></td>
          </tr>
          <c:forEach items="${lab.labmeta}" var="meta" varStatus="status">
          <tr>      
            <td><fmt:message key="lab.${fn:replace(meta.k, \"lab.\", \"\")}.label"/>:</td>
            <td>
            <c:if test="${not empty meta.property.control}">
            	<c:if test="${meta.property.control.items != null}">            	
            		<c:set var="selectItems" scope="request" value="${requestScope[meta.property.control.items]}"/>
            		<c:set var="itemValue" scope="request">${meta.property.control.itemValue}</c:set>
            		<c:set var="itemLabel" scope="request">${meta.property.control.itemLabel}</c:set>                       	
            	</c:if>
            	<c:if test="${meta.property.control.items == null}">
            		<c:set var="selectItems" scope="request" value="${meta.property.control.options}" />
            		<c:set var="itemValue" scope="request">value</c:set>
            		<c:set var="itemLabel" scope="request">label</c:set>            	
            	</c:if>
            	 
            	 <select name="labmeta_${meta.k}">
	                <option value=''>-- select --</option>
                	<c:forEach var="option" items="${selectItems}">
                	<option value="${option[itemValue]}" <c:if test="${option[itemValue] == meta.v}"> selected</c:if>><c:out value="${option[itemLabel]}"/></option>
                	</c:forEach>               	              
	            </select>
            
            </c:if>
            <c:if test="${empty meta.property.control}"><input name="labmeta_${meta.k}" value="${meta.v}" /></c:if>
            </td>            	           
            <td><form:errors path="labmeta[${status.index}]" /> </td>            
          </tr>
          </c:forEach>
          <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>    
            	      
   </table> 
    <c:forEach items="${labuser}" var="ul">
      <p>
      <a href="/wasp/user/detail/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
      <span><c:out value="${ul.role.name}" /></span>
      </p>
    </c:forEach>

    <c:if test="${!  empty( job ) }">   
      <h2>Jobs</h2>
      <c:forEach items="${job}" var="j">
        <div>
          <a href="/wasp/job/detail/<c:out value="${j.jobId}" />.do">
          <c:out value="${j.name}" />
          </a>
        </div>
      </c:forEach>
    </c:if>

    <div>
      <a href="/wasp/job/create/form.do?labid=<c:out value="${lab.labId}" />">create job</a>
    </div>
   </form:form>
  </body>
</html>
