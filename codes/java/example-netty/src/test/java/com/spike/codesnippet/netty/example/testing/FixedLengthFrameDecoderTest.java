package com.spike.codesnippet.netty.example.testing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Assert;
import org.junit.Test;

import com.spike.codesnippet.netty.support.ByteBufs;

/**
 * @author zhoujiagen
 * @see EmbeddedChannel#writeInbound(Object...)
 * @see EmbeddedChannel#readInbound()
 * @see EmbeddedChannel#finish()
 */
public class FixedLengthFrameDecoderTest {

  private static final int LENGTH = 3;

  @Test
  public void simple() {
    ByteBuf bb = Unpooled.buffer();
    for (int i = 0; i < 9; i++) {
      bb.writeByte(i);
    }

    // 共享内容, 不共享index和mark
    ByteBuf inputBB = bb.duplicate();

    EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(LENGTH));

    // 写inbound数据
    Assert.assertTrue(channel.writeInbound(inputBB.retain())); // 引用计数+1
    Assert.assertTrue(channel.finish()); // 标记为完成, 后续写入数据会失败

    // 读inbound数据
    ByteBuf read = channel.readInbound();
    ByteBuf copy = read.copy(0, LENGTH);
    Assert.assertEquals(copy, read);
    ByteBufs.RELEASE(read, copy);

    read = channel.readInbound();
    copy = read.copy(0, LENGTH);
    Assert.assertEquals(copy, read);
    ByteBufs.RELEASE(read, copy);

    read = channel.readInbound();
    copy = read.copy(0, LENGTH);
    Assert.assertEquals(copy, read);
    ByteBufs.RELEASE(read, copy);

    Assert.assertNull(channel.readInbound());
    ByteBufs.RELEASE(bb, inputBB);
  }

  @Test
  public void complicated() {

    ByteBuf bb = Unpooled.buffer();
    for (int i = 0; i < 9; i++) {
      bb.writeByte(i);
    }

    // 共享内容, 不共享index和mark
    ByteBuf inputBB = bb.duplicate();

    EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(LENGTH));

    // 写inbound数据
    // `true` if the write operation did add something to the inbound buffer
    Assert.assertFalse(channel.writeInbound(inputBB.readBytes(2)));
    Assert.assertTrue(channel.writeInbound(inputBB.readBytes(7)));
    Assert.assertTrue(channel.finish()); // 标记为完成, 后续写入数据会失败

    // 读inbound数据
    ByteBuf read = channel.readInbound();
    ByteBuf copy = read.copy(0, LENGTH);
    Assert.assertEquals(copy, read);
    ByteBufs.RELEASE(read, copy);

    read = channel.readInbound();
    copy = read.copy(0, LENGTH);
    Assert.assertEquals(copy, read);
    ByteBufs.RELEASE(read, copy);

    read = channel.readInbound();
    copy = read.copy(0, LENGTH);
    Assert.assertEquals(copy, read);
    ByteBufs.RELEASE(read, copy);

    Assert.assertNull(channel.readInbound());
    ByteBufs.RELEASE(bb, inputBB);
  }
}
