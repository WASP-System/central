<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 <br />
<title><fmt:message key="pageTitle.facility/platformunit/showPlatformUnit.label"/></title>
<h1><fmt:message key="pageTitle.facility/platformunit/showPlatformUnit.label"/></h1>
<br /> 

<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.typeOfPlatformUnit.label"/>:</td><td class="DataTD"><c:out value="${typeOfPlatformUnit}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.barcodeName.label"/>:</td><td class="DataTD"><c:out value="${barcodeName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.readType.label"/>:</td><td class="DataTD"><c:out value="${readType}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.readlength.label"/>:</td><td class="DataTD"><c:out value="${readlength}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.numberOfCellsOnThisPlatformUnit.label"/>:</td><td class="DataTD"><c:out value="${numberOfCellsOnThisPlatformUnit}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.comment.label"/>:</td><td class="DataTD"><textarea style='font-size:9px' READONLY cols='30' rows='4' wrap='virtual'><c:out value="${comment}" /></textarea></td></tr>
<tr><td colspan='2' style='text-align:center; padding:10px' >
<a href='<c:url value="/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=${platformUnitSampleSubtypeId}&sampleId=${platformUnitSampleId}" />'>Edit</a> | <a href='javascript:' onclick = 'if(confirm("Do you really want to delete this platform unit record?")){location.href="<c:url value="/facility/platformunit/deletePlatformUnit.do?sampleId=${platformUnitSampleId}" />";}'>Delete</a> | <a href='<c:url value="/run/createUpdateRun.do?resourceId=0&runId=0&platformUnitId=${platformUnitSampleId}" />'>Add To Run</a> 
</td></tr>
</table>

<c:set var="idCounter" value="0" scope="page" />
<c:set var="idNewControlCounter" value="0" scope="page" />

<%-- commetned out 10-5-12
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.type.label"/>:</td><td class="DataTD"><c:out value="${platformUnit.sampleSubtype.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.platformUnit.label"/>:</td><td class="DataTD"><c:out value="${platformUnit.name}" /></td></tr>
<c:forEach items="${platformUnit.sampleBarcode}" var="sampleBarcodeItem">
	<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.barcode.label"/>:</td><td class="DataTD"><c:out value="${sampleBarcodeItem.barcode.barcode}" /></td></tr></c:forEach>
<!--  <tr class="FormData"><td class="CaptionTD">Lanes:</td><td class="DataTD"><c:out value="${platformUnit.sampleSource.size()}" /></td></tr> -->
<c:forEach items="${platformUnit.sampleMeta}" var="pusm">
	<c:if test="${fn:substringAfter(pusm.k, '.') != 'comment'}">
		<tr class="FormData"><td class="CaptionTD" style="text-transform: capitalize"><c:out value="${fn:toLowerCase(fn:substringAfter(pusm.k, '.'))}" />:</td><td class="DataTD"><c:out value="${pusm.v}" /></td></tr>
	</c:if>
</c:forEach>
<c:forEach items="${platformUnit.sampleMeta}" var="pusm">
	<c:if test="${fn:substringAfter(pusm.k, '.') == 'comment' && pusm.v != '' }">
		<tr class="FormData"><td class="CaptionTD" style="text-transform: capitalize"><c:out value="${fn:toLowerCase(fn:substringAfter(pusm.k, '.'))}" />:</td><td class="DataTD"><textarea style='font-size:9px' READONLY cols='25' rows='4' wrap='virtual'><c:out value="${pusm.v}" /></textarea></td></tr>
	</c:if>
</c:forEach>
--%>

<%-- was commented out a long time ago
<sec:authorize access="hasRole('su')">
<c:if test="${runList.size() > 0}">
<c:choose>
	<c:when test="${platformUnitStatus == 'UNKNOWN'}">
		<tr class="FormData"><td colspan="2" class="CaptionTD" style="text-align:center"><hr><br /><fmt:message key="showPlatformUnit.platformUnitStatus.label"/>: <c:out value="${platformUnitStatus}" /></td></tr>
	</c:when>
	<c:otherwise>
		<tr class="FormData">
			<td colspan="2" class="DataTD" style="text-align:center">
				<form method='post' action="<c:url value="/facility/platformunit/lockPlatformUnit.do" />" >
					<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
					<c:set var="created" value="" scope="page" />
					<c:set var="completed" value="" scope="page" />
					<c:if test="${platformUnitStatus == 'CREATED'}">
						<c:set var="created" value="checked" scope="page" />
					</c:if>
					<c:if test="${platformUnitStatus == 'COMPLETED' || platformUnitStatus == 'FINALIZED'}">
						<c:set var="completed" value="checked" scope="page" />
					</c:if>
					<hr><br />
					<input type="radio" name="lock" <c:out value="${created}" /> value="CREATED"> <fmt:message key="showPlatformUnit.unlocked.label"/> 
					&nbsp;&nbsp;<input type="radio" name="lock" <c:out value="${completed}" /> value="COMPLETED"> <fmt:message key="showPlatformUnit.locked.label"/>&nbsp;&nbsp;<input type="button" value="<fmt:message key="showPlatformUnit.update.label"/>" onclick='this.form.submit()' /> &nbsp;&nbsp;<input type="button" value="<fmt:message key="showPlatformUnit.reset.label"/>" onclick='this.form.reset()' /> 
					<br /> 
				</form>
			</td>
		</tr>
	</c:otherwise>
</c:choose>
</c:if>
</sec:authorize>
--%>

<%-- commented out 10-5-12
<tr><td colspan='2' style='text-align:center; padding:10px' >
<a href='<c:url value="/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=${platformUnit.sampleSubtypeId}&sampleId=${platformUnit.sampleId}" />'>Edit</a> | <a href='<c:url value="/facility/platformunit/deletePlatformUnit.do?sampleId=${platformUnit.sampleId}" />'>Delete</a> | <a href='<c:url value="/facility/platformunit/addToRun.do?sampleId=${platformUnit.sampleId}" />'>Add To Run</a> 
</td></tr>
</table>
--%>

<br />

<c:if test="${runList.size()==0}">
<div id="newCreateRunButtonDiv">
	<input type="button" value="<fmt:message key="showPlatformUnit.createNewRun.label"/>" onclick='toggleDisplayOfCreateNewRunForm("create")' />
</div>
<div id="newCreateRunFormDiv" style="display:none">
		<form id="newRunForm"  method='post' action="<c:url value="/facility/platformunit/createNewRun.do" />" >
		<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
		<table class="EditTable ui-widget ui-widget-content">
			<tr class="FormData"><td colspan="2" class="CaptionTD" style="text-align:center;color:red"><fmt:message key="showPlatformUnit.warning1.label"/></td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.runName.label"/>: </td><td class="DataTD"><input type='text' name='runName' id='runName' size='25' maxlength='30' /></td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.machine.label"/>: </td><td class="DataTD">
				<select id="resourceId" name="resourceId" >
		  			<option value=""><fmt:message key="showPlatformUnit.selectMachine.label"/></option>
						<c:forEach items="${resources}" var="resource">
							<option value='<c:out value="${resource.getResourceId()}" />'><c:out value="${resource.getName()}" /> - <c:out value="${resource.getResourceCategory().getName()}" /></option>
						</c:forEach> 
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.readLength.label"/>: </td><td class="DataTD">
				<select id="readLength" name="readLength">
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.readType.label"/>: </td><td class="DataTD">
				<select id="readType" name="readType">
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.runTechnician.label"/>: </td><td class="DataTD">
				<select id="technicianId" name="technicianId" >
		  			<option value=""><fmt:message key="showPlatformUnit.selectTechnician.label"/></option>
						<c:forEach items="${technicians.keySet()}" var="technicianId">
							<option value='<c:out value="${technicianId}" />'><c:out value="${technicians.get(technicianId)}" /></option>
						</c:forEach> 
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.startDate.label"/>: </td><td class="DataTD"><input type="text" name = "runStartDate" id="runStartDate" value="" /></td></tr>
			<tr class="FormData"><td colspan="2" class="CaptionTD">
				<input id="submitButtonCreateNewRun" disabled = "disabled" type="button" value="<fmt:message key="showPlatformUnit.submit.label"/>" onclick='validateCreateNewRunForm()' />&nbsp;<input type="button" value="<fmt:message key="showPlatformUnit.cancel.label"/>" onclick='toggleDisplayOfCreateNewRunForm("cancel")' />
			</td></tr>
		</table>
		</form>
</div>
</c:if>


<c:if test="${runList.size() > 0}">
		<form id="newRunForm"  method='post' action="<c:url value="/facility/platformunit/createNewRun.do" />" >
		<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
		<input type='hidden' name='runId' value='<c:out value="${runList.get(0).getRunId()}" />'/>
		<table class="EditTable ui-widget ui-widget-content">
			<tr class="FormData"><td colspan="2" class="CaptionTD" style="text-align:center;color:red"><fmt:message key="showPlatformUnit.warning2.label"/></td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.runName.label"/>: </td><td class="DataTD"><input type='text' name='runName' id='runName' size='25' maxlength='30' value='<c:out value="${runList.get(0).name}" />'  /></td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.machine.label"/>: </td><td class="DataTD">
				<select id="resourceId" name="resourceId" >
		  			<option value=""><fmt:message key="showPlatformUnit.selectMachine.label"/></option>
						<c:forEach items="${resources}" var="resource">
							<c:set var="selected" value="" scope="page" />
							<c:if test="${resource.getResourceId() == runList.get(0).getResourceId()}">
								<c:set var="selected" value="SELECTED" scope="page" />
							</c:if>
							<option value='<c:out value="${resource.getResourceId()}" />'    <c:out value="${selected}" />     ><c:out value="${resource.getName()}" /> - <c:out value="${resource.getResourceCategory().getName()}" /></option>
						</c:forEach> 
				</select>			
			</td></tr>
 			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.readLength.label"/>: </td><td class="DataTD">
				<select id="readLength" name="readLength">
					<c:out value="${readLength}" escapeXml="false" />
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.readType.label"/>: </td><td class="DataTD">
				<select id="readType" name="readType">
					<c:out value="${readType}"  escapeXml="false" />
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.runTechnician.label"/>: </td><td class="DataTD">
				<select id="technicianId" name="technicianId" >
		  			<option value=""><fmt:message key="showPlatformUnit.selectTechnician.label"/></option>
						<c:forEach items="${technicians.keySet()}" var="technicianId">
						<c:set var="selected" value="" scope="page" />
							<c:if test="${technicianId == runList.get(0).getUserId()}">
								<c:set var="selected" value="SELECTED" scope="page" />
							</c:if>
							<option value='<c:out value="${technicianId}" />' <c:out value="${selected}" />><c:out value="${technicians.get(technicianId)}" /></option>
						</c:forEach> 
				</select>			
			</td></tr>
			<c:set var="date" value="${runList.get(0).getStartts()}" />
			<tr class="FormData"><td class="CaptionTD"><fmt:message key="showPlatformUnit.startDate.label"/>: </td><td class="DataTD"><input type="text" name = "runStartDate" id="runStartDate" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${date}" />" /></td></tr>
			<tr class="FormData"><td colspan="2" class="CaptionTD">
				<input id="submitButtonCreateNewRun" type="button" value="<fmt:message key="showPlatformUnit.update.label"/>" onclick='validateCreateNewRunForm()' />&nbsp;<input type="button" value="<fmt:message key="showPlatformUnit.reset.label"/>" onclick='location.href="<c:url value="/facility/platformunit/showPlatformUnit/${platformUnit.sampleId}.do" />"' />
			</td></tr>
		</table>
		</form>
</c:if>


<c:if test="${platformUnit.sampleSource.size() > 0}">
<br />
<div class="fixed-width_scrollable">
	<table class="data" >
		<tr><td colspan="${platformUnit.sampleSource.size()}" class="label-centered" style="background-color:#FAF2D6" nowrap><c:out value="${platformUnit.name}" /></td></tr>
		<tr>
			<c:forEach items="${platformUnit.sampleSource}" var="ss1">
				<c:set var="cell" value="${ss1.sourceSample}" scope="page" />
					<td class="label-centered"  style="background-color:#FAF2D6" nowrap><fmt:message key="showPlatformUnit.cell.label"/>: <c:out value="${fn:substringAfter(cell.name, '/')}" /></td>
			</c:forEach>
		</tr>
		<tr>
		  
			<c:forEach items="${platformUnit.sampleSource}" var="ss1">
				<c:set var="numberControlLibrariesPerLane" value="0" scope="page" />
				<c:set var="cell" value="${ss1.sourceSample}" scope="page" />
				 <td class="value-centered-small-heavyborder" nowrap>			
					<c:forEach items="${cell.sampleSource}" var="ss2">
				  		<c:set var="controlLibrary" value="${ss2.sourceSample}" scope="page" />
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
            				<c:set var="ss2Meta" value="${ss2.sampleSourceMeta}" scope="page" />
            				<c:forEach items="${ss2Meta}" var="ss2MetaItem">
            					<c:if test="${fn:indexOf(ss2MetaItem.k,'libConcInLanePicoM') > -1 }"><br /><fmt:message key="showPlatformUnit.concOnCell.label"/>: <c:out value="${ss2MetaItem.v}"/> <fmt:message key="showPlatformUnit.pM.label"/> </c:if>					
            				</c:forEach>
            				
            				<form  name='removeLib' method='post' action="<c:url value="/facility/platformunit/assignRemove.do" />" onsubmit='return confirm("<fmt:message key="showPlatformUnit.removeControlFromThisCell.label"/>");'>
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='samplesourceid' value='<c:out value="${ss2.sampleSourceId}" />'/>
							<input type='submit' value='<fmt:message key="showPlatformUnit.removeControl.label"/>'/>
							</form>	
            				
            					
            				<hr>
            			</c:if>
					</c:forEach>			  						  		
					<c:if test="${numberControlLibrariesPerLane == 0 }">
						<fmt:message key="showPlatformUnit.noControlOnCell.label"/>
						<hr>
					</c:if>
					
					<c:set var="idNewControlCounter" value="${idNewControlCounter + 1}" scope="page" />
					<a href="javascript:void(0)" id="newControlAnchor_<c:out value="${idNewControlCounter}" />" onclick="toggleDisplayAddNewControlForm('show_form',<c:out value="${idNewControlCounter}" />)"><fmt:message key="showPlatformUnit.addControl.label"/></a>
					<div id="idNewControlFormDiv_<c:out value="${idNewControlCounter}" />" style="display:none">
						<form id="addNewControlToLaneForm_<c:out value="${idNewControlCounter}" />"  method='post' action="<c:url value="/facility/platformunit/addNewControlToLane.do" />" >
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
								<tr><td class="value-centered-small"><fmt:message key="showPlatformUnit.finalConcPM.label"/>: <input type='text' name='newControlConcInLanePicoM' id="newControlConcInLanePicoM_<c:out value="${idNewControlCounter}" />" size='3' maxlength='5' ></td></tr>
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
		<c:forEach items="${platformUnit.sampleSource}" var="ss1">
			<c:set var="cell" value="${ss1.sourceSample}" scope="page" />
			<td class="value-centered-top-small-heavyborder" >
				<c:set var="counter" value="1" scope="page" />
				<c:set var="numberRegularLibrariesPerLane" value="0" scope="page" />
				<c:forEach items="${cell.sampleSource}" var="ss2">
				  <c:set var="library" value="${ss2.sourceSample}" scope="page" />
				  <c:if test="${counter > 1}"><hr></c:if>
				  
				  <c:if test="${library.sampleSubtype.getIName() != 'controlLibrarySample'}">	
					<c:set var="numberRegularLibrariesPerLane" value="${numberRegularLibrariesPerLane + 1}" scope="page" />						
					<c:out value="${library.name}" />
					<c:set var="ss2Meta" value="${ss2.sampleSourceMeta}" scope="page" />
					<c:forEach items="${ss2Meta}" var="ss2MetaItem">
						<c:if test="${fn:indexOf(ss2MetaItem.k,'jobId') > -1}"><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${ss2MetaItem.v}.do" />"> (<fmt:message key="showPlatformUnit.jobJ.label"/><c:out value="${ss2MetaItem.v}"/>)</a></c:if>
					</c:forEach>
					<br />				
					<c:set var="libraryMeta" value="${library.sampleMeta}" scope="page" />
					<c:forEach items="${libraryMeta}" var="libraryMetaItem">
						<c:if test="${fn:substringAfter(libraryMetaItem.k, 'Library.') == 'adaptor'}">
            				<c:out value="${adaptors.get(libraryMetaItem.v).adaptorset.name}"/><br />
            				<fmt:message key="showPlatformUnit.index.label"/> <c:out value="${adaptors.get(libraryMetaItem.v).barcodenumber}"/>: <c:out value="${adaptors.get(libraryMetaItem.v).barcodesequence}"/><br />
            			</c:if> 
					</c:forEach>
					<c:forEach items="${ss2Meta}" var="ss2MetaItem">
						<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
						<%--  	<c:if test="${ss2MetaItem.k=='libConcInLanePicoM'}"><fmt:message key="showPlatformUnit.concOnCell.label"/>: <c:out value="${ss2MetaItem.v}"/> <fmt:message key="showPlatformUnit.pM.label"/> <br /></c:if>  --%>
						<c:set var="currentConcentration" value="" scope="page" />
						<div id="editAnchorDiv_<c:out value="${idCounter}" />" >
						<c:if test="${fn:indexOf(ss2MetaItem.k, 'libConcInLanePicoM') > -1 }"><c:set var="currentConcentration" value="${ss2MetaItem.v}" scope="page" /><fmt:message key="showPlatformUnit.concOnCell.label"/>: <c:out value="${ss2MetaItem.v}"/> <fmt:message key="showPlatformUnit.pM.label"/> <a href="javascript:void(0)" onclick='toggleDisplayOfUpdateForm("show", <c:out value="${idCounter}" />);'>[<fmt:message key="showPlatformUnit.edit.label"/>]</a><br />	</c:if>					
						</div>	
						
						<div id="updatePicoFormDiv_<c:out value="${idCounter}" />" style="display:none">
						<form id="updatePicoForm_<c:out value="${idCounter}" />"  method='post' action="<c:url value="/facility/platformunit/updateConcInLane.do" />" >
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='sampleSourceMetaId' value='<c:out value="${ss2MetaItem.sampleSourceMetaId}" />'/>
							<table class="data">
								<tr><td class="value-centered-small"><fmt:message key="showPlatformUnit.currentConcPM.label"/>: <c:out value="${currentConcentration}" /></td></tr>
								<tr><td class="value-centered-small"><fmt:message key="showPlatformUnit.newConcPM.label"/>: <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5' ></td></tr>
								<tr><td class="value-centered-small">
								<input type="button" value="<fmt:message key="showPlatformUnit.update.label"/>" onclick='validateUpdateForm(<c:out value="${idCounter}" />)' />&nbsp;<input type="button" value="<fmt:message key="showPlatformUnit.cancel.label"/>" onclick='toggleDisplayOfUpdateForm("cancel", <c:out value="${idCounter}" />)' />
								</td></tr>
							</table>
						</form>
						</div>						
					
					</c:forEach>
					<form  name='removeLib' method='post' action="<c:url value="/facility/platformunit/assignRemove.do" />" onsubmit='return confirm("<fmt:message key="showPlatformUnit.removeLibFromCell_alert.label"/>");'>
						<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
						<input type='hidden' name='samplesourceid' value='<c:out value="${ss2.sampleSourceId}" />'/>
						<input type='submit' value='Remove Library'/>
					</form>					
					<c:set var="counter" value="${counter + 1}" scope="page" />
				  </c:if>	
				</c:forEach>
			
			<c:if test="${numberRegularLibrariesPerLane == 0 }">
				<fmt:message key="showPlatformUnit.noLibrariesOnCell.label"/>
			</c:if>	
			
		</td>
		</c:forEach>		
		</tr>
	</table>
	</div>
</c:if>


