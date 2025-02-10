package com.howwelike.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.howwelike.constant.MessageConstant;
import com.howwelike.dto.UserLoginDTO;
import com.howwelike.entity.User;
import com.howwelike.exception.LoginFailedException;
import com.howwelike.mapper.UserMapper;
import com.howwelike.properties.WeChatProperties;
import com.howwelike.service.UserService;
import com.howwelike.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {

        /*Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(json);
        String openid =  jsonObject.getString("openid");*/

        String openid = getOpenid(userLoginDTO.getCode());
        // 判断openid是否为空,为空则表示登录失败，抛出业务异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 如果是新用户，自动完成注册
        User user = userMapper.getByOpenid(openid);
        if (user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }
        // 返回用户对象
        return user;
    }

    /**
     * 调用微信接口服务，获得当前微信用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code){

        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
