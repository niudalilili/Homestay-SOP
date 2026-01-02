package com.tanyde.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "homestay.jwt")
@Data
public class JwtProperties {
    /**
     *管理端员工生成jwt相关配置
     **/
    //密钥
    private String adminSecretKey;
    //持续时间
    private long adminTtl;
    //前端接受密钥名
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
