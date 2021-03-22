package com.spike.codesnippet.netty.example.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

import com.spike.codesnippet.netty.support.ChannelHandlers.SimpleWebSocketFrame;
import com.spike.codesnippet.netty.support.ChannelHandlers.WebSocketFrameConverters;

/**
 * <pre>
 * WebSocketFrame <=> WebSocketFrameCodec.SimpleWebSocketFrame
 * 
 * 注意: 在encoded/decoded消息时, {@link MessageToMessageCodec}会调用<code>ReferenceCounted.release()</code>.
 * </pre>
 * 
 * @author zhoujiagen
 */
public class WebSocketFrameCodec
    extends MessageToMessageCodec<WebSocketFrame, SimpleWebSocketFrame> {

  @Override
  protected void encode(ChannelHandlerContext ctx, SimpleWebSocketFrame msg, List<Object> out)
      throws Exception {
    // => WebSocketFrame
    out.add(WebSocketFrameConverters.convert(msg, true));
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
      throws Exception {
    // => WebSocketFrameCodec.SimpleWebSocketFrame
    out.add(WebSocketFrameConverters.convert(msg, true));
  }

}
