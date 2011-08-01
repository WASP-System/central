<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>New User</title><head>
  <body>
    
    <h1>New User</h1>
    <form:form commandName="user">
      <table>
     	  <tr><td colspan=2 align=left></br><b>User Details:</b></td></tr>
     	   <c:if test="${user.userId == 0}">
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

<tr valign="top">
  <td>Lab</td>
  <td>
    <div>
      <select name="existing" onchange="
        var target = (this.options[this.selectedIndex].value == '1' ?'existing':'new');
        var other = (this.options[this.selectedIndex].value == '1' ?'new':'existing');
        document.getElementById(target).style.display  = 'block';
        document.getElementById(other).style.display  = 'none';
      ">
      <option value="1" selected="selected">Existing Lab</option>
      <option value="0">New Lab </option>
      </select>
    </div>

    <div id="existing">
       <h2>Existing Lab</h2>
       P.I. Email address <input type="name" name="piEmail">
    </div>

    <div id="new" style="display:none">
       <h2>New Lab</h2>
    
       <div> 
       Lab Name
       <input type="name" name="labname"/>
       </div>

       <div> 
       Department
       <select name="labdepartment">
         <c:forEach items="${departments}" var="d"> 
           <option value="<c:out value="${d.departmentId}" />"><c:out value="${d.name}"/></option>
         </c:forEach>
       </select>
       </div>
    </div>
  </td>
</tr>

           <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>    
     </table>
  
    </form:form>
  
  </body>
</html>
