package com.spike.codesnippet.netty.example.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <pre>
 * Byte => Integer decoder
 * 
 * 1 integer is 4 bytes
 * </pre>
 * 
 * @author zhoujiagen
 */
public class ByteToIntegerDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (in.readableBytes() >= 4) {
      out.add(in.readInt());
    }
  }

}
