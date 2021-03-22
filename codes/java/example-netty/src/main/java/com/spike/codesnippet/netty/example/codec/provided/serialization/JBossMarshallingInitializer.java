package com.spike.codesnippet.netty.example.codec.provided.serialization;

import java.io.Serializable;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * JBoss序列化支持
 * @author zhoujiagen
 * @see MarshallingDecoder
 * @see MarshallingEncoder
 */
public class JBossMarshallingInitializer extends ChannelInitializer<Channel> {
  private final MarshallerProvider marshallerProvider;
  private final UnmarshallerProvider unmarshallerProvider;

  public JBossMarshallingInitializer(MarshallerProvider marshallerProvider,
      UnmarshallerProvider unmarshallerProvider) {
    this.marshallerProvider = marshallerProvider;
    this.unmarshallerProvider = unmarshallerProvider;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
    pipeline.addLast(new MarshallingEncoder(marshallerProvider));

    pipeline.addLast(new SerializableHandler());
  }

  // ======================================== classes

  public static final class SerializableHandler extends SimpleChannelInboundHandler<Serializable> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
      System.out.println(msg);
    }
  }

}
