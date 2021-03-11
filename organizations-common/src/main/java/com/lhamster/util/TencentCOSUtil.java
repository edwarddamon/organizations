package com.lhamster.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.File;

/**
 * 腾讯云存储工具类
 */
public class TencentCOSUtil {
    // 1 初始化用户身份信息(secretId, secretKey，可在腾讯云后台中的API密钥管理中查看！
    private static COSCredentials cred = new BasicCOSCredentials(Constants.SMSSECREID, Constants.SMSSECREKEY);

    // 2 设置bucket的区域, COS地域的简称请参照
    // https://cloud.tencent.com/document/product/436/6224，根据自己创建的存储桶选择地区
    private static ClientConfig clientConfig = new ClientConfig(new Region("ap-nanjing"));

    private static String prefixUrl = "https://lhamster-organizations-1302533254.cos.ap-nanjing.myqcloud.com";

    /**
     * @describe 上传文件的方法
     * @methods uploadObject 方法名
     * @parameter fileUrl 上传文件地址
     * @parameter fileKey 文件对象名称
     * @parameter @return 对象列表
     */
    public static String uploadObject(File localFile, String fileKey) {
        // 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        try {
            // 桶名称、fileKey 指定要上传到 COS 上对象键、指定要上传的文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(Constants.bucketName, fileKey, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return prefixUrl + "/" + fileKey;
        } catch (CosServiceException serverException) {
            throw new RuntimeException("上传文件平台Server异常" + serverException.getErrorMessage());
        } catch (CosClientException clientException) {
            throw new RuntimeException("上传文件平台Client异常" + clientException.getMessage());
        }
    }

    /**
     * @return
     * @Title: downFile
     * @Description: 下载文件
     */
    public static void downFile() {
        // 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        //要下载的文件路径和名称
        String key = "down/demo1.jpg";
        // 指定文件的存储路径
        File downFile = new File("src/test/resources/mydown.txt");
        // 指定要下载的文件所在的 bucket 和对象键
        GetObjectRequest getObjectRequest = new GetObjectRequest(Constants.bucketName, key);
        ObjectMetadata downObjectMeta = cosclient.getObject(getObjectRequest, downFile);
    }


    /**
     * 删除文件
     *
     * @param key
     */
    public static void deletefile(String key) throws CosClientException, CosServiceException {
        // 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // 指定要删除的 bucket 和路径
        cosclient.deleteObject(Constants.bucketName, key);
        // 关闭客户端(关闭后台线程)
        cosclient.shutdown();
    }
}
