package org.example.redisdemo.util;

import com.power.common.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: anti-fraud
 * @description: okhttp请求封装
 * @author: Xsir
 * @create: 2020-07-13 14:28
 **/
@Slf4j
public class OkHttpUtils {

    private static okhttp3.OkHttpClient mInstance;

    /**
     * Get 请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> headers, Map<String, String> params) {

        Request request = new Request.Builder()
                .url(url)
                .headers(parseHeaders(headers))
                .build();

        Response response;
        try {
            response = OkHttpUtils.getInstance().newCall(request).execute();
            return parseResult(response);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Get {}, throw exception, {}", url, e.getMessage());
            return "SYSTEM_ERROR_01";
        }
    }


    /**
     * Post 请求 application/json
     *
     * @param url
     * @param headers
     * @param jsonParams
     * @return
     */
    public static String post(String url, Map<String, String> headers, String jsonParams) {

        // 创建Content-Type头为JSON
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // 根据ContentType构建请求体
        RequestBody body = RequestBody.create(mediaType, jsonParams);

        Request request = new Request.Builder()
                .url(url)
                .headers(parseHeaders(headers))
                .addHeader("Connection", "close")
                .post(body)
                .build();

        Response response;
        try {
            response = OkHttpUtils.getInstance().newCall(request).execute();
            return parseResult(response);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Post {}, throw exception, {}", url, e.getMessage());
            return "SYSTEM_ERROR_02";
        }
    }


    /**
     * 解析返回值
     *
     * @param response
     * @return
     * @throws IOException
     */
    private static String parseResult(Response response) throws IOException {
        int code = response.code();
        if (code == HttpStatus.OK.value()) {
            return response.body().string();
        } else {
            return "SYSTEM_ERROR";
        }
    }

    /**
     * Headers 封装
     *
     * @param headersParams
     * @return
     */
    public static Headers parseHeaders(Map<String, String> headersParams) {
        Headers headers;
        okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();
        if (headersParams != null && !headersParams.isEmpty()) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersbuilder.build();
        return headers;
    }

    /**
     * Get 请求封装Url
     *
     * @param params
     * @return
     */
    public static String getParams(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer("?");
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> item : params.entrySet()) {
                Object value = item.getValue();
                if (!StringUtils.isEmpty(value)) {
                    sb.append("&");
                    sb.append(item.getKey());
                    sb.append("=");
                    sb.append(value);
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }


    /**
     * 单例模式创建OkHttpUtil
     *
     * @return mInstance
     */
    public static okhttp3.OkHttpClient getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                mInstance = new okhttp3.OkHttpClient.Builder()
                        //设置连接超时
                        .connectTimeout(10, TimeUnit.SECONDS)
                        //设置读超时
                        .readTimeout(50, TimeUnit.SECONDS)
                        //设置写超时
                        .writeTimeout(20, TimeUnit.SECONDS)
                        //是否自动重连
                        .retryOnConnectionFailure(true)
                        .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
                        .build();
            }
        }
        return mInstance;
    }

}
