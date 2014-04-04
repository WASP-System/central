<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script>
function showAssignForm(e) {
  e.parentNode.getElementsByTagName("A")[0].style.display = "none";
  e.parentNode.getElementsByTagName("A")[1].style.display = "block";
  e.parentNode.getElementsByTagName("DIV")[0].style.display = "block"; 
}
function hideAssignForm(e) {
  e.parentNode.getElementsByTagName("A")[0].style.display = "block";
  e.parentNode.getElementsByTagName("A")[1].style.display = "none";
  e.parentNode.getElementsByTagName("DIV")[0].style.display = "none"; 
}
function showCoverage(e) {
  e.parentNode.getElementsByTagName("A")[0].style.display = "none";
  e.parentNode.getElementsByTagName("A")[1].style.display = "block";
  e.parentNode.getElementsByTagName("DIV")[0].style.display = "block"; 
}
function hideCoverage(e) {
  e.parentNode.getElementsByTagName("A")[0].style.display = "block";
  e.parentNode.getElementsByTagName("A")[1].style.display = "none";
  e.parentNode.getElementsByTagName("DIV")[0].style.display = "none"; 
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
			<br />	
			<div>
				<a href="javascript:{}" onclick="showCoverage(this)"><fmt:message key="platformunit_assign.showRequestedCoverage.label" /></a> 							
				<a style="display:none" href="javascript:{}" onclick="hideCoverage(this)"><fmt:message key="platformunit_assign.close.label" /></a>			  				
				<div id="user_requested_coverage_data" style="display:none">	
					<table class="data">
					<tr class="FormData">
						<td class="label-centered" style="background-color:#FAF2D6; font-size:10px">&nbsp;</td><c:forEach var="i" begin="0" end="${jobTotalNumberOfCellsRequested.get(j) - 1}" ><td class="label-centered" style="background-color:#FAF2D6; font-size:10px"><fmt:message key="listJobSamples.cell.label" /> <c:out value="${i + 1}" /></td></c:forEach>
					</tr>
					
					<c:set value="${jobCoverageMap.get(j)}" var="coverageMap" scope="page" /> 
					<c:forEach items="${coverageMap.keySet()}" var="coverageItem">
						<tr class="FormData">
							<td class="label-centered" style="background-color:#FAF2D6; font-size:10px" >
								<c:out value="${coverageItem.getName()}" />
							</td>
							<c:set var="string" value="${coverageMap.get(coverageItem)}" scope="page" />
							<c:forEach var="i" begin="0" end="${fn:length(string)-1}" step="1">
					   			<td  class="value-centered" style="text-align: center; vertical-align: middle;"> 
					   				<c:choose>
					   					<c:when test='${fn:substring(string, i, i + 1)=="0"}'><input type="checkbox" DISABLED /></c:when>
					   					<c:otherwise><input type="checkbox" DISABLED checked="checked" /></c:otherwise>
					   				</c:choose>   
					  			</td>   
							</c:forEach>
						</tr>
					</c:forEach>		
					</table>
				</div>
			</div>
			
	      <c:forEach items="${j.sample}" var="sample">
	      	<c:if test="${sample.sampleType.IName != 'facilityLibrary'}">
               <div class="sample">
               
                 <label><fmt:message key="platformunit_assign.userSubmitted.label"/> <c:out value="${sample.sampleType.name}"/> </label>
                 <c:out value="${sample.name}"/>
                 [<c:out value="${sample.sampleType.name}"/>]
                 <c:if test='${receivedStatusMap.get(sample)!="RECEIVED"}'>
                	<div><label><fmt:message key="platformunit_assign.recievedStatus.label"/></label>: <c:out value="${receivedStatusMap.get(sample)}"/></div>
                 </c:if>
                 <c:if test='${receivedStatusMap.get(sample)=="RECEIVED"}'>
                 	<div><label><fmt:message key="platformunit_assign.qcStatus.label"/></label>: <c:out value="${qcStatusMap.get(sample)}"/></div>
                 </c:if> 
                 <c:if test="${sample.sampleType.IName == 'library'}">
	                  <c:forEach items="${sample.sampleMeta}" var="sm">
	                  		<c:if test="${fn:substringAfter(sm.k, '.library.') == 'adaptor'}">
	                   			<div><label><fmt:message key="platformunit_assign.adaptor.label"/></label>: <c:out value="${adaptors[sm.v]}"/></div>
	                  		</c:if>
	                  </c:forEach> 
	                  <c:if test='${assignLibraryToPlatformUnitStatusMap.get(sample) == true}'> 
					   <div>
							<a href="javascript:{}" onclick="showAssignForm(this)"><fmt:message key="platformunit_assign.addToCell.label" /></a> 							
							<a style="display:none" href="javascript:{}" onclick="hideAssignForm(this)"><fmt:message key="platformunit_assign.close.label" /></a>			  				
			  				<div style="display:none">
								<form method="POST" action="<wasp:relativeUrl value="facility/platformunit/assignAdd1.do" />">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="librarysampleid" value="${sample.sampleId}">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobid" value="${j.jobId}">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" value="${resourceCategoryId}">
			  						<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobsToWorkWith" value="${jobsToWorkWith}"> 
			  							<fmt:message key="platformunit_assign.finalConc.label"/>(<fmt:message key="platformunit_assign.theConcUnits.label"/>): <input class="FormElement ui-widget-content ui-corner-all" type="text" size="5" maxlength="5" name="libConcInCellPicoM"><br>
			  						<select class="FormElement ui-widget-content ui-corner-all selectCell" name="cellsampleid"></select>
			  						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="assign">			  						
								</form>
							</div>
						</div>
					</c:if>
			  
                </c:if>
                 
                 
                <c:forEach items="${sample.getChildren()}" var="schild">
                  <div class="samplechild">
	                  <c:if test="${schild.sampleType.IName == 'facilityLibrary'}">
	                      <label><c:out value="${schild.sampleType.name}"/></label> <c:out value="${schild.name}" />
	                       <div><label><fmt:message key="platformunit_assign.qcStatus.label"/></label>: <c:out value="${qcStatusMap.get(schild)}"/></div>
	                       <c:forEach items="${schild.sampleMeta}" var="sm">
			                   <c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
			                    <div><label><fmt:message key="platformunit_assign.adaptor.label"/></label>: <c:out value="${adaptors[sm.v]}"/></div>
			                    </c:if> 
			                  </c:forEach> 
			                  <c:if test='${assignLibraryToPlatformUnitStatusMap.get(schild) == true}'> 
		        				<div>
									<a href="javascript:{}" onclick="showAssignForm(this)"><fmt:message key="platformunit_assign.addToCell.label" /></a>
									<a style="display:none" href="javascript:{}" onclick="hideAssignForm(this)"><fmt:message key="platformunit_assign.close.label" /></a>	
									<div style="display:none">
										<form method="POST" action="<wasp:relativeUrl value="facility/platformunit/assignAdd1.do" />">
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="librarysampleid" value="${schild.sampleId}">
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobid" value="${j.jobId}"> 
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" value="${resourceCategoryId}"> 
			  								<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobsToWorkWith" value="${jobsToWorkWith}"> 
			 								 <fmt:message key="platformunit_assign.finalConc.label"/>(<fmt:message key="platformunit_assign.theConcUnits.label"/>): <input class="FormElement ui-widget-content ui-corner-all" type="text" size="5" maxlength="5" name="libConcInCellPicoM"><br>
			  								<select class="FormElement ui-widget-content ui-corner-all selectCell" name="cellsampleid"></select>
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
			           <label><fmt:message key="platformunit_assign.cell.label"/></label>
			      <!--       <c:out value="${puparent.sampleSourceId}" />  -->
			           <c:out value="${puparent.sourceSample.name}" /> 
			
			           <c:forEach items="${puparent.sourceSample.sampleSource}" var="lib">
			             <div class="library">
			               <label><fmt:message key="platformunit_assign.library.label"/></label> <c:out value="${lib.sourceSample.name}" />
			               
			               
			               <c:if test="${lib.sourceSample.sampleType.IName == 'library' || lib.sourceSample.sampleType.IName == 'facilityLibrary'}">
			                  <c:forEach items="${lib.sourceSample.sampleMeta}" var="sm">
			                   <c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
			                    <div><label><fmt:message key="platformunit_assign.adaptor.label"/></label>: <c:out value="${adaptors[sm.v]}"/></div>
			                    </c:if> 
			                  </c:forEach> 
			                 <div><a href="<wasp:relativeUrl value="facility/platformunit/assignRemove.do?cellLibraryId=${lib.sampleSourceId}&resourceCategoryId=${resourceCategoryId}&jobsToWorkWith=${jobsToWorkWith}"/>"><fmt:message key="platformunit_assign.removeFromCell.label"/></a></div>
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
  var cells = $(".selectCell");
    <c:forEach items="${platformUnits}" var="pu">
	cells.append($('<option></option>').val('<c:out value="0" />').html('<b><c:out value="${pu.name}" /></b>'));
      <c:forEach items="${pu.sampleSource}" var="puparent">
         cells.append($('<option></option>').val('<c:out value="${puparent.sourceSample.sampleId}" />').html(' - <c:out value="${puparent.sourceSample.name}" />'));
      </c:forEach>
  </c:forEach>
</script>

<%-- /* TODO  javascript to read selected SampleId and scroll to it */ --%>