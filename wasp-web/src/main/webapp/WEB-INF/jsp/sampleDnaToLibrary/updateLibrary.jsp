<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    <wasp:message />  
 TESTING
<br /><br /><br />
  <h1><fmt:message key="pageTitle.user/detail_rw.label" /></h1>


    <form:form  cssClass="FormGrid" commandName="sample">
     <table class="EditTable ui-widget ui-widget-content">
     
          <c:set var="_area" value = "sample" scope="request"/>
          <c:set var="_metaArea" value = "genericLibrary" scope="request"/>	
		  <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          
          
          <tr class="FormData">
              <td colspan="3" align="left" class="submitBottom">
              	  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="userDetail.cancel.label" />" />
                  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="userDetail.save.label" />" />
              </td>
          </tr>
	</table>
    </form:form>

  
