<%@ include file="/WEB-INF/jsp/taglib.jsp"%>


<h1>
	<c:out value="${workflow.name}" /> <fmt:message key="workflow.configure.label"/>
</h1>

<form:form  cssClass="FormGrid" method="POST">
	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="workflowId" value="<c:out value="${workflowId}" />" />
	<c:forEach items="${workflowTypeResourceMap}"
		var="workflowTypeResource">
		<section style="margin-bottom: 20px">
			<h2>
				<c:out value="${workflowTypeResource.typeResource.name}" />
			</h2>
			<c:forEach items="${workflowTypeResource.typeResource.resourceCategory}"	var="rc">
				<c:if test="${rc.isActive == 1 }">
					<c:set var="wrc" value="" />
					<c:set var="resourceOptions" value="" />
					<c:if test="${! empty workflowResourceCategoryMap[rc.IName]}">
						<c:set var="wrc" value="${workflowResourceCategoryMap[rc.IName]}" />
					</c:if>
	
					<div style="margin-bottom: 20px;">
						<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="resourceCategory"	value='<c:out value="${rc.IName}" />'
							<c:if test="${!empty wrc}">CHECKED</c:if>>
						<c:out value="${rc.name}" />
						<div style="margin-left: 20px;">
							<c:forEach items="${rc.resourceCategoryMeta}" var="rcm">
								<c:if test="${fn:contains(rcm.k, '.allowableUiField.')}">
									<div>
										<c:set var="optionName" value="" />
										<c:if test="${! empty wrc && wrc != ''}">
	
											<c:set var="optionName"
												value="${wrc.resourceCategory.IName};${fn:substringAfter(rcm.k, '.allowableUiField.')}" />
	
										</c:if>
										<c:out
											value="${fn:substringAfter(rcm.k, '.allowableUiField.')}" />
										<c:forEach items="${fn:split(rcm.v,';')}" var="option">
											<div style="margin-left: 10px">
												<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="resourceCategoryOption" value='<c:out value="${rc.IName};${rcm.k};${option}" />'
													<c:if test="${workflowResourceOptions[optionName].contains(option)}" >
	CHECKED
													</c:if>>
												<c:set var="optionValue" value="${fn:split(option, ':')}" />
												<c:out value="${optionValue[1]}" />
											</div>
										</c:forEach>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</c:forEach>
			<c:forEach items="${workflowTypeResource.typeResource.software}"
				var="software">
				<c:if test="${software.isActive == 1 }">
					<c:set var="ws" value="" />
					<c:set var="resourceOptions" value="" />
					<c:if test="${! empty workflowSoftwareMap[software.IName]}">
						<c:set var="ws" value="${workflowSoftwareMap[software.IName]}" />
					</c:if>
	
					<div style="margin-bottom: 20px;">
						<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="software"	value='<c:out value="${software.IName}" />'
							<c:if test="${!empty ws}">CHECKED</c:if>>
						<c:out value="${workflowSoftwareVersionedNameMap[software.IName]}" />
						<div style="margin-left: 20px;">
							<c:forEach items="${software.softwareMeta}" var="sm">
								<c:if test="${fn:contains(sm.k, '.allowableUiField.')}">
									<div>
										<c:out value="${sm.k}" />
										<c:set var="optionName"	value="${ws.resourceCategory.IName};${fn:substringAfter(sm.k, '.allowableUiField.')}" />
										<c:forEach items="${fn:split(sm.v,';')}" var="option">
											<div style="margin-left: 10px">
												<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="resourceCategoryOption" value='<c:out value="${software.IName};${sm.k};${option}" />'
													<c:if test="${workflowResourceOptions[optionName].contains(option)}" >
	CHECKED
													</c:if>>
												<c:set var="optionValue" value="${fn:split(option, ':')}" />
												<c:out value="${optionValue[1]}" />
											</div>
										</c:forEach>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</c:forEach>
		</section>
	</c:forEach>
	<div class="submit">
		<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="workflow.submit.label" />" />
		<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="workflow.cancel.label" />">
	</div>
</form:form>


