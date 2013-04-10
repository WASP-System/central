<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/verify.label"/></h1>

<c:set var="jobDraftDb" value="${jobDraft}" />
<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
<div class="instructions"><fmt:message key="jobDraft.verify_instructions.label"/></div>
<div id="hiddenMessage"><br />Processing! Please Wait.....</div>
<form method="POST" onsubmit="return submitDocument(this)">
  <div id="buttons" class="submit">
    <input name="waitButton" class="fm-button" type="button" onClick="location.href='/wasp/dashboard.do';" value="<fmt:message key='jobDraft.submit_later_button.label'/>" /> 
    &nbsp;&nbsp;<input name="submitButton" class="fm-button" type="submit" value="<fmt:message key="jobDraft.submit_button.label"/>" />
  </div>
</form>
<div class="bottomtxt"></div>

<script type="text/javascript">
function submitDocument(thisForm){
	
	document.getElementById('hiddenMessage').style.visibility = "visible";
	document.getElementById('buttons').style.visibility = "hidden";
	thisForm.waitButton.disabled = true;
	thisForm.submitButton.disabled=true;	
	thisForm.submit(); 
	return true; 
}
</script>
<style>
#hiddenMessage{
	visibility: hidden;	
	font-size:xx-large;
	color:red;
	position: absolute;
}
</style>


<%-- 
<div class="instructions">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>


<c:set var="_area" value = "${parentarea}" scope="request"/>
<c:set var="_metaArea" value = "${area}" scope="request"/>
<c:set var="_metaList" value = "${jobDraft.jobDraftMeta}" scope="request" />

<table class="EditTable ui-widget ui-widget-content">
  <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
</table>

<hr>
 <c:forEach items="${sampleDraft}" var="sd">
   <div>
     Sample:
     <c:out value="${sd.name}" />
     <c:forEach items="${sd.sampleDraftMeta}" var="sdm">
       <c:if test="${sdm.v != ''}">
         <div>
           <fmt:message key="${sdm.k}" />:
           <c:out value="${sdm.v}" />
         </div>
       </c:if>
     </c:forEach>

     <c:forEach items="${sd.sampleDraftJobDraftCellSelection}" var="sdc">
	Cell <c:out value="${sdc.jobDraftCellSelection.cellIndex}" />
     </c:forEach>

     <c:if test="${! empty(sd.fileId)}">
       FILE
       <c:out value="${sd.file.filelocation}" />
     </c:if>
   </div>
   <hr>
 </c:forEach>


<form method="POST">
  <div class="submit">
    <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Save Changes" />
  </div>
</form>

<div class="bottomtxt">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>

--%>
