package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Autowired
    private SaleChanceMapper saleChanceMapper;
    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        /**
         * 1.参数校验
         *      营销机会id 非空记录必须存在
         *      计划项得内容非空
         *      计划项时间 非空
         * 2.参数默认值设置
         *      is_valid createDate updateDate
         * 3.执行添加 判断结果
         *
         */
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setIsValid(1);
        cusDevPlan.setUpdateDate(new Date());

        AssertUtil.isTrue(insertSelective(cusDevPlan)<1,"计划项添加失败");

    }

    /**
     * 参数非空校验
     * @param saleChanceId
     * @param planItem
     * @param planDate
     */
    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        AssertUtil.isTrue(null==saleChanceId || null == saleChanceMapper.selectByPrimaryKey(saleChanceId),"请设置营销机会的id");
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划项");
        AssertUtil.isTrue(null==planDate,"请指定计划项日期");
    }

    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        /**
         * 1.参数校验
         *      营销机会id 非空 记录存在
         *      计划项得内容非空
         *      计划项时间 非空
         * 2.参数默认值设置
         *       updateDate
         * 3.执行更新 判断结果
         *
         */
        AssertUtil.isTrue(null ==cusDevPlan.getId() || null ==selectByPrimaryKey(cusDevPlan.getId()),"待更新的记录不存在");
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项更新失败");
    }

    public void deleteCusDevPlan(Integer id){
        CusDevPlan cusDevPlan =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id || null==selectByPrimaryKey(id),"带删除记录不存在");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项更新失败");
    }
}
