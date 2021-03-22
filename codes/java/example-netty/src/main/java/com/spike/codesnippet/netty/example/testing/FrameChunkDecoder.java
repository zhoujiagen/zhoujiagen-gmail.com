package com.spike.codesnippet.netty.example.testing;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.spike.codesnippet.netty.support.ByteBufs;

/**
 * 限制帧最大长度的{@link ByteToMessageDecoder}
 * @author zhoujiagen
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

  private final int maxLength;

  public FrameChunkDecoder(int maxLength) {
    this.maxLength = maxLength;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    int readableBytes = in.readableBytes();
    if (readableBytes > maxLength) {
      ByteBufs.CLEAR(in);
      throw new IllegalStateException(//
          String.format("readableBytes(%d) > maxLength(%d)", readableBytes, maxLength));
    }

    out.add(in.readBytes(readableBytes));
  }

}
