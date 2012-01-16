<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<font color="blue"><wasp:message /></font>

<h1>Create a Job</h1>

<div class="instructions">
{Instructions} In publishing and graphic design, lorem ipsum[p][1] is placeholder text (filler text) commonly used to demonstrate the graphics elements of a document or visual presentation, such as font, typography, and layout. The lorem ipsum text is typically a section of a Latin text by Cicero with words altered, added and removed that make it nonsensical in meaning and not proper Latin.[1]

Even though "lorem ipsum" may arouse curiosity because of its resemblance to classical Latin, it is not intended to have meaning. Where text is comprehensible in a document, people tend to focus on the textual content rather than upon overall presentation, so publishers use lorem ipsum when displaying a typeface or design elements and page layout in order to direct the focus to the publication style and not the meaning of the text. In spite of its basis in Latin, use of lorem ipsum is often referred to as greeking, from the phrase "it's all Greek to me," which indicates that this is not meant to be readable text.[2]{/Instructions}
</div>

<form:form commandName="jobDraft">
<table class="data">
  <tr>
    <td class="label"><fmt:message key="jobDraft.name.label"/>:</td>
    <td class="input">
      <input name="name" value="<c:out value="${jobDraft.name}"/>">
    </td>
    <td class="error"><form:errors path="name" /></td>
  </tr>
  <tr>
    <td class="label"><fmt:message key="jobDraft.labId.label"/>:</td>
    <td class="input">
      <select name="labId">
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="lab" items="${labs}">
          <option value="${lab.labId}" <c:if test="${lab.labId == jobDraft.labId}"> selected</c:if>><c:out value="${lab.name}"/></option>
        </c:forEach>
      </select>
    </td>
    <td class="error"><form:errors path="labId" /></td>
  </tr>
  <tr>
    <td class="label"><fmt:message key="jobDraft.workflowId.label"/>:</td>
    <td class="input">
      <c:forEach var="workflow" items="${workflows}">
        <div class="radioelement">
          <input type="radio" name="workflowId" value="${workflow.workflowId}" <c:if test="${workflow.workflowId == jobDraft.workflowId}"> checked</c:if> >
          <span><fmt:message key="${workflow.IName}.workflow.label"/></span>
        </div>
      </c:forEach>
    </td>
    <td class="error"><form:errors path="workflowId" /></td>
  </tr>
  </table>

  <div class="submit">
    <input type="submit">
  </div>

  </form:form>

  <div class="bottomtxt">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam 
  </div>


