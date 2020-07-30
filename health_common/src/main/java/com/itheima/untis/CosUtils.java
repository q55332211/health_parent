package com.itheima.untis;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @Description:
 * @Date: Create in 11:38 2020/7/29
 */
@Service
public class CosUtils {

    private static String ACCESSKEY = "AKIDS9c2CHS3YmnZ5UCqeNp2KHyOZZImPvCE";
    private static String SECRETKEY = "uNbtugIgohZ5XhbIPHuTn32kEXaM6AiM";
    private static String bucketName = "czjk-1258010513";
    private static String key;
    private static String regionName = "ap-guangzhou";

    /***
     * 上传指定文件
     * @param file
     * @return
     */
    public static String uploadImgVuserInfo(MultipartFile file) {
        COSClient cosClient = null;
        String path = "https://czjk-1258010513.cos.ap-guangzhou.myqcloud.com/";
        try {
            COSCredentials cred = new BasicCOSCredentials(ACCESSKEY, SECRETKEY);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
            ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
            // 3 生成cos客户端
            cosClient = new COSClient(cred, clientConfig);
            // 指定要上传到 COS 上对象键
            // 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-chengdu.myqcloud.com/mydemo.jpg` 中，对象键为 mydemo.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
            String string = new SimpleDateFormat("yyyyMMdd").format(new Date());
            /*key就是文件名？*/
            key = "/uploads/" + string + "/" + UUID.randomUUID() + ".jpg";//生成唯一图片路径
            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            InputStream inputStream = null;

            inputStream = file.getInputStream();

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, null);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            String etag = putObjectResult.getETag();
            Logger logger = Logger.getLogger("com.itheima.utils");
            logger.info("upload success");

            // System.out.println("upload success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
        return path + key;
    }

    /***
     * 获取存储空间中所有文件
     * @param secretId
     * @param secretKey
     * @param regionName
     * @param bucketName
     * @return
     */
    public static List<String> listAllObjects(String secretId, String secretKey, String regionName, String bucketName) {
        List<String> list = new ArrayList<>();
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix("");
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosclient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return null;
            } catch (CosClientException e) {
                e.printStackTrace();
                return null;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                //  System.out.println(key);
                // cosclient.deleteObject(bucketName, key);
                // System.out.println("delete " + key + " success");
                list.add(key);
                // 文件的etag
                String etag = cosObjectSummary.getETag();
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();
            }

            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        cosclient.shutdown();
        return list;
    }


    /**
     * 查询数据库里面所有img   cosList
     * 通过工具类获得所有存储对象 dbList
     *  cos.list.remo(db.list)
     * 返回不需要的 tempList
     * 调用cosclient.deleteObject(bucketName, key);
     * 循环遍历tempList删除垃圾的图片
     *
     * 定时任务框架
     *  任务管理中心
     *  ~~忘记了~~  看看文档
     *
     */


}
