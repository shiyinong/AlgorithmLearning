package com.syn.learning.work.geocodetest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/7/12 17:17
 **/
public class NavGeocoderTest {

    /**
     * 测试函数
     *
     * @param geocodeAddress 查询对象
     * @return 结果
     * @throws IOException
     */
    public static GeocodeResult navGeocoderTest(GeocodeAddress geocodeAddress) throws IOException {
        BufferedReader br = null;
        String url;
        if (geocodeAddress.getCityCode() != null) {
            url = "http://localhost:5290/geocode?address="
                    + URLEncoder.encode(geocodeAddress.getName(), "utf-8")
                    + "&countycode=" + URLEncoder.encode(geocodeAddress.getCityCode().toString(), "utf-8");
        } else {
            url = "http://localhost:5290/geocode?address="
                    + URLEncoder.encode(geocodeAddress.getName(), "utf-8");
        }
        URL readUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) readUrl.openConnection();
        connection.connect();
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        JSONObject jsonObject = JSON.parseObject(result.toString());
        GeocodeResult geocodeResult;
        if (jsonObject.getInteger("addressCount") > 0) {
            JSONObject address = jsonObject.getJSONArray("addresses").getJSONObject(0);
            String res = address.getString("provinceAdminName")
                    + address.getString("cityAdminName")
                    + address.getString("countyAdminName")
                    + address.getString("address");
            double lon = address.getDouble("longitude");
            double lat = address.getDouble("latitude");
            int confidence = address.getInteger("confidence");
            geocodeResult = new GeocodeResult(res, lon, lat, confidence);
        } else {
            geocodeResult = new GeocodeResult("", 0d, 0d, 0);
        }
        return geocodeResult;
    }

}
