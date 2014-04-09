<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/createLibrary.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/createLibrary.label"/></h1>
<div style="width=100%; overflow:hidden">
	<div style="float:left">
		<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
	</div>
	<div style="padding-left:0.5cm; overflow:hidden">
		<form:form  cssClass="FormGrid" commandName="sample">
		 <form:hidden path='sampleTypeId' />
		 <form:hidden path='sampleSubtypeId' />
		 
		<table class="EditTable ui-widget ui-widget-content">
		  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.primarySampleName.label" />:</td><td colspan="2" class="DataTD"><c:out value="${macromoleculeSample.name}" /></td></tr>
		  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.primarySampleType.label" />:</td><td colspan="2" class="DataTD"><c:out value="${macromoleculeSample.sampleType.name}" /></td></tr>
		    <tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.primarySampleSpecies.label" />:</td><td colspan="2" class="DataTD"><c:out value="${organism}"/></td></tr>
		 
		   	<tr class="FormData"><td colspan="3" class="label-centered" style="font-weight:bold;text-decoration:underline"><fmt:message key="createLibrary.libraryDetails.label" /></td></tr>
		  
		  	 <tr class="FormData">
		      <td class="CaptionTD"><fmt:message key="createLibrary.libraryName.label" />:</td>
		      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
		      <td class="CaptionTD error"><form:errors path="name" /></td>
		     </tr>
		     <tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.libraryType.label" />:</td><td class="DataTD"><c:out value="${sample.getSampleType().getName()}" /></td><td>&nbsp;</td></tr>
		     <tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.librarySubtype.label" />:</td><td class="DataTD"><c:out value="${sample.getSampleSubtype().getName()}" /></td><td>&nbsp;</td></tr>
		     <c:set var="_area" value = "sample" scope="request"/>
			 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
		     <c:import url="/WEB-INF/jsp/meta_rw.jsp" />
		     <sec:authorize access="hasRole('su') or hasRole('ft')">
		    <tr class="FormData">
		              <td colspan="3" align="left" class="submitBottom">
		              	  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="createLibrary.cancel.label" />" />
		                  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="createLibrary.save.label" />" />
		              </td>
		          </tr>
		     </sec:authorize>
		</table>
		</form:form>
	</div>
</div>