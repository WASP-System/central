<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>


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
			<h2 style="font-weight: bold;">
				<c:out value="${workflowResourceType.resourceType.name}" /> 
			</h2>
			<table class="data" style="margin: 0px 0px"><tr>
			<c:forEach items="${workflowResourceType.resourceType.resourceCategory}"	var="rc">
				<c:if test="${rc.isActive == 1 }">
					<c:set var="wrc" value="" />
					<c:if test="${! empty workflowResourceCategoryMap[rc.IName]}">
						<c:set var="wrc" value="${workflowResourceCategoryMap[rc.IName]}" />
					</c:if>
					<th class="label-centered"><c:out value="${rc.name}" /> <input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="resourceCategory"	id="<c:out value="${rc.IName}" />" value='<c:out value="${rc.IName}" />'
					<c:if test="${!empty wrc}">CHECKED</c:if>></th>
				</c:if>
			</c:forEach>
			</tr><tr>
			<c:forEach items="${workflowResourceType.resourceType.resourceCategory}"	var="rc">
				<c:if test="${rc.isActive == 1 }">
						<c:set var="wrc" value="" />
						<c:set var="resourceOptions" value="" />
						<c:if test="${! empty workflowResourceCategoryMap[rc.IName]}">
							<c:set var="wrc" value="${workflowResourceCategoryMap[rc.IName]}" />
						</c:if>
							<td style="vertical-align:text-top;padding:10px;">
							<table style="border: solid 1px">
							<c:forEach items="${rc.resourceCategoryMeta}" var="rcm" varStatus="rcmStatus">
								<c:if test="${fn:contains(rcm.k, '.allowableUiField.')}">
										
										<c:set var="requiredResourceCategoryOptions" value="${requiredResourceCategoryOptions}${rcm.k};" />
										<c:set var="optionName" value="" />
										<c:if test="${! empty wrc && wrc != ''}">
											<c:set var="optionName"
												value="${wrc.resourceCategory.IName};${fn:substringAfter(rcm.k, '.allowableUiField.')}" />
	
										</c:if>
										<c:set var="options" value="${fn:split(rcm.v,';')}"/>
										<tr><th style="border: solid 1px" rowspan='<c:out value="${fn:length(options)}"/>'><c:out value="${fn:substringAfter(rcm.k, '.allowableUiField.')}" /></th>
										<c:forEach items="${options}" var="option" varStatus="optionStatus">
											<c:if test="${!optionStatus.first}"><tr></c:if>
											<c:if test="${!optionStatus.last}"><td style="border-bottom: 0"></c:if>
											<c:if test="${optionStatus.last}"><td style="border-bottom: solid 1px"></c:if>
												<input class="FormElement ui-widget-content ui-corner-all" type="checkbox" name="resourceCategoryOption" value='<c:out value="${rc.IName};${rcm.k};${option}"  />' onchange="checkParent(this,'${rc.IName}')"
													<c:if test="${workflowResourceOptions[optionName].contains(option)}" >
	CHECKED
													</c:if>>
												<c:set var="optionValue" value="${fn:split(option, ':')}" />
												<c:out value="${optionValue[1]}" /></td></tr>
											
										</c:forEach>
								
								</c:if>
							</c:forEach>
							</table>
				</td>
				</c:if>
			</c:forEach>
			</tr></table>
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
						<c:out value="${software.IName}" />
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
			<c:if test='${fn:containsIgnoreCase(workflowResourceType.resourceType.getIName(), "strategy")}'>
				<span style="padding:3px; border: 1px solid black;">
					<a id="checkAllStrategies" <%-- class="button" --%> href="javascript:void(0);"  >check all</a>
					| <a id="uncheckAllStrategies" <%-- class="button" --%> href="javascript:void(0);" >uncheck all</a>
				</span><br /><br />
				 <table class="data" style="margin: 0px 0px">
			  		<tr class="FormData">
			  			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="strategy.commonName.label"/></td><td  class="label-centered" style="background-color:#FAF2D6"><fmt:message key="strategy.strategy.label"/></td><td  class="label-centered" style="background-color:#FAF2D6"><fmt:message key="strategy.definition.label" /></td>
			  		</tr>
			  		<c:forEach items="${strategies}" var="strategy">
				  		<tr>
				  			<td style="font-size:x-small">
				  				<input type="checkbox" id="strategyKey${strategy.getStrategy()}" name="strategyKey" value="<c:out value="${strategy.getType()}" /><c:out value="." /><c:out value="${strategy.getStrategy()}" />" 
				  					<c:forEach items="${thisWorkflowsStrategies}" var="workflowStrategy">						
										<c:if test="${strategy.getId()==workflowStrategy.getId()}">
											CHECKED 
										</c:if> 
									</c:forEach>
				  				/>
				  				&nbsp;<c:out value="${strategy.getDisplayStrategy()}" /></td><td style="font-size:x-small"><c:out value="${strategy.getStrategy()}" /></td><td style="font-size:x-small;width:250px"><c:out value="${strategy.getDescription()}" />
				  			</td>
				  		</tr>
			  		</c:forEach>
				</table>	
			</c:if>
		</section>
	</c:forEach>
	<input type="hidden" name="requiredResourceCategoryOptions" value="<c:out value="${requiredResourceCategoryOptions}" />" />
	<input type="hidden" name="requiredSoftwareOptions" value="<c:out value="${requiredSoftwareOptions}" />" />
	<div class="submit">
		<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="workflow.submit.label" />" />
		<input class="FormElement ui-widget-content ui-corner-all" type="reset" name="reset" value="Reset">
		<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="workflow.cancel.label" />">
	</div>
</form:form>


