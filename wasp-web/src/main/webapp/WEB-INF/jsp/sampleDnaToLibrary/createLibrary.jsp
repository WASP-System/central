<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    <wasp:message />  
 TESTING CREATE LIBRARY
<br /><br /><br />
DNA sample id  <c:out value="${macromoleculeSampleId}" /><br /> 
 JobId: <c:out value="${jobId}" /><br />
 Submitter JobId: <c:out value="${library.submitterJobId}" /><br /> 
 Submitter LabId: <c:out value="${library.submitterLabId}" /><br /> 
 Submitter UserId: <c:out value="${library.submitterUserId}" /><br />
 TypeSampleId: <c:out value="${library.typeSampleId}" /><br /> 
 SubTypeSampleId: <c:out value="${library.subtypeSampleId}" /><br /> <br />
 
 
  