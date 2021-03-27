/*
 * Author: jianqing
 * Date: Dec 16, 2020
 * Description: This document is created for Accessing OSS buckets
 */
package com.ppr.model;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.setting.Setting;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OSS Access classes.
 *
 * @author Jianqing Gao
 */
public class AliOSS
{

    final static Setting creds = new Setting("oss.setting");

    public static String getDateGMT()
    {
        return getDateGMT(-8);
    }

    public static String getDateGMT(int adjust)
    {
        return (LocalDateTime.now().plusHours(adjust).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }

    public static String generateOSSSignature(String secret, String method, String resource, String date)
    {
        String content = method + "\n"//HTTP METHOD
                + "\n"//Content-MD5
                + "\n"//Content-Type
                + date + "\n"//Date in GMT format
                + resource;//resource to access
        return generateOSSSignature(secret, content);
    }

    public static String generateOSSSignature(String secret, String content)
    {
        HMac encrypter = new HMac(HmacAlgorithm.HmacSHA1, secret.getBytes());
        return java.util.Base64.getEncoder().encodeToString(encrypter.digest(content));
    }

    public static String generateOSSAuthHeader(String accessKeyId, String secret, String method, String resource, String date)
    {
        return "OSS " + accessKeyId + ":" + generateOSSSignature(secret, method, resource, date);
    }

    public static String generateOSSAuthHeader(String accessKeyId, String secret, String method, String resource)
    {
        return generateOSSAuthHeader(accessKeyId, secret, method, resource, getDateGMT());
    }

    /**
     * Try to send aliyun OSS a request.
     */
    public static void trySendReqOSS()
    {
        final String ACCESSKEY_ID = creds.get("id");
        final String accessKeySecret = creds.get("secret");
        String authorization = generateOSSAuthHeader(ACCESSKEY_ID, accessKeySecret, "GET", "/xeduo/index.html");
        String date = getDateGMT();
        HttpRequest request = HttpUtil.createRequest(Method.GET, creds.get("endpoint") + "index.html");
        request.header("Authorization", authorization);
        request.header("Date", date);
        HttpResponse response = request.execute();
        System.out.println(request);
        System.out.println(response);
    }

    /**
     * Try to send aliyun OSS a reques
     *
     * @param file.
     * @param aliyunAddress
     * @param pathtofile
     * @param contentType
     * @return
     */
    public static HttpResponse uploadFileAliyun(File file, String aliyunAddress, String pathtofile, String contentType)
    {
        //Setting creds = new Setting("oss.setting");
        final String ACCESSKEY_ID = creds.get("id");
        final String accessKeySecret = creds.get("secret");
        String date = getDateGMT();
        HttpRequest request = HttpUtil.createPost(aliyunAddress);
        // String policy;
        String policy = "{\"expiration\": \"2120-01-01T12:00:00.000Z\","
                + "\"conditions\": [{\"bucket\":\"xeduo\"}]}";
        String encodePolicy = new String(Base64.getEncoder().encode(policy.getBytes()));
        request.header("Date", date);
        //add form
        request.form("key", pathtofile + file.getName());
        request.form("OSSAccessKeyId", ACCESSKEY_ID);
        request.form("Policy", encodePolicy);//needs to be encoded as well.
        request.form("Signature", generateOSSSignature(accessKeySecret, encodePolicy));
        request.form("Content-Disposition", "attachment; filename=" + file.getName());
        request.form("file", file);
        request.form("x-oss-meta-uuid", UUID.fastUUID());
        request.form("submit", "Upload to OSS");
        if (!StrUtil.isBlank(contentType))
        {
            request.form("Content-Type", contentType);
        }
        HttpResponse response = request.execute();
        System.out.println(request);
        System.out.println(response);
        return response;
    }

    public static void logError(Throwable t)
    {
        String error = ExceptionUtil.stacktraceToString(t);
        File up;
        try
        {
            up = File.createTempFile("exception." + t.getStackTrace().length + ".", ".txt");
            up = FileUtil.writeUtf8String(error, up);
            up.deleteOnExit();
            uploadFileAliyun(up, creds.get("endpoint"), "logs/tomcat/PackagePickupRegister/", "text/plain");
        } catch (IOException ex)
        {
            Logger.getLogger(AliOSS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void log(String name,String extension, String content)
    {
        try
        {
            File up;
            up = File.createTempFile(name, extension);
            up = FileUtil.writeUtf8String(content, up);
            up.deleteOnExit();
            uploadFileAliyun(up, creds.get("endpoint"), "logs/tomcat/PackagePickupRegister/", "text/plain");
        } catch (IOException ex)
        {
            Logger.getLogger(AliOSS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException
    {
        try
        {
            Integer.parseInt("shabi");
        } catch (NumberFormatException e)
        {
            logError(e);
        }
        //trySendReqOSS();
//        JFileChooser chooser = new JFileChooser();
//        int acceptStatus = chooser.showOpenDialog(null);
//        if(acceptStatus==JFileChooser.APPROVE_OPTION)
//        {
        //uploadFileAliyun(new File("/Users/jianqing/Desktop/iShot2020-12-13 21.15.00.png"),"http://xeduo.oss-cn-hongkong.aliyuncs.com/","/");
//        }
    }

}
