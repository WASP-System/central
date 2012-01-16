<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<font color="blue"><wasp:message /></font>

<h1>Create a Job -- Cell Assignment </h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="jobsubmitnav">
  <a href="<c:url value="/jobsubmit/modify/${jobDraft.jobDraftId}.do"/>">modify</a>
</div>

<div class="instructions">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>

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
<th class="label">Sample</th>
  <c:forEach var="i" begin="1" end="10">
    <th name="column_${i}" class="input" style="display:none">
      Cell <c:out value="${i}" />
    </th>
  </c:forEach>
</tr>

<c:forEach items="${sampleDrafts}" var="sd">

<tr class="row">
<td class="label"><c:out value="${sd.name}" /></td>
  <c:forEach var="i" begin="1" end="10">
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

<div class="submit">
  <input type="submit" value="Save Changes" />
</div>

</form>

<div class="bottomtxt">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>

<script>
$("#jobcells").trigger("change");
</script>


