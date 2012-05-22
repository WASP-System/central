<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/librarydetail_rw.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/librarydetail_rw.label"/></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br /> 
<%-- 5/22/12 this likely will not work here, so leave out for the moment
<table>
<c:if test="${otherAdaptorsets != null && otherAdaptorsets.size() > 0}">
	<tr class="FormData"><td colspan="2">
		<form method="GET" action="<c:url value="/sampleDnaToLibrary/librarydetail_rw/${job.jobId}/${sample.sampleId}.do" />">
	  		<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  		<input type='hidden' name='macromolSampleId' value='<c:out value="${parentMacromolecule.sampleId}" />'/>
	  		
				<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
				<option value="0"><fmt:message key="librarydetail_rw.selectNewAdaptorSet.label" />
				<c:forEach items="${otherAdaptorsets}" var="adaptorset">
					<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
				</c:forEach>
				</select>
			
		</form>
	</td></tr>
</c:if>
</table>
--%>
<br /> 

<form:form cssClass="FormGrid" commandName="sample">
<table class="EditTable ui-widget ui-widget-content">
 	<tr class="FormData"><td colspan="3" class="label-centered" style="font-weight:bold;text-decoration:underline"><fmt:message key="librarydetail_rw.libraryDetails.label" /></td></tr>
	<form:hidden path='sampleSubtypeId' />
	<form:hidden path='sampleTypeId' />
 
	 <tr class="FormData">
      <td class="CaptionTD"><fmt:message key="librarydetail_rw.libraryName.label" />:</td>
      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
      <td class="CaptionTD error"><form:errors path="name" /></td>
     </tr>
     <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_rw.libraryType.label" />:</td><td class="DataTD">Library</td><td>&nbsp;</td></tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${sample.getSampleMeta()}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
     <sec:authorize access="hasRole('su') or hasRole('ft')">
    <tr class="FormData">
              <td colspan="3" align="left" class="submitBottom">
              	  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="librarydetail_rw.cancel.label" />" />
                  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="librarydetail_rw.save.label" />" />
              </td>
          </tr>
     </sec:authorize>
</table>
</form:form>