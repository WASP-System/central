<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<c:choose>

<c:when test="${libraryBelongsToJob == false}">
<div>
<%--  TODO: Internationalize this!!!! --%>
Unexpectedly unable to locate requested record
</div>
</c:when>
<c:otherwise>

<div>
<table style="border:1px solid black;">
<c:if test="${parentMacromolecule!=null}">

<%--  TODO: Internationalize this!!!! --%>

<tr><td class="CaptionTD" style="text-align:center; background-color:#FAF2D6" colspan="2">Macromolecule</td></tr>
<tr><td class="CaptionTD">Sample Name: </td><td class="DataTD"><c:out value="${parentMacromolecule.getName()}" /></td></tr>
<tr><td class="CaptionTD">Sample Type: </td><td class="DataTD"><c:out value="${parentMacromolecule.sampleType.name}" /></td></tr>
<c:set var="_area" value = "sample" scope="request"/>
<c:set var="_metaList" value = "${normalizedMacromoleculeMeta}" scope="request" />		
<c:import url="/WEB-INF/jsp/meta_ro.jsp"/>

</c:if>

<tr><td class="CaptionTD" style="text-align:center; background-color:#FAF2D6" colspan="2">Library</td></tr>
<tr><td class="CaptionTD">Library Name: </td><td class="DataTD"><c:out value="${library.getName()}" /></td></tr>
<tr><td class="CaptionTD">Library Type: </td><td class="DataTD"><c:out value="${library.sampleType.name}" /></td></tr>
<c:set var="_area" value = "library" scope="request"/>
<c:set var="_metaList" value = "${normalizedLibraryMeta}" scope="request" />		
<c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
			
			
</table>
</div>

</c:otherwise>
</c:choose>