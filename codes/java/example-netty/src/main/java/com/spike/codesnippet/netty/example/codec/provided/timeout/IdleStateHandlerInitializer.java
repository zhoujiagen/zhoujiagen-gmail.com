package com.spike.codesnippet.netty.example.codec.provided.timeout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import com.spike.codesnippet.netty.support.ByteBufs;
import com.spike.codesnippet.netty.support.ChannelFutures;

/**
 * 停顿的连接和超时支持
 * @author zhoujiagen
 * @see io.netty.handler.timeout.IdleStateHandler
 * @see io.netty.channel.ChannelInboundHandler#userEventTriggered(io.netty.channel.
 *      ChannelHandlerContext, Object)
 * @see io.netty.handler.timeout.ReadTimeoutHandler
 * @see io.netty.handler.timeout.WriteTimeoutHandler
 * @see io.netty.channel.ChannelInboundHandler#exceptionCaught(io.netty.channel.ChannelHandlerContext,
 *      Throwable)
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    long readerIdleTime = 0; // Specify 0 to disable
    long writerIdleTime = 0; // Specify 0 to disable
    long allIdleTime = 60; // Specify 0 to disable
    pipeline.addLast(//
      new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.SECONDS));

    pipeline.addLast(new HeartbeatHandler());
  }

  // ======================================== classes

  /** 心跳{@link ChannelHandler} */
  public static final class HeartbeatHandler extends ChannelDuplexHandler {
    private static final ByteBuf HEARTBEAT_MSG = ByteBufs.wrapUnreleasable("HEARTBEAT");

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      if (evt instanceof IdleStateEvent) {
        // 续命, 发送失败时关闭连接
        ctx.writeAndFlush(HEARTBEAT_MSG.duplicate())//
            .addListener(ChannelFutures.CFL_CLOSE_ON_FAILURE);
      } else {
        super.userEventTriggered(ctx, evt);
      }
    }
  }

}
