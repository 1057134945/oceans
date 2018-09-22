package cn.iocoder.user.service.impl;

import cn.iocoder.occeans.core.exception.ServiceException;
import cn.iocoder.user.api.UserService;
import cn.iocoder.user.api.constants.ErrorCodeConstants;
import cn.iocoder.user.api.dto.UserDTO;
import cn.iocoder.user.service.dao.UserMapper;
import cn.iocoder.user.service.dao.UserRegisterMapper;
import cn.iocoder.user.service.po.UserPO;
import cn.iocoder.user.service.po.UserRegisterPO;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRegisterMapper userRegisterMapper;
    @Autowired
    private MobileCodeServiceImpl mobileCodeService;

    public UserPO getUser(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    @Override
    @Transactional
    public UserDTO createUser(String mobile, String code) {
        // TODO 芋艿，校验手机格式
        // 校验手机号的最后一个手机验证码是否有效
        if (mobileCodeService.validLastMobileCode(mobile, code) == null) {
            throw new ServiceException(-1, "手机验证码不正确"); // TODO 芋艿
        }
        // 校验用户是否已经存在
        if (getUser(mobile) != null) {
            throw new ServiceException(ErrorCodeConstants.USER_MOBILE_AREADY_REGISTERED, "用户已经注册"); // TODO 芋艿，已经注册
        }
        // 创建用户
        UserPO userPO = (UserPO) new UserPO().setMobile(mobile).setCreateTime(new Date());
        userMapper.insert(userPO);
        // 插入注册信息
        createUserRegister(userPO);
        // 转换返回
        return new UserDTO().setUid(userPO.getUid());
    }

    private void createUserRegister(UserPO userPO) {
        UserRegisterPO userRegisterPO = (UserRegisterPO) new UserRegisterPO().setUid(userPO.getUid())
                .setCreateTime(new Date());
        userRegisterMapper.insert(userRegisterPO);
    }

}