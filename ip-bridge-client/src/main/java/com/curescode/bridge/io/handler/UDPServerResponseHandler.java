package com.curescode.bridge.io.handler;

import com.curescode.bridge.domain.EnabledChannel;
import com.curescode.bridge.domain.IPAddress;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

/**
 * handler server response
 * @author Cure
 * @date 2024/02/01 11:44
 */
public class UDPServerResponseHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final IPAddress clientAddr;

    private final Channel clientChannel;

    private final EnabledChannel enabledChannel;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        // server response forwarded to client
        this.clientChannel.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(msg.content()),
                new InetSocketAddress(clientAddr.getIp(),clientAddr.getPort())));

        // refresh data update time
        enabledChannel.setUpdateTimeMillis(System.currentTimeMillis());
    }

    UDPServerResponseHandler(Channel clientChannel, IPAddress clientAddr, EnabledChannel enabledChannel){
        this.clientChannel = clientChannel;
        this.clientAddr = clientAddr;
        this.enabledChannel = enabledChannel;
    }
}
