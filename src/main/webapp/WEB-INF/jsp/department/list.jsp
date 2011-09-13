<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title><fmt:message key="department.list.title" /></title>
  <script language="JavaScript">
		<!--
		function validate(){
			//alert("test123"); return false;
			var error = false;
			var message = '<fmt:message key="department.list.missingparam.error" />';
			if(document.f.name.value == ""){
				error = true
				document.f.name.focus();
			}
			if(error){ alert(message); return false; }
  			return true;
		}
		//-->
	</script>  
	 </head>

  <body onload='document.f.name.focus();'>

	<div>
	  <h1><fmt:message key="department.list.create.label" /></h1>
	  <font color="red"><wasp:message /></font>
    	<form name="f" action="<c:url value='/department/create.do'/>" method="POST" onsubmit='return validate();'>
      		<fmt:message key="department.list.department.label" />: <input type='text' name='name' value=''/><br/>
      		<input type="submit" value="<fmt:message key="department.list.submit" />" />
    	</form>
	</div>

	<h1><fmt:message key="department.list.title" /></h1>
	<table cellpadding="0" cellspacing="0" border="0">
	<c:forEach items="${department}" var="d">
		<div>
		<tr>
		<td>
			<a href="/wasp/department/detail/<c:out value="${d.departmentId}" />.do"><c:out value="${d.name}" /></a>
		</td>
		</tr>
	</c:forEach>
</table>
</body></html>