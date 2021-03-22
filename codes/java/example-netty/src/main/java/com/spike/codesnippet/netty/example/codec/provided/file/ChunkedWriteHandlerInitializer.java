package com.spike.codesnippet.netty.example.codec.provided.file;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 将文件由操作系统拷贝到内存中支持: 异步的写大文件流
 * @author zhoujiagen
 * @see io.netty.handler.stream.ChunkedWriteHandler
 * @see io.netty.handler.stream.ChunkedStream
 * @see ChunkedFile
 * @see ChunkedNioFile
 * @see ChunkedNioStream
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

  private final File file;
  private final SslContext sslContext;

  public ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
    this.file = file;
    this.sslContext = sslContext;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new SslHandler(sslContext.newEngine(ch.alloc())));
    pipeline.addLast(new ChunkedWriteHandler());
    pipeline.addLast(new WriteStreamHandler());
  }

  // ======================================== classes
  public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      // 连接建立时写数据
      ctx.fireChannelActive();
      ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
    }
  }

}
