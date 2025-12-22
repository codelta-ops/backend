package com.give_hand.controller;

import com.give_hand.result.Result;
import com.give_hand.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class UploadController {

    @Autowired
    private AliOssUtil aliOssUtil;
    @RequestMapping("/upload/image")
    public Result<String> upload(MultipartFile file) throws Exception {
        log.info("文件上传：{}", file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        fileName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString() + fileName;
        String url = aliOssUtil.upload(file.getBytes(),fileName);
        log.info("文件上传完成，上传路径：{}", url);
        return Result.success(url);
    }
}
