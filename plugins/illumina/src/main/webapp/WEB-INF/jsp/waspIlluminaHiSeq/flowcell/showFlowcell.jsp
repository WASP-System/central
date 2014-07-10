<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
 <br />
<h1><fmt:message key="pageTitle.waspIlluminaHiSeq/flowcell/showflowcell.label"/></h1>
<%-- <c:out value="${runLocked}" /> --%>
<div>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.typeOfPlatformUnit.label"/>:</td><td class="DataTD"><c:out value="${typeOfPlatformUnit}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.barcodeName.label"/>:</td><td class="DataTD"><c:out value="${barcodeName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.readType.label"/>:</td><td class="DataTD"><c:out value="${readType}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.readLength.label"/>:</td><td class="DataTD"><c:out value="${readLength}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="waspIlluminaPlugin.showPlatformUnit_cellcount.label"/>:</td><td class="DataTD"><c:out value="${numberOfCellsOnThisPlatformUnit}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.comment.label"/>:</td><td class="DataTD"><textarea style='font-size:9px' DISABLED cols='30' rows='4' wrap='virtual'><c:out value="${comment}" /></textarea></td></tr>
<tr><td colspan='2' style='text-align:center; padding:10px' >
<a href='<wasp:relativeUrl value="facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=${platformUnitSampleSubtypeId}&sampleId=${platformUnitSampleId}" />'><fmt:message key="platformunitShow.edit.label"/></a> 
<%--permit platformUnit to be deleted only if it has no runs --%>
<c:if test="${sequenceRuns.size()==0}">
| <a href='javascript:void(0)' onclick = 'if(confirm("<fmt:message key="platformunitShow.wantToDeletePU.label"/>")){location.href="<wasp:relativeUrl value="facility/platformunit/deletePlatformUnit.do?sampleId=${platformUnitSampleId}" />";}'><fmt:message key="platformunitShow.delete.label"/></a> 
| <a href='<wasp:relativeUrl value="waspIlluminaHiSeq/flowcell/${platformUnitSampleId}/run/create.do"/>'><fmt:message key="platformunitShow.addToRun.label"/></a>
</c:if>
</td></tr>
</table>
</div>

<c:set var="idCounter" value="0" scope="page" />
<c:set var="idNewControlCounter" value="0" scope="page" />

<c:if test="${sequenceRuns.size() > 0}">
<div>
<table class="data">
<tr>
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.run.label"/></td>
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.machine.label"/></td>
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.length.label"/></td>
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.type.label"/></td>
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.start.label"/></td>
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.end.label"/></td>
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.status.label"/></td>
<c:if test="${runLocked == false}">
<td class="value-centered-small-heavyborder" style="background-color:#FAF2D6" nowrap><fmt:message key="platformunitShow.action.label"/></td>
</c:if>
</tr>
<c:forEach items="${sequenceRuns}" var="sequenceRun">
<tr>
<td class="value-centered-small"><c:out value="${sequenceRun.getName()}" /></td>
<td class="value-centered-small"><c:out value="${sequenceRun.resource.name}" /> - <c:out value="${sequenceRun.resourceCategory.name}" /></td>
<c:set var="detailMap" value="${runDetails[sequenceRun.runId]}" scope="page" />
<td class="value-centered-small"><c:out value='${detailMap["readLength"]}' /></td>
<td class="value-centered-small"><c:out value='${detailMap["readType"]}' /></td>
<td class="value-centered-small"><c:out value='${detailMap["dateRunStarted"]}' /></td>
<td class="value-centered-small"><c:out value='${detailMap["dateRunEnded"]}' /></td>
<td class="value-centered-small"><c:out value='${detailMap["runStatus"]}' /></td>
<c:if test="${runLocked == false}">
<td class="value-centered-small"><a href='<wasp:relativeUrl value="waspIlluminaHiSeq/run/${sequenceRun.runId}/update.do" />'><fmt:message key="platformunitShow.editSmall.label"/></a> | <a href='javascript:void(0)' onclick = 'if(confirm("<fmt:message key="platformunitShow.wantToDeleteRun.label"/>")){location.href="<wasp:relativeUrl value="run/${sequenceRun.runId}/delete.do" />";}'><fmt:message key="platformunitShow.deleteSmall.label"/></a></td>
</c:if>
</tr>
</c:forEach>
</table>
</div>
</c:if>

<c:if test="${platformUnit.sampleSource.size() > 0}">

<div class="fixed-width_scrollable">
	<table class="data" >
		<tr><td colspan="${platformUnit.sampleSource.size()}" class="label-centered" style="background-color:#FAF2D6" nowrap><c:out value="${platformUnit.name}" /></td></tr>
		<tr>
			<c:forEach items="${platformUnit.sampleSource}" var="puCell">
				<c:set var="cell" value="${puCell.sourceSample}" scope="page" />
					<td class="label-centered"  style="background-color:#FAF2D6" nowrap><fmt:message key="waspIlluminaPlugin.showPlatformUnit_cell.label"/>: <c:out value="${fn:substringAfter(cell.name, '/')}" /></td>
			</c:forEach>
		</tr>
		<tr>
		  
			<c:forEach items="${platformUnit.sampleSource}" var="puCell">
				<c:set var="numberControlLibrariesPerLane" value="0" scope="page" />
				<c:set var="cell" value="${puCell.sourceSample}" scope="page" />
				 <td class="value-centered-small-heavyborder" nowrap>			
					<c:forEach items="${cell.sampleSource}" var="cellLibrary">
				  		<c:set var="controlLibrary" value="${cellLibrary.sourceSample}" scope="page" />
				  		<c:if test="${controlLibrary.sampleSubtype.getIName() == 'controlLibrarySample'}">
				  			<c:set var="numberControlLibrariesPerLane" value="${numberControlLibrariesPerLane + 1}" scope="page" />
				  			<c:out value="${controlLibrary.name}" /> 
				  			<c:set var="controlLibraryMeta" value="${controlLibrary.sampleMeta}" scope="page" />
							<c:forEach items="${controlLibraryMeta}" var="controlLibraryMetaItem">
								<c:if test="${fn:substringAfter(controlLibraryMetaItem.k, 'Library.') == 'adaptor'}">
            						<br /><c:out value="${adaptors.get(controlLibraryMetaItem.v).adaptorset.name}"/>
            						<br /><fmt:message key="showPlatformUnit.index.label"/> <c:out value="${adaptors.get(controlLibraryMetaItem.v).barcodenumber}"/>: <c:out value="${adaptors.get(controlLibraryMetaItem.v).barcodesequence}"/>
            					</c:if> 
            				</c:forEach>
            				<c:set var="cellLibraryMeta" value="${cellLibrary.sampleSourceMeta}" scope="page" />
            				<c:forEach items="${cellLibraryMeta}" var="cellLibraryMetaItem">
            					<c:if test="${fn:indexOf(cellLibraryMetaItem.k,'libConcInCellPicoM') > -1 }"><br /><fmt:message key="waspIlluminaPlugin.showPlatformUnit_concOnCell.label"/>: <c:out value="${cellLibraryMetaItem.v}"/> <fmt:message key="showPlatformUnit.pM.label"/> </c:if>					
            				</c:forEach>
            				<c:if test="${runLocked == false}">
	            				<%-- <form  name='removeLib' method='post' action="<wasp:relativeUrl value="facility/platformunit/assignRemove.do" />" onsubmit='return confirm("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_removeControlFromThisCell.label"/>");'> --%>
								<form  name='removeLib' method='post' action="<wasp:relativeUrl value="facility/platformunit/assignRemove.do" />" onsubmit='return confirmRemove("controlLibrary");'>
								<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
								<input type='hidden' name='cellLibraryId' value='<c:out value="${cellLibrary.getId()}" />'/>
								<input type='submit' value='<fmt:message key="showPlatformUnit.removeControl.label"/>'/>
								</form>	
							</c:if>
            				
            					
            				<%--not needed anymore; only one control permitted, at least for now    <hr> --%>
            			</c:if>
					</c:forEach>			  						  		
					<c:if test="${numberControlLibrariesPerLane == 0 }">
						<fmt:message key="waspIlluminaPlugin.showPlatformUnit_noControlOnCell.label"/>
						<hr>
					</c:if>
					
					<c:set var="idNewControlCounter" value="${idNewControlCounter + 1}" scope="page" />
					<c:if test="${runLocked == false && numberControlLibrariesPerLane == 0}"><%--only allow one control per lane --%>
						<a href="javascript:void(0)" id="newControlAnchor_<c:out value="${idNewControlCounter}" />" onclick="toggleDisplayAddNewControlForm('show_form',<c:out value="${idNewControlCounter}" />)"><fmt:message key="showPlatformUnit.addControl.label"/></a>
					</c:if>
					<div id="idNewControlFormDiv_<c:out value="${idNewControlCounter}" />" style="display:none">
						<form id="addNewControlToLaneForm_<c:out value="${idNewControlCounter}" />"  method='post' action="<wasp:relativeUrl value="waspIlluminaHiSeq/flowcell/addNewControlToLane.do" />" >
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='cellId' value='<c:out value="${cell.sampleId}" />'/>
							<table class="data">
								<tr><td class="value-centered-small">
								<select id="newControlId_<c:out value="${idNewControlCounter}" />" name="newControlId">
									<option value=""><fmt:message key="showPlatformUnit.selectControl.label"/></option>
									 <c:forEach items="${controlLibraryList}" var="controlLibrary">
									 	<option value='<c:out value="${controlLibrary.sampleId}" />'><c:out value="${controlLibrary.name}" />
									 	<c:forEach items="${controlLibrary.sampleMeta}" var="controlLibrarySampleMetaItem">
									 		<c:if test="${fn:substringAfter(controlLibrarySampleMetaItem.k, 'Library.') == 'adaptor'}">
            									&nbsp;-&nbsp;<c:out value="${adaptors.get(controlLibrarySampleMetaItem.v).adaptorset.name}"/> [<fmt:message key="showPlatformUnit.index.label"/> <c:out value="${adaptors.get(controlLibrarySampleMetaItem.v).barcodenumber}"/> (<c:out value="${adaptors.get(controlLibrarySampleMetaItem.v).barcodesequence}"/>)]
            								</c:if> 
									 	</c:forEach>
									 	</option>
									 </c:forEach>
								</select>
								
								</td></tr>
								<tr><td class="value-centered-small"><fmt:message key="showPlatformUnit.finalConcPM.label"/>: <input type='text' name='newControlConcInCellPicoM' id="newControlConcInCellPicoM_<c:out value="${idNewControlCounter}" />" size='3' maxlength='5' ></td></tr>
								<tr><td class="value-centered-small">
								<input type="button" value="<fmt:message key="showPlatformUnit.add.label"/>" onclick='validateAddNewControlToLaneForm(<c:out value="${idNewControlCounter}" />)' />&nbsp;<input type="button" value="<fmt:message key="showPlatformUnit.cancel.label"/>" onclick='toggleDisplayAddNewControlForm("cancel_form", <c:out value="${idNewControlCounter}" />)' />
								</td></tr>
							</table>
						</form>
					</div>										
				</td>
			</c:forEach>			
		</tr>
		
		<tr>
		<c:forEach items="${platformUnit.sampleSource}" var="puCell">
			<c:set var="cell" value="${puCell.sourceSample}" scope="page" />
			<td class="value-centered-top-small-heavyborder" >
				<c:set var="counter" value="1" scope="page" />
				<c:set var="numberRegularLibrariesPerLane" value="0" scope="page" />
				<c:forEach items="${cell.sampleSource}" var="cellLibrary">
				  <c:set var="library" value="${cellLibrary.sourceSample}" scope="page" />
				  
				  
				  <c:if test="${library.sampleSubtype.getIName() != 'controlLibrarySample'}">	
				    <c:if test="${counter > 1}"><hr></c:if>
					<c:set var="numberRegularLibrariesPerLane" value="${numberRegularLibrariesPerLane + 1}" scope="page" />						
					<c:out value="${library.name}" />
					<c:set var="cellLibraryMeta" value="${cellLibrary.sampleSourceMeta}" scope="page" />
					<c:forEach items="${cellLibraryMeta}" var="cellLibraryMetaItem">
						<c:if test="${fn:indexOf(cellLibraryMetaItem.k,'jobId') > -1}"><a href="<wasp:relativeUrl value="job/${cellLibraryMetaItem.v}/homepage.do" />"> (<fmt:message key="showPlatformUnit.jobJ.label"/><c:out value="${cellLibraryMetaItem.v}"/>)</a></c:if>
					</c:forEach>
					<br />				
					<c:set var="libraryMeta" value="${library.sampleMeta}" scope="page" />
					<c:forEach items="${libraryMeta}" var="libraryMetaItem">
						<c:if test="${fn:substringAfter(libraryMetaItem.k, 'Library.') == 'adaptor'}">
            				<c:out value="${adaptors.get(libraryMetaItem.v).adaptorset.name}"/><br />
            				<fmt:message key="showPlatformUnit.index.label"/> <c:out value="${adaptors.get(libraryMetaItem.v).barcodenumber}"/>: <c:out value="${adaptors.get(libraryMetaItem.v).barcodesequence}"/><br />
            			</c:if> 
					</c:forEach>
					<c:forEach items="${cellLibraryMeta}" var="cellLibraryMetaItem">
						<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
						<%--  	<c:if test="${cellLibraryMetaItem.k=='libConcInCellPicoM'}"><fmt:message key="showPlatformUnit.concOnCell.label"/>: <c:out value="${cellLibraryMetaItem.v}"/> <fmt:message key="showPlatformUnit.pM.label"/> <br /></c:if>  --%>
						<c:set var="currentConcentration" value="" scope="page" />
						<div id="editAnchorDiv_<c:out value="${idCounter}" />" >
						<c:if test="${fn:indexOf(cellLibraryMetaItem.k, 'libConcInCellPicoM') > -1 }"><c:set var="currentConcentration" value="${cellLibraryMetaItem.v}" scope="page" /><fmt:message key="showPlatformUnit.concOnCell.label"/>: <c:out value="${cellLibraryMetaItem.v}"/> <fmt:message key="showPlatformUnit.pM.label"/> <a href="javascript:void(0)" onclick='toggleDisplayOfUpdateForm("show", <c:out value="${idCounter}" />);'>[<fmt:message key="showPlatformUnit.edit.label"/>]</a><br />	</c:if>					
						</div>	
						
						<div id="updatePicoFormDiv_<c:out value="${idCounter}" />" style="display:none">
						<form id="updatePicoForm_<c:out value="${idCounter}" />"  method='post' action="<wasp:relativeUrl value="waspIlluminaHiSeq/flowcell/updateConcInLane.do" />" >
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='cellLibraryId' value='<c:out value="${cellLibrary.getId()}" />'/>
							<table class="data">
								<tr><td class="value-centered-small"><fmt:message key="showPlatformUnit.currentConcPM.label"/>: <c:out value="${currentConcentration}" /></td></tr>
								<tr><td class="value-centered-small"><fmt:message key="showPlatformUnit.newConcPM.label"/>: <input type='text' name='libConcInCellPicoM' id="libConcInCellPicoM_<c:out value="${idCounter}" />" size='3' maxlength='5' ></td></tr>
								<tr><td class="value-centered-small">
								<input type="button" value="<fmt:message key="showPlatformUnit.update.label"/>" onclick='validateUpdateForm(<c:out value="${idCounter}" />)' />&nbsp;<input type="button" value="<fmt:message key="showPlatformUnit.cancel.label"/>" onclick='toggleDisplayOfUpdateForm("cancel", <c:out value="${idCounter}" />)' />
								</td></tr>
							</table>
						</form>
						</div>						
					
					</c:forEach>
					<c:if test="${runLocked == false}">
					<%--	<form  name='removeLib' method='post' action="<wasp:relativeUrl value="facility/platformunit/assignRemove.do" />" onsubmit='return confirm("<fmt:message key="waspIlluminaPlugin.showPlatformUnit_removeLibFromCell_alert.label"/>");'> --%>
						<form  name='removeLib' method='post' action="<wasp:relativeUrl value="facility/platformunit/assignRemove.do" />" onsubmit='return confirmRemove("thisIsNotControlLibrary");'>
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='cellLibraryId' value='<c:out value="${cellLibrary.getId()}" />'/>
							<input type='submit' value='<fmt:message key="showPlatformUnit.removeLibrary.label"/>'/>
						</form>	
					</c:if>				
					<c:set var="counter" value="${counter + 1}" scope="page" />
				  </c:if>	
				</c:forEach>
			
			<c:if test="${numberRegularLibrariesPerLane == 0 }">
				<fmt:message key="waspIlluminaPlugin.showPlatformUnit_noLibrariesOnCell.label"/>
			</c:if>	
			
		</td>
		</c:forEach>		
		</tr>
	</table>
	</div>
</c:if>


