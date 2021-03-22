package com.spike.codesnippet.netty.example.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.codesnippet.netty.support.ByteBufs;
import com.spike.codesnippet.netty.support.Nettys;

/**
 * @author zhoujiagen
 * @see OioEventLoopGroup
 * @see OioServerSocketChannel
 */
@SuppressWarnings("deprecation")
public class NettyOIOServer implements TransportServer {

  private static final Logger LOG = LoggerFactory.getLogger(NettyNIOServer.class);

  public static void main(String[] args) throws IOException {
    new NettyOIOServer().start(Nettys.DEFAULT_HOST, Nettys.DEFAULT_PORT);
  }

  @Override
  public void start(String host, int port) throws IOException {

    ServerBootstrap serverBootstrap = new ServerBootstrap();
    EventLoopGroup eventLoopGroup = new OioEventLoopGroup();

    try {
      final ByteBuf bb = ByteBufs.wrapUnreleasable("Hi!\r\n");

      serverBootstrap.group(eventLoopGroup);
      serverBootstrap.channel(OioServerSocketChannel.class);
      serverBootstrap.localAddress(Nettys.SOCKET_ADDRESS(host, port));
      serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
          ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
              LOG.info("\nChannel ID: {}", ctx.channel().id().asLongText());
              ctx.writeAndFlush(bb.duplicate()).addListener(ChannelFutureListener.CLOSE);
            }
          });
        }
      });

      try {
        serverBootstrap.bind().sync();

        // ChannelFuture cf = serverBootstrap.bind().sync();
        // cf.channel().close().sync();
      } catch (InterruptedException e) {
        LOG.error("", e);
      }
    } finally {
      // try {
      // eventLoopGroup.shutdownGracefully().sync();
      // } catch (InterruptedException e) {
      // LOG.error("", e);
      // }
    }
  }
}
