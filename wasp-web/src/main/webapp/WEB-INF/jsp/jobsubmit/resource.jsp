<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<font color="blue"><wasp:message /></font>

<h1>Create a Job -- <c:out value="${workflowResources[0].resource.typeResource.name}" /></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>

<form method="POST" class="resourcelistform">

<select name="changeResource" onchange="this.parentNode.submit()">
  <option value="-1">-- select --</option>
<c:forEach items="${workflowResourceCategories}" var="w">
  <option value="<c:out value="${w.resourceCategory.resourceCategoryId}" />" <c:if test="${w.resourceCategory.resourceCategoryId == jobDraftResourceCategory.resourceCategory.resourceCategoryId}"> SELECTED</c:if>>
    <c:out value="${w.resourceCategory.name}" />
  </option>
</c:forEach>
</select>
</form>

<form:form command="jobDraft">
  <input type="hidden" name="name" value="<c:out value="${jobDraft.name}"/>">
  <input type="hidden" name="workflowId" value="<c:out value="${jobDraft.workflowId}"/>">
  <input type="hidden" name="labId" value="<c:out value="${jobDraft.labId}"/>">
  <c:set var="_area" value = "${parentarea}" scope="request"/>
  <c:set var="_metaArea" value = "${area}" scope="request"/>
  <c:set var="_metaList" value = "${jobDraft.jobDraftMeta}" scope="request" />
  <table class="data">
     <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
  </table>

<div class="submit">
  <input type="submit" value="Save Changes" />
</div>

</form:form>

<div class="bottomtxt">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>

