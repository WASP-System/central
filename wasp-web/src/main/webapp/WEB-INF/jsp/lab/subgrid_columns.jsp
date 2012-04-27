<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'/wasp/lab/subgridJSON.do',
subGridModel: [ 
    {
	    name  : ['<fmt:message key="user.user_list.label"/>'],
	    width : ['auto'],
	    align : ['center']                
    }
],
  