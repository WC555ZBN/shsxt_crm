function searchUsers() {
    $("#dg").datagrid("load",{
        userName:$("#s_userName").val(),
        trueName:$("#s_trueName").val(),
        phone:$("#s_phone").val()

    })
}


function openUserAddDialog() {
    $("#dlg").dialog("open").dialog("setTitle","用户添加");
}

function closeUserDialog() {
    $("#dlg").dialog("close")
}

/**
 * 清除表单数据
 */
function clearTab() {
    $("#userName").val('');
    $("#trueName").val('');
    $("#email").val('');
    $("#phone").val('');
    $("input[name='id']").val('');
}
function saveOrUpdateUser() {
    var url = ctx+"/user/save";
    if(!(isEmpty($("input[name='id']").val()))){
        url = ctx+"/user/update";
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            if(data.code==200){
                closeUserDialog();
                searchUsers();
                clearTab();
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}

function openUserModifyDialog() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的数据!!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂时不支持批量修改","error");
        return;
    }

    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","用户查询");

}

function deleteUser() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择要删除的数据!!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量删除!!","error");
        return;
    }

    $.messager.confirm("来自crm","请确认选中的删除记录!!",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:{
                    userId:rows[0].id
                },
                dataType:"json",
                success:function (data) {
                    if(data.code ==200){
                        searchUsers();
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            })
        }
    })

}