package com.curescode.bridge.system;

import com.curescode.bridge.domain.EnabledChannel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * garbage collection runnable
 * @author Cure
 * @date 2024/02/03 23:46
 */
public class UDPChannelGCRunnable implements Runnable {

    private final int waitTimeMillis;

    public final ConcurrentHashMap<String, EnabledChannel> clientIPList;

    @Override
    public void run() {
        long now;
        while (true) {
            // get the current timestamp
            now = System.currentTimeMillis();

            if (!clientIPList.isEmpty()) {
                // traversing alive channel
                for (ConcurrentHashMap.Entry<String, EnabledChannel> entry : clientIPList.entrySet()) {
                    String key = entry.getKey();
                    EnabledChannel enabledChannel = entry.getValue();
                    if ((enabledChannel.getUpdateTimeMillis() + waitTimeMillis) < now){
                        // resources will be released if there is no activity during the timeout period
                        enabledChannel.getChannel().close();
                        clientIPList.remove(key);
                    }
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(waitTimeMillis);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
    public UDPChannelGCRunnable(int waitTimeMillis,ConcurrentHashMap<String, EnabledChannel> clientIPList){
        this.waitTimeMillis = waitTimeMillis;
        this.clientIPList = clientIPList;
    }
}
