package com.spike.codesnippet.netty.application.mysql;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.codesnippet.netty.application.mysql.pipeline.NettyMySQLClientConnectionChannelInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

/**
 * 基于Netty的MySQL客户端.
 */
public class NettyMySQLClient {
  private static final Logger LOG = LoggerFactory.getLogger(NettyMySQLClient.class);

  public static void main(String[] args) {
    final String host = "127.0.0.1";
    final int port = 3306;
    final NettyMySQLClientConfiguration configuration =
        new NettyMySQLClientConfiguration.Builder().host(host).port(port)//
            .username("root").password("admin")//
            .database("test")//
            .heartbeatSQL("SELECT 1")//
            .build();

    final EventLoopGroup group = new NioEventLoopGroup();
    final Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group)//
        .channel(NioSocketChannel.class)//
        .handler(new NettyMySQLClientConnectionChannelInitializer(configuration));

    ChannelFuture future = bootstrap.connect(host, port);
    future.addListener(new NettyMySQLClientChannelFutureListener());

    // try {
    // System.in.read();
    // } catch (IOException e1) {
    // e1.printStackTrace();
    // }
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        Future<?> shutdownFuture = group.shutdownGracefully();
        try {
          shutdownFuture.syncUninterruptibly().get();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        }
      }
    }));

  }

  static class NettyMySQLClientChannelFutureListener implements ChannelFutureListener {

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
      if (future.isSuccess()) {
        LOG.info("Connection established!");
      } else {
        LOG.error("Connection attempt failed!", future.cause());
      }
    }
  }

}
