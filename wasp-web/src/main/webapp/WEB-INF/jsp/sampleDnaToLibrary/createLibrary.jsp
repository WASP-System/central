<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<h1>Create New Library</h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br /> 
<form:form  cssClass="FormGrid" commandName="sample">
 <input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
 <input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='macromolSampleId' value='<c:out value="${macromoleculeSample.sampleId}" />'/>
 <input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='adaptorsetId' value='<c:out value="${adaptorsets.get(0).adaptorsetId}" />'/>
 
<table class="EditTable ui-widget ui-widget-content">
  	<tr class="FormData"><td class="CaptionTD">Primary Sample Name:</td><td colspan="2" class="DataTD"><c:out value="${macromoleculeSample.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD">Primary Sample Type:</td><td colspan="2" class="DataTD"><c:out value="${macromoleculeSample.typeSample.name}" /></td></tr>
    <c:forEach items="${macromoleculeSample.sampleMeta}" var="msm">
    	<c:if test="${fn:substringAfter(msm.k, 'Biomolecule.') == 'species'}">
            <tr class="FormData"><td class="CaptionTD">Primary Sample Species:</td><td colspan="2" class="DataTD"><c:out value="${msm.v}"/></td></tr>
        </c:if> 
    </c:forEach> 
   	<tr class="FormData"><td colspan="3" class="label-centered">NEW LIBRARY DETAILS</td></tr>
  
  	 <tr class="FormData">
      <td class="CaptionTD">Library Name:</td>
      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
      <td class="CaptionTD error"><form:errors path="name" /></td>
     </tr>
     <tr class="FormData"><td class="CaptionTD">Sample Type:</td><td class="DataTD">Library</td><td>&nbsp;</td></tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw.jsp" />
     <sec:authorize access="hasRole('su') or hasRole('ft')">
    <tr class="FormData">
              <td colspan="3" align="left" class="submitBottom">
              	  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="Cancel" />
                  <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="Save" />
              </td>
          </tr>
     </sec:authorize>
</table>
</form:form>