<%@ include file="/WEB-INF/jsp/taglib.jsp" %>



<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${jobDraft.getWorkflow().getIName()}.jobsubmit/cells.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
	<fmt:message key="jobDraft.cell_instructions.label"/>
</div>

<form name="jobDraft" method="POST">
<div class= "greyback">
	<table class="EditTable ui-widget ui-widget-content">
	<tr class="FormData">
	<td  class="CaptionTD">
	<fmt:message key="jobDraft.numberofcells.label" />
	<select class="FormElement ui-widget-content ui-corner-all" id="jobcells" name="jobcells" onchange="adjustcolumns(this.options[this.selectedIndex].innerHTML)">
	  <c:forEach var="i" begin="1" end="15">
	    <c:set var="selected" value="" />
	    <c:if test="${i == fn:length(jobDraft.jobDraftCellSelection)}">
	      <c:set var="selected" value='selected="yes"' />
	    </c:if>
	    <option ${selected} value="${i}"><c:out value="${i}" /></option>
	  </c:forEach>
	</select>
	</td></tr><tr class="FormData"><td align="center">
	<table id="cells" class="data">
	<tr class="FormData">
	<td class="CaptionTD"><fmt:message key="jobDraft.sample.label" /></td>
	  <c:forEach var="i" begin="1" end="15">
	    <td name="column_${i}" class="input-centered" style="display:none">
	      <fmt:message key="jobDraft.cell.label" /> <c:out value="${i}" />
	    </td>
	  </c:forEach>
	</tr>
	
	<c:forEach items="${sampleDrafts}" var="sd">
	
	<tr class="row">
	<td class="CaptionTD"><c:out value="${sd.name}" /></td>
	  <c:forEach var="i" begin="1" end="15">
	    <td name="column_${i}" class="input-centered" style="display:none">
	      <!-- ${sd.sampleDraftId}_${i} -->
	      <c:set var="key" value="${sd.sampleDraftId}_${i}" />
	      <c:set var="checked" value="" />
	      <c:if test="${fn:contains(selectedSampleCell, key)}">
	        <c:set var="checked" value="CHECKED" />
	      </c:if>
	      <input class="FormElement ui-widget-content ui-corner-all" type="checkbox" value="1" ${checked} name="sdc_${sd.sampleDraftId}_${i}">
	    </td>
	  </c:forEach>
	</tr>
	</c:forEach>
	</table>
	</td></tr></table>
</div>
<br />
<div class="submit">
  <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.submit.label" />" />
</div>

</form>

<script>
$("#jobcells").trigger("change");
</script>


