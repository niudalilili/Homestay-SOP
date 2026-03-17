package com.tanyde.service.Impl;

import com.tanyde.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * WxAuthServiceImpl 单元测试
 */
class WxAuthServiceImplTest {

    /**
     * 验证微信接口返回有效 openid 时能够正确解析
     */
    @Test
    void shouldReturnOpenidWhenWxResponseValid() {
        WxAuthServiceImpl service = new WxAuthServiceImpl();
        ReflectionTestUtils.setField(service, "appId", "app");
        ReflectionTestUtils.setField(service, "secret", "sec");
        RestTemplate restTemplate = (RestTemplate) ReflectionTestUtils.getField(service, "restTemplate");

        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo(org.hamcrest.Matchers.containsString("https://api.weixin.qq.com/sns/jscode2session")))
                .andExpect(method(GET))
                .andRespond(withSuccess("{\"openid\":\"openid-1\"}", org.springframework.http.MediaType.APPLICATION_JSON));

        String openid = service.getOpenidByCode("code-1");

        assertEquals("openid-1", openid);
        server.verify();
    }

    /**
     * 验证微信配置缺失时抛出业务异常
     */
    @Test
    void shouldThrowWhenWxConfigMissing() {
        WxAuthServiceImpl service = new WxAuthServiceImpl();
        ReflectionTestUtils.setField(service, "appId", "");
        ReflectionTestUtils.setField(service, "secret", "");

        assertThrows(BaseException.class, () -> service.getOpenidByCode("code"));
    }
}
