<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head>
   <title><fmt:message key="department.detail.title" /></title>
   <script language="JavaScript">
		<!--
		function validate(){
			//alert("test123"); return false;
			var error = false;
			var message = '<fmt:message key="department.detail.missingparam.error" />';
			if(document.f.useremail.value == ""){
				error = true
				document.f.useremail.focus();
			}
			if(error){ alert(message); return false; }
  			return true;
		}
		//-->
	</script>  
  <head>
  <body>
    <h1><c:out value="${department.name}"/></h1>

    <h2><fmt:message key="department.detail.administrators.label" /></h2>
    
    <div>
    <h3><fmt:message key="department.detail.createadmin.label" /></h3>
    <font color="red"><wasp:message /></font>
    <form name="f" action="<c:url value='/department/user/roleAdd.do'/>" method="POST" onsubmit='return validate();'>
      <fmt:message key="department.detail.email.label" />: 
      <input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>
      <input type='text' name='useremail' value=''/>
      <input type="submit" value="<fmt:message key="department.detail.submit" />" /> 
    </form>
    </div>
    <h3><fmt:message key="department.detail.existingadmin.label" /></h3>
    <c:forEach items="${departmentuser}" var="u">
      <p>
      <a href="/wasp/user/detail/<c:out value="${u.user.userId}" />.do"><c:out value="${u.user.login}" /></a>
        <c:out value="${u.user.firstName}" />
        <c:out value="${u.user.lastName}" />
        <a href="/wasp/department/user/roleRemove/<c:out value="${department.departmentId}" />/<c:out value="${u.user.userId}" />.do">
          <fmt:message key="department.detail.remove" />
        </a>
      </p>
    </c:forEach>

    

    <h2><fmt:message key="department.detail.labs.label" /></h2>
    <c:forEach items="${lab}" var="l">
      <div>
        <a href="/wasp/lab/detail_ro/<c:out value="${department.departmentId}" />/<c:out value="${l.labId}" />.do">
        <c:out value="${l.name}" />
        </a>
      </div>
    </c:forEach>

    <h2><fmt:message key="department.detail.pendinglabs.label" /></h2>
    <c:forEach items="${labpending}" var="lp">
      <div>
      	<a href="/wasp/lab/pending/detail_ro/<c:out value="${department.departmentId}" />/<c:out value="${lp.labPendingId}" />.do">
      	<c:out value="${lp.name}" />
      	</a>
      </div>
    </c:forEach>

  </body>
</html>
