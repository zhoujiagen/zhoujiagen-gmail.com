package com.spike.codesnippet.netty.application.mysql.handler;

import java.util.concurrent.TimeUnit;

import com.spike.codesnippet.netty.application.mysql.NettyMySQLClientConfiguration;
import com.spike.codesnippet.netty.application.mysql.packet.MySQLHandshakeResponse41;
import com.spike.codesnippet.netty.application.mysql.packet.MySQLHandshakeV10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 */
public class NettyMySQLClientConnectionHandler extends SimpleChannelInboundHandler<ByteBuf> {

  public final NettyMySQLClientConfiguration configuration;

  public NettyMySQLClientConnectionHandler(NettyMySQLClientConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    int sequenceId = msg.readByte();
    int payloadLength = msg.readableBytes();
    MySQLHandshakeV10 handshakeV10 = new MySQLHandshakeV10(payloadLength, sequenceId, msg);
    System.err.println(handshakeV10);

    byte[] authPluginData = handshakeV10.getAuthPluginData();
    System.err.println(new String(authPluginData));

    // construct login request
    MySQLHandshakeResponse41 handshakeResponse41 = new MySQLHandshakeResponse41(
        configuration.getSequenceId().incrementAndGet(), configuration, authPluginData);
    // configuration.getSequenceId().incrementAndGet();
    System.err.println(handshakeResponse41);
    ctx.writeAndFlush(handshakeResponse41.raw());

    // remove connection handler
    ctx.pipeline().remove(NettyMySQLClientConnectionHandler.class);
    // ctx.pipeline().addFirst("outbound-4", new NettyMySQLClientRequestPacketHandler());
    ctx.pipeline().addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
    ctx.pipeline().addLast(new NettyMySQLHeartbeatHandler(configuration));

    System.err.println(ctx.pipeline());
  }

}
