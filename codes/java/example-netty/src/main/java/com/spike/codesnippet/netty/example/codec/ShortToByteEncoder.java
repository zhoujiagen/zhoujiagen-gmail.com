package com.spike.codesnippet.netty.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <pre>
 * Short => Byte encoder
 * 
 * 1 short is 2 bytes
 * </pre>
 * 
 * @author zhoujiagen
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
    out.writeShort(msg);
  }

}
