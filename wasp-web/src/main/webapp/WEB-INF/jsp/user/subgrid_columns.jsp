<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'/wasp/user/subgridJSON.do',
subGridModel: [ 
                {
                name  : ['<fmt:message key="user.job_list.label"/>'],
                width : ['auto'],
                align : ['center']                
                }
],
  