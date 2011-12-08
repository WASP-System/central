//height: '300',
pager: '#gridpager',
caption:  'Labs in ${departmentName}',
sortable: true,
sortname: name,
sortorder: "desc",
url: '/wasp/department/detail/${departmentId}/listLabJSON.do?selId=${param.selId}'