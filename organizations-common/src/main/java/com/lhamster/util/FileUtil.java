package com.lhamster.util;

import com.lhamster.response.exception.ServerException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/13
 */
@Api(value = "处理文件相关工具类")
@Slf4j
public class FileUtil {
    /*图片格式*/
    private static final List<String> suffix = new ArrayList<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif"));

    /*
        生成随机文件名
     */
    public static String randomFileName(String filename) {
        log.info("文件名:" + filename);
        // uuid生成随机的文件名
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        // 新的文件名+文件后缀
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        if (!suffix.contains(fileSuffix)) { // 不是指定格式
            throw new ServerException(Boolean.FALSE, "文件格式不正确");
        }
        return uuid + fileSuffix;
    }
}
