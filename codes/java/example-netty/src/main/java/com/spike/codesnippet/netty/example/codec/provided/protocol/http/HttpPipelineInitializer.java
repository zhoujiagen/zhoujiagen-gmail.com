package com.spike.codesnippet.netty.example.codec.provided.protocol.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import com.spike.codesnippet.netty.support.ChannelHandlers;

/**
 * HTTP支持
 * @author zhoujiagen
 * @see HttpResponseDecoder
 * @see HttpResponseEncoder
 * @see HttpRequestDecoder
 * @see HttpRequestEncoder
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
  private final boolean client;

  public HttpPipelineInitializer(boolean client) {
    this.client = client;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (client) {
      // CLIENT MODE
      // decode response to application
      pipeline.addLast(ChannelHandlers.DECODER_NAME, new HttpResponseDecoder());
      pipeline.addLast(ChannelHandlers.ENCODER_NAME, new HttpRequestEncoder());
    } else {
      // SERVER MODE
      // decode request to application
      pipeline.addLast(ChannelHandlers.DECODER_NAME, new HttpRequestDecoder());
      pipeline.addLast(ChannelHandlers.ENCODER_NAME, new HttpResponseEncoder());
    }
  }

}
