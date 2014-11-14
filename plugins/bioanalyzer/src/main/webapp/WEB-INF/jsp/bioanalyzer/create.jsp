<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%-- message keys link to internationalized text. Conversions can be specified in the .properties files in /src/main/resources/i18n --%>

<h1>
	<fmt:message key="pageTitle.bioanalyzer/create.label" /> 
</h1>
<div class="instructions">
	<fmt:message key="bioanalyzer.create_instruction.label" />
</div>
<div id="container_div_for_adding_rows" >
<div style="float:left"> 
	<form:form  cssClass="FormGrid" >
	
	<table class="EditTable ui-widget ui-widget-content">
		  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_lab.label" />:</td>
	    <td class="DataTD" >
	    	<select class="FormElement ui-widget-content ui-corner-all" id="labId" name="labId">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="lab" items="${labList}">
	      			<option value="${lab.getId()}" <c:if test="${lab.getId() == userSelectedLabId}"> SELECTED</c:if> ><c:out value="${lab.getUser().getNameFstLst()}"/>
	      			    <c:set value="${labPiInstitutionMap.get(lab)}" var="institution"/>
	      				<c:if test="${not empty institution}">
	      					[<c:out value="${institution}"/>]
	      				</c:if>
	      			</option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${labError}"/> </td>
	  </tr>
	  
	  
	 <tr class="FormData" id="grantSelectRowId"  style="display:none">
	   <td class="CaptionTD"><fmt:message key="jobDraft.selectGrant.label"/>:</td>
		    <td class="DataTD" >
		    	<select class="FormElement ui-widget-content ui-corner-all" id="selectGrantId" name="selectGrantId">
		    		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
		        	<c:forEach var="grant" items="${grantsAvailable}">
		          		<option value="${grant.getId()}"  <c:if test="${grant.getId() == userSelectedGrantId}"> SELECTED</c:if>  >
		          			<c:out value="${grant.getCode()}"/>
		          			<c:if test="${not empty grant.getName()}">
		          				(<c:out value="${grant.getName()}"/> )
		          			</c:if>
		          		</option>
		        	</c:forEach>
		      	</select>
		      	<span class="requiredField">*</span>
		      	
		      	<a id="viewAddGrantAnchor" href="javascript:void(0);"><fmt:message key="jobsubmitCreate.viewAddGrant.label" /></a>
		      	<table class="EditTable ui-widget ui-widget-content " id="addGrantTable" style="display:none;margin-top:5px">
				   <tr>
				  	<th class="label-centered" style="background-color:#FAF2D6" colspan="3"><fmt:message key="jobDraft.grantDetails.label" /></th>
				  </tr>
				  <tr class="FormData" >
					    <td class="CaptionTD"><fmt:message key="jobDraft.grantCode.label" />:</td>
					    <td class="DataTD">
					    	<input type="text" size="15" name="newGrantCode" id="newGrantCode" />
					    	<span class="requiredField">*</span>
					    </td>
					    <td class="CaptionTD error" id="newGrantCodeError">&nbsp;</td>
				  </tr>
				  <tr class="FormData" >
					    <td class="CaptionTD"><fmt:message key="jobDraft.grantName.label" />:</td>
					    <td class="DataTD">
					    	<input type="text" size="15" name="newGrantName" id="newGrantName" />
					    </td>
					    <td class="CaptionTD error" >&nbsp;</td>
				  </tr>
				  <tr class="FormData" >
				    	<td class="CaptionTD"><fmt:message key="jobDraft.dateGrantExp.label" />:</td>
					    <td class="DataTD">
					    	<input type="text" size="15" name="newGrantExp" id="newGrantExp" />
					    	<span class="requiredField">*</span>
					    </td>
					    <td class="CaptionTD error" id="newGrantExpError">&nbsp;</td>
			      </tr>
				  <tr>
					    <td colspan="3">
					    	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.addGrantButton.label" />" onClick="addNewGrant()" /> 
					    	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.closeGrantButton.label" />" onClick="closeGrantAddTable()" /> 
					    </td>
				  </tr>
				  </table>
		    </td>
		    <td class="CaptionTD error"><c:out value="${grantSelectError}"/></td>
	  </tr>
	  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_bioanalyzerChip.label" />:</td>
	    <td class="DataTD" >
	    	<select class="FormElement ui-widget-content ui-corner-all" id="bioanalyzerChip" name="bioanalyzerChip">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="bioanalyzerChip" items="${availableBioanalyzerChipList}">
	      			<option value="${bioanalyzerChip}"  <c:if test="${bioanalyzerChip == userSelectedBioanalyzerChip}"> SELECTED</c:if>  ><c:out value="${bioanalyzerChip}"/></option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${chipError}"/> </td>
	  </tr>
	  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_workflow.label" />:</td>
	    <td class="DataTD" >
	    	<select class="FormElement ui-widget-content ui-corner-all" name="workflowIdLibrariesAreDesignedFor">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="workflow" items="${workflowList}">
	      			<option value="${workflow.getId()}" <c:if test="${workflow.getId() == userSelectedWorkflowIdLibrariesAreDesignedFor}"> SELECTED</c:if>  ><c:out value="${workflow.getName()}"/></option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${workflowError}"/> </td>
	  </tr>
	  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_jobName.label" />:</td>
	    <td class="DataTD" >
	    	<input class="FormElement ui-widget-content ui-corner-all" name="jobName" value="<c:out value="${userSelectedJobName}"/>">	     
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${jobNameError}"/> </td>
	  </tr>
	  
	  </table>
	  
	  <br />
	  <span style="font-size:x-small"><fmt:message key="bioanalyzer.create_clickFirst.label" /></span>
	  <c:set var="colspan" value = '0' scope="request"/>
	  <div class="fixed-width_scrollable">
			<table class="data" style="margin: 0px 0px" >
			<c:forEach items="${librarySampleList}" var="sample" varStatus="sampleStatus">
				<c:if test="${sampleStatus.first}">
					<tr class="FormData">
						<c:if test="${fn:length(libraryErrorList)>0}">
							<td align='center' style="background-color:#FAF2D6; font-weight:bold; color:red" nowrap><fmt:message key="bioanalyzer.create_errors.label" /></td>
							<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
						</c:if>
						<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_libraryName.label" /><span style="color:red">*</span></td>
						<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
					    <c:set var="_area" value = "sample" scope="request"/>
						<c:set var="_metaList" value = "${sample.getSampleMeta()}" scope="request" />		
					    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
							<c:if test="${_meta.property.formVisibility != 'ignore'}">
								<c:set var="_myArea">${_area}.</c:set>
								<c:set var="_myCtxArea">${_area}.</c:set>
								<c:if test="${_metaArea != null}">		
									<c:set var="_myCtxArea">${_metaArea}.</c:set>
								</c:if>
								<c:set var="labelKey" value="${_meta.property.label}" />								
								<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" /><!-- on this page, this variable behaves oddly, almost like a keyword, with a value, when printed out, of: edu.yu.einstein.wasp.plugin:bioanalyzer:jar:0.1.0-SNAPSHOT. So, use id2 -->
								<c:set var="id2" value="${fn:substringAfter(_meta.k,'.')}" />								
								<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap> ${labelKey}<!--  -${_meta.k}-${id}-${id2} -->
									<c:if test="${not empty _meta.property.constraint}">
										<span style="color:red">*</span>
									</c:if>
									<c:if test="${not empty _meta.property.tooltip}">
										<wasp:tooltip value="${_meta.property.tooltip}" />
									</c:if>	
									<%-- <br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var idRE = /^<c:out value="${id2}" />/; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( idRE.test(els[i].id) ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' >first &rarr; all</a>--%>		
									<br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var id = "<c:out value="${id2}" />"; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( id==els[i].id ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' ><fmt:message key="bioanalyzer.create_firstOthers.label" /></a>		
								</td>
								<c:set var="colspan" value = '${colspan + 1}' scope="request"/>								
							</c:if>
						</c:forEach>
						<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_action.label" /></td>
						<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
					</tr>
				</c:if>
				
				<tr>
					<c:if test="${fn:length(libraryErrorList)>0}">
						<c:choose>
							<c:when test="${empty libraryErrorList.get(sampleStatus.index)}">
								<td >&nbsp;</td>
							</c:when>
							<c:otherwise>
								<td align='center' style="background-color:red;" nowrap><wasp:tooltip value="${libraryErrorList.get(sampleStatus.index)}" /></td>
							</c:otherwise>
						</c:choose>
					</c:if>
					<td>						
						<input type="text" class="FormElement ui-widget-content ui-corner-all"   name='sampleName' id='sampleName' value="${sample.getName()}">
					</td>
					<c:set var="_area" value = "sample" scope="request"/>
					<c:set var="_metaList" value = "${sample.getSampleMeta()}" scope="request" />		
					<c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>
							<c:set var="labelKey" value="${_meta.property.label}" />
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" /><!-- on this page, this variable behaves oddly, almost like a keyword, with a value, when printed out, of: edu.yu.einstein.wasp.plugin:bioanalyzer:jar:0.1.0-SNAPSHOT. So, use id2 -->
							<c:set var="id2" value="${fn:substringAfter(_meta.k,'.')}" />
							<td align='center' class="DataTD">
								<c:choose>
									<c:when test="${not empty _meta.property.control}">
								    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
								    <wasp:metaSelect control="${_meta.property.control}"/>
						       			<select class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id2}" class="FormElement ui-widget-content ui-corner-all">
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
										<input type="text" class="FormElement ui-widget-content ui-corner-all" size="10" maxlength="25" name="${_area}Meta_${_meta.k}" id="${id2}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if> />
									</c:otherwise>
								</c:choose>
							</td>					
						</c:if>			 
					</c:forEach>
					<td align='center'>						
						<input type="button" class="delRow" value="<fmt:message key="bioanalyzer.create_deleteRow.label" />"/><%--this button IS controlled by the javascript that removes a new row; it's used for new samples or new libraries --%>					
					</td>
				</tr>			
			</c:forEach>
			<tr ><td colspan="${colspan}" align="center"><c:if test="${empty edit}"><input style="width:300" type="button" class="addRow" value="<fmt:message key="bioanalyzer.create_addAdditionalRow.label" />"/></c:if></td></tr>
			</table>
		</div>

	  <div class="submit">
	    <div id="continueButtonDivId"   style="display:none" >
		 <input  class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="bioanalyzer.create_continue.label"/>">
		</div>
	  </div>
	  
	  </form:form>
	  
</div>
</div>