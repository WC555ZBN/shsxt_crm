package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.StateStatus;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.vo.SaleChance;
import com.shsxt.utils.AssertUtil;
import com.shsxt.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    public Map<String,Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> result=new HashMap<String,Object>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getRows());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(selectByParams(saleChanceQuery));
        result.put("total",pageInfo.getTotal());
        result.put("rows",pageInfo.getList());
        return result;

    }

    public void saveSaleChance(SaleChance saleChance){
        /**
         * 1.参数校验
         * customerName 非空
         * linkMan  非空
         * linkPhone 非空  11位手机号
         *2.设置参数值的默认值
         *     state:默认未分配  如果选择分配人  state为已分配
         *     assignTime 如果选择分配人  时间为当前系统时间
         *     devResult 默认未开发  如果选择分配人 decResult为开发中
         *     isValid  默认为有效
         *     createDate 默认当前时间
         *     updateDate 默认为当前系统时间
         *3.执行添加 判断结果
         */
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setState(StateStatus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.UNDEV.getStatus());
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        }

        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(saleChance)<1,"机会数据添加失败");


    }

    /**
     * 参数的非空校验
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名!!!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人!!!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系电话!!!!");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"手机号格式不合法!!!");
    }

    public void updateSaleChance(SaleChance saleChance){
        /**
         * 1.参数的校验
         * id记录的存在校验
         * customerName 非空
         * linkMan  非空
         * linkPhone 非空  11位手机号
         * 2.设置相关的参数值
         * updateDate 当前系统时间
         * state  值设置
         *      原始记录 :未分配 修改 :已分配(由分配人决定)
         *          state: 0->1
         *          assignTIme 系统当前时间
         *          devResult 0->1
         *      原始记录 已分配 修改后 未分配
         *          state 1->0
         *          assignTime 待定 null
         *          devResult 1->0
         *3.执行更新 判断结果
         *
         *
         */
        AssertUtil.isTrue(null==saleChance.getId(),"待更新记录不存在");
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null==saleChance.getId() || null==temp,"待更新记录不存在");
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        if(StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }else if(StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setAssignMan("");
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }

        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"机会数据更新失败");


    }

    public void deleteSaleChanceByIds(Integer[] ids){
        AssertUtil.isTrue(null==ids || ids.length==0,"请选择待删除的机会数据");
        AssertUtil.isTrue(deleteBatch(ids)<ids.length,"机会数据删除失败");

    }

}
