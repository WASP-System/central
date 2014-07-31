<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
	
<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/chipSeq/chipSeqSpecificSampleReview.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
 
<div class="instructions">
   <fmt:message key="${workflowIName}.chipSeqSpecificSampleReview_instructions.label"/>
</div>

<br />
	
	<c:set var="colspan" value = '0' scope="request"/>
	
	<form action="<wasp:relativeUrl value="jobsubmit/chipSeq/chipSeqSpecificSampleReview/${jobDraft.getId()}.do" />" method="POST" >
	
		<div class="fixed-width_scrollable">
			<table class="EditTable ui-widget ui-widget-content" style="margin: 0px 0px" >			
			<c:forEach items="${sampleDraftList}" var="sampleDraft" varStatus="sampleDraftStatus">
				<c:if test="${sampleDraftStatus.first}">
					<tr >
						
						<td class="CaptionTD top-heading" nowrap><fmt:message key="jobsubmitManySamples.sampleName.label" /></td>
						<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
					    <c:set var="_area" value = "sampleDraft" scope="request"/>
						<c:set var="_metaList" value = "${sampleDraft.getSampleDraftMeta()}" scope="request" />		
					    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
							<c:if test="${_meta.property.formVisibility != 'ignore'}">
								<c:set var="_myArea">${_area}.</c:set>
								<c:set var="_myCtxArea">${_area}.</c:set>
								<c:if test="${_metaArea != null}">		
									<c:set var="_myCtxArea">${_metaArea}.</c:set>
								</c:if>
								<c:set var="labelKey" value="${_meta.property.label}" />								
								<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
								<c:if test="${id=='inputOrIP' || id=='antibody' || id=='peakType' || id=='organism'}">
									<td class="CaptionTD top-heading" nowrap>${labelKey}
										<c:if test="${not empty _meta.property.constraint}">
											<span style="color:red">*</span>
										</c:if>
										<c:if test="${not empty _meta.property.tooltip}">
											<wasp:tooltip value="${_meta.property.tooltip}" />
										</c:if>	
									</td>
								<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
								</c:if>
							</c:if>
						</c:forEach>
						
						<c:if test="${errorsExist=='true'}">
							<td class="CaptionTD top-heading" nowrap>
								<fmt:message key="chipSeq.chipSeqSpecificSampleReview_errors.label" />
							</td>
							<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
						</c:if>
						
					</tr>
				</c:if>
				
				<tr>
					
					<td class="DataTD value-centered <c:if test="${sampleDraftStatus.count % 2 == 0}"> td-even-number</c:if>">					
						<input type='hidden' name="sampleId" id="" value='${sampleDraft.getId()}'/>
						<c:out value="${sampleDraft.getName()}" />
					</td>
					<c:set var="_area" value = "sampleDraft" scope="request"/>
					<c:set var="_metaList" value = "${sampleDraft.getSampleDraftMeta()}" scope="request" />		
					<c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>
							<c:set var="labelKey" value="${_meta.property.label}" />
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${id=='inputOrIP' || id=='antibody' || id=='peakType' || id=='organism'}">
							<td class="DataTD value-centered <c:if test="${sampleDraftStatus.count % 2 == 0}"> td-even-number</c:if>">
								<c:choose>
									<c:when test="${not empty _meta.property.control}">
								    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
								    <wasp:metaSelect control="${_meta.property.control}"/>
						       			<select class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}" class="FormElement ui-widget-content ui-corner-all">
											<c:if test= "${fn:length(selectItems) > 1 && _meta.property.formVisibility != 'immutable'}">
												<option value=''><fmt:message key="wasp.default_select.label"/></option>
											</c:if>
											<c:set var="useDefault" value="0" />
											<c:if test="${not empty _meta.property.defaultVal}">
												<c:set var="useDefault" value="1" />
												<c:forEach var="option" items="${selectItems}">
													<c:if test="${option[itemValue] == _meta.v}" >
														<c:set var="useDefault" value="0" />
													</c:if>
												</c:forEach>
											</c:if>
											<c:forEach var="option" items="${selectItems}">
												<c:if test="${fn:length(selectItems) == 1 || option[itemValue] == _meta.v || _meta.property.formVisibility != 'immutable' }">
													<option value="${option[itemValue]}"<c:if test="${fn:length(selectItems) == 1 || (not empty _meta.v && option[itemValue] == _meta.v) || (useDefault==1 && option[itemValue] == _meta.property.defaultVal)}"> selected</c:if>>
													<c:out value="${option[itemLabel]}"/></option>
												</c:if>
											</c:forEach>																									
										</select>									 						
									</c:when>
									<c:otherwise>
										<c:set var="inputVal" value="${_meta.v}" />
										<c:if test="${(empty inputVal) &&  (not empty _meta.property.defaultVal)}">
											<c:set var="inputVal" value="${_meta.property.defaultVal}" />
										</c:if>
										<input type="text" class="FormElement ui-widget-content ui-corner-all" size="10" maxlength="25" name="${_area}Meta_${_meta.k}" id="${id}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if> />
									</c:otherwise>
								</c:choose>
							</td>
							</c:if>
							
						</c:if>			 
					</c:forEach>
					
						<c:if test="${errorsExist=='true'}">
						<td class="DataTD value-centered <c:if test="${sampleDraftStatus.count % 2 == 0}"> td-even-number</c:if>">
						   <c:set var="errorList" value="${sampleDraftErrorListMap.get(sampleDraft)}" />
						   <c:forEach items="${errorList}" var="error" varStatus="errorStatus">
						   		<c:if test="${not errorStatus.first}">
						   			<br />
						   		</c:if>
						   		<span style="color:red; font-weight:bold"><c:out value="${error }" /></span>
						   </c:forEach>
						</td>
						</c:if>
					
				</tr>
			</c:forEach>
			
			</table>
		</div>
		<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
		<input type="submit" name="submit" id="submit" value="<fmt:message key="jobDraft.continue.label"/>" />
	</form>

