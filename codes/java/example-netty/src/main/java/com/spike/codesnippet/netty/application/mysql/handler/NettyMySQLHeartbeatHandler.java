package com.spike.codesnippet.netty.application.mysql.handler;

import com.spike.codesnippet.netty.application.mysql.NettyMySQLClientConfiguration;
import com.spike.codesnippet.netty.support.Bytes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;

/**
 * HEARTBEAR, 例: SELECT 1.
 */
public class NettyMySQLHeartbeatHandler extends ChannelInboundHandlerAdapter {

  private String sql = "SELECT 1";

  public NettyMySQLHeartbeatHandler(NettyMySQLClientConfiguration configuration) {
    if (!StringUtil.isNullOrEmpty(configuration.getHeartbeatSQL())) {
      this.sql = configuration.getHeartbeatSQL();
    }
  }

  // private static final ByteBuf HEARTBEAT =
  // Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(SQL.getBytes(), new byte[] { 0x00 }));

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    if (evt instanceof IdleStateEvent) {

      // payload_length = 28
      // payload_length_bytes = payload_length.to_bytes(3, byteorder='little')
      // result += payload_length_bytes
      // # sequence_id
      // result += b'\x00'
      // # int<1>
      // command = b'\x03'
      // result += command
      // # string<EOF>
      // query = 'SELECT * FROM test.example' # 26
      // result += query.encode('utf-8')
      // result += b'\x00'

      ByteBuf packet = Unpooled.buffer();
      packet.writeMediumLE(sql.length() + 2);
      packet.writeByte(0x00);
      packet.writeByte(0x03);
      packet.writeBytes(Bytes.nullEndString(sql));
      System.err.println(ByteBufUtil.prettyHexDump(packet));

      ctx.writeAndFlush(packet).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

}
