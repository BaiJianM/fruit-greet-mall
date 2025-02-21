package com.liyuyouguo.common.service;

import com.alibaba.fastjson2.JSON;
import com.liyuyouguo.common.beans.enums.AliExpressStatusEnum;
import com.liyuyouguo.common.beans.enums.KdnInterfaceType;
import com.liyuyouguo.common.beans.vo.express.AliExpressResponseVo;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.config.express.AliExpressProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExpressService {

    /**
     * 字符编码
     **/
    private static final String charset = "UTF-8";
    /**
     * 客户编码，快递鸟提供，注意保管，不要泄漏
     **/
    private static final String customerCode = "1880250";
    /**
     * 客户秘钥，快递鸟提供，注意保管，不要泄漏
     **/
    private static final String appKey = "6cbefe4a-aa35-4280-a927-0941cdafa244";
    /**
     * 接口请求地址（沙箱环境）
     **/
    private static final String sandRequestUrl = "http://183.62.170.46:38092/tcpapi/orderdist";
    /**
     * 接口请求地址（正式环境）
     **/
    private static final String prodRequestUrl = "https://api.kdniao.com/tcpapi/orderdist";

    private final AliExpressProperties properties;

    public AliExpressResponseVo getExpress(String shipperName, String expressNo) {
        String host = "https://wuliu.market.alicloudapi.com";// 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/kdi";  // 【2】后缀
//        String no = "780098068058";// 【4】请求参数，详见文档描述
//        String type = "zto"; //  【4】请求参数，不知道可不填 95%能自动识别
        String urlSend = host + path + "?no=" + expressNo +"&type=" + shipperName;  // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpUrlCon = (HttpURLConnection) url.openConnection();
            httpUrlCon .setRequestProperty("Authorization", "APPCODE " + properties.getAppCode());// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpUrlCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpUrlCon.getInputStream());
                log.info("获取返回的json: {}", json);
                AliExpressResponseVo response = JSON.parseObject(json, AliExpressResponseVo.class);
                AliExpressStatusEnum statusEnum = AliExpressStatusEnum.fromCode(response.getStatus());
                if (!AliExpressStatusEnum.NORMAL.equals(statusEnum)) {
                    throw new FruitGreetException(statusEnum.getDesc());
                }
                return response;
            } else {
                Map<String, List<String>> map = httpUrlCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && "Invalid AppCode `not exists`".equals(error)) {
                    log.error("AppCode错误");
                } else if (httpCode == 400 && "Invalid Url".equals(error)) {
                    log.error("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && "Invalid Param Location".equals(error)) {
                    log.error("参数错误");
                } else if (httpCode == 403 && "Unauthorized".equals(error)) {
                    log.error("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && "Quota Exhausted".equals(error)) {
                    log.error("套餐包次数用完");
                } else if (httpCode == 403 && "Api Market Subscription quota exhausted".equals(error)) {
                    log.error("套餐包次数用完，请续购套餐");
                } else {
                    log.error("参数名错误 或 其他错误: {}", error);
                }
            }
        } catch (MalformedURLException e) {
            log.error("URL格式错误");
        } catch (UnknownHostException e) {
            log.error("URL地址错误");
        } catch (Exception e) {
            log.error("未知的阿里云快递查询错误: {}", e.getMessage());
             e.printStackTrace();
        }
        return null;
    }

    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), StandardCharsets.UTF_8);
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 获取快递鸟面单
     *
     * @return String
     * @throws Exception
     */
    public String getKdnMianDan() throws Exception {
        // 为方便理解使用原始URL请求，此处案列仅供参考。真实使用可按贵司需要进行自行业务封装，或使用第三方工具包如OkHttp、Hutool工具类等
        // 1、预下单请求
        // 组装应用级参数，详细字段说明参考接口文档
        String requestData = "{\"storeBusinessType\":2,\"merchantCode\":\"merchantCode\",\"merchantName\":\"快递鸟测试数据\",\"productTypeCode\":\"B\",\"intraCode\":\"ZNDD\",\"appointmentType\":0,\"appointmentTime\":\"\",\"travelWay\":0,\"deliveryType\":1,\"lbsType\":2,\"goodsType\":4,\"goodsNum\":2,\"totalWeight\":1.00,\"totalVolume\":8000.00,\"insured\":0,\"declaredValue\":0.00,\"orderName\":\"快递鸟测试\",\"orderMobile\":\"13600009999\",\"orderPhone\":\"\",\"goodsList\":[{\"categoryThree\":\"2197\",\"goodsName\":\"绝鼎卤Q蛋蛋鹌鹑蛋五香味*5(约138g)\",\"goodsPrice\":6.50,\"quantity\":1,\"unit\":\"包\"},{\"categoryThree\":\"2197\",\"goodsName\":\"海底捞番茄牛肉粉147克\",\"goodsPrice\":17.00,\"quantity\":2,\"unit\":\"盒\"}],\"sender\":{\"provinceName\":\"广东省\",\"cityName\":\"深圳市\",\"areaName\":\"福田区\",\"address\":\"广东省深圳市福田区沙头街道上沙地铁站A出口\",\"name\":\"张三\",\"mobile\":\"13600009999\",\"phone\":\"\",\"longitude\":\"114.03413\",\"latitude\":\"22.52514\"},\"receiver\":{\"provinceName\":\"广东省\",\"cityName\":\"深圳市\",\"areaName\":\"福田区\",\"address\":\"广东省深圳市福田区金田路3038号现代国际大厦2809\",\"name\":\"李四\",\"mobile\":\"13600009999\",\"phone\":\"\",\"longitude\":\"114.06373\",\"latitude\":\"22.53422\"}}";
        return remoteRequest(KdnInterfaceType.R2201.getCode(), requestData);
    }

    /**
     * 远程请求
     *
     * @param interfaceType 接口码
     * @param requestData   应用级参数
     * @return java.lang.String
     * @author kdniao
     * @date 2024/12/20 17:14
     **/
    private static String remoteRequest(String interfaceType, String requestData) throws Exception {
        // 组装系统级参数
        Map<String, String> params = new HashMap<>(4);
        params.put("customerCode", customerCode);
        params.put("interfaceType", interfaceType);
        params.put("requestData", requestData);
        String dataSign = encrypt(requestData, appKey, charset);
        params.put("dataSign", urlEncoder(dataSign));
        // 以form表单形式提交post请求，post请求体中包含了应用级参数和系统级参数
        return sendPost(sandRequestUrl, params);
    }
    /**
     * MD5加密
     * str 内容
     * charset 编码方式
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (byte b : result) {
            int val = b & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }
    /**
     * base64编码
     * str 内容
     * charset 编码方式
     *
     * @throws UnsupportedEncodingException
     */
    private static String base64(String str, String charset) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(str.getBytes(charset)));
    }
    @SuppressWarnings("unused")
    private static String urlEncoder(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, charset);
    }
    /**
     * Sign签名生成
     * <p>
     * 1、数据内容签名，加密方法为：把(请求内容(未编码)+ApiKey)进行MD5加密--32位小写，然后Base64编码，最后进行URL(utf-8)编码
     * <p>
     *
     * @param content  内容
     * @param keyValue ApiKey
     * @param charset  编码方式
     * @return java.lang.String dataSign签名
     * @author kdniao
     * @date 2024/12/20 15:25
     **/
    @SuppressWarnings("unused")
    private static String encrypt(String content, String keyValue, String charset) throws Exception {
        if (keyValue != null) {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }
    /**
     * 向指定 URL 发送POST方法的请求
     * url 发送请求的 URL
     * params 请求的参数集合
     *
     * @return 远程资源的响应结果
     */
    @SuppressWarnings("unused")
    private static String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), charset);
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }
                System.out.println("param:" + param.toString());
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

}
