package com.spike.codesnippet.netty.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class ChannelFutures {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelFutures.class);

  // ======================================== properties
  public static final ChannelFutureListener CFL_CLOSE = ChannelFutureListener.CLOSE;
  public static final ChannelFutureListener CFL_CLOSE_ON_FAILURE =
      ChannelFutureListener.CLOSE_ON_FAILURE;
  public static final ChannelFutureListener CFL_FIRE_EXCEPTION_ON_FAILURE =
      ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

  // ======================================== methods
  /**
   * 默认的{@ChannelFutureListener实现}
   * @return
   */
  public static ChannelFutureListener DEFAULT_CHANNEL_FUTURE_LISTENER() {
    return new ChannelFutureListener() {

      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          LOG.info("OK: Channel={}, Result={}", //
            future.channel().id().asLongText(), future.get());
        } else {
          LOG.error("Something wrong happened: Channel={}", //
            future.channel().id().asLongText(), future.cause());
        }
      }

    };
  }
}
