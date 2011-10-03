<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
[<c:out value="${fn:length(jobDraft.jobDraftCell)}" />]

<form command="jobDraft" method="POST">

jobsubmit.numberofcells.label
<select id="jobcells" name="jobcells" onchange="adjustcolumns(this.options[this.selectedIndex].innerHTML)">
  <c:forEach var="i" begin="1" end="10">
    <c:set var="selected" value="" />
    <c:if test="${i == fn:length(jobDraft.jobDraftCell)}">
      <c:set var="selected" value='selected="yes"' />
    </c:if>
    <option ${selected} value="${i}"><c:out value="${i}" /></option>
  </c:forEach>
</select>

<table id="cells">
<tr>
<td>Sample</td>
  <c:forEach var="i" begin="1" end="10">
    <td name="column_${i}" style="display:none">
      Cell <c:out value="${i}" />
    </td>
  </c:forEach>
</tr>

<c:forEach items="${sampleDrafts}" var="sd">

<tr class="row">
<td><c:out value="${sd.name}" /></td>
  <c:forEach var="i" begin="1" end="10">
    <td name="column_${i}" style="display:none">
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

<input type="submit">

</form>


<script>
$("#jobcells").trigger("change");
</script>


