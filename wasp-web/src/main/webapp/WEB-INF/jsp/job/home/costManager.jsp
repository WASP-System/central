<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />

<c:if test="${viewerIsFacilityStaff==true}"><%--must use this if statement; we cannot use sec:authorize for this page since sometimes we arrive at this page via a callable() <sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('da')">--%>
	<br />
	<span style="padding:3px; border: 1px solid black;">
		<a <%--class="button"--%> href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="/job/${job.getId()}/acctQuote/0/createUpdateQuote.do" />");' >Create A Quote</a>
		| <a <%--class="button"--%> href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="/job/${job.getId()}/uploadQuoteOrInvoice.do" />");' >Upload A Quote</a>
	</span>
	<br />
</c:if>
<c:if test="${not empty mostRecentQuote }">
	<br /><h2>Most Recent Quote: <c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mostRecentQuote}" /></h2>
</c:if>
<br />
<table class="data" style="margin: 0px 0px">
	<c:set value="${true}" var="headingNeedsDisplay"/>
	<c:choose>
		<c:when test="${empty fileGroups }">
			<tr class="FormData">
				<td class="label-centered" style="background-color:#FAF2D6">Job Quotes / Invoices</td>
				<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
				<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
			</tr>
			<tr class="FormData">
				<td colspan="3" class="DataTD value-centered">No Quotes Or Invoices Have Been Generated</td>
			</tr>			
		</c:when>
		<c:otherwise>
		<c:forEach items="${acctQuoteList}" var="acctQuote" >
			<c:if test="${not empty acctQuoteFileGroupMap.get(acctQuote) }">
				<c:set value="${acctQuoteFileGroupMap.get(acctQuote)}" var="fileGroup"/>				
				<c:if test="${headingNeedsDisplay==true}">
					<tr class="FormData">
						<td class="label-centered" style="background-color:#FAF2D6">Job Quotes / Invoices</td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
						<td class="label-centered" style="background-color:#FAF2D6">Record Created</td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
					</tr>
					<c:set value="${false}" var="headingNeedsDisplay"/>		
				</c:if>
			 	<c:set value="${fileGroupFileHandlesMap.get(fileGroup)}" var="fileHandles"/>
			 	<c:choose>
			 		<c:when test="${fn:length(fileHandles)==1}">
			 		  	<c:forEach items="${fileHandles}" var="fileHandle" >
			 		  		<tr>
			 		  			<td class="DataTD value-centered"><c:out value="${fileHandle.getFileName()}" /></td>
			 		  			<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>
			 		  			<td class="DataTD value-centered"><fmt:formatDate pattern="yyyy-MM-dd" value="${acctQuote.getCreated()}" /></td>  			
			 		  			<td class="DataTD value-centered">
			 		  				<a href="<wasp:relativeUrl value="/file/fileHandle/${fileHandle.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a> 
			 		  				<c:if test="${fileHandlesThatCanBeViewedList.contains(fileHandle)}">
		 		  						| <a href="javascript:void(0);" onclick='parent.showModalessDialog("<wasp:relativeUrl value="/file/fileHandle/${fileHandle.getId()}/view.do" />");' ><fmt:message key="listJobSamples.file_view.label"/></a>
		 		  					</c:if>
		 		  					<c:if test="${viewerIsFacilityStaff==true && acctQuotesWithJsonEntry.contains(acctQuote)}">
		 		  						| <a href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="/job/${job.getId()}/acctQuote/${acctQuote.getId()}/createUpdateQuote.do" />");' >Update With Care</a> 
		 		  					</c:if>
			 		  			</td>
			 		  		</tr>
			 			</c:forEach>
			 		</c:when>			 		  
			 		<c:otherwise>
			 			<tr>
			 		  		<td class="DataTD value-centered"><c:out value="${fn:length(fileHandles)}" /> <fmt:message key="listJobSamples.file_download_grouped_files.label"/></td>
			 		  		<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>			 		  			
			 		  		<td class="DataTD value-centered"><a href="<wasp:relativeUrl value="/file/fileGroup/${fileGroup.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a></td>
			 		  	</tr>
			 		</c:otherwise>			 		
			 	</c:choose>				
			</c:if>
		</c:forEach>	
		</c:otherwise>
	</c:choose>		
</table>
<br />
