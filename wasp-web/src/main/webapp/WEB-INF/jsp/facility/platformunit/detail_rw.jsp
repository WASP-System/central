<%@ include file="/WEB-INF/jsp/taglib.jsp" %>



<form:form  cssClass="FormGrid" commandName="sample">

  <table class="EditTable ui-widget ui-widget-content">
  	<tr class="FormData">
    	<td class="CaptionTD"><fmt:message key="sample.name.label" />:</td>
    	<td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /></td>
    	<td class="CaptionTD error"><form:errors path="name"/></td>
    </tr>


 <c:set var="_area" value = "sample" scope="request"/>
 <c:set var="_metaArea" value = "platformunit" scope="request"/>

 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />
 <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>

  </table>

  <input class="FormElement ui-widget-content ui-corner-all" type="submit">
</form:form>
