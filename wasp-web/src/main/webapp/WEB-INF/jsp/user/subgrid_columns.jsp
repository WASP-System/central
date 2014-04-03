<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'<wasp:relativeUrl value="user/subgridJSON.do" />',
subGridModel: [ 
                {
                name  : ['<fmt:message key="user.job_list.label"/>'],
                width : ['auto'],
                align : ['center']                
                }
],
  