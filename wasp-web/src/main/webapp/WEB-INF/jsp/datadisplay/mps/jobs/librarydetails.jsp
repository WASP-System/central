<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:choose>

<c:when test="${libraryBelongsToJob == false}">
<div>
Unexpectedly unable to requested locate record
</div>
</c:when>
<c:otherwise>
<div>
<table style="border:1px solid black;">
<c:if test="${parentMacromolecule!=null}">

<tr><td class="CaptionTD" style="text-decoration:underline; text-align:center" colspan="2">Submitted Macromolecule</td></tr>
<tr><td class="CaptionTD">Sample Name: </td><td class="DataTD"><c:out value="${parentMacromolecule.getName()}" /></td></tr>
<tr><td colspan="2" style="background-color:#FAF2D6">&nbsp;</td></tr>

</c:if>

<tr><td class="CaptionTD" style="text-decoration:underline; text-align:center" colspan="2">Library</td></tr>
<tr><td class="CaptionTD">Library Name: </td><td class="DataTD"><c:out value="${library.getName()}" /></td></tr>
</table>

</div>
</c:otherwise>
</c:choose>