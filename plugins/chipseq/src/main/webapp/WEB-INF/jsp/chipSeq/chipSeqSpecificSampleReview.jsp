<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
	
<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/chipSeq/sampleReview.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
 
<div class="instructions">
   <fmt:message key="${workflowIName}.sampleReview_instructions.label"/>
</div>

<br />
	
	<c:set var="colspan" value = '0' scope="request"/>
	
	<form action="<wasp:relativeUrl value="jobsubmit/chipSeq/chipSeqSpecificSampleReview/${jobDraft.getId()}.do" />" method="POST" >
	
		<div class="fixed-width_scrollable">
			<table class="data" style="margin: 0px 0px" >			
			<c:forEach items="${sampleDraftList}" var="sampleDraft" varStatus="sampleDraftStatus">
				<c:if test="${sampleDraftStatus.first}">
					<tr class="FormData">
						<c:if test="${fn:length(errorList)>0}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold; color:red" nowrap><fmt:message key="jobsubmitManySamples.errors.label" /></td>
							<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
						</c:if>
						<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="jobsubmitManySamples.sampleName.label" /></td>
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
								<c:if test="${id=='inputOrIP' || id=='antibody' || id=='peakType'}">
									<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap>${labelKey}
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
						<c:if test="${empty edit}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="jobsubmitManySamples.action.label" /></td>
						</c:if>
						<c:if test="${edit=='true'}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap>
								<fmt:message key="jobsubmitManySamples.deleteRow.label" />
							</td>
						</c:if>
						<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
					</tr>
				</c:if>
				
				<tr>
					<c:if test="${fn:length(errorList)>0}">
						<c:choose>
							<c:when test="${empty errorList.get(sampleDraftStatus.index)}">
								<td >&nbsp;</td>
							</c:when>
							<c:otherwise>
								<td id="errorMessageThatShouldNotBeCopied" align='center' style="background-color:red;" nowrap><wasp:tooltip value="${errorList.get(sampleDraftStatus.index)}" /></td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<td>					
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
							<c:if test="${id=='inputOrIP' || id=='antibody' || id=='peakType'}">
							<td align='center' class="DataTD">
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
							<c:if test="${ id=='adaptorset' }">
								 <input type='hidden' name="${_area}Meta_${_meta.k}" id="${id}" value=''/>			 				
							</c:if>
						</c:if>			 
					</c:forEach>
					<td align='center'>
						<c:if test="${empty edit}">
							<input type="button" class="delRow" value="<fmt:message key="jobsubmitManySamples.deleteRow.label" />"/><%--this button IS controlled by the javascript that removes a new row; it's used for new samples or new libraries --%>
						</c:if>
						<c:if test="${edit=='true'}">
							<%-- <input type="button" class="delRow" value="Need To Do: Delete Row"/>--%><%--NOT controlled by that javascript that removes new rows --%>
							<select name="deleteRow" id="deleteRow" class="FormElement ui-widget-content ui-corner-all">
								<option value="no"  <c:if test="${deleteRowsList.get(sampleDraftStatus.index)=='no'}">selected</c:if>  ><fmt:message key="jobsubmitManySamples.NO.label" /></option>
								<option value="yes" <c:if test="${deleteRowsList.get(sampleDraftStatus.index)=='yes'}">selected</c:if>  ><fmt:message key="jobsubmitManySamples.YES.label" /></option>
							</select>
							<input type='hidden' name="edit" id="" value='${edit}'/><%--need this here if user hits save button and there are errors in the post; we need to inform that this is an edit --%>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			<%--do NOT remove this next line; it's colspan is needed to set colspan of first table row if there are libraries!! --%>
			<tr ><td id="singleCellInVeryLastTableRow" colspan="${colspan}" align="center"><c:if test="${empty edit}"><input style="width:300" type="button" class="addRow" value="<fmt:message key="jobsubmitManySamples.addAdditionalRow.label" />"/></c:if></td></tr>
			
			</table>
		</div>
		<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
		
		<input type="submit" name="submit" id="submit" value="<fmt:message key="jobDraft.continue.label"/>" />
	</form>

