<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<script src="/wasp/scripts/jquery/jquery-1.6.2.js" type="text/javascript"></script>
<script>
function showAssignForm(e) {
  e.parentNode.getElementsByTagName("A")[0].style.display = "none"; 
  e.parentNode.getElementsByTagName("DIV")[0].style.display = "block"; 
}
</script>

<style>
DIV {margin-left: 10px; }
BODY {margin: 0; padding: 0;}
.assignmentContent {width:900px; height: 400px; }
.sampleSide {float:left; width:400px; height: 100%; overflow: auto}
.platformUnitSide {float:left; wldth:450px; height:100%; overflow: auto};
</style>

<wasp:message />
<br /> <!--   [<c:out value="${error}" />] -->
<h1>Assigning Libraries For A Run On: <c:out value="${machineName}" /></h1>
<div class="assignmentContent">
<div class="sampleSide">
 <c:choose>
 <c:when test='${fn:length(jobs) > "0"}'>
  <c:forEach items="${jobs}" var="j">
    <div class="job">
      <label>Job <c:out value="${j.jobId}" /></label>
      <c:out value="${j.name}" /> [Analysis: <c:out value="${j.workflow.name}" />] 
      <c:forEach items="${j.jobCell}" var="jc">
        <div class="jobcell">
          Job Cell: <c:out value="${jc.cellindex}" /> 
          <c:forEach items="${jc.sampleCell}" var="sc">
            <div class="samplecell">
              Sample Cell: <c:out value="${sc.libraryindex}" /> 
               <div class="sample">
                
                        <c:choose>
                         <c:when test="${sc.sample.typeSample.IName == 'library'}">
                  			<label>User-submitted Library </label>
                		</c:when>
                		<c:otherwise>
                			<label>User-submitted <c:out value="${sc.sample.typeSample.name}"/> </label>
                		</c:otherwise>
                		</c:choose>
                  <c:out value="${sc.sample.name}"/>
                  [<c:out value="${sc.sample.typeSample.name}"/>]
                  <c:if test="${sc.sample.typeSample.IName == 'library'}">
                  	<c:forEach items="${sc.sample.sampleMeta}" var="sm">
                   		<c:if test="${fn:substringAfter(sm.k, '.library.') == 'adaptorid'}">
                    		<div><label>Adaptor</label> <c:out value="${adaptors[sm.v]}"/></div>
                    	</c:if> 
                   </c:forEach>  
				   <div>
					<a href="javascript:{}" onclick="showAssignForm(this)">(+)</a>
					<div style="display:none">
					<form method="POST" action="<c:url value="/facility/platformunit/assignAdd.do" />">
  						<input type="hidden" name="librarysampleid" value="${sc.sample.sampleId}">
  						<input type="hidden" name="jobid" value="${j.jobId}">
  						<input type="hidden" name="resourceCategoryId" value="${resourceCategoryId}"> 
  							pmole: <input type="text" size="5" maxlength="5" name="pmolAdded"><br>
  						<select class="selectLane" name="lanesampleid"></select>
  						<input type="submit" value="assign">
					</form>
					</div>
				  </div>
                  </c:if>
                  
                  <c:forEach items="${sc.sample.sampleSourceViaSourceSampleId}" var="schild">
                    <div class="samplechild">
                    <c:if test="${schild.sample.typeSample.IName == 'library'}">
                        <label>Facility-generated Library</label>
                        <c:out value="${schild.sample.name}" />
                        [<c:out value="${schild.sample.typeSample.name}"/>]
                       <c:forEach items="${schild.sample.sampleMeta}" var="sm">
                   		  <c:if test="${fn:substringAfter(sm.k, '.library.') == 'adaptorid'}">
                    		<div><label>Adaptor</label> <c:out value="${adaptors[sm.v]}"/></div>
                    	  </c:if> 
                  		</c:forEach> 
        				<div>
							<a href="javascript:{}" onclick="showAssignForm(this)">(+)</a>
							<div style="display:none">
							<form method="POST" action="<c:url value="/facility/platformunit/assignAdd.do" />">
  								<input type="hidden" name="librarysampleid" value="${schild.sample.sampleId}">
  								<input type="hidden" name="jobid" value="${j.jobId}"> 
  								<input type="hidden" name="resourceCategoryId" value="${resourceCategoryId}"> 
 								pmole: <input type="text" size="5" maxlength="5" name="pmolAdded"><br>
  								<select class="selectLane" name="lanesampleid"></select>
  								<input type="submit" value="assign">
							</form>
							</div>
						</div>
                    </c:if>
                    <c:if test="${schild.sample.typeSample.IName == 'lane'}">
                        <label>One Deep Lane</label>
                        <c:out value="${schild.sample.name}" />
						<div>
						<a href="javascript:{}" onclick="showAssignForm(this)">(-)</a>
							<div style="display:none">
  							<form method="POST" action="<c:url value="/facility/platformunit/assignRemove.do" />">
						  		<input type="hidden" name="samplesourceid" value="<c:out value="${schild.sampleSourceId}" />">
  								<input type="submit" value="Remove">
  							</form>
							</div>
						</div>
                    </c:if>
                      <c:forEach items="${schild.sample.sampleSourceViaSourceSampleId}" var="schild2">
                        <c:if test="${schild2.sample.typeSample.IName == 'lane'}">
                          <div class="samplechild2">
                            <label>Two Deep</label>
                              <c:out value="${schild2.sample.name}" />
								<div>
								<a href="javascript:{}" onclick="showAssignForm(this)">(-)</a>
									<div style="display:none">
  									<form method="POST" action="<c:url value="/facility/platformunit/assignRemove.do" />">
										<input type="hidden" name="samplesourceid" value="<c:out value="${schild.sampleSourceId}" />">
  										<input type="submit" value="Remove">
  									</form>
									</div>
								</div>
                          </div>
                        </c:if>
                      </c:forEach>
                    </div>
                  </c:forEach>                    
               </div>
            </div>
          </c:forEach>
        </div>
      </c:forEach>
    </div><br />
  </c:forEach>
 </c:when>
 <c:otherwise>
 No Libraries Waiting For <c:out value="${machineName}" />
 </c:otherwise>
 </c:choose> 
</div>


<!-- 
<div class="platformUnitSide">
  <c:forEach items="${platformUnitStates}" var="state">
  <c:forEach items="${state.statesample}" var="stateSample">
    <c:set var="pu" value="${stateSample.sample}" />
    <div class="platformunit">
      <label>PU</label>
      <c:out value="${pu.name}" /> 
      <c:forEach items="${pu.sampleSource}" var="puparent">
         <div class="cell">
           <label>Cells</label>
           <c:out value="${puparent.sampleSourceId}" /> 
           <c:out value="${puparent.sampleViaSource.name}" /> 

           <c:forEach items="${puparent.sampleViaSource.sampleSource}" var="lib">
             <div class="library">
               <label>Library</label><c:out value="${lib.sampleViaSource.name}" />
               
               
               <c:if test="${lib.sampleViaSource.typeSample.IName == 'library'}">
                  <c:forEach items="${lib.sampleViaSource.sampleMeta}" var="sm">
                   <c:if test="${fn:substringAfter(sm.k, '.library.') == 'adaptorid'}">
                    <div><label>Adaptor</label> <c:out value="${adaptors[sm.v]}"/></div>
                    </c:if> 
                  </c:forEach> 
               </c:if>
               
               
               
                
             </div>
           </c:forEach>
         </div>
      </c:forEach>
    </div>
  </c:forEach>
  </c:forEach>
</div>
 -->


<div class="platformUnitSide">
<c:choose>
 <c:when test='${fn:length(flowCells) > "0"}'>
  <c:forEach items="${flowCells}" var="pu">
    <div class="platformunit">
      <label>Flow Cell</label>
      <c:out value="${pu.name}" /> 
      <c:forEach items="${pu.sampleSource}" var="puparent">
         <div class="cell">
           <label>Lane</label>
           <c:out value="${puparent.sampleSourceId}" /> 
           <c:out value="${puparent.sampleViaSource.name}" /> 

           <c:forEach items="${puparent.sampleViaSource.sampleSource}" var="lib">
             <div class="library">
               <label>Library</label> <c:out value="${lib.sampleViaSource.name}" />
               
               
               <c:if test="${lib.sampleViaSource.typeSample.IName == 'library'}">
                  <c:forEach items="${lib.sampleViaSource.sampleMeta}" var="sm">
                   <c:if test="${fn:substringAfter(sm.k, '.library.') == 'adaptorid'}">
                    <div><label>Adaptor</label> <c:out value="${adaptors[sm.v]}"/></div>
                    </c:if> 
                  </c:forEach> 
               </c:if>
             </div>
           </c:forEach>
         </div>
      </c:forEach>
    </div><br />
  </c:forEach>
  </c:when>
  <c:otherwise>
  No FlowCells Waiting For <c:out value="${machineName}" />
 </c:otherwise>
 </c:choose> 
</div>

 
 
<div style="clear:both" />
</div><!-- /assignmentContent -->


<script>
  var lanes = $(".selectLane");
    <c:forEach items="${flowCells}" var="pu">
	lanes.append($('<option></option>').val('<c:out value="0" />').html('<b><c:out value="${pu.name}" /></b>'));
      <c:forEach items="${pu.sampleSource}" var="puparent">
         lanes.append($('<option></option>').val('<c:out value="${puparent.sampleViaSource.sampleId}" />').html(' - <c:out value="${puparent.sampleViaSource.name}" />'));
      </c:forEach>
  </c:forEach>
</script>

<% /* TODO  javascript to read selected SampleId and scroll to it */ %>