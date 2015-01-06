<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 <br />
<title><fmt:message key="pageTitle./waspIlluminaHiSeq/flowcell/createUpdateRun.label"/></title>
<c:choose>
	<c:when test="${action == 'create'}">
		<h1><fmt:message key="runInstance.headerCreate.label"/></h1>
	</c:when>
	<c:otherwise>
		<h1><fmt:message key="runInstance.headerUpdate.label"/></h1>
	</c:otherwise>
</c:choose> 

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<div id="containerForTables" style="width:100%;overflow:hidden" >

<div id="left" style="float:left; margin-right:10px">
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.typeOfPlatformUnit.label"/>:</td><td class="DataTD"><c:out value="${typeOfPlatformUnit}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.barcodeName.label"/>:</td><td class="DataTD"><c:out value="${barcodeName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.readType.label"/>:</td><td class="DataTD"><c:out value="${readType}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.readLength.label"/>:</td><td class="DataTD"><c:out value="${readLength}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.numberOfCellsOnThisPlatformUnit.label"/>:</td><td class="DataTD"><c:out value="${numberOfCellsOnThisPlatformUnit}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="platformunitShow.comment.label"/>:</td><td class="DataTD"><textarea style='font-size:9px' READONLY cols='30' rows='4' wrap='virtual'><c:out value="${comment}" /></textarea></td></tr>
</table>
</div>


<div id="right" style="overflow:hidden">

<table class="EditTable ui-widget ui-widget-content">
<c:if test="${action == 'create'}">
	<form name="selectRunFolder" class="FormGrid" method="get">
	<tr class="FormData">
		<td class="CaptionTD"><fmt:message key="runInstance.chooseRunFolder.label"/>:</td>
		<td class="DataTD">
			<select class="FormElement ui-widget-content ui-corner-all" id="runFolderName" name="runFolderName" size="1" onchange="document.selectRunFolder.submit()">
				<option value=""><fmt:message key="wasp.default_select.label"/>		
				<c:forEach items="${runFolderSet}" var="runFolder">
					<c:set var="selectedFlag2" value=""/>
					<c:if test='${run.getName() == runFolder}'>
						<c:set var="selectedFlag2" value="selected='selected'"/>
					</c:if>
					<option value='<c:out value="${runFolder}" />' <c:out value="${selectedFlag2}" />><c:out value="${runFolder}" /></option>
				</c:forEach>
			 </select>	
			 <table>
			 	<tr class="FormData">
				 	<td class="CaptionTD"><fmt:message key="runInstance.showAll.label"/></td>&nbsp;</td>
				   	<td class="DataTD"><input type="checkbox" name="showAll" value="true" <c:if test="${showAll == true }">checked="checked"</c:if> onclick="document.selectRunFolder.submit()" /></td>
				</tr>
			 </table>
		</td>
		<td>&nbsp;</td>
	</tr>
	<sec:authorize access="hasRole('su')">
	<tr class="FormData">
		<td class="CaptionTD">or Enter Manually:</td>
		<td class="DataTD"><input type="text" name="runFolderNameManual" id="runFolderNameManual" value="<c:if test='${not empty runFolderNameManual }'><c:out value='${runFolderNameManual}'></c:out></c:if>" />
		<input class="fm-button" type="button" onClick="document.selectRunFolder.submit()" value="add" /> </td>
		<td>&nbsp;</td>
	</tr>
	</sec:authorize>
	</form>
</c:if>

<c:if test='${run.getResource() != null}'>

  	<form:form  cssClass="FormGrid" commandName="run" method="post">
  	<form:hidden path="name" />
  	<input type="hidden" name="isRunStart" id="isRunStart" value="true" />
  	<input type="hidden" name="showAll" value="${showAll}" />
	<tr class="FormData">
        <td class="CaptionTD"><fmt:message key="runInstance.name.label" />:</td>
        <td class="DataTD"><c:out value="${run.getName()}" /></td>
      	<td>&nbsp;</td>
	</tr>
	<tr class="FormData">
        <td class="CaptionTD"><fmt:message key="runInstance.resourceName.label" />:</td>
        <td class="DataTD"><c:out value="${run.getResource().getName()}" /></td>
      	<td class="CaptionTD error"><c:out value="${resourceNameError}"/></td>
	</tr>
	
	<c:if test="${empty(resourceNameError)}" >
		<c:set var="_area" value = "run" scope="request"/>
		<c:set var="_metaArea" value = "runInstance" scope="request"/>
	    <c:set var="_metaList" value = "${run.getRunMeta()}" scope="request" />
	    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
	
		<tr class="FormData">
	        <td class="CaptionTD"><fmt:message key="runInstance.technician.label" />:</td>
	        <td class="DataTD">        	
	        	<form:select class="FormElement ui-widget-content ui-corner-all" path="userId" size="1">
					<option value="0"><fmt:message key="wasp.default_select.label"/> <%-- --select-- --%>
					<c:forEach items="${technicians}" var="technician">
						<c:set var="selectedFlag3" value=""/>
						<c:if test='${technician.getUserId()==run.getUserId()}'>
							<c:set var="selectedFlag3" value="SELECTED"/>
						</c:if>
						<option value="<c:out value="${technician.getUserId()}"/>"  <c:out value="${selectedFlag3}" /> /> <c:out value="${technician.getFirstName()}" /><c:out value=" " /><c:out value="${technician.getLastName()}" />
					</c:forEach>
			 	</form:select>        
	        	<span class="requiredField">*</span></td>
	       <td class="CaptionTD error"><form:errors path="userId" /></td>
		</tr>
	</c:if>
	<tr class="FormData">
        <td nowrap class="CaptionTD"><fmt:message key="runInstance.dateRunStarted.label" />:</td>
        <td class="DataTD"><fmt:formatDate pattern="yyyy/MM/dd" value="${run.getStarted()}"/></td>
        <td>&nbsp;</td>
	</tr>
	<tr class="FormData">
        <td nowrap class="CaptionTD"><fmt:message key="runInstance.dateRunEnded.label" />:</td>
        
        <td class="DataTD">
        <c:if test="${run.getFinished() == null}" >
        	N/A
        </c:if>
        <c:if test="${run.getFinished() != null}" >
        	<fmt:formatDate pattern="yyyy/MM/dd" value="${run.getFinished()}"/>
        </c:if>
        </td>
       <td>&nbsp;</td>
	</tr>

	<tr><td colspan="3">
    	<div class="submit">
    		<c:if test="${empty(resourceNameError)}" >
   	    		<input class="fm-button" type="button" onClick="submit();openWaitDialog();" value="<fmt:message key='runInstance.submit.label'/>" /> 
   	    		<sec:authorize access="hasRole('su')">
   	    			<input class="fm-button" type="button" onClick="$('#isRunStart').val('false');submit();openWaitDialog();" value="<fmt:message key='runInstance.submitNoStart.label'/>" /> 
   	    		</sec:authorize>
   	    	</c:if>
   	    	<c:if test="${not empty(resourceNameError)}" >
   	    		<input class="fm-button" type="button" onClick="location.href='<wasp:relativeUrl value="resource/list.do" />';" value="<fmt:message key='resource.resource_list.label'/>" /> 
			</c:if>
    		<c:if test="${run.getId() > 0}">
    			&nbsp;<input class="fm-button" type="button" onClick="location.href='<wasp:relativeUrl value="waspIlluminaHiSeq/run/${run.getId()}/update.do" />';" value="<fmt:message key='runInstance.reset.label'/>" /> 
    		</c:if>
    		&nbsp;<input class="fm-button" type="button" onClick="location.href='<wasp:relativeUrl value="waspIlluminaHiSeq/flowcell/${run.getPlatformUnit().getId()}/show.do" />';" value="<fmt:message key='runInstance.cancel.label'/>" /> 
    	</div>
    </td></tr>

	</form:form>

</c:if>


</table>

</div>

</div>