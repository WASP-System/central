<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/genomes.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<c:if test="${ noBuildButAnalysisSelected == true }">
	<div class="instructions">
	   <fmt:message key="jobDraft.noBuildButAnalysisSelected_instructions.label"/>
	</div>
	<form method="post" > 
	<input type="hidden" name="noBuildButAnalysisSelected" value="<c:out value='${ noBuildButAnalysisSelected }' />" />
	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
	</form>
</c:if>

<c:if test="${ noBuildButAnalysisSelected == false }">
	<div class="instructions">
	   <fmt:message key="jobDraft.genome_instructions.label"/>
	</div>
	<form method="post" > 
	<input type="hidden" name="noBuildButAnalysisSelected" value="<c:out value='${ noBuildButAnalysisSelected }' />" />
	<table class="EditTable ui-widget ui-widget-content">
		<tr>
			<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_name.label"/></td>
			<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_organism.label"/></td>
			<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_genome_select.label"/></td>
		</tr>
		<tr>
		<c:forEach items="${sampleDraftsByOrganism.keySet()}" var="organism" varStatus="statusOrg">
			<c:set var="sdCount" value="${sampleDraftsByOrganism.get(organism).size()}" />
			<c:set var="currentBuildName" value="" />
			<c:set var="currentGenomeName" value="" />
			<c:if test="${currentBuildByOrganism.get(organism) != null}">
				<c:set var="currentBuildName" value="${currentBuildByOrganism.get(organism).getName()}" />
				<c:set var="currentGenomeName" value="${currentBuildByOrganism.get(organism).getGenome().getName()}" />
			</c:if>
			<c:forEach items="${sampleDraftsByOrganism.get(organism)}" var="sampleDraft" varStatus="statusSD">
				<tr <c:if test="${statusSD.count == 1}">class="top-heading"</c:if> >
					<td class="DataTD value-centered">${sampleDraft.getName()}</td>
					<c:if test="${statusSD.count == 1}">
						<td class="DataTD value-centered" rowspan="${sdCount}">${organism.getName()}</td>
						<td class="DataTD " rowspan="${sdCount}">
							<table>
								<tr>
									<td class="CaptionTD value-centered"><fmt:message key="jobDraft.sample_genome.label"/></td>
									<td class="DataTD value-centered">
										<select name="genomeSelect_${organism.getNcbiID()}" id="genomeSelect_${organism.getNcbiID()}">
											<option value=""><fmt:message key="wasp.default_select.label" /></option>
											<c:forEach items="${organism.getGenomes().keySet()}" var="genomeName" varStatus="statusGenome" >
												<c:set var="genomeSelectedStatus" value="" />
												<c:set var="defaultValue" value="" />
												<c:if test="${organism.getGenomes().get(genomeName).isDefault() == true}">
													<c:set var="defaultValue" value=" (default)" />
													<c:if test='${currentGenomeName == ""}' >
														<c:set var="currentGenomeName" value="${genomeName}" />
													</c:if>
												</c:if>
												<c:if test="${genomeName == currentGenomeName}">
													<c:set var="genomeSelectedStatus" value="selected='selected'" />
												</c:if>
												<option value="organism/${organism.getNcbiID()}/genome/${genomeName}" <c:out value="${genomeSelectedStatus}" />><c:out value="${genomeName}${defaultValue}" /></option>
											</c:forEach>
										</select>
										<span class="requiredField">*</span>
									</td>
									<td class="error"><c:out value="${genomeError.get(organism)}" /> </td>
								</tr>
							</table>
							<table>
								<tr id="buildSelectTr_${organism.getNcbiID()}">
									<td class="CaptionTD value-centered"><fmt:message key="jobDraft.sample_build.label"/></td>
									<td class="DataTD value-centered">
										<select name="buildSelect_${organism.getNcbiID()}" id="buildSelect_${organism.getNcbiID()}">
											
										</select>
									</td>
								</tr>
							</table>
							<table style="width:500px;">
								<tr id="buildDescriptionTr_${organism.getNcbiID()}">
									<td id="buildDescriptionTd_${organism.getNcbiID()}"></td>
								</tr>
							</table>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</c:forEach>
		</tr>
	</table>
	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
	</form>
</c:if>





