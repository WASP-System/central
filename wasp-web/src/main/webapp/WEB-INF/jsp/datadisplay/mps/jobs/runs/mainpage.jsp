<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<%--these 2 dialog areas are not displayed until called; don't know where is best to put them, but they have to be somewhere or it doesn't work --%> 
<div id="modalDialog">
	<iframe id="modalIframeId" name="modalIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<div id="modalessDialog">
	<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>
<div class="pageContainer">
	<div id="selectionLeft" class="selectionLeft">	  
		<label>Job Name: <c:out value="${job.getName()}" /></label>	[<a style="color:red; font-weight:bold; background-color:Aqua;" id="jobDetailsAnchor"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />");' >DETAILS</a><c:if test="${fn:length(platformUnitSet) > 1}"> | <a id="openAllRunsAnchor"  href="javascript:void(0);" onclick='openAllRuns();' >open all runs</a> | <a id="closeAllRunsAnchor" href="javascript:void(0);"  onclick='closeAllRuns();' >close all runs</a></c:if>]
		<c:if test="${fn:length(platformUnitSet) > 0}">
		<div>
			<label>Aggregate Analysis</label> [<a id="aggregateAnalysis" href="javascript:void(0);" onclick='<%--toggleAnchors(this);--%> alert("Not yet implemented");'>details</a>] 
		</div>
		</c:if>	
		<c:forEach items="${platformUnitSet}" var="platformUnit">
			<c:set value="${platformUnitRunMap.get(platformUnit)}" var="run"/>
			<div id="runDivToHighlight_${run.getId()}">
			<label>Sequence Run:</label> <c:out value="${run.getName()}" /> <%-- (<label>FlowCell:</label> <c:out value="${platformUnit.getName()}" />)--%> 
				[<a id="runDetailsAnchor_${run.getId()}" href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/rundetails.do" />");' >details</a> 
				| <a id="runExpandAnchor_${run.getId()}" href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleExpandHide(this);' >expand</a>] 
				<div id="runDivToToggle_${run.getId()}" style="display:none;">					
					<c:set value="${platformUnitOrderedCellListMap.get(platformUnit)}" var="cellList"/>
					<c:forEach items="${cellList}" var="cell">
					<div>
						<c:set value="${cellIndexMap.get(cell)}" var="index"/>
						<label>Lane: 
							<c:choose>
								<c:when test="${not empty index }">							
								 	<c:out value="${index}" />
								</c:when>
								<c:otherwise>
									<c:out value="${cell.getName()}" /> 
								</c:otherwise>
							</c:choose>	
						</label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/celldetails.do" />");' >details</a>
						| <a id="cellSequencesDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/sequencedetails.do" />");' >sequences</a>
						| <a id="cellAlignmentsDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" <%-- target="myIframe" --%> onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/alignmentdetails.do" />");' >alignments</a> 
						| <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >fqc</a>
						| <a id="statsDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >stats</a>]
						<c:set value="${cellLibraryListMap.get(cell)}" var="libraryList"/>
						<div>
							<table class="data">
								<c:forEach items="${libraryList}" var="library" varStatus="statusMainPage">
									<c:if test="${statusMainPage.first}">
										<tr class="FormData">
											<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Parent</td>
											<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Library</td>
											<%-- <td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Species</td>--%>
											<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Index-Tag</td>
											<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">&nbsp;</td>
										</tr>																	
										<c:set value="${cellControlLibraryListMap.get(cell)}" var="controlLibraryList"/>							
										<c:forEach items="${controlLibraryList}" var="controlLibrary">
									  		<tr class="FormData">
									  			<td class="DataTD" style="text-align:center; white-space:nowrap;">N/A</td>
									 			<td class="DataTD" style="text-align:center; white-space:nowrap;" title="<c:out value="${controlLibrary.getName()}" />">
													<c:choose>
														<c:when test="${fn:length(controlLibrary.getName()) > 26}">
															<c:out value="${fn:substring(controlLibrary.getName(),0,24)}" />...
														</c:when>
														<c:otherwise>
															<c:out value="${controlLibrary.getName()}" />
														</c:otherwise>
													</c:choose>									 			
									 			</td>
												<c:set value="${libraryAdaptorMap.get(controlLibrary)}" var="adaptor"/>
													<c:choose>
														<c:when test="${not empty adaptor }">
															<td class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${adaptor.getBarcodenumber()}" />-<c:out value="${adaptor.getBarcodesequence()}" /></td>
														</c:when>
														<c:otherwise>
															<td class="DataTD" style="text-align:center; white-space:nowrap;">none</td>
														</c:otherwise>
													</c:choose>
													<td class="DataTD" style="text-align:center; white-space:nowrap;">details | fqc | stats</td>
									  			</tr>								
										</c:forEach>
									</c:if>	
									<tr>
										<c:set value="${libraryMacromoleculeMap.get(library)}" var="parentMacromolecule"/>
										<c:choose>
											<c:when test="${not empty parentMacromolecule }">
												<td class="DataTD" style="text-align:center; white-space:nowrap;" title="<c:out value="${parentMacromolecule.getName()}" />">
													<c:choose>
														<c:when test="${fn:length(parentMacromolecule.getName()) > 20}">
															<c:out value="${fn:substring(parentMacromolecule.getName(),0,18)}" />...
														</c:when>
														<c:otherwise>
															<c:out value="${parentMacromolecule.getName()}" />
														</c:otherwise>
													</c:choose>
												</td>
											</c:when>
											<c:otherwise>
												<td class="DataTD" style="text-align:center; white-space:nowrap;">N/A</td>
											</c:otherwise>
										</c:choose>										
										<td class="DataTD" style="text-align:center; white-space:nowrap;" title="<c:out value="${library.getName()}" />">
											<c:choose>
												<c:when test="${fn:length(library.getName()) > 26}">
													<c:out value="${fn:substring(library.getName(),0,24)}" />...
												</c:when>
												<c:otherwise>
													<c:out value="${library.getName()}" />
												</c:otherwise>
											</c:choose>
										</td>										
										<c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
										<c:choose>
										<c:when test="${not empty adaptor }">
											<td class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${adaptor.getBarcodenumber()}" />-<c:out value="${adaptor.getBarcodesequence()}" /></td>
										</c:when>
										<c:otherwise>
											<td class="DataTD" style="text-align:center; white-space:nowrap;">none</td>
										</c:otherwise>
										</c:choose>
										<td class="DataTD" style="text-align:center; white-space:nowrap;">
										
											<a id="libraryDetailsAnchor_${cell.getId()}${library.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/libraries/${library.getId()}/librarydetails.do" />");' >details</a>
											| <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >fqc</a>
											| <a id="statsDetailsAnchor_${run.getId()}"href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >stats</a>
											<%-- | <a id="cellSequencesDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellSequencesDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >sequences</a> | <a id="cellAlignmentsDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellAlignmentsDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >alignments</a>] --%>
						
										</td>
									</tr>
								</c:forEach>							
							</table>
							
							
							
							
							
							<table class="data">
								<c:forEach items="${libraryList}" var="library" varStatus="statusMainPage2">
									<c:if test="${statusMainPage2.first}">
										<tr class="FormData">
											<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Sample</td>
											<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Index</td>
											<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">&nbsp;</td>
										</tr>
																	
										<c:set value="${cellControlLibraryListMap.get(cell)}" var="controlLibraryList"/>							
										<c:forEach items="${controlLibraryList}" var="controlLibrary">
									  		<tr class="FormData">
									 			<td class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${controlLibrary.getName()}" /></td>
												<c:set value="${libraryAdaptorMap.get(controlLibrary)}" var="adaptor"/>
													<td class="DataTD" style="text-align:center; white-space:nowrap;">
														<c:choose>
															<c:when test="${not empty adaptor }">
																<c:out value="${adaptor.getBarcodenumber()}" />
															</c:when>
															<c:otherwise>
																none
															</c:otherwise>
														</c:choose>
													</td>
													<td class="DataTD" style="text-align:center;">[details | fastqc | stats]</td>
									  			</tr>								
										</c:forEach>
									</c:if>	
									<tr>
										<c:set value="${libraryMacromoleculeMap.get(library)}" var="parentMacromolecule"/>
										<td class="DataTD" style="text-align:center; white-space:nowrap;">
											<c:choose>
												<c:when test="${not empty parentMacromolecule }">
													<c:out value="${parentMacromolecule.getName()}" />
												</c:when>
												<c:otherwise>
													<c:out value="${library.getName()}" />
												</c:otherwise>
											</c:choose>	
										</td>									
										<c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
										<td class="DataTD" style="text-align:center; white-space:nowrap;">
											<c:choose>
												<c:when test="${not empty adaptor }">
													<c:out value="${adaptor.getBarcodenumber()}" />
												</c:when>
												<c:otherwise>
													none
												</c:otherwise>
											</c:choose>
										</td>
										<td class="DataTD" style="text-align:center; white-space:nowrap;">
										
											[<a id="libraryDetailsAnchor_${cell.getId()}${library.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/libraries/${library.getId()}/librarydetails.do" />");' >details</a>
											| <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >fastqc</a>
											| <a id="statsDetailsAnchor_${run.getId()}"href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >stats</a>] 
											<%-- | <a id="cellSequencesDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellSequencesDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >sequences</a> | <a id="cellAlignmentsDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellAlignmentsDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >alignments</a>] --%>
						
										</td>
									</tr>
								</c:forEach>							
							</table>
							
							
							
							
								<c:forEach items="${libraryList}" var="library" >								
									<c:set value="${cellControlLibraryListMap.get(cell)}" var="controlLibraryList"/>							
									<c:forEach items="${controlLibraryList}" var="controlLibrary">
										<div>
								 			<label>Control: </label><c:out value="${controlLibrary.getName()}" />
											<c:set value="${libraryAdaptorMap.get(controlLibrary)}" var="adaptor"/>
												(Library Index:
												<c:choose>
													<c:when test="${not empty adaptor }">
														<c:out value="${adaptor.getBarcodenumber()}" />
													</c:when>
													<c:otherwise>
														none
													</c:otherwise>
												</c:choose>
												)
												[details | fastq | stats]
										</div>							
									</c:forEach>
									
									<div>
										<c:set value="${libraryMacromoleculeMap.get(library)}" var="parentMacromolecule"/>
										<label>Sample: </label>
										<c:choose>
											<c:when test="${not empty parentMacromolecule }">
												<c:out value="${parentMacromolecule.getName()}" />
											</c:when>
											<c:otherwise>
												<c:out value="${library.getName()}" />
											</c:otherwise>
										</c:choose>										
										<c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
										(Library Index:
										<c:choose>
											<c:when test="${not empty adaptor }">
												<c:out value="${adaptor.getBarcodenumber()}" />
											</c:when>
											<c:otherwise>
												none
											</c:otherwise>
										</c:choose>
										<label>)</label>										
										[<a id="libraryDetailsAnchor_${cell.getId()}${library.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/libraries/${library.getId()}/librarydetails.do" />");' >details</a>
										| <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >fastqc</a>
										| <a id="statsDetailsAnchor_${run.getId()}"href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >stats</a>] 
										<%-- | <a id="cellSequencesDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellSequencesDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >sequences</a> | <a id="cellAlignmentsDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellAlignmentsDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >alignments</a>] --%>
									</div>		
								</c:forEach>							
							
							
							
							
							
						</div>
					</div>
					</c:forEach>
				</div>
	  		</div>
		</c:forEach>
 	</div>
	<div class="viewerRight">
		<div id="viewerFrame" style="display:block;">
  			<%--  <iframe id="myIframe" name="myIframe" src="http://webdesign.about.com/#lp-main" style="overflow-x: scroll; overflow-y: scroll" height="500px" width="500px" ><p>iframes not supported</p></iframe> --%>
  			<%-- <iframe id="myIframe" name="myIframe" src="<wasp:relativeUrl value="/sampleDnaToLibrary/jobDetails/${job.getId()}.do" />" style="overflow-x: scroll; overflow-y: scroll" height="800px" width="600px" ><p>iframes not supported</p></iframe>--%>
   		</div>
	</div>	
	<div style="clear:both;"></div>	
</div>

	<%-- do not remove without speaking to rob
	
	 	<br />more stuff: FOR DEMO ONLY; DO NOT NOW REMOVE PLEASE<br/>	
		 <input id="toggleButton" class="fm-button" type="button" value="Hide Viewport"  onClick="toggleViewerFrame(this)" />
		<br />
		<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
		<br />
		<a href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
		<br />
		<a href="<wasp:relativeUrl value="/datadisplay/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="window.open('<wasp:relativeUrl value="/datadisplay/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<wasp:relativeUrl value="/datadisplay/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<wasp:relativeUrl value="/datadispaly/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="/datadispaly/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
		<br /><br />
		<a style="color:green; font-weight:bold; background-color:white;" id="jobDetailsAnchorzzzzzzzz"  href="javascript:void(0);"  onclick='loadNewPage(this, "<wasp:relativeUrl value="/datadispaly/showplay.do" />");' >use SHOWPLAY INTO div on right</a>
		<br /><br />
		<a style="color:green; font-weight:bold; background-color:white;" id="jobDetailsAnchorzzzzzzzz"  href="javascript:void(0);" target="myIframe" onclick='loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/showplay.do" />");' >use SHOWPLAY INTO div on right</a>
--%>
<%--NO INNER TABLES, just 
job
	run
		lane
			control
			library:
			Library:
		lane
			library:
	run
	run
	
<div class="pageContainer">
	<div id="selectionLeft" class="selectionLeft">	  
		<label>Job Name: <c:out value="${job.getName()}" /></label>	[<a style="color:red; font-weight:bold; background-color:white;" id="jobDetailsAnchor"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/jobdetails.do" />");' >DETAILS</a><c:if test="${fn:length(platformUnitSet) > 1}"> | <a id="openAllRunsAnchor"  href="javascript:void(0);" onclick='openAllRuns();' >open all runs</a> | <a id="closeAllRunsAnchor" href="javascript:void(0);"  onclick='closeAllRuns();' >close all runs</a></c:if>]
		<c:if test="${fn:length(platformUnitSet) > 0}">
		<div>
			<label>Aggregate Analysis</label> [<a id="aggregateAnalysis" href="javascript:void(0);" onclick='alert("Not yet implemented");'>details</a>] 
		</div>
		</c:if>	
		<c:forEach items="${platformUnitSet}" var="platformUnit">
			<c:set value="${platformUnitRunMap.get(platformUnit)}" var="run"/>
			<div id="runDivToHighlight_${run.getId()}">
			<label>Sequence Run:</label> <c:out value="${run.getName()}" />  
			[<a id="runDetailsAnchor_${run.getId()}" href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/rundetails.do" />");' >details</a> 
			| <a id="runExpandAnchor_${run.getId()}" href="javascript:void(0);"  onclick='toggleExpandHide(this);' >expand</a>] 
					<div id="runDivToToggle_${run.getId()}" style="display:none;">					
					<c:set value="${platformUnitOrderedCellListMap.get(platformUnit)}" var="cellList"/>
					<c:forEach items="${cellList}" var="cell">
						<div>
							<c:set value="${cellIndexMap.get(cell)}" var="index"/>
							<c:choose>
								<c:when test="${not empty index }">							
									<label>Lane: <c:out value="${index}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);" onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/celldetails.do" />");' >details</a> | <a id="fastQCDetailsAnchor_${run.getId()}" href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >fastqc</a> | <a id="statsDetailsAnchor_${run.getId()}"href="javascript:void(0);" onclick='showPopupWindow("http://wasp.einstein.yu.edu/results/production_wiki/JLocker/JTian/P520/J10728/stats/stats_TrueSeqUnknown.BC1G0RACXX.lane_5_P0_I0.fastq.html");' >graphical Stats</a> | <a id="cellSequencesDetailsAnchor_${cell.getId()}"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellSequencesDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >sequences</a> | <a id="cellAlignmentsDetailsAnchor_${cell.getId()}"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/sampleDnaToLibrary/cellAlignmentsDetails/${cell.getId()}.do?jobId=${job.getId()}&runId=${run.getId()}" />");' >alignments</a>] 
								</c:when>
								<c:otherwise>
									<label>Lane: <c:out value="${cell.getName()}" /></label> [<a id="cellDetailsAnchor_${cell.getId()}"  href="javascript:void(0);"  onclick='toggleAnchors(this); loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/celldetails.do" />");' >details</a>] 
								</c:otherwise>
							</c:choose>													
							<c:set value="${cellControlLibraryListMap.get(cell)}" var="controlLibraryList"/>
							<c:if test="${not empty controlLibraryList }">
								<c:forEach items="${controlLibraryList}" var="controlLibrary">
								  <div>									
									<label>Control:</label> 
									<c:set value="${libraryAdaptorMap.get(controlLibrary)}" var="adaptor"/>
									<c:if test="${not empty adaptor }">
										
										[Index <c:out value="${adaptor.getBarcodenumber()}" />; <c:out value="${adaptor.getBarcodesequence()}" />]
									</c:if>
								  </div>									
								</c:forEach>
							</c:if>						
							<c:set value="${cellLibraryListMap.get(cell)}" var="libraryList"/>
							<c:forEach items="${libraryList}" var="library">
								<div>
									<label>Library:</label> <c:out value="${library.getName()}" />
									 
									<c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
									<c:if test="${not empty adaptor }">
										
										[Index <c:out value="${adaptor.getBarcodenumber()}" />; <c:out value="${adaptor.getBarcodesequence()}" />]
									</c:if>
									
									<c:set value="${libraryMacromoleculeMap.get(library)}" var="parentMacromolecule"/>
									<c:choose>
									<c:when test="${not empty parentMacromolecule }">
										(<label>Parent:</label> <c:out value="${parentMacromolecule.getName()}" />)
									</c:when>
									<c:otherwise>
										(<label>Parent:</label> N/A)
									</c:otherwise>
									</c:choose>
								</div>
							</c:forEach>
						</div>
					</c:forEach>
				</div>
		  	</div>
		</c:forEach>

		<br /><br />
		________________________________	
	 	<br /><br />more stuff: FOR DEMO ONLY; DO NOT NOW REMOVE PLEASE<br/>	
		 <input id="toggleButton" class="fm-button" type="button" value="Hide Viewport"  onClick="toggleViewerFrame(this)" />
		<br />
		<a href="http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html" target="myIframe">Right Frame: View Fastqc report from /results/production_wiki</a>
		<br />
		<a href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />" target="myIframe">Right Frame: Wasp job 87's home page</a>
		<br />
		<a href="<wasp:relativeUrl value="/datadisplay/showplay.do" />" target="myIframe">Right Frame: view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html');">Aligned Popup WINDOW: fastqc</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />');">Aligned Popup WINDOW: Wasp job 87's home page</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="window.open('<wasp:relativeUrl value="/datadisplay/showplay.do" />', 'Child Window','width=1200,height=800,left=0,top=0,scrollbars=1,status=0,');">Popup WINDOW (left): view a FILE stored on Chiam</a>
		<br />	
		<a href="javascript:void(0);" title="popup"  onclick="showPopupWindow('<wasp:relativeUrl value="/datadisplay/showplay.do" />');">Aligned Popup WINDOW: File on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modal Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modal Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalDialog("<wasp:relativeUrl value="/datadisplay/showplay.do" />");' >Modal Dialog: view a FILE stored on Chiam</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("http://wasp.einstein.yu.edu/results/production_wiki/TestPI/TestPI/P498/J10740/stats/TrueSeqUnknown.BC1G0RACXX.lane_8_P0_I0.hg19.sequence.fastq.passFilter_fastqc/fastqc_report.html");' >Modaless Dialog: fastqc</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/87.do" />");' >Modaless Dialog: Wasp job 87's home page</a>
		<br />
		<a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="/datadisplay/showplay.do" />");' >Modaless Dialog: view a FILE stored on Chiam</a>
		<br /><br />
		<a style="color:green; font-weight:bold; background-color:white;" id="jobDetailsAnchorzzzzzzzz"  href="javascript:void(0);"  onclick='loadNewPage(this, "<wasp:relativeUrl value="/datadisplay/showplay.do" />");' >use SHOWPLAY INTO div on right</a>
 	</div>
	<div class="viewerRight">
		<div id="viewerFrame" style="display:block;">
   		</div>
	</div>	
	<div style="clear:both;"></div>	
</div>	
	
	
	
	 --%>
