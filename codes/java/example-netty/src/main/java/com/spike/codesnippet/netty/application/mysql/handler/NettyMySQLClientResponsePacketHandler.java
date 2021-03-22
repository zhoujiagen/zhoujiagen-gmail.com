package com.spike.codesnippet.netty.application.mysql.handler;

import com.spike.codesnippet.netty.application.mysql.packet.MySQLPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 */
public class NettyMySQLClientResponsePacketHandler extends SimpleChannelInboundHandler<ByteBuf> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    int sequenceId = msg.readByte();
    int payloadLength = msg.readableBytes();
    MySQLPacket packet = new MySQLPacket(payloadLength, sequenceId);
    packet.setPayload(msg);
    System.err.println(packet);
  }

}
