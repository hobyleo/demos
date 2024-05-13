package com.hoby.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SignUtils {

    /**
     * 将参数转换成查询字符串形式，按字母表排序</br>
     *
     * @param
     * @return String 转换后的字符串
     */
    public static String buildSignString(JSONObject jsonObj) {
        // 中间重复转换下，避免浮点数后面的0引起客户端和后台报文不一致而导致验签失败
        JSONObject jsonTmp = JSONObject.fromObject(jsonObj.toString());
        // 对JSONObject排序，避免jsonMap2Map对key编号不一致
        JSONObject json = sortJsonObject(jsonTmp);
        Map jsonMap = new LinkedHashMap();
        Map normalMap = new LinkedHashMap();

        jsonMap = convertJson2Map(json, jsonMap);

        Map<String, Integer> keyIndex = new LinkedHashMap<>();
        normalMap = jsonMap2Map(jsonMap, normalMap, keyIndex);

        return buildSignStringInner(normalMap);
    }

    /**
     * 将参数转换成查询字符串形式，按字母表排序
     *
     * @param params 请求参数
     * @return String 转换后的字符串
     */
    private static String buildSignStringInner(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.size());
        for (String key : params.keySet()) {
            if (StringUtils.isEmpty(params.get(key).toString())) {
                continue;
            }
            keys.add(key);
        }
        // 按字母表排序
        Collections.sort(keys);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key).toString();
            if (i == keys.size() - 1) {
                buf.append(key).append("=").append(value);
            } else {
                buf.append(key).append("=").append(value).append("&");
            }
        }
        return buf.toString();
    }

    /**
     * JSONObject排序
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("all")
    public static JSONObject sortJsonObject(JSONObject obj) {
        Map map = new LinkedHashMap();
        Iterator<String> it = obj.keys();
        while (it.hasNext()) {
            String key = it.next();
            Object value = obj.get(key);
            if (value instanceof JSONObject) {
                map.put(key, sortJsonObject(JSONObject.fromObject(value)));
            } else if (value instanceof JSONArray) {
                map.put(key, sortJsonArray(JSONArray.fromObject(value)));
            } else {
                map.put(key, value);
            }
        }
        return JSONObject.fromObject(map);
    }

    /**
     * JSONArray排序
     *
     * @param array
     * @return
     */
    @SuppressWarnings("all")
    public static JSONArray sortJsonArray(JSONArray array) {
        List list = new LinkedList();
        int size = array.size();
        for (int i = 0; i < size; i++) {
            Object obj = array.get(i);
            if (obj instanceof JSONObject) {
                list.add(sortJsonObject(JSONObject.fromObject(obj)));
            } else if (obj instanceof JSONArray) {
                list.add(sortJsonArray(JSONArray.fromObject(obj)));
            } else {
                list.add(obj);
            }
        }
        Collections.sort(list, new Comparator<Object>() {
            // 升序排序
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return JSONArray.fromObject(list);
    }

    /**
     * 递归转换json至map，转换后的map包含子map
     *
     * @param jsonObject json对象
     * @param map        转换后的map
     * @return 转换后的map
     */
    private static Map<String, Object> convertJson2Map(JSONObject jsonObject, Map<String, Object> map) {
        for (Object key : jsonObject.keySet()) {
            if (jsonObject.get(key).getClass().equals(JSONObject.class)) {
                LinkedHashMap<String, Object> _map = new LinkedHashMap<>();
                map.put((String) key, _map);
                convertJson2Map(jsonObject.getJSONObject((String) key), _map);
            } else if (jsonObject.get(key).getClass().equals(JSONArray.class)) {
                ArrayList<Object> list = new ArrayList<>();
                map.put((String) key, list);
                convertJsonArray2List(jsonObject.getJSONArray((String) key), list);
            } else {
                String value = jsonObject.optString((String) key);
                // 过滤掉null值和空值
                if (!JSONNull.getInstance().toString().equalsIgnoreCase(value)
                        && StringUtils.isNotBlank(value)) {
                    map.put((String) key, value);
                }
            }
        }
        return map;
    }

    /**
     * 递归转换JSONArray至list
     *
     * @param jsonArray json对象
     * @param list      转换后的list
     */
    private static void convertJsonArray2List(JSONArray jsonArray, List<Object> list) {
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).getClass().equals(JSONArray.class)) {
                ArrayList<Object> _list = new ArrayList<>();
                list.add(_list);
                convertJsonArray2List(jsonArray.getJSONArray(i), _list);
            } else if (jsonArray.get(i).getClass().equals(JSONObject.class)) {
                Map<String, Object> _map = new LinkedHashMap<>();
                list.add(_map);
                convertJson2Map(jsonArray.getJSONObject(i), _map);
            } else {
                String value = jsonArray.get(i).toString();
                if (!JSONNull.getInstance().toString().equalsIgnoreCase(value)
                        && StringUtils.isNotBlank(value)) {
                    list.add(value);
                }
            }
        }
    }

    /**
     * 将递归的map转换至一般的map，所有value中不再包含map
     *
     * @param map    递归后的map
     * @param target 普通value的map
     * @return 普通value的map
     */
    @SuppressWarnings("all")
    private static Map<String, String> jsonMap2Map(Map<String, Object> map, Map<String, String> target, Map<String, Integer> keyIndex) {
        for (String key : map.keySet()) {
            if (map.get(key).getClass().equals(LinkedHashMap.class)) {
                jsonMap2Map((Map) map.get(key), target, keyIndex);
            } else if (map.get(key).getClass().equals(ArrayList.class)) {
                jsonList2Map((List) map.get(key), target, key, keyIndex);
            } else {
                String value = (String) map.get(key);
                // 过滤掉null值和空值
                if (!JSONNull.getInstance().toString().equalsIgnoreCase(value)
                        && StringUtils.isNotBlank(value)) {
                    if (keyIndex.containsKey(key)) {
                        // 重复的key按序号递增
                        String newKey = key + String.valueOf((keyIndex.get(key) + 1));
                        keyIndex.put(key, (keyIndex.get(key) + 1));
                        target.put(newKey, value);
                    } else {
                        keyIndex.put(key, 0);
                        target.put(key, value);
                    }

                }
            }
        }
        return target;
    }

    /**
     * 将递归的List转换至一般的map，所有value不再包含map
     *
     * @param list   递归的list
     * @param target 转换后的map
     * @param key    该list所在map中的key值
     */
    @SuppressWarnings("all")
    private static void jsonList2Map(List list, Map target, String key, Map<String, Integer> keyIndex) {
        for (Object o : list) {
            if (o.getClass().equals(ArrayList.class)) {
                jsonList2Map((List) o, target, null, keyIndex);
            } else if (o.getClass().equals(LinkedHashMap.class)) {
                jsonMap2Map((Map) o, target, keyIndex);
            } else {
                String appendValue = o.toString();
                // 过滤掉null值和空值
                if (!JSONNull.getInstance().toString().equalsIgnoreCase(appendValue)
                        && StringUtils.isNotBlank(appendValue)) {
                    if (target.containsKey(key)) {
                        // 针对普通list，如果已经包含该key，则添加到value后面
                        String value = target.get(key).toString();
                        target.put(key, value + "," + appendValue);
                    } else {
                        if (keyIndex.containsKey(key)) {
                            // 重复的key按序号递增
                            String newKey = key + String.valueOf((keyIndex.get(key) + 1));
                            keyIndex.put(key, (keyIndex.get(key) + 1));
                            target.put(newKey, o.toString());
                        } else {
                            keyIndex.put(key, 0);
                            target.put(key, o.toString());
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // String json = "{\"ActCode\":\"3721\",\"BranID\":\"2859768\",\"DeviceID\":\"7433cb06-d3d3-3e7d-994a-8a76358749e2\",\"GDeviceSn\":\"1741BC101323\",\"GDeviceType\":\"AECR C10\",\"GEnv\":\"1\",\"GFromClientType\":\"1\",\"GManufacturer\":\"LANDI\",\"GOSVersion\":\"android 7.1.2\",\"GPlatform\":\"1\",\"GProduct\":\"1\",\"GVersion\":\"2.5.5\",\"MCCode\":\"41116\",\"PosNo\":\"1001\",\"ReqTime\":\"2023-10-07 09:28:42\",\"Sign\":\"00000000\",\"clientId\":\"0b1568ce3fd10fdc695e8f9cc0d08cc1\",\"pushId\":\"170976fa8afb6cf89b7\"}";
        String json = "{\"ActCode\":\"3721\",\"BranID\":\"2859768\",\"Test\":[{\"DD\":\"1D\",\"CC\":\"1C\"},{\"DD\":\"2D\",\"CC\":\"2C\"},{\"DD\":null,\"CC\":\"3C\"}],\"Amount\":56.70,\"Nums\":[1,2,3]}";

        String key = "4CF7D0E384633FBD8EE9F063A2E06E89F60FAE359515459369F29F6ED039EDFE";

        JSONObject jsonTmp = JSONObject.fromObject(json);

        System.out.println(json);

        String formatData = SignUtils.buildSignString(jsonTmp);
        // 拼接字段之后的结果
        System.out.println(formatData);

        String hex = Sm3Utils.encryptWithKey(formatData, key);
        // 结果：8B417E379D9ED1645E78F55764A00CC0689FBC102260272F14F67FC09E9AEADA
        System.out.println(hex);

        boolean flag = Sm3Utils.verifyWithKey(formatData, hex, key);

        // 结果：true
        System.out.println(flag);
    }

}
