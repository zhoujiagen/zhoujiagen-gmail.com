package com.spike.codesnippet.netty.example.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.codesnippet.netty.support.Nettys;

/**
 * Echo protocol Client with channel handler
 * @author zhoujiagen
 * @see NioEventLoopGroup
 * @see Bootstrap
 * @see ChannelInitializer
 * @see NioSocketChannel
 * @see SimpleChannelInboundHandler
 */
public class EchoClient {
  private static final Logger LOG = LoggerFactory.getLogger(EchoClient.class);

  public static void main(String[] args) {
    new EchoClient().start();
  }

  public void start() {
    EventLoopGroup group = new NioEventLoopGroup();

    Bootstrap bootstrap = new Bootstrap();

    ChannelHandler handler = new ChannelInitializer<Channel>() {
      @Override
      protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new EchoClientHandler());
      }
    };

    bootstrap.group(group);
    bootstrap.channel(NioSocketChannel.class);
    bootstrap.remoteAddress(Nettys.DEFAULT_ADDRESS);
    bootstrap.handler(handler);

    try {
      ChannelFuture future = bootstrap.connect().sync();

      future.channel().closeFuture().sync();

    } catch (InterruptedException e) {
      LOG.error("connect failed", e);
    } finally {
      try {
        group.shutdownGracefully().sync();
      } catch (InterruptedException e) {
        LOG.error("shut down event loop group failed", e);
      }
    }

  }

  @Sharable
  public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
      // logger.info("Client received: " +
      // ByteBufUtil.hexDump(msg.readBytes(msg.readableBytes())));
      LOG.info("Client reviced: " + CharsetUtil.UTF_8.decode(msg.nioBuffer()));
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
      LOG.info("Client send some message...");

      // COMMENT: should flush the message!!!
      context.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
      LOG.error("An exception caught: ", cause);

      context.close();
    }
  }

}
