<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('fm')"> 
	<br />
	<a class="button" href="<wasp:relativeUrl value="job/${job.getId()}/jobSampleReviewForLabDownload.do" />" ><fmt:message key="sampleDetails.downloadJobSampleSummaryForLab.label" /></a>
	<br /><br />
</sec:authorize>

<div class="fixed-width_scrollable">
<c:if test="${fn:length(submittedMacromoleculeList)>0}">
<h2><fmt:message key="sampleDetails.submittedSamples.label" /></h2>
	<table class="data" style="margin: 0px 0px" >	
		<c:forEach items="${submittedMacromoleculeList}" var="submittedMacromolecule" varStatus="statusSubmittedMacromolecule">		
			<c:if test="${statusSubmittedMacromolecule.first}">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.sampleName.label" /><br />(<fmt:message key="sampleDetails.internalID.label" />)</label></td> 
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.type.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.species.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.arrived?.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.qc.label" /></label></td>
					<c:set var="_area" value = "sample" scope="request"/>
					<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedMacromolecule)}" scope="request" />		
				    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">						
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>						
							<c:set var="labelKey" value="${_meta.property.label}" />							
							<c:if test="${fn:contains(labelKey,'Average')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Av.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Fragmentation')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Concentration')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Volume')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Quant. Method')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">								
								<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>${labelKey}</label></td>
							</c:if>	
						</c:if>
					</c:forEach>
				</tr>
			</c:if>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedMacromolecule.getName()}" /><br />(<fmt:message key="sampleDetails.ID.label" />:<c:out value="${submittedMacromolecule.getId()}" />)</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedMacromolecule.getSampleType().getName()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedObjectOrganismMap.get(submittedMacromolecule)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${receivedStatusMap.get(submittedMacromolecule)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:choose>
						<c:when test='${receivedStatusMap.get(submittedMacromolecule) == "RECEIVED"}'>
							<c:out value="${qcStatusMap.get(submittedMacromolecule)}" />
						</c:when>
						<c:otherwise>
							<fmt:message key="sampleDetails.notApplicable.label" />
						</c:otherwise>
					</c:choose>
				</td>
				<c:set var="_area" value = "sample" scope="request"/>
				<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedMacromolecule)}" scope="request" />		
				<c:forEach items="${_metaList}" var="_meta" varStatus="status">
					<c:if test="${_meta.property.formVisibility != 'ignore'}">						
						<c:set var="_myArea">${_area}.</c:set>
						<c:set var="_myCtxArea">${_area}.</c:set>
						<c:if test="${_metaArea != null}">		
							<c:set var="_myCtxArea">${_metaArea}.</c:set>
						</c:if>						
						<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
						<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">	
							<c:set var="inputVal" value="${_meta.v}" />							
							<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label>${inputVal}</label></td>
						</c:if>	
					</c:if>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
</c:if>	
</div>





<br />
<div class="fixed-width_scrollable">
<c:if test="${fn:length(submittedLibraryList)>0}">
<h2><fmt:message key="sampleDetails.submittedLibraries.label" /></h2>
	<table class="data" style="margin: 0px 0px" >	
		<c:forEach items="${submittedLibraryList}" var="submittedLibrary" varStatus="statusSubmittedLibrary">		
			<c:if test="${statusSubmittedLibrary.first}">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.sampleName.label" /><br />(<fmt:message key="sampleDetails.internalID.label" />)</label></td> 
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.type.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.species.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.arrived?.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.qc.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.adaptor.label" /></label></td>
					<c:set var="_area" value = "sample" scope="request"/>
					<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedLibrary)}" scope="request" />		
				    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">						
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>						
							<c:set var="labelKey" value="${_meta.property.label}" />							
							<c:if test="${fn:contains(labelKey,'Average')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Av.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Fragmentation')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Concentration')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Volume')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Quant. Method')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">								
								<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>${labelKey}</label></td>
							</c:if>	
						</c:if>
					</c:forEach>
				</tr>
			</c:if>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedLibrary.getName()}" /><br />(<fmt:message key="sampleDetails.ID.label" />:<c:out value="${submittedLibrary.getId()}" />)</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedLibrary.getSampleType().getName()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedObjectOrganismMap.get(submittedLibrary)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${receivedStatusMap.get(submittedLibrary)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:choose>
						<c:when test='${receivedStatusMap.get(submittedLibrary) == "RECEIVED"}'>
							<c:out value="${qcStatusMap.get(submittedLibrary)}" />
						</c:when>
						<c:otherwise>
							<fmt:message key="sampleDetails.notApplicable.label" />
						</c:otherwise>
					</c:choose>
				</td>
				<c:set value="${libraryAdaptorsetMap.get(submittedLibrary)}" var="adaptorSet"/>
				<c:set value="${libraryAdaptorMap.get(submittedLibrary)}" var="adaptor"/>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${adaptorSet.getName()}" /><br />
					<fmt:message key="sampleDetails.index.label" /> <c:out value="${adaptor.getBarcodenumber()}" /> [<c:out value="${adaptor.getBarcodesequence()}" />]
				</td>
				<c:set var="_area" value = "sample" scope="request"/>
				<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedLibrary)}" scope="request" />		
				<c:forEach items="${_metaList}" var="_meta" varStatus="status">
					<c:if test="${_meta.property.formVisibility != 'ignore'}">						
						<c:set var="_myArea">${_area}.</c:set>
						<c:set var="_myCtxArea">${_area}.</c:set>
						<c:if test="${_metaArea != null}">		
							<c:set var="_myCtxArea">${_metaArea}.</c:set>
						</c:if>						
						<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
						<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">	
							<c:set var="inputVal" value="${_meta.v}" />							
							<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label>${inputVal}</label></td>
						</c:if>	
					</c:if>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
</c:if>	
</div>




<br />
<div class="fixed-width_scrollable">
<c:if test="${fn:length(facilityLibraryList)>0}">
<h2><fmt:message key="sampleDetails.facilityGeneratedLibraries.label" /></h2>
	<table class="data" style="margin: 0px 0px" >	
		<c:forEach items="${facilityLibraryList}" var="facilityLibrary" varStatus="statusFacilityLibrary">		
			<c:if test="${statusFacilityLibrary.first}">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.sampleName.label" /><br />(<fmt:message key="sampleDetails.internalID.label" />)<br />[<fmt:message key="sampleDetails.derivedFrom.label" />]</label></td> 
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.type.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.qc.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.adaptor.label" /></label></td>
					<c:set var="_area" value = "sample" scope="request"/>
					<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(facilityLibrary)}" scope="request" />		
				    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">						
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>						
							<c:set var="labelKey" value="${_meta.property.label}" />							
							<c:if test="${fn:contains(labelKey,'Average')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Av.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Fragmentation')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Concentration')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Volume')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Quant. Method')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">								
								<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>${labelKey}</label></td>
							</c:if>	
						</c:if>
					</c:forEach>
				</tr>
			</c:if>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${facilityLibrary.getName()}" /><br />(<fmt:message key="sampleDetails.ID.label" />:<c:out value="${facilityLibrary.getId()}" />)<br />[<fmt:message key="sampleDetails.parent.label" />: <c:out value="${facilityLibrary.getParent().getName()}" /> (<fmt:message key="sampleDetails.ID.label" />:<c:out value="${facilityLibrary.getParent().getId()}" />)]</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${facilityLibrary.getSampleType().getName()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${qcStatusMap.get(facilityLibrary)}" /></td>
				<c:set value="${libraryAdaptorsetMap.get(facilityLibrary)}" var="adaptorSet"/>
				<c:set value="${libraryAdaptorMap.get(facilityLibrary)}" var="adaptor"/>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${adaptorSet.getName()}" /><br />
					<fmt:message key="sampleDetails.index.label" /> <c:out value="${adaptor.getBarcodenumber()}" /> [<c:out value="${adaptor.getBarcodesequence()}" />]
				</td>
				<c:set var="_area" value = "sample" scope="request"/>
				<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(facilityLibrary)}" scope="request" />		
				<c:forEach items="${_metaList}" var="_meta" varStatus="status">
					<c:if test="${_meta.property.formVisibility != 'ignore'}">						
						<c:set var="_myArea">${_area}.</c:set>
						<c:set var="_myCtxArea">${_area}.</c:set>
						<c:if test="${_metaArea != null}">		
							<c:set var="_myCtxArea">${_metaArea}.</c:set>
						</c:if>						
						<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
						<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">	
							<c:set var="inputVal" value="${_meta.v}" />							
							<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label>${inputVal}</label></td>
						</c:if>	
					</c:if>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
</c:if>	
</div>
<br />