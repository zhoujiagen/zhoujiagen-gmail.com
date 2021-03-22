package com.spike.codesnippet.netty.example.codec;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * Integer => String encoder
 * @author zhoujiagen
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
    out.add(String.valueOf(msg));
  }

}
