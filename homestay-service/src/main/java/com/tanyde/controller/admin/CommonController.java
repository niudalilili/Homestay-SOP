package com.tanyde.controller.admin;


import com.tanyde.result.Result;
import com.tanyde.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Tag(name = "通用接口", description = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file,
                                 @RequestParam(defaultValue="plan-cover")String bizType){
        try{
            //校验文件参数
            if(file==null || file.isEmpty()){
                return Result.error("文件不能为空");
            }
            if(file.getSize() > 5* 1024 *1024){
                return Result.error("文件不能大于5MB");
            }
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension= originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            //构造新文件名
            String folder="plan-cover";
            if("user-avatar".equals(bizType)){
                folder="user-avatar";
            }
            //构造新的文件名称
            String objectName = folder+"/"+ UUID.randomUUID() +extension;
            String fileUrl= aliOssUtil.upload(file.getBytes(),objectName);
            return Result.success(fileUrl);
        }
        catch(IOException e){
            log.info("文件上传失败",e);
            return Result.error("上传失败");
        }

    }




}
