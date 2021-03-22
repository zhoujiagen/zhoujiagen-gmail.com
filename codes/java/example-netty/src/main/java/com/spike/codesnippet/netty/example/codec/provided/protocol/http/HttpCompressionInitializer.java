package com.spike.codesnippet.netty.example.codec.provided.protocol.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

import com.spike.codesnippet.netty.support.ChannelHandlers;

/**
 * HTTP压缩支持
 * @author zhoujiagen
 * @see io.netty.handler.codec.http.HttpContentDecompressor
 * @see io.netty.handler.codec.http.HttpContentCompressor
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
  private final boolean client;

  public HttpCompressionInitializer(boolean client) {
    this.client = client;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {

    ChannelPipeline pipeline = ch.pipeline();
    if (client) {
      // CLIENT MODE
      pipeline.addLast(ChannelHandlers.CODEC_NAME, new HttpClientCodec());
      // 解压
      pipeline.addLast(ChannelHandlers.DECOMPRESSOR_NAME, new HttpContentDecompressor());
    } else {
      // SERVER MODE
      pipeline.addLast(ChannelHandlers.CODEC_NAME, new HttpServerCodec());
      // 压缩
      pipeline.addLast(ChannelHandlers.COMPRESSOR_NAME, new HttpContentCompressor());
    }
  }

}
