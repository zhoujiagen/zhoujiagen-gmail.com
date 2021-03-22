package com.spike.codesnippet.netty.example.codec;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * Integer => String decoder
 * @author zhoujiagen
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

  @Override
  protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
    out.add(String.valueOf(msg));
  }

}
