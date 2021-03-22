package com.spike.codesnippet.netty.application.mysql.pipeline;

import java.nio.ByteOrder;

import com.spike.codesnippet.netty.application.mysql.NettyMySQLClientConfiguration;
import com.spike.codesnippet.netty.application.mysql.NettyMySQLClientConstants;
import com.spike.codesnippet.netty.application.mysql.handler.NettyMySQLClientConnectionHandler;
import com.spike.codesnippet.netty.application.mysql.handler.NettyMySQLClientResponsePacketHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * MySQL CS协议连接阶段pipeline初始化器.
 */
public class NettyMySQLClientConnectionChannelInitializer extends ChannelInitializer<Channel> {

  public final NettyMySQLClientConfiguration configuration;

  public NettyMySQLClientConnectionChannelInitializer(NettyMySQLClientConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    final ByteOrder byteOrder = NettyMySQLClientConstants.BYTE_ORDER;
    final int maxFrameLength = NettyMySQLClientConstants.PACKET_MAX_SIZE_BYTE;
    final int lengthFieldOffset = 0;
    final int lengthFieldLength = NettyMySQLClientConstants.PACKET_PAYLOAD_LENGTH_BYTE;
    final int lengthAdjustment = NettyMySQLClientConstants.PACKET_SEQUENCE_ID_SIZE_BYTE;
    final int initialBytesToStrip = NettyMySQLClientConstants.PACKET_PAYLOAD_LENGTH_BYTE;
    final boolean failFast = true;

    pipeline.addLast("inbound-1", new LengthFieldBasedFrameDecoder(byteOrder, maxFrameLength,
        lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast));
    pipeline.addLast("inbound-2", new NettyMySQLClientConnectionHandler(configuration));
    pipeline.addLast("inbound-3", new NettyMySQLClientResponsePacketHandler());
  }

}
