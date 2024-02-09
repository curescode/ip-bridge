package com.curescode.bridge.io;

import com.curescode.bridge.domain.EnabledChannel;
import com.curescode.bridge.domain.IPAddress;
import com.curescode.bridge.io.handler.UDPClientRequestHandler;
import com.curescode.bridge.system.UDPChannelGCRunnable;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.concurrent.ConcurrentHashMap;

import static com.curescode.bridge.utils.thread.ThreadUtils.newVirtualThread;

/**
 * bridge local server<br/>
 * used to listen for client requests
 * @author Cure
 * @date 2024/01/31 22:18
 */
public class ClientServer {

    /**
     * which port server will occupy
     */
    private final int serverPort;

    private final IPAddress serverIp;

    private Channel udpChannel;

    /**
     * start udp listening
     */
    public void initUDP() throws InterruptedException {
        // avoid throwing concurrentModificationException during garbage collection
        ConcurrentHashMap<String, EnabledChannel> clientIPList = new ConcurrentHashMap<>();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .handler(new UDPClientRequestHandler(this.serverIp,clientIPList))
                .option(ChannelOption.SO_BROADCAST, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        this.udpChannel = bootstrap.bind(serverPort).sync().channel();

        // create a new virtual thread to run channel garbage collection
        Thread udpChannelGC = newVirtualThread(new UDPChannelGCRunnable(1000 * 60,clientIPList));
        udpChannelGC.start();
    }


    public void initTCP()  {
        // todo
    }


    /**
     * stop udp server
     */
    public void stopUDPBridge(){
        if (udpChannel != null && udpChannel.isOpen()){
            udpChannel.close();
        }
    }

    public ClientServer(int port, IPAddress serverIp){
        this.serverIp = serverIp;
        this.serverPort = port;
    }
}
