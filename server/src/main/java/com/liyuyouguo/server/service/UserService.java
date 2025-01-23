package com.liyuyouguo.server.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.server.beans.dto.shop.SettingsSaveDto;
import com.liyuyouguo.server.beans.vo.shop.UserLoginInfoVo;
import com.liyuyouguo.server.commons.FruitShopException;
import com.liyuyouguo.server.commons.SystemError;
import com.liyuyouguo.server.entity.shop.User;
import com.liyuyouguo.server.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

/**
 * 用户信息服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final WxMaService wxMaService;

    /**
     * 微信登录
     *
     * @param code 登录时的jsCode
     * @return UserRegisterInfoVo 用户信息
     */
    public UserLoginInfoVo loginByWeChat(String code) {
        // 调用微信小程序登录接口
        WxMaJscode2SessionResult sessionResult;
        try {
            sessionResult = wxMaService.jsCode2SessionInfo(code);
        } catch (WxErrorException e) {
            log.error("微信小程序登录失败: ", e);
//            throw new FruitShopException(ShopError.WECHAT_LOGIN_ERROR);
            return null;
        }
        String openId = sessionResult.getOpenid();
        // 查找用户是否已注册
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getWeixinOpenid, openId));
        boolean isNew = false;
        if (user == null) {
            String wxName = "微信用户";
            User newUser = new User();
            newUser.setUsername(wxName + UUID.randomUUID());
            newUser.setPassword(openId);
            newUser.setRegisterTime(LocalDateTime.now());
            newUser.setRegisterIp("");
            newUser.setLastLoginTime(LocalDateTime.now());
            newUser.setLastLoginIp("");
            newUser.setMobile("");
            newUser.setWeixinOpenid(openId);
            newUser.setNickname(Base64.getEncoder().encodeToString(wxName.getBytes()));
            newUser.setAvatar("/static/images/default_avatar.png");
            userMapper.insert(newUser);
            user = newUser;
            isNew = true;
        }
        UserLoginInfoVo registerInfoVo = new UserLoginInfoVo();
        registerInfoVo.setUserInfo(user);
        registerInfoVo.setIsNew(isNew);
        // TODO 少一个token生成逻辑
        registerInfoVo.setToken("");
        return registerInfoVo;
    }

    /**
     * 获取登录用户信息
     *
     * @return User 登录用户信息
     */
    public User getUserDetail() {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        if (userId != 0) {
            User user = userMapper.selectById(userId);
            user.setNickname(new String(Base64.getDecoder().decode(user.getNickname())));
            return user;
        } else {
            throw new FruitShopException(SystemError.LOGIN_FIRST);
        }
    }

    public Integer save(SettingsSaveDto dto) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        String name = dto.getName();
        String mobile = dto.getMobile();
        String nickName = dto.getNickName();
        String avatar = dto.getAvatar();

        // 设置name_mobile为0
        int nameMobile = 0;

        // 如果name和mobile不为空，则name_mobile为1
        if (name != null && !name.isEmpty() && mobile != null && !mobile.isEmpty()) {
            nameMobile = 1;
        }

        // 对nickName进行Base64编码
        String nicknameBase64 = Base64.getEncoder().encodeToString(nickName.getBytes());

        // 创建要更新的数据
        User user = new User();
        user.setName(name);
        user.setMobile(mobile);
        user.setNickname(nicknameBase64);
        user.setAvatar(avatar);
        user.setNameMobile(nameMobile);

        return userMapper.update(user, Wrappers.lambdaUpdate(User.class).eq(User::getId, userId));

    }
}
