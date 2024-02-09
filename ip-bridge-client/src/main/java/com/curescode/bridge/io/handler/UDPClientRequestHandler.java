package com.curescode.bridge.io.handler;

import com.curescode.bridge.domain.EnabledChannel;
import com.curescode.bridge.domain.IPAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.Map;


/**
 * client request handler
 * create an udp bridge between client and client.<p/>
 * &#9;<--------<br/>
 * client&#9;udp data&#9;server<br/>
 * &#9;--------><br/>
 * @author Cure
 * @date 2024/02/01 15:48
 */
public class UDPClientRequestHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    /**
     * alive channel
     */
    public final Map<String, EnabledChannel> clientIPList;

    /**
     * client ip address
     */
    private final IPAddress serverIp;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        // resolve client address
        IPAddress clientAddr = new IPAddress(msg.sender().getAddress().toString().substring(1),
                msg.sender().getPort());
        String ipStr = clientAddr.getIp() + ":" + clientAddr.getPort();
        Channel toServer;
        EnabledChannel enabledChannel = new EnabledChannel();
        if (clientIPList.containsKey(ipStr)){
            // If the client is active,extracts channel from the map
            enabledChannel = clientIPList.get(ipStr);
            toServer = enabledChannel.getChannel();

        }else {
            // If the client has never sent data or has not exchanged data for a long time,
            // create a new connection to forward the client's data
            Bootstrap clientStrap = new Bootstrap();
            toServer = clientStrap.group(new NioEventLoopGroup())
                    .channel(NioDatagramChannel.class)
                    // record client info,send to server response handler
                    .handler(new UDPServerResponseHandler(ctx.channel(),clientAddr,enabledChannel))
                    .option(ChannelOption.SO_BROADCAST, true)
                    .bind(0).channel();

            enabledChannel.setChannel(toServer);

            clientIPList.put(ipStr, enabledChannel);
        }
        enabledChannel.setUpdateTimeMillis(System.currentTimeMillis());

        // send client request to server
        toServer.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(msg.content()),
                new InetSocketAddress(serverIp.getIp(),serverIp.getPort())
        ));


    }


    public UDPClientRequestHandler(IPAddress serverIp,Map<String, EnabledChannel> clientIPList){
        this.serverIp = serverIp;
        this.clientIPList = clientIPList;
    }
}
