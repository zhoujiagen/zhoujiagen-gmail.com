package com.spike.codesnippet.netty.example.testing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Assert;
import org.junit.Test;

import com.spike.codesnippet.netty.support.ByteBufs;

/**
 * @author zhoujiagen
 * @see EmbeddedChannel#writeOutbound(Object...)
 * @see EmbeddedChannel#readOutbound()
 * @see EmbeddedChannel#finish()
 */
public class AbsoluteIntegerEncoderTest {

  @Test
  public void simple() {
    ByteBuf bb = Unpooled.buffer();
    for (int i = 1; i < 10; i++) {
      bb.writeInt(-1 * i);
    }

    EmbeddedChannel channel = new EmbeddedChannel(new AbsoluteIntegerEncoder());

    // 写outbound数据
    Assert.assertTrue(channel.writeOutbound(bb));
    Assert.assertTrue(channel.finish());

    // 读outbound数据
    for (int i = 1; i < 10; i++) {
      Assert.assertEquals(i, channel.readOutbound());
    }
    Assert.assertNull(channel.readOutbound());
    ByteBufs.RELEASE(bb);
  }
}
