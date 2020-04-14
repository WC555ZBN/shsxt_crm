package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.vo.User;
import com.shsxt.crm.vo.UserRole;
import com.shsxt.utils.AssertUtil;
import com.shsxt.utils.MD5Util;
import com.shsxt.utils.PhoneUtil;
import com.shsxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    public UserModel login(String userName, String userPwd){
        /**
         *
         * 1.参数检验
         *      用户名
         *      密码
         * 2.根据用户名查询用户记录
         * 3.校验用户所存在性
         *      不存在  -->记录不存在  方法结束
         * 4.用户存在
         *      校验密码
         *      密码错误 -->提示密码不正确 方法结束
         * 5.密码正确
         *      用户登录成功  -->返回用户相关信息
         *
         */
        checkLoginParams(userName,userPwd);
        User user=userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null==user,"用户不存在或已注销");
        AssertUtil.isTrue(!(user.getUserPwd().equals(MD5Util.encode(userPwd))),"密码错误!");
        return buildUserModelInfo(user);


    }

    private UserModel buildUserModelInfo(User user) {

        return new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName());
    }

    /**
     * 参数校验
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");

    }

    /**
     *
     */
    /*注解实现事务*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String configPassword){

        /**
         * 1.参数校验
         *      userId 非空 -->记录必须存在
         *      oldPassword 非空
         *      newPassword 非空  新密码不能与old密码相同
         *      configPassword 非空
         *2. 设置用户新密码
         *      新密码加密
         * 3.执行更新
         *
         */

        checkParams(userId,oldPassword,newPassword,configPassword);
        /*设置新密码*/
        User user = selectByPrimaryKey(userId);
        user.setUserPwd(MD5Util.encode(newPassword));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"密码更新失败");


    }

    /**
     * 参数校验
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param configPassword
     */
    private void checkParams(Integer userId, String oldPassword, String newPassword, String configPassword) {
        User user =selectByPrimaryKey(userId);
        AssertUtil.isTrue(null==userId || null==user ,"用户不存在或用户未登录");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码!!");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        AssertUtil.isTrue(StringUtils.isBlank(configPassword),"请输入确认密码");
        AssertUtil.isTrue(!user.getUserPwd().equals(MD5Util.encode(oldPassword)),"原始密码不正确");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"旧密码与新密码不能相同");
        AssertUtil.isTrue(!(newPassword.equals(configPassword)),"新密码输入不一致");



    }

    /**
     *1.参数的校验
     *  用户名  非空
     *  email 非空 格式合法
     *  手机号 非空 格式合法
     * 2 设置默认参数
     *      isValid 1   数据有效
     *      createDate  updateDate  当前系统时间
     *      userPwd 123456 ->md5加密 默认
     *      3.执行添加判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user){
        checkParams(user.getUserName(),user.getEmail(),user.getPhone());
        User temp =userMapper.queryUserByUserName(user.getUserName());
        AssertUtil.isTrue(null!=temp && temp.getIsValid()==1,"用户名已存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认结果
        user.setUserPwd(MD5Util.encode("123456"));
        AssertUtil.isTrue(insertHasKey(user)==null,"用户添加失败");
        int userId= user.getId();
        /**
         * 用户角色的分配
         * userId
         * roleIds
         */

        relaionUserRole(userId,user.getRoleIds());



    }

    /**
     * 角色分配
     * @param userId
     * @param roleIds
     */
    private void relaionUserRole(int userId, List<Integer> roleIds) {
        /**
         * 角色分配
         *      原始角色不存在 添加新的角色记录
         *      原始角色存在,添加新的角色记录
         *      原始角色存在 清空所有角色
         *      元素角色存在 移除部分角色
         * 进行角色的分配
         *      如果用户原始角色存在  首先清空原始角色
         *      添加新的角色记录到用户角色表
         */

        int count =userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色删除失败");
        }
        if(null != roleIds && roleIds.size()>0){
            List<UserRole> userRoles = new ArrayList<>();
            roleIds.forEach(roleId->{
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            });


            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) < userRoles.size(), "用户角色分配失败!");
        }

    }

    /**
     * 重载方法 校验用户非空
     * @param userName
     * @param email
     * @param phone
     */
    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"请输入邮箱的地址");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(phone)),"手机号格式不合法");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        /**
         *1.参数的校验
         *  id  非空 记录必须存在
         *  用户名  非空 唯一
         *  email 非空 格式合法
         *  手机号 非空 格式合法
         * 2 设置默认参数
         *       updateDate 系统时间
         *3.执行添加判断结果
         *
         *
         */
        AssertUtil.isTrue(null==user.getId() || null == selectByPrimaryKey(user.getId()),"待更新记录不存在");

        checkParams(user.getUserName(),user.getEmail(),user.getPhone());

        User temp =userMapper.queryUserByUserName(user.getUserName());
        if(null != temp || temp.getIsValid()==1){
            AssertUtil.isTrue(!(user.getId().equals(temp.getId())),"该用户以存在");
        }
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户更新失败");
        relaionUserRole(user.getId(),user.getRoleIds());



    }


    public void deleteUser(Integer userId) {
        User user = selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == userId || null == user, "待删除记录不存在!");
        int count = userRoleMapper.countUserRoleByUserId(userId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色分配失败!");
        }
        user.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(user) < 1, "用户记录删除失败!");

    }

}
