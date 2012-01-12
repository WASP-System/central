height: 450,
rowNum: 20,
pager: '#gridpager',
caption:  'Labs in ${departmentName}',
sortable: true,
sortname: name,
sortorder: "desc",
url: '/wasp/department/detail/${departmentId}/listLabJSON.do?selId=${param.selId}'