<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" object="user" type="text"/>
<wasp:field name="password" object="user" type="password"/>
<wasp:field name="firstName" object="user" type="text"/>
<wasp:field name="lastName" object="user" type="text"/>
<wasp:field name="email" object="user" type="text"/>
<wasp:field name="locale" object="user" type="select" items="${locales}" itemValue="key" itemLabel="value"/>
<wasp:field name="isActive" object="user" type="text"/>
  
email.jq['editrules']={custom:true,custom_func:_validate_email};

isActive.jq['edittype']='checkbox';   
isActive.jq['editoptions']={value:"1:0"}; 
isActive.jq['formatter']='checkbox';
isActive.jq['formatoptions']={disabled : true};
isActive.jq['align']='center';
isActive.jq['search']=false;

//Password is required on Add screen and is optional on Edit screen
//We cannot require password on Edit screen because we cannot pre-populate it (it's hashed) 
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

