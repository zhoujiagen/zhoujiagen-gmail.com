package com.spike.codesnippet.netty.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @see ByteToIntegerDecoder
 * @see io.netty.handler.codec.ReplayingDecoderByteBuf
 */
public class AlternativeByteToIntegerDecoder extends ReplayingDecoder<Integer> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    // NO NEED TO CHECK READABLE BYTES COUNT
    out.add(in.readInt());
  }
}
