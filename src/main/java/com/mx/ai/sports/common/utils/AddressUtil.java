package com.mx.ai.sports.common.utils;

import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Ip地址工具类
 * @author Mengjiaxin
 * @date 2019-08-20 16:25
 */
public class AddressUtil {

    private static Logger log = LoggerFactory.getLogger(AddressUtil.class);

    public static String getCityInfo(String ip) {
        DbSearcher searcher = null;
        try {
            String dbPath = AddressUtil.class.getResource("/ip2region/ip2region.db").getPath();
            File file = new File(dbPath);
            if (!file.exists()) {
                String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                dbPath = tmpDir + "ip.db";
                file = new File(dbPath);
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(AddressUtil.class.getClassLoader().getResourceAsStream("classpath:ip2region/ip2region.db")), file);
            }
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, file.getPath());
            Method method = searcher.getClass().getMethod("btreeSearch", String.class);
            if (!Util.isIpAddress(ip)) {
                log.error("Error: Invalid ip address");
            }
            DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);

            if(dataBlock == null){
                return "";
            }
            return dataBlock.getRegion();
        } catch (Exception e) {
//            log.error("获取地址信息异常:{}", e.getMessage());
        }finally {
            if (searcher !=null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
        }
        return "";
    }

}