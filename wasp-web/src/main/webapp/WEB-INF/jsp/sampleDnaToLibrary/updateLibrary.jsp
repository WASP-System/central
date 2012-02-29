<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    <wasp:message />  
 TESTING
<br /><br /><br />
  <h1><fmt:message key="pageTitle.user/detail_rw.label" /></h1>


    <form:form commandName="sample">
     <table class="data">
     
          <c:set var="_area" value = "sample" scope="request"/>
          <c:set var="_metaArea" value = "genericLibrary" scope="request"/>	
		  <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          
          
          <tr>
              <td colspan="2" align="left" class="submit">
              	  <input type="submit" name="submit" value="<fmt:message key="userDetail.cancel.label" />" />
                  <input type="submit" name="submit" value="<fmt:message key="userDetail.save.label" />" />
              </td>
              <td>&nbsp;</td>
          </tr>
	</table>
    </form:form>

  
