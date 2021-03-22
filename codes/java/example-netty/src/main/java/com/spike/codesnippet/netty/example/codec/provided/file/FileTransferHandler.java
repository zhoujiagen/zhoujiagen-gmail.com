package com.spike.codesnippet.netty.example.codec.provided.file;

import java.io.File;
import java.io.FileInputStream;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <pre>
 * 文件传输支持
 * 
 * a region of a file that is sent via a Channel that supports zero-copy file transfer
 * </pre>
 * 
 * @author zhoujiagen
 * @see io.netty.channel.FileRegion
 */
public class FileTransferHandler extends SimpleChannelInboundHandler<String> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    File file = new File(msg);

    final FileInputStream fis = new FileInputStream(file);
    FileRegion fileRegion = new DefaultFileRegion(fis.getChannel(), 0, file.length());

    ChannelFuture cf = ctx.channel().writeAndFlush(fileRegion);
    cf.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()) {
          System.out.println("Transfer failed: " + future.cause().toString());
        }
        fis.close();
      }
    });
  }
}
