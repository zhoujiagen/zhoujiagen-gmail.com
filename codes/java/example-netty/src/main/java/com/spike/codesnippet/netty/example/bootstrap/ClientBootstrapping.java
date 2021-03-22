package com.spike.codesnippet.netty.example.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import com.spike.codesnippet.netty.support.Bootstraps;
import com.spike.codesnippet.netty.support.ChannelFutures;
import com.spike.codesnippet.netty.support.ChannelHandlers;
import com.spike.codesnippet.netty.support.Channels;
import com.spike.codesnippet.netty.support.EventLoops;
import com.spike.codesnippet.netty.support.Nettys;

public class ClientBootstrapping {
  public static void main(String[] args) {
    EventLoopGroup elg = EventLoops.nio();
    Bootstrap bootstrap =
        Bootstraps.CLIENT(elg, Channels.nio(), Nettys.DEFAULT_ADDRESS, ChannelHandlers.SIMPLE());

    ChannelFuture channelFuture = bootstrap.connect();

    channelFuture.addListener(ChannelFutures.DEFAULT_CHANNEL_FUTURE_LISTENER());

    io.netty.util.concurrent.Future<?> f = elg.shutdownGracefully();
    f.syncUninterruptibly();
  }
}
