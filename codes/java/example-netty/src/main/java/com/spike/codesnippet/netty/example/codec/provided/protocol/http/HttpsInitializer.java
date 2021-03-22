package com.spike.codesnippet.netty.example.codec.provided.protocol.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

import com.spike.codesnippet.netty.support.ChannelHandlers;

/**
 * HTTPS支持
 * @author zhoujiagen
 * @see io.netty.handler.ssl.SslHandler
 */
public class HttpsInitializer extends ChannelInitializer<Channel> {
  private final SslContext sslContext;
  private final boolean client;

  public HttpsInitializer(SslContext sslContext, boolean client) {
    this.sslContext = sslContext;
    this.client = client;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    SSLEngine sslEngine = sslContext.newEngine(ch.alloc());

    ChannelPipeline pipeline = ch.pipeline();

    // ADD FIRST IN THE PIPELINE
    pipeline.addFirst(ChannelHandlers.SSL_NAME, new SslHandler(sslEngine));

    if (client) {
      // CLIENT MODE
      pipeline.addLast(ChannelHandlers.CODEC_NAME, new HttpClientCodec());
    } else {
      // SERVER MODE
      pipeline.addLast(ChannelHandlers.CODEC_NAME, new HttpServerCodec());
    }

  }

}