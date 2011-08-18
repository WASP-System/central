<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" object="user" />
<wasp:field name="password" object="user" />
<wasp:field name="firstName" object="user" />
<wasp:field name="lastName" object="user" />
<wasp:field name="email" object="user" />
<wasp:field name="locale" object="user" />
<wasp:field name="isActive" object="user" />

email.jq['editrules']={custom:true,custom_func:_validate_email};

isActive.jq['edittype']='checkbox';
isActive.jq['editoptions']={value:"1:0"}; 
isActive.jq['formatter']='checkbox';
isActive.jq['formatoptions']={disabled : true};
isActive.jq['align']='center';
isActive.jq['search']=false;

locale.jq['edittype']='select';
locale.jq['editoptions']={value:{}};
locale.jq['search']=false;
<c:forEach var="localeEntry" items="${locales}">     
	locale.jq['editoptions']['value']['${localeEntry.key}']='${localeEntry.value}';
</c:forEach>

password.jq['hidden']=true;
password.jq['edittype']='password';
password.jq['editrules']={};	
password.jq['editrules']['edithidden']=true;	


_beforeShowAddForm = function (formId) {
    var myPass=jQuery("#grid_id").jqGrid ('getGridParam', 'colModel')[1];//TODO: get password field dynammicly
    myPass['editrules']['custom_func']=_validate_required;
	myPass['editrules']['custom']=true;
	myPass['formoptions']={elmsuffix:'<font color=red>*</font>'};//TODO: make it work		
}
  
_beforeShowEditForm = function (formId) {
    var myPass=jQuery("#grid_id").jqGrid ('getGridParam', 'colModel')[1];//TODO: get password field dynammicly    
    myPass['editrules']['custom']=false;
	myPass['formoptions']={};	
}

