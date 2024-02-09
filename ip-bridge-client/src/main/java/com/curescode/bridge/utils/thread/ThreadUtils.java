package com.curescode.bridge.utils.thread;

import java.util.concurrent.ThreadFactory;

/**
 * thread utils
 * @author Cure
 * @date 2024/01/17 13:34
 * @version 202401171334
 * @since 21
 */
public class ThreadUtils {

    /**
     * create new virtual factory
     * @return virtual factory
     */
    public static ThreadFactory newVirtualThreadFactory(){
        return Thread.ofVirtual().factory();
    }

    /**
     * create new virtual thread which will run a runnable
     * @param r runnable
     * @return virtual thread
     */
    public static Thread newVirtualThread(Runnable r){
        return newVirtualThreadFactory().newThread(r);
    }
    private ThreadUtils(){}
}
