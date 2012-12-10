<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<script src="/wasp/scripts/jquery/jquery-1.7.1.js" type="text/javascript"></script>
<script>
function showAssignForm(e) {
  e.parentNode.getElementsByTagName("A")[0].style.display = "none"; 
  e.parentNode.getElementsByTagName("DIV")[0].style.display = "block"; 
}
</script>

<style>
.assignmentContent {width:850px;}
.assignmentContent div {margin:3px 0px 3px 10px; }
.sampleSide {float:left; width:400px; }
.platformUnitSide {float:left; width:400px;};
</style>


<br /> 
<h1><fmt:message key="platformunit_assign.assignLibToRun.label"/>: <c:out value="${machineName}" /></h1>
<div class="assignmentContent">
	<div class="sampleSide">
	 <c:choose>
	 <c:when test='${fn:length(jobs) > "0"}'>
	  <c:forEach items="${jobs}" var="j">
	    <div class="job">
	      <label><fmt:message key="platformunit_assign.job.label"/> <c:out value="${j.jobId}" /></label>
	      <c:out value="${j.name}" /> [<fmt:message key="platformunit_assign.analysis.label"/>: <c:out value="${j.workflow.name}" />] 
	      <c:forEach items="${j.sample}" var="sample">
	      	<c:if test="${sample.sampleType.IName != 'facilityLibrary'}">
               <div class="sample">
               
                 <label><fmt:message key="platformunit_assign.userSubmitted.label"/> <c:out value="${sample.sampleType.name}"/> </label>
                 <c:out value="${sample.name}"/>
                 [<c:out value="${sample.sampleType.name}"/>]
                 <c:if test="${sample.sampleType.IName == 'library'}">
	                  <c:forEach items="${sample.sampleMeta}" var="sm">
	                  		<c:if test="${fn:substringAfter(sm.k, '.library.') == 'adaptor'}">
	                   			<div><label><fmt:message key="platformunit_assign.adaptor.label"/></label> <c:out value="${adaptors[sm.v]}"/></div>
	                  		</c:if> 
	                  </c:forEach>  
	                  <c:if test='${assignLibraryToPlatformUnitStatusMap.get(sample) == true}'> 
					   <div>
							<a href="javascript:{}" onclick="showAssignForm(this)"><fmt:message key="platformunit_assign.addToLane.label" /></a>
							<div style="display:none">
								<form method="POST" action="<c:url value="/facility/platformunit/assignAdd1.do" />">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="librarysampleid" value="${sample.sampleId}">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobid" value="${j.jobId}">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" value="${resourceCategoryId}">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobsToWorkWith" value="${jobsToWorkWith}"> 
			  							<fmt:message key="platformunit_assign.finalConc.label"/>(<fmt:message key="platformunit_assign.theConcUnits.label"/>): <input class="FormElement ui-widget-content ui-corner-all" type="text" size="5" maxlength="5" name="libConcInLanePicoM"><br>
			  						<select class="FormElement ui-widget-content ui-corner-all selectLane" name="lanesampleid"></select>
			  						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="assign">
								</form>
							</div>
						</div>
					</c:if>
			  
                </c:if>
                 
                 
                <c:forEach items="${sample.getChildren()}" var="schild">
                  <div class="samplechild">
	                  <c:if test="${schild.sampleType.IName == 'facilityLibrary'}">
	                      <label><c:out value="${schild.sampleType.name}"/></label>
	                      <c:out value="${schild.name}" />
	                       <c:forEach items="${schild.sampleMeta}" var="sm">
			                   <c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
			                    <div><label><fmt:message key="platformunit_assign.adaptor.label"/></label> <c:out value="${adaptors[sm.v]}"/></div>
			                    </c:if> 
			                  </c:forEach> 
	                		<c:if test='${assignLibraryToPlatformUnitStatusMap.get(schild) == true}'> 
		        				<div>
									<a href="javascript:{}" onclick="showAssignForm(this)"><fmt:message key="platformunit_assign.addToLane.label" /></a>
									<div style="display:none">
										<form method="POST" action="<c:url value="/facility/platformunit/assignAdd1.do" />">
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="librarysampleid" value="${schild.sampleId}">
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobid" value="${j.jobId}"> 
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" value="${resourceCategoryId}"> 
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobsToWorkWith" value="${jobsToWorkWith}"> 
			 								 <fmt:message key="platformunit_assign.finalConc.label"/>(<fmt:message key="platformunit_assign.theConcUnits.label"/>): <input class="FormElement ui-widget-content ui-corner-all" type="text" size="5" maxlength="5" name="libConcInLanePicoM"><br>
			  								<select class="FormElement ui-widget-content ui-corner-all selectLane" name="lanesampleid"></select>
			  								<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="assign">
										</form>
									</div>
								</div>
							</c:if>
	                  	</c:if>
             		</div>
                </c:forEach>                    
             </div>
          	</c:if>
	      </c:forEach>
	      </div>
	      <br />
	      </c:forEach>
	 </c:when>
	 <c:otherwise>
	 <fmt:message key="platformunit_assign.noLibWaitingFor.label"/> <c:out value="${machineName}" />
	 </c:otherwise>
	 </c:choose> 
	 </div>


	<div class="platformUnitSide">
	<c:choose>
		 <c:when test='${fn:length(platformUnits) > "0"}'>
			  <c:forEach items="${platformUnits}" var="pu">
			    <div class="platformunit">
			      <label><fmt:message key="platformunit_assign.platformUnit.label"/></label>
			      <c:out value="${pu.name}" /> 
			      <c:forEach items="${pu.sampleSource}" var="puparent">
			         <div class="cell">
			           <label><fmt:message key="platformunit_assign.lane.label"/></label>
			      <!--       <c:out value="${puparent.sampleSourceId}" />  -->
			           <c:out value="${puparent.sourceSample.name}" /> 
			
			           <c:forEach items="${puparent.sourceSample.sampleSource}" var="lib">
			             <div class="library">
			               <label><fmt:message key="platformunit_assign.library.label"/></label> <c:out value="${lib.sourceSample.name}" />
			               
			               
			               <c:if test="${lib.sourceSample.sampleType.IName == 'library' || lib.sourceSample.sampleType.IName == 'facilityLibrary'}">
			                  <c:forEach items="${lib.sourceSample.sampleMeta}" var="sm">
			                   <c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
			                    <div><label><fmt:message key="platformunit_assign.adaptor.label"/></label> <c:out value="${adaptors[sm.v]}"/></div>
			                    </c:if> 
			                  </c:forEach> 
			                 <div><a href="<c:url value="/facility/platformunit/assignRemove.do?samplesourceid=${lib.sampleSourceId}&resourceCategoryId=${resourceCategoryId}&jobsToWorkWith=${jobsToWorkWith}"/>"><fmt:message key="platformunit_assign.removeFromLane.label"/></a></div>
			              </c:if>
			             </div>
			           </c:forEach>
			         </div>
			      </c:forEach>
			    </div><br />
			  </c:forEach>
		  </c:when>
	  <c:otherwise>
	  	<fmt:message key="platformunit_assign.noPUWaitingFor.label"/> <c:out value="${machineName}" />
	 </c:otherwise>
	 </c:choose> 
	</div>
</div>

 
 


<script>
  var lanes = $(".selectLane");
    <c:forEach items="${platformUnits}" var="pu">
	lanes.append($('<option></option>').val('<c:out value="0" />').html('<b><c:out value="${pu.name}" /></b>'));
      <c:forEach items="${pu.sampleSource}" var="puparent">
         lanes.append($('<option></option>').val('<c:out value="${puparent.sourceSample.sampleId}" />').html(' - <c:out value="${puparent.sourceSample.name}" />'));
      </c:forEach>
  </c:forEach>
</script>

<%-- /* TODO  javascript to read selected SampleId and scroll to it */ --%>