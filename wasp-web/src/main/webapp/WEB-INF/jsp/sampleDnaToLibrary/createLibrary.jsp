<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/createLibrary.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/createLibrary.label"/></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br /> 

<table>
<c:if test="${otherAdaptorsets != null && otherAdaptorsets.size() > 0}">
	<tr class="FormData"><td colspan="2">
		<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  		<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  		<input type='hidden' name='macromolSampleId' value='<c:out value="${macromoleculeSample.sampleId}" />'/>
	  		
				<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
				<option value="0"><fmt:message key="createLibrary.selectNewAdaptorSet.label" />
				<c:forEach items="${otherAdaptorsets}" var="adaptorset">
					<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
				</c:forEach>
				</select>
			
		</form>
	</td></tr>
</c:if>
</table>
<br /> 
<form:form  cssClass="FormGrid" commandName="sample">
 <form:hidden path='sampleTypeId' />
 <form:hidden path='sampleSubtypeId' />
 
<table class="EditTable ui-widget ui-widget-content">
  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.primarySampleName.label" />:</td><td colspan="2" class="DataTD"><c:out value="${macromoleculeSample.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.primarySampleType.label" />:</td><td colspan="2" class="DataTD"><c:out value="${macromoleculeSample.sampleType.name}" /></td></tr>
    <c:forEach items="${macromoleculeSample.sampleMeta}" var="msm">
    	<c:if test="${fn:substringAfter(msm.k, 'Biomolecule.') == 'species'}">
            <tr class="FormData"><td class="CaptionTD"><fmt:message key="createLibrary.primarySampleSpecies.label" />:</td><td colspan="2" class="DataTD"><c:out value="${msm.v}"/></td></tr>
        </c:if> 
    </c:forEach> 
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