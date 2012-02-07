<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<font color="blue"><wasp:message /></font>

<h1>Create a Job -- Verify</h1>

<c:set var="jobDraftDb" value="${jobDraft}" />
<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>


<c:set var="_area" value = "${parentarea}" scope="request"/>
<c:set var="_metaArea" value = "${area}" scope="request"/>
<c:set var="_metaList" value = "${jobDraft.jobDraftMeta}" scope="request" />

<table class="data">
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

     <c:forEach items="${sd.sampleDraftCell}" var="sdc">
	Cell <c:out value="${sdc.jobDraftCell.cellindex}" />
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
    <input type="submit" value="Save Changes" />
  </div>
</form>

<div class="bottomtxt">
    sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam
</div>

