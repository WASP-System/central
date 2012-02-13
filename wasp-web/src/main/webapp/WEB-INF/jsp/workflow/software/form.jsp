<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<<wasp:message />
<h1>
	<c:out value="${workflow.name}" />
	Resources Assignments
</h1>

<form:form method="POST">
	<c:forEach items="${workflowTypeResourceMap}"
		var="workflowTypeResource">
		<section style="margin-bottom: 20px">
			<h2>
				<c:out value="${workflowTypeResource.typeResource.name}" />
			</h2>
			<c:forEach items="${workflowTypeResource.typeResource.resourceCategory}"	var="rc">

				<c:set var="wrc" value="" />
				<c:set var="resourceOptions" value="" />
				<c:if test="${! empty workflowResourceCategoryMap[rc.IName]}">
					<c:set var="wrc" value="${workflowResourceCategoryMap[rc.IName]}" />
				</c:if>

				<div style="margin-bottom: 20px;">
					<input type="checkbox" name="resourceCategory"	value='<c:out value="${rc.IName}" />'
						<c:if test="${!empty wrc}">CHECKED</c:if>>
					<c:out value="${rc.name}" />
					<div style="margin-left: 20px;">
						<c:forEach items="${rc.resourceCategoryMeta}" var="rcm">
							<c:if test="${fn:contains(rcm.k, '.allowableUiField.')}">
								<div>
									<c:set var="optionName" value="" />
									<c:if test="${! empty wrc &amp;&amp; wrc != ''}">

										<c:set var="optionName"
											value="${wrc.resourceCategory.IName};${fn:substringAfter(rcm.k, '.allowableUiField.')}" />

									</c:if>
									<c:out
										value="${fn:substringAfter(rcm.k, '.allowableUiField.')}" />
									<c:forEach items="${fn:split(rcm.v,';')}" var="option">
										<div style="margin-left: 10px">
											<input type="checkbox" name="resourceCategoryOption" value='<c:out value="${rc.IName};${rcm.k};${option}" />'
												<c:if test="${workflowResourceOptions[optionName].contains(option)}" >
CHECKED
												</c:if>>
											<c:out value="${option}" />
										</div>
									</c:forEach>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
			<c:forEach items="${workflowTypeResource.typeResource.software}"
				var="rc">
				<c:set var="wrc" value="" />
				<c:set var="resourceOptions" value="" />
				<c:if test="${! empty workflowSoftwareMap[rc.IName]}">
					<c:set var="wrc" value="${workflowSoftwareMap[rc.IName]}" />
				</c:if>

				<div style="margin-bottom: 20px;">
					<input type="checkbox" name="software"	value='<c:out value="${rc.IName}" />'
						<c:if test="${!empty wrc}">CHECKED</c:if>>
					<c:out value="${rc.name}" />
					<div style="margin-left: 20px;">
						<c:forEach items="${rc.softwareMeta}" var="rcm">
							<c:if test="${fn:contains(rcm.k, '.allowableUiField.')}">
								<div>
									<c:out value="${rcm.k}" />
									<c:set var="optionName"	value="${wrc.resourceCategory.IName};${fn:substringAfter(rcm.k, '.allowableUiField.')}" />
									<c:forEach items="${fn:split(rcm.v,';')}" var="option">
										<div style="margin-left: 10px">
											<input type="checkbox" name="resourceCategoryOption" value='<c:out value="${rc.IName};${rcm.k};${option}" />'
												<c:if test="${workflowResourceOptions[optionName].contains(option)}" >
CHECKED
												</c:if>>
											<c:out value="${option}" />
										</div>
									</c:forEach>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
		</section>
	</c:forEach>
	<div class="submit">
		<input type="submit" value="modify">
	</div>
</form:form>


