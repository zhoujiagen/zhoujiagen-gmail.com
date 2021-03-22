package com.spike.codesnippet.netty.example.testing;

import java.util.List;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 固定长度的{@link ByteToMessageDecoder}
 * @author zhoujiagen
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

  private final int length;

  public FixedLengthFrameDecoder(int length) {
    Preconditions.checkArgument(length > 0, "length should great than 0");
    this.length = length;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    while (in.readableBytes() >= length) {
      ByteBuf bb = in.readBytes(length);
      out.add(bb);
    }
  }

}
