package com.spike.codesnippet.netty.example.testing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Assert;
import org.junit.Test;

import com.spike.codesnippet.netty.support.ByteBufs;

public class FrameChunkDecoderTest {
  @Test
  public void simple() {

    ByteBuf bb = Unpooled.buffer();
    for (int i = 1; i < 10; i++) {
      bb.writeInt(i);
    }

    EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

    // 写inbound数据
    Assert.assertTrue(channel.writeInbound(bb.readBytes(2)));
    try {
      channel.writeInbound(bb.readBytes(4));
      Assert.fail(); // 期望异常出现
    } catch (Exception e) {
      e.printStackTrace();
    }
    Assert.assertTrue(channel.writeInbound(bb.readBytes(3)));

    Assert.assertTrue(channel.finish());

    // 读inbound数据
    ByteBuf read = channel.readInbound();
    ByteBuf copy = read.copy(0, 2);
    Assert.assertEquals(read, copy);
    ByteBufs.RELEASE(read, copy);

    read = channel.readInbound();
    copy = read.copy(0, 3);
    Assert.assertEquals(read, copy);
    ByteBufs.RELEASE(read, copy);

    Assert.assertNull(channel.readInbound());
    ByteBufs.RELEASE(bb);

  }
}
