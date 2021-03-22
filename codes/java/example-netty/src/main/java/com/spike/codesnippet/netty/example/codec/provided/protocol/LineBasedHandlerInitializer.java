package com.spike.codesnippet.netty.example.codec.provided.protocol;

import com.spike.codesnippet.netty.support.ByteBufs;
import com.spike.codesnippet.netty.support.ChannelHandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 基于行的协议支持
 * @author zhoujiagen
 * @see io.netty.handler.codec.LineBasedFrameDecoder
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new LineBasedFrameDecoder(ChannelHandlers.CONTENT_LENGTH_64B));
    pipeline.addLast(new LineFrameHandler());
  }

  // ======================================== classes

  /** 处理行帧的{@link ChannelHandler} */
  public static final class LineFrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
      System.out.println(ByteBufs.string(msg));
    }
  }
}
