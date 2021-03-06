package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dto.TreeDto;
import com.shsxt.crm.vo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@SuppressWarnings("all")
public class ModuleService extends BaseService<Module,Integer> {
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }

    public List<TreeDto> queryAllModules02(Integer roleId){
     List<TreeDto> treeDtos=moduleMapper.queryAllModules();
        //根据角色id查询角色有的菜单id list

        List<Integer> roleHasMids=permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        if(null != roleHasMids && roleHasMids.size()>0){
            treeDtos.forEach(treeDto -> {
                if(roleHasMids.contains(treeDto.getId())){
                   //说明当前角色 分配了该菜单
                    treeDto.setChecked(true);
                }
            });
        }
        return treeDtos;
    }
}
