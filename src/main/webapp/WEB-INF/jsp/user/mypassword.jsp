<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title><fmt:message key="user.mypassword.title" /></title>
  <script language="JavaScript">
		<!--
		function validate(){
			//alert("test123"); return false;
			var error = false;
			var message = '<fmt:message key="user.mypassword.missingparam.error" />';
			if(document.f.oldpassword.value == "" || document.f.newpassword1.value == ""|| document.f.newpassword2.value == "" ){
				error = true;
			}
			if(error){ alert(message); return false; }
  			return true;
		}
		//-->
	</script>  
	 </head>

  <body onload='document.f.oldpassword.focus();'>

    <h1><fmt:message key="user.mypassword.title" /></h1>
    <h4><fmt:message key="user.mypassword.instructions" /></h4>

     <font color="red"><wasp:message /></font>

    <form name="f" action="<c:url value='/user/mypassword.do'/>" method="POST" onsubmit='return validate();'>

      <table>
        <tr><td><fmt:message key="user.mypassword.oldpassword.label" /></td><td><input type='password' name='oldpassword' value=''/></td></tr>
        <tr><td><fmt:message key="user.mypassword.newpassword1.label" /></td><td><input type='password' name='newpassword1' value=''/></td></tr>
        <tr><td><fmt:message key="user.mypassword.newpassword2.label" /></td><td><input type='password' name='newpassword2' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="user.mypassword.submit" />"/></td></tr>
      </table>
    </form>

</body></html>