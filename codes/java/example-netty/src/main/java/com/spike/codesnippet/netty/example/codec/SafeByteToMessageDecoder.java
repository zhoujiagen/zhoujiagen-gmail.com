package com.spike.codesnippet.netty.example.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

/**
 * 安全的{@link ByteToMessageDecoder}, 过大的帧长会抛出异常.
 * @author zhoujiagen
 * @see TooLongFrameException
 * @see io.netty.handler.codec.DecoderException
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

  // 最大帧长
  private static final int MAX_FRAME_SIZE = 1024;

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    int readableBytes = in.readableBytes();
    if (readableBytes > MAX_FRAME_SIZE) {
      in.skipBytes(readableBytes);
      throw new TooLongFrameException("Frame too long");
    }

    // DO REGUALR THINGS HERE
  }

}
