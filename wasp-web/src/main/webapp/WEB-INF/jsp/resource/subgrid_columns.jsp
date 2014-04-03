<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'<wasp:relativeUrl value="resource/subgridJSON.do" />',
subGridModel: [ 
                {
                name  : ['<fmt:message key="run.run_list.label"/>'],
                width : ['auto'],
                align : ['left']            	    
                }
],
  