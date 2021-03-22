package com.spike.codesnippet.netty.example.codec.provided.protocol.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import com.spike.codesnippet.netty.support.ChannelHandlers;

/**
 * HTTP消息聚合支持
 * @author zhoujiagen
 * @see io.netty.handler.codec.http.HttpClientCodec
 * @see io.netty.handler.codec.http.HttpServerCodec
 * @see io.netty.handler.codec.http.HttpObjectAggregator
 */
public class HttpMessageAggregatorInitializer extends ChannelInitializer<Channel> {
  private final boolean client;

  public HttpMessageAggregatorInitializer(boolean client) {
    this.client = client;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (client) {
      // CLIENT MODE
      pipeline.addLast(ChannelHandlers.CODEC_NAME, new HttpClientCodec());
    } else {
      // SERVER MODE
      pipeline.addLast(ChannelHandlers.CODEC_NAME, new HttpServerCodec());
    }

    pipeline.addLast(ChannelHandlers.AGGREGATOR_NAME, //
      new HttpObjectAggregator(ChannelHandlers.CONTENT_LENGTH_512B));
  }
}
