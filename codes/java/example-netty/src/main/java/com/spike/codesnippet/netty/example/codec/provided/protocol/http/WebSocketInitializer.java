package com.spike.codesnippet.netty.example.codec.provided.protocol.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import com.spike.codesnippet.netty.support.ChannelHandlers;

/**
 * WebSocket支持
 * @author zhoujiagen
 */
public class WebSocketInitializer extends ChannelInitializer<Channel> {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ch.pipeline().addLast(//
      new HttpServerCodec(), //
      new HttpObjectAggregator(ChannelHandlers.CONTENT_LENGTH_64B), //
      new WebSocketServerProtocolHandler("/websocket"), //
      new TextWebSocketFrameHandler(), //
      new BinaryWebSocketFrameHandler(), //
      new ContinuationWebSocketFrameHandler());
  }

  // ======================================== classes
  // BinaryWebSocketFrame
  public static final class BinaryWebSocketFrameHandler
      extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg)
        throws Exception {
      System.out.println(ChannelHandlers.WebSocketFrameConverters.convert(msg, false));
    }

  }

  // TextWebSocketFrame
  public static final class TextWebSocketFrameHandler
      extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
        throws Exception {
      System.out.println(ChannelHandlers.WebSocketFrameConverters.convert(msg, false));
    }

  }

  // ContinuationWebSocketFrame
  public static final class ContinuationWebSocketFrameHandler
      extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg)
        throws Exception {
      System.out.println(ChannelHandlers.WebSocketFrameConverters.convert(msg, false));
    }

  }
}
