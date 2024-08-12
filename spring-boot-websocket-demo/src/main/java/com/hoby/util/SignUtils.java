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
        String json = "{\"baseData\":{\"msgSendTime\":\"20240812104829\",\"msgCrrltnId\":\"60d02c16-216f-4171-b517-cc8396ae8134\",\"accessId\":\"P37020304446\",\"sign\":\"0FD33135EE016AB85871615E98632C1B1CBC879E5D7104EE8E6CA86993B9AC1B\"},\"bizData\":{\"ActCode\":\"9950\",\"infno\":\"9950\",\"MchntNo\":\"1723430909113\",\"Flag\":\"1\",\"ReqTime\":\"2024-08-12 10:48:29\",\"Ver\":\"0\",\"msgid\":\"P37020304446202408121048294357\",\"mdtrtarea_admvs\":\"370200\",\"insuplc_admdvs\":\"\",\"recer_sys_code\":\"MBS_LOCAL\",\"dev_no\":\"\",\"dev_safe_info\":\"\",\"cainfo\":\"\",\"signtype\":\"SM3\",\"infver\":\"V1.0\",\"opter_type\":\"1\",\"opter\":\"POS3.0\",\"opter_name\":\"POS3.0收银系统\",\"inf_time\":\"2024-08-12 10:48:29\",\"fixmedins_code\":\"P37020304446\",\"fixmedins_name\":\"青岛市海王星辰健康药房连锁有限公司海泊雅苑店\",\"sign_no\":\"\",\"input\":{\"purcinfo\":{\"med_list_codg\":\"Q02000000\",\"fixmedins_hilist_id\":\"P37020304446\",\"fixmedins_hilist_name\":\"青岛市海王星辰健康药房连锁有限公司海泊雅苑店\",\"dynt_no\":\"\",\"fixmedins_bchno\":\"D254_TR202441698855_6\",\"spler_name\":\"稳健医疗（天门）有限公司\",\"spler_pmtno\":\"\",\"manu_lotnum\":\"20240517\",\"prodentp_name\":\"稳健医疗（天门）有限公司\",\"aprvno\":\"粤械注准20152140780\",\"manu_date\":\"2024-05-17\",\"expy_end\":\"2027-05-16\",\"finl_trns_pric\":\"\",\"purc_retn_cnt\":3,\"purc_invo_codg\":\"\",\"purc_invo_no\":\"D254_TR202441698855_6\",\"rx_flag\":\"0\",\"purc_retn_stoin_time\":\"2024-08-07 00:00:00\",\"purc_retn_opter_name\":\"unknown\",\"prod_geay_flag\":\"\",\"memo\":\"\"}}}}";

        String key = "86d57deacfef406686aecb9f93db10b6";

        JSONObject jsonTmp = JSONObject.fromObject(json);

        Object sign = jsonTmp.getJSONObject("baseData").remove("sign");

        String formatData = SignUtils.buildSignString(jsonTmp);
        System.out.println("待签名字符串: " + formatData);

        String hex = Sm3Utils.encryptWithKey(formatData, key);
        System.out.println("签名: " + hex);

        boolean flag = hex.equalsIgnoreCase(sign.toString());
        if (flag) {
            System.out.println("验签成功");
        } else {
            System.out.printf("验签失败, 传值签名: %s, 正确签名: %s %n", sign, hex);
        }
    }

}
