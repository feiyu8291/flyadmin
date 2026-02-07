package com.fly.common.util;

import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * IP 地址工具类
 * 用于根据 IP 地址获取地理位置信息
 */
@Component
public class IpUtil {

    private static Ip2regionSearcher ip2regionSearcher;

    @Autowired
    public void setIp2regionSearcher(Ip2regionSearcher ip2regionSearcher) {
        IpUtil.ip2regionSearcher = ip2regionSearcher;
    }

    /**
     * 根据 IP 地址获取地理位置
     *
     * @param ip IP 地址
     * @return 地理位置字符串，格式：国家 省份 城市 运营商
     */
    public static String getLocationByIp(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return "未知";
        }

        // 处理本地 IP
        if (isLocalIp(ip)) {
            return "内网IP";
        }

        try {
            IpInfo ipInfo = ip2regionSearcher.memorySearch(ip);
            if (ipInfo != null) {
                return formatLocation(ipInfo);
            }
        } catch (Exception e) {
            // 解析失败，返回未知
        }

        return "未知";
    }

    /**
     * 格式化地理位置信息
     *
     * @param ipInfo IP 信息对象
     * @return 格式化后的地理位置字符串
     */
    private static String formatLocation(IpInfo ipInfo) {
        StringBuilder location = new StringBuilder();

        // 国家
        String country = ipInfo.getCountry();
        if (country != null && !country.isEmpty() && !"0".equals(country)) {
            location.append(country);
        }

        // 省份
        String province = ipInfo.getProvince();
        if (province != null && !province.isEmpty() && !"0".equals(province)) {
            if (location.length() > 0) {
                location.append(" ");
            }
            location.append(province);
        }

        // 城市
        String city = ipInfo.getCity();
        if (city != null && !city.isEmpty() && !"0".equals(city)) {
            if (location.length() > 0) {
                location.append(" ");
            }
            location.append(city);
        }

        // 运营商
        String isp = ipInfo.getIsp();
        if (isp != null && !isp.isEmpty() && !"0".equals(isp)) {
            if (location.length() > 0) {
                location.append(" ");
            }
            location.append(isp);
        }

        return location.length() > 0 ? location.toString() : "未知";
    }

    /**
     * 判断是否为本地 IP
     *
     * @param ip IP 地址
     * @return true 表示是本地 IP
     */
    private static boolean isLocalIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        // 127.0.0.1 或 localhost
        if ("127.0.0.1".equals(ip) || "localhost".equalsIgnoreCase(ip)) {
            return true;
        }

        // 0:0:0:0:0:0:0:1 (IPv6 本地地址)
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return true;
        }

        // 192.168.x.x
        if (ip.startsWith("192.168.")) {
            return true;
        }

        // 10.x.x.x
        if (ip.startsWith("10.")) {
            return true;
        }

        // 172.16.x.x - 172.31.x.x
        if (ip.startsWith("172.")) {
            String[] parts = ip.split("\\.");
            if (parts.length >= 2) {
                try {
                    int secondOctet = Integer.parseInt(parts[1]);
                    if (secondOctet >= 16 && secondOctet <= 31) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // 忽略
                }
            }
        }

        return false;
    }

    /**
     * 获取详细的 IP 信息对象
     *
     * @param ip IP 地址
     * @return IP 信息对象
     */
    public static IpInfo getIpInfo(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return null;
        }

        try {
            return ip2regionSearcher.memorySearch(ip);
        } catch (Exception e) {
            return null;
        }
    }
}
