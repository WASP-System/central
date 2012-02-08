<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<font color="red"><wasp:message /></font>

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${jobDraft.getWorkflow().getIName()}.jobsubmit/cells.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
	<fmt:message key="jobDraft.cell_instructions.label"/>
</div>

<form command="jobDraft" method="POST">
<div class= "greyback">
	<table>
	<tr><td>
	<fmt:message key="jobDraft.numberofcells.label" />
	<select id="jobcells" name="jobcells" onchange="adjustcolumns(this.options[this.selectedIndex].innerHTML)">
	  <c:forEach var="i" begin="1" end="15">
	    <c:set var="selected" value="" />
	    <c:if test="${i == fn:length(jobDraft.jobDraftCell)}">
	      <c:set var="selected" value='selected="yes"' />
	    </c:if>
	    <option ${selected} value="${i}"><c:out value="${i}" /></option>
	  </c:forEach>
	</select>
	</td></tr><tr><td align="center">
	<table id="cells" class="data">
	<tr>
	<td class="label"><fmt:message key="jobDraft.sample.label" /></td>
	  <c:forEach var="i" begin="1" end="15">
	    <td name="column_${i}" class="input" style="display:none">
	      <fmt:message key="jobDraft.cell.label" /> <c:out value="${i}" />
	    </td>
	  </c:forEach>
	</tr>
	
	<c:forEach items="${sampleDrafts}" var="sd">
	
	<tr class="row">
	<td class="label"><c:out value="${sd.name}" /></td>
	  <c:forEach var="i" begin="1" end="15">
	    <td name="column_${i}" class="input" style="display:none">
	      <!-- ${sd.sampleDraftId}_${i} -->
	      <c:set var="key" value="${sd.sampleDraftId}_${i}" />
	      <c:set var="checked" value="" />
	      <c:if test="${fn:contains(selectedSampleCell, key)}">
	        <c:set var="checked" value="CHECKED" />
	      </c:if>
	      <input type="checkbox" value="1" ${checked} name="sdc_${sd.sampleDraftId}_${i}">
	    </td>
	  </c:forEach>
	</tr>
	</c:forEach>
	</table>
	</td></tr></table>
</div>
<br />
<div class="submit">
  <input type="submit" value="<fmt:message key="jobDraft.submit.label" />" />
</div>

</form>

<script>
$("#jobcells").trigger("change");
</script>


