package com.spike.codesnippet.netty.support;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import java.net.SocketAddress;

public class Bootstraps {
  // ======================================== properties

  // ======================================== methods

  // ======================================== classes
  /**
   * 创建服务端bootstrap
   * @param eventLoopGroup
   * @param channelClass
   * @param localAddress
   * @param handler MAY null
   * @param childHandler MAY null
   * @return
   */
  public static ServerBootstrap SERVER(//
      EventLoopGroup eventLoopGroup, //
      Class<? extends ServerChannel> channelClass, //
      SocketAddress localAddress, //
      ChannelHandler handler, //
      ChannelHandler childHandler//
  ) {
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    serverBootstrap.group(eventLoopGroup);
    serverBootstrap.channel(channelClass);
    serverBootstrap.localAddress(localAddress);
    if (handler != null) {
      serverBootstrap.handler(handler);
    }
    if (childHandler != null) {
      serverBootstrap.childHandler(childHandler);
    }
    return serverBootstrap;
  }

  public static Bootstrap CLIENT(//
      EventLoopGroup eventLoopGroup, //
      Class<? extends Channel> channelClass, //
      SocketAddress remoteAddress, //
      ChannelHandler handler) {
    Bootstrap serverBootstrap = new Bootstrap();

    serverBootstrap.group(eventLoopGroup);
    serverBootstrap.channel(channelClass);
    serverBootstrap.remoteAddress(remoteAddress);
    if (handler != null) {
      serverBootstrap.handler(handler);
    }

    return serverBootstrap;
  }

}
