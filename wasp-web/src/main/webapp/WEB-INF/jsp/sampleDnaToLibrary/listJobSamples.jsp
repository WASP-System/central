<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<head>
<script src="/wasp/scripts/jquery/jquery-1.7.1.js" type="text/javascript"></script>
<script>
function showAssignForm(e) {
	  e.parentNode.getElementsByTagName("A")[0].style.display = "none"; 
	  e.parentNode.getElementsByTagName("DIV")[0].style.display = "block"; 
	}

</script>
</head>
  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></title>
<h1><fmt:message key="sampleDnaToLibrary.listJobSamples.title_label" /></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br />
<hr />
<br />
<table class="data"> <!-- EditTable ui-widget ui-widget-content -->
<tr class="FormData"><td class="label-centered">Initial Macromolecule</td><td class="label-centered">Libraries</td></tr>

<c:forEach items="${samplesSubmitted}" var="sample" varStatus="counter">
<tr class="FormData">

<c:choose>
<c:when test='${sample.typeSample.IName=="library"}'>
	<td class="value-centered" ><br />N/A </td>
	<td class="value-centered">
		<br />Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${sample.sampleId}.do" />"><c:out value="${sample.name}" /></a><br />
		 Molecule: <c:out value="${sample.typeSample.name}" /><br /> 
		 <c:forEach items="${sample.sampleMeta}" var="sm">
        	<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            	Species: <c:out value="${sm.v}"/><br />
            </c:if> 
        </c:forEach> <c:forEach items="${sample.sampleMeta}" var="sm">
        	<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            	Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            	Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            </c:if> 
        </c:forEach> 
		<c:choose>
			<c:when test='${received[counter.index]=="RECEIVED"}'>			
				<input class="FormElement ui-widget-content ui-corner-all" type="button" value="Add To FlowCell" />
		 	 </c:when>
		 	 <c:otherwise>
		 	 	Status: <c:out value="${received[counter.index]}" />
		 	 	<c:if test='${received[counter.index]=="NOT ARRIVED"}'>	
		 	 		<a href="<c:url value="/task/samplereceive/list.do" />">
		 	 			<c:out value=" (log sample)" />
		 	 		</a>
		 	 	</c:if>
		 	 </c:otherwise>
		</c:choose>	
	</td>
</c:when>
<c:when test='${sample.typeSample.IName=="dna" || sample.typeSample.IName=="rna"}'>
	<td class="value-centered"><br />Name: <a href="<c:url value="/sampleDnaToLibrary/sampledetail_ro/${job.jobId}/${sample.sampleId}.do" />"><c:out value="${sample.name}" /></a><br />
	  Molecule: <c:out value="${sample.typeSample.name}" /><br /> 
	  <c:forEach items="${sample.sampleMeta}" var="sm">
        	<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            	Species: <c:out value="${sm.v}"/><br />
            </c:if> 
        </c:forEach> 
	  
	  
	  
	  <c:choose>
	  	<c:when test='${received[counter.index]=="RECEIVED"}'>
	  	
	  	
	  		<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  		<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  		<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='macromolSampleId' value='<c:out value="${sample.sampleId}" />'/>
	  		
				<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
				<option value="0">--ADAPTORS FOR NEW LIBRARY--
				<c:forEach items="${adaptorsets}" var="adaptorset">
					<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
				</c:forEach>
				</select>
			</form>
	 	 </c:when>
	 	 <c:otherwise>
	 	 	Status: <c:out value="${received[counter.index]}" />
	 	 	<c:if test='${received[counter.index]=="NOT ARRIVED"}'>	
		 	 	<a href="<c:url value="/task/samplereceive/list.do" />">
		 	 		<c:out value=" (log sample)" />
		 	 	</a>
		 	 </c:if>
	 	 </c:otherwise>
	  </c:choose>					  
	</td>
	<td class="value-centered">
		<c:set var="i" value="0" scope="page" /> 
		<c:forEach items="${sample.sampleSourceViaSourceSampleId}" var="samplesource">
			<c:set var="lib" value="${samplesource.sample}"/> 
				<c:if test="${i > 0}">
					<hr />
				</c:if>
				<c:set var="i" value="${i + 1}" scope="page" />		
				Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${lib.sampleId}.do" />"><c:out value="${lib.name}" /></a><br />
				Molecule: <c:out value="${sample.typeSample.name}" /><br />
				<c:forEach items="${sample.sampleMeta}" var="sm">
        			<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            			Species: <c:out value="${sm.v}"/><br />
            		</c:if> 
        		</c:forEach>
        		<c:forEach items="${lib.sampleMeta}" var="sm">
        			<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            			Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            			Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            		</c:if> 
		        </c:forEach> 
		        
		        
				
				
				<div>
		<!-- 			<a href="javascript:{}" onclick="showAssignForm(this)">(+)</a>
					<div style="display:none">
			 -->	
				
				
				<table class='data'>
				<tr class="FormData"><td class="label-centered">Add Library To Flow Cell Lane</td></tr>
				<tr><td>
				<form action="" method='post' name='addLibToPU' onsubmit='alert("Not Ready"); return false;'>
				
 				 <input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
				 <input type='hidden' name='libraryId' value='<c:out value="${lib.sampleId}" />'/>
				 <br />
				 <select class="FormElement ui-widget-content ui-corner-all" name="platformunitId" size="1">
					<option value="0">--SELECT A FLOW CELL LANE--
					<c:forEach items="${flowCells}" var="flowCell">
						<option value="<c:out value="${flowCell.sampleId}" />" >FlowCell: <c:out value="${flowCell.name}" />
						<c:forEach items="${flowCell.sampleSource}" var="cell">
							<option value="<c:out value="${cell.sampleViaSource.sampleId}" />" >&nbsp;&nbsp;&nbsp;Lane: <c:out value="${cell.sampleViaSource.name}" />
							<c:forEach items="${cell.sampleViaSource.sampleSource}" var="library">
								<option value="<c:out value="${library.sampleViaSource.sampleId}" />" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library: <c:out value="${library.sampleViaSource.name}" />
								
								
									<c:forEach items="${library.sampleViaSource.sampleMeta}" var="sm">
        								<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            								&nbsp;[Index: <c:out value="${adaptors.get(sm.v).barcodesequence}"/>]
            							</c:if> 
		        					</c:forEach> 							
								
							</c:forEach> 
						</c:forEach> 
					</c:forEach>
				</select>
				<br />&nbsp;Provide picoM Added: <input type='text' name='picoMadded' size='3' maxlength='5'>
				<br />&nbsp;<input type='submit' value='Submit'/>
				</form>
				</td></tr></table>
						
						
				</div>
				  </div>		
						
								
		</c:forEach>
	</td>
	
</c:when>
</c:choose>
</tr>
</c:forEach>
</table>