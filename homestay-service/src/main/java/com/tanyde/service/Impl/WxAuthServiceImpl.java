package com.tanyde.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanyde.exception.BaseException;
import com.tanyde.service.WxAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WxAuthServiceImpl implements WxAuthService {

    @Value("${wx.appid:}")
    private String appId;

    @Value("${wx.secret:}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getOpenidByCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new BaseException("code不能为空");
        }
        if (appId == null || appId.isEmpty() || secret == null || secret.isEmpty()) {
            throw new BaseException("微信配置缺失");
        }
        String url = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/sns/jscode2session")
                .queryParam("appid", appId)
                .queryParam("secret", secret)
                .queryParam("js_code", code)
                .queryParam("grant_type", "authorization_code")
                .toUriString();
        String response = restTemplate.getForObject(url, String.class);
        try {
            JsonNode node = objectMapper.readTree(response);
            if (node.has("errcode")) {
                throw new BaseException("微信登录失败");
            }
            String openid = node.path("openid").asText();
            if (openid == null || openid.isEmpty()) {
                throw new BaseException("微信openid获取失败");
            }
            return openid;
        } catch (Exception e) {
            throw new BaseException("微信登录解析失败");
        }
    }
}
