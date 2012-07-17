<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'/wasp/run/subgridJSON.do',
subGridModel: [ 
                {
                name  : ['<fmt:message key="run.name.label"/>'],
                width : ['auto'],
                align : ['center']                
                }
],
  