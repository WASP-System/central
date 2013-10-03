<%@ include file="/WEB-INF/jsp/taglib.jsp"%>


<h1>
	<c:out value="${workflow.name}" /> <fmt:message key="workflow.configure.label"/>
</h1>

<form:form  cssClass="FormGrid" method="POST">
	<input type="hidden" name="workflowId" value="<c:out value="${workflowId}" />" />
	<c:set var="requiredResourceCategoryOptions" value="" />
	<c:set var="requiredSoftwareOptions" value="" />
	
	<c:forEach items="${workflowResourceTypeMap}"
		var="workflowResourceType">
		<section style="margin-bottom: 20px">
			<h2>
				<c:out value="${workflowResourceType.resourceType.name}" /> 
				<c:if test='${workflowResourceType.resourceType.getIName()=="libraryStrategy"}'>
					<a <%-- class="button" --%> id="libraryStrategyAnchor" style="font-size: x-small;" href="javascript:void(0);" >View / Hide SRA Definitions</a>
				</c:if>
			</h2>
			<c:forEach items="${workflowResourceType.resourceType.resourceCategory}"	var="rc">
				<c:if test="${rc.isActive == 1 }">
					<c:set var="wrc" value="" />
					<c:set var="resourceOptions" value="" />
					<c:if test="${! empty workflowResourceCategoryMap[rc.IName]}">
						<c:set var="wrc" value="${workflowResourceCategoryMap[rc.IName]}" />
					</c:if>
					<div style="margin-bottom: 20px;">
						<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="resourceCategory"	id="<c:out value="${rc.IName}" />" value='<c:out value="${rc.IName}" />'
							<c:if test="${!empty wrc}">CHECKED</c:if>>
						<c:out value="${rc.name}" />
						<div style="margin-left: 20px;">
							<c:forEach items="${rc.resourceCategoryMeta}" var="rcm">
								<c:if test="${fn:contains(rcm.k, '.allowableUiField.')}">
									<div>
										<c:set var="requiredResourceCategoryOptions" value="${requiredResourceCategoryOptions}${rcm.k};" />
										<c:set var="optionName" value="" />
										<c:if test="${! empty wrc && wrc != ''}">
	
											<c:set var="optionName"
												value="${wrc.resourceCategory.IName};${fn:substringAfter(rcm.k, '.allowableUiField.')}" />
	
										</c:if>
										<c:out
											value="${fn:substringAfter(rcm.k, '.allowableUiField.')}" />
										<c:forEach items="${fn:split(rcm.v,';')}" var="option">
											<div style="margin-left: 10px">
												<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="resourceCategoryOption" value='<c:out value="${rc.IName};${rcm.k};${option}"  />' onchange="checkParent(this,'${rc.IName}')"
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
			<c:forEach items="${workflowResourceType.resourceType.software}" var="software">
				<c:if test="${software.isActive == 1}">
					<c:set var="ws" value="" />
					<c:set var="resourceOptions" value="" />
					<c:if test="${! empty workflowSoftwareMap[software.IName]}">
						<c:set var="ws" value="${workflowSoftwareMap[software.IName]}" />
					</c:if>
	
					<div style="margin-bottom: 20px;">
						<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="software"	id="<c:out value="${software.IName}" />" value='<c:out value="${software.IName}" />' 
							<c:if test="${!empty ws}">CHECKED</c:if>>
						<c:out value="${workflowSoftwareVersionedNameMap[software.IName]}" />
						<div style="margin-left: 20px;">
							<c:forEach items="${software.softwareMeta}" var="sm">
								<c:if test="${fn:contains(sm.k, '.allowableUiField.')}">
									<div>
										<c:set var="requiredSoftwareOptions" value="${requiredSoftwareOptions}${sm.k};" />
										<c:set var="optionName" value="" />
										<c:if test="${! empty ws && ws != ''}">
	
											<c:set var="optionName"	value="${ws.software.IName};${fn:substringAfter(sm.k, '.allowableUiField.')}" />
	
										</c:if>
										<c:out
											value="${fn:substringAfter(sm.k, '.allowableUiField.')}" />
										<c:forEach items="${fn:split(sm.v,';')}" var="option">
											<div style="margin-left: 10px">
												<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="softwareOption" value='<c:out value="${software.IName};${sm.k};${option}"  />' onchange="checkParent(this, '${software.IName}')"
													<c:if test="${workflowSoftwareOptions[optionName].contains(option)}" >
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
			<c:if test='${workflowResourceType.resourceType.getIName()=="libraryStrategy"}'>
				<div id="libraryStrategyTable" style="display:none">
				    <br />
				  	<table class="data" style="margin: 0px 0px">
				  		<tr class="FormData">
				  			<td class="label-centered" style="background-color:#FAF2D6">Common-Name Strategy</td><td  class="label-centered" style="background-color:#FAF2D6">SRA Strategy</td><td  class="label-centered" style="background-color:#FAF2D6">SRA Definition</td>
				  		</tr>
				  		<c:forEach items="${strategies}" var="strategy">
				  		<tr>
				  			<td ><c:out value="${strategy.getDisplayStrategy()}" /></td><td ><c:out value="${strategy.getStrategy()}" /></td><td style="width:250px"><c:out value="${strategy.getDescription()}" /></td>
				  		</tr>
				  		</c:forEach>
				 	</table>
				 	<br />
			  	</div>
			  	<select class="FormElement ui-widget-content ui-corner-all" name="strategy" id="strategy" size="1" >
					<option value="">--Select--</option>
					<c:forEach items="${strategies}" var="strategy">								
						<option value="<c:out value="${strategy.getStrategy()}" />" <c:if test="${strategy.getStrategy()==thisWorkflowStrategy.getStrategy()}">SELECTED</c:if> ><c:out value="${strategy.getDisplayStrategy()}" /> (SRA: <c:out value="${strategy.getStrategy()}" />)</option>
					</c:forEach>
				</select>			
			</c:if>
		</section>
	</c:forEach>
	<input type="hidden" name="requiredResourceCategoryOptions" value="<c:out value="${requiredResourceCategoryOptions}" />" />
	<input type="hidden" name="requiredSoftwareOptions" value="<c:out value="${requiredSoftwareOptions}" />" />
	<div class="submit">
		<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="workflow.submit.label" />" />
		<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="workflow.cancel.label" />">
	</div>
</form:form>


