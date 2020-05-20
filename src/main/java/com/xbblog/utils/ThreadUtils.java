package com.xbblog.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    private static ExecutorService pool;

    private ThreadUtils()
    {

    }

    public static synchronized ExecutorService getPool()
    {
        if(pool == null)
        {
            pool = Executors.newFixedThreadPool(20);
        }
        return pool;
    }
}
