package com.hoby.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liaozh
 * @since 2024-05-11
 */
@Slf4j
public class Ucssm {

    // 访问ID
    private static String accessId = "001";
    // 访问密钥
    private static String accessSecret = "c344ca9b-c94c-4c37-seft-06ef5c05f128";

    // 生产
    private static String PROD_URL = "https://xu.chinaums.com/xucsfront/extProcess";
    // 测试
    private static String TEST_URL = "https://erpwh-test.chinaums.com/padfront/extProcess";
    // 本地
    private static String LOCAL_URL = "http://127.0.0.1:48905";


    // 测试前置系统
    public static void testPadFrontSys(String actCode, JSONObject params) throws Exception {
        // 用于第三方透传接口
        Map<String, String> baseData = new HashMap<>();
        baseData.put("msgSendTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        baseData.put("msgCrrltnId", UUID.randomUUID().toString());
        baseData.put("accessId", accessId);

        Map<String, String> bizData = new HashMap<>();
        bizData.put("ActCode", actCode);
        bizData.put("MchntNo", "180856928442219");
        bizData.put("Flag", "1");
        bizData.put("ReqTime", "2019-05-06 23:59:59");
        bizData.put("Ver", "0");
        bizData.putAll(params);

        Map<String, Object> req = new HashMap<>();
        req.put("baseData", baseData);
        req.put("bizData", bizData);

        String formatData = SignUtils.buildSignString(JSONObject.fromObject(req));
        log.info("待签名字符串: {}", formatData);
        String encrypt = Sm3Utils.encryptWithKey(formatData, accessSecret);
        log.info("签名: {}", encrypt);

        baseData.put("sign", encrypt);

        HttpURLConnection connection = null;
        // 创建连接
        URL url = new URL(LOCAL_URL);
        connection = (HttpURLConnection) url.openConnection();
        // 设置http连接属性
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);

        log.info("请求报文: {}", JSONUtil.toJsonStr(req));
        OutputStream out = connection.getOutputStream();
        out.write(JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8));
        out.flush();

        // 读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line = null;
        StringBuilder buffer = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        out.close();
        connection.disconnect();

        log.info("响应报文: {}", buffer);

        JSONObject responseData = JSONObject.fromObject(buffer.toString());

        Object sign = responseData.remove("sign");
        if (sign == null) {
            log.info("签名sign为空");
            return;
        }

        String returnFormatData = SignUtils.buildSignString(responseData);
        log.info("返回的待签名字符串: {}", returnFormatData);
        String mySign = Sm3Utils.encryptWithKey(returnFormatData, accessSecret);
        log.info("生成签名: {}", mySign);
        if (mySign.equalsIgnoreCase(sign.toString())) {
            log.info("验签成功");
        } else {
            log.info("验签失败");
        }
    }

    public static void main(String[] args) throws Exception {
        String req = "{\n" +
                "\"infno\": \"3507\",\n" +
                "\"msgid\": \"P37020302787202309121625158309\",\n" +
                "\"cainfo\": \"\",\n" +
                "\"signtype\": \"\",\n" +
                "\"infver\": \"V1.0\",\n" +
                "\"opter\": \"Y370203003520\",\n" +
                "\"input\": {\n" +
                "\"data\": {\n" +
                "\"fixmedins_bchno\": \"370203\",\n" +
                "\"inv_data_type\": \"4\"\n" +
                "}\n" +
                "},\n" +
                "\"mdtrtarea_admvs\": \"370203\",\n" +
                "\"insuplc_admdvs\": \"370202\",\n" +
                "\"recer_sys_code\": \"MBS_LOCAL\",\n" +
                "\"dev_no\": \"\",\n" +
                "\"dev_safe_info\": \"\",\n" +
                "\"opter_type\": \"1\",\n" +
                "\"opter_name\": \"倪潇\",\n" +
                "\"inf_time\": \"2023-09-12 16:25:15\",\n" +
                "\"fixmedins_code\": \"P37020302787\",\n" +
                "\"fixmedins_name\": \"青岛市海王星辰健康药房连锁有限公司宁夏路店\",\n" +
                "\"sign_no\": \"370200QD000004849550\"\n" +
                "}";
        JSONObject params = JSONObject.fromObject(req);
        testPadFrontSys("9954", params);
    }

}
