<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'<c:url value="resource/subgridJSON.do" />',
subGridModel: [ 
                {
                name  : ['<fmt:message key="run.run_list.label"/>'],
                width : ['auto'],
                align : ['left']            	    
                }
],
  