<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/genomes.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="jobDraft.genome_instructions.label"/>
</div>
<form method="post" > 
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
		<c:if test="not empty(${currentBuildByOrganism.get(organism)})">
			<c:set var="currentBuildName" value="${currentBuildByOrganism.get(organism).getName()}" />
			<c:set var="currentGenomeName" value="${currentBuildByOrganism.get(organism).genGenome().getName()}" />
		</c:if>
		<c:forEach items="${sampleDraftsByOrganism.get(organism)}" var="sampleDraft" varStatus="statusSD">
			<tr>
				<td class="DataTD value-centered">${sampleDraft.getName()}</td>
				<c:if test="${statusSD.count == 1}">
					<td class="DataTD value-centered" rowspan="${sdCount}">${organism.getName()}</td>
					<td class="DataTD value-centered" rowspan="${sdCount}">
						<table style="width:600px;">
							<tr>
								<td class="CaptionTD label-right"><fmt:message key="jobDraft.sample_genome.label"/></td>
								<td class="DataTD value-centered">
									<select name="genomeSelect_${organism.getNcbiID()}" id="genomeSelect_${organism.getNcbiID()}">
										<option value=""><fmt:message key="wasp.default_select.label" /></option>
										<c:set var="default" value="" />
										<c:forEach items="${organism.getGenomes().keySet()}" var="genomeName" varStatus="statusGenome" >
											<c:forEach items="${organism.getGenomes().get(genomeName).getBuilds().values()}" var="build">
												<c:if test="${build.isDefault() == true}">
													<c:set var="defaultValue" value=" (default)" />
													<c:if test="${empty(currentGenomeName)}" >
														<c:set var="currentGenomeName" value="${genomeName}" />
														<c:set var="currentBuildName" value="${build.getName()}" />
													</c:if>
												</c:if>
											</c:forEach>
											<c:set var="genomeSelectedStatus" value="" />
											<c:if test="${genomeName == currentGenomeName}"><c:set var="genomeSelectedStatus" value="selected='selected'" /></c:if>
											<option value="organism/${organism.getNcbiID()}/genome/${genomeName}" <c:out value="${genomeSelectedStatus}" />><c:out value="${genomeName}${defaultValue}" /></option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr id="buildSelectTr_${organism.getNcbiID()}">
								<td class="CaptionTD label-right"><fmt:message key="jobDraft.sample_build.label"/></td>
								<td class="DataTD value-centered">
									<select name="buildSelect_${organism.getNcbiID()}" id="buildSelect_${organism.getNcbiID()}">
										
									</select>
								</td>
							</tr>
							<tr id="buildDescriptionTr_${organism.getNcbiID()}">
								<td>&nbsp;</td><td id="buildDescriptionTd_${organism.getNcbiID()}"></td>
							</tr>
							
						</table>
					</td>
				</c:if>
			</tr>
		</c:forEach>
	</c:forEach>
	</tr>
</table>
<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<c:url value="/dashboard.do"/>'" /> 
<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
</form>






