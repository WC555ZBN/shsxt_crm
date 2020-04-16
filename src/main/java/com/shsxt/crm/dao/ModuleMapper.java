package com.shsxt.crm.dao;

import com.shsxt.base.BaseMapper;
import com.shsxt.crm.dto.ModuleDto;
import com.shsxt.crm.dto.TreeDto;
import com.shsxt.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    public List<TreeDto> queryAllModules();

    public Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);


    public Module queryModuleByGradeAndUrl(@Param("grade") Integer grade,@Param("url") String url);

    public Module queryModuleByOptValue(String optValue);

    public List<Map<String, Object>> queryAllModulesByGrade(Integer grade);

    public int countSubModuleByParentId(Integer mid);

    public  List<ModuleDto> queryUserHasRoleHasModuleDtos(@Param("userId") Integer userId,@Param("grade") Integer grade,@Param("parentId") Integer parentId);
}