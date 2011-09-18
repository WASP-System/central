<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <script src="/wasp/scripts/jquery/jquery-1.6.2.js" type="text/javascript"></script>

  <script>
var maxColumns = 10; 

function adjustcolumns(c) {
  for (var i = 0; i < maxColumns; i++) {
    var display = "table-cell";
    if (i >  c) {
      display = "none"
    }
    $("[name=column_"+i+"]").css("display", display);
  }
}
  </script>

</head>
<body>
[<c:out value="${fn:length(jobDraft.jobDraftCell)}" />]

<form command="jobDraft" method="POST">
<select id="jobcells" name="jobcells" onchange="adjustcolumns(this.options[this.selectedIndex].innerHTML)">
  <c:forEach var="i" begin="1" end="10">
    <c:set var="selected" value="" />
    <c:if test="${i == fn:length(jobDraft.jobDraftCell)}">
      <c:set var="selected" value='selected="yes"' />
    </c:if>
    <option ${selected} value="${i}"><c:out value="${i}" /></option>
  </c:forEach>
</select>

<table>

<c:forEach items="${sampleDrafts}" var="sd">

<tr>
<td><c:out value="${sd.name}" /></td>
  <c:forEach var="i" begin="1" end="10">
    <td name="column_${i}" style="display:none">
      ${sd.sampleDraftId}_${i}
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

</body>
</html>

