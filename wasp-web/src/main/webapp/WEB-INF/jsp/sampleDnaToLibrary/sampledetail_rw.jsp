<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/sampledetail_rw.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/sampledetail_rw.label"/></h1>
<div style="width=100%; overflow:hidden">
	<div style="float:left">
		<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
	</div>
	<div style="padding-left:0.5cm; overflow:hidden">
		<form:form cssClass="FormGrid" commandName="sample">
		<form:hidden path='sampleSubtypeId' />
		<form:hidden path='sampleTypeId' />
		<table class="EditTable ui-widget ui-widget-content">
		  	 <tr class="FormData">
		      <td class="CaptionTD"><fmt:message key="sampledetail_rw.sampleName.label" />:</td>
		      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
		      <td class="CaptionTD error"><form:errors path="name" /></td>
		     </tr>
		     <tr class="FormData"><td class="CaptionTD"><fmt:message key="sampledetail_rw.sampleType.label" />:</td><td class="DataTD"><c:out value="${sample.sampleType.name}" /></td><td class="CaptionTD error"><form:errors path="" /></td></tr>
		     <c:set var="_area" value = "sample" scope="request"/>
			 <c:set var="_metaList" value = "${normalizedSampleMeta}" scope="request" />		
		     <c:import url="/WEB-INF/jsp/meta_rw.jsp" />
		     <sec:authorize access="hasRole('su') or hasRole('ft')">
		    <tr class="FormData">
		        <td colspan="3" align="left" class="submitBottom">
		        	<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="sampledetail_rw.cancel.label" />" />
		            <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="sampledetail_rw.save.label" />" />
		        </td>
		    </tr>
		    </sec:authorize>
		</table>
		</form:form>
	</div>
</div>