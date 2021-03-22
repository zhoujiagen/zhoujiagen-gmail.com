package com.spike.codesnippet.netty.example.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.codesnippet.netty.support.Nettys;

/**
 * Echo protocol Server with channel handler
 * @author zhoujiagen
 * @see NioEventLoopGroup
 * @see ServerBootstrap
 * @see ChannelInitializer
 * @see NioServerSocketChannel
 * @see ChannelInboundHandlerAdapter
 * @see ChannelFutureListener
 */
public class EchoServer {
  private static final Logger LOG = LoggerFactory.getLogger(EchoServer.class);

  public static void main(String[] args) {
    new EchoServer().start();
  }

  public void start() {
    // NIO transport
    EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    ServerBootstrap bootstrap = new ServerBootstrap();

    ChannelHandler channelHandler = new ChannelInitializer<Channel>() {
      @Override
      protected void initChannel(Channel ch) throws Exception {
        // add handler to channel pipeline
        ch.pipeline().addLast(new EchoServerHandler());
      }
    };

    // 设置ServerSocketChannel的选项
    // bootstrap.option(option, value);
    // 设置SocketChannel的选项
    // bootstrap.childOption(childOption, value)

    bootstrap.group(eventLoopGroup);
    bootstrap.channel(NioServerSocketChannel.class);
    bootstrap.localAddress(Nettys.DEFAULT_ADDRESS);
    // 设置处理请求的ChannelHandler
    // bootstrap.handler(handler);
    // 设置处理Channel中请求的ChannelHandler
    bootstrap.childHandler(channelHandler);

    try {
      ChannelFuture channelFuture = bootstrap.bind().sync();
      LOG.info(EchoServer.class.getName() + " started and listen on "
          + channelFuture.channel().localAddress());

      channelFuture.channel().closeFuture().sync();

    } catch (InterruptedException e) {
      LOG.error("bind failed", e);
    } finally {
      try {
        eventLoopGroup.shutdownGracefully().sync();
      } catch (InterruptedException e) {
        LOG.error("shut down event loop group failed", e);
      }
    }

  }

  // @Sharable标明该ChannelHandler可以安全的被不同的Channel共享
  @Sharable
  public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context, Object messge) {
      ByteBuf msg = (ByteBuf) messge; // PooledUnsafeDirectByteBuf

      LOG.info("Server received: {}", msg.toString(CharsetUtil.UTF_8));

      // echo back
      context.write(messge);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
      context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
      LOG.error("an exception caught", cause);

      context.close();
    }
  }
}
