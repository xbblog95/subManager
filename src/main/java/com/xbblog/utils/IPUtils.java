package com.xbblog.utils;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.File;
import java.io.FileNotFoundException;

public class IPUtils {

    public static final String UNKOWN_ADDRESS = "未知位置";

    /**
     * 根据IP获取地址
     *
     * @return 国家|区域|省份|城市|ISP
     */
    public static String getAddress(String ip) throws DbMakerConfigException, FileNotFoundException {
        return getAddress(ip, DbSearcher.BTREE_ALGORITHM);
    }

    /**
     * 根据IP获取地址
     *
     * @param ip
     * @param algorithm 查询算法
     * @return 国家|区域|省份|城市|ISP
     * @see DbSearcher
     * DbSearcher.BTREE_ALGORITHM; //B-tree
     * DbSearcher.BINARY_ALGORITHM //Binary
     * DbSearcher.MEMORY_ALGORITYM //Memory
     */
    public static String getAddress(String ip, int algorithm) throws DbMakerConfigException, FileNotFoundException {
        if (!Util.isIpAddress(ip)) {
            return UNKOWN_ADDRESS;
        }
        String dbPath = "/usr/local/subManager/ip2region.db";
        File file = new File(dbPath);
        if (!file.exists()) {
            return UNKOWN_ADDRESS;
        }
        DbSearcher searcher = new DbSearcher(new DbConfig(), dbPath);
        DataBlock dataBlock;
        try {
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    dataBlock = searcher.btreeSearch(ip);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    dataBlock = searcher.binarySearch(ip);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    dataBlock = searcher.memorySearch(ip);
                    break;
                default:
                    return UNKOWN_ADDRESS;
            }
        }
        catch (Exception e)
        {
            e.fillInStackTrace();
            return UNKOWN_ADDRESS;
        }
        return dataBlock.getRegion();
    }
}
