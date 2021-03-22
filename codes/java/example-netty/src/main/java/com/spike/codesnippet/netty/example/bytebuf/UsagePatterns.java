package com.spike.codesnippet.netty.example.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpContent;

import com.google.common.base.Preconditions;
import com.spike.codesnippet.netty.support.Bytes;
import com.spike.codesnippet.netty.support.Nettys;

/**
 * <pre>
 * {@link ByteBuf}的使用模式:
 * 
 * 1 heap
 * 2 direct
 * 3 composite
 * </pre>
 * 
 * @author zhoujiagen
 */
public class UsagePatterns {
  private static final String SENTENCE = "Netty rocks!";

  public static void main(String[] args) {
    heap();
    System.out.println();
    direct();
    System.out.println();
    composite();
    System.out.println();
    holder();
  }

  static void heap() {

    ByteBuf bb = Unpooled.buffer();
    bb.writeCharSequence(SENTENCE, Nettys.UTF_8);
    Preconditions.checkState(!bb.isDirect());
    // 有底层数组
    Preconditions.checkState(bb.hasArray());

    // 访问数据: 直接访问底层数组
    byte[] array = bb.array();
    // 第一个可读的字节
    int offset = bb.arrayOffset() + bb.readerIndex();
    // 可读的字节数量
    int length = bb.readableBytes();

    System.out.println(Bytes.string(array, offset, length));
  }

  static void direct() {
    ByteBuf bb = Unpooled.directBuffer();
    bb.writeCharSequence(SENTENCE, Nettys.UTF_8);
    Preconditions.checkState(bb.isDirect());
    // 无底层数组
    Preconditions.checkState(!bb.hasArray());

    int readerIndexBefore = bb.readerIndex();

    // 访问数据
    int length = bb.readableBytes();
    byte[] array = new byte[length];
    // 不影响readerIndex
    bb.getBytes(bb.readerIndex(), array);
    int readerIndexAfter = bb.readerIndex();
    Preconditions.checkState(readerIndexBefore == readerIndexAfter,
      "get...() should not change readerIndex");
    System.out.println(Bytes.string(array));
    readerIndexAfter = bb.readerIndex();
    Preconditions.checkState(readerIndexBefore == readerIndexAfter,
      "get...() should not change readerIndex");
  }

  static void composite() {
    CompositeByteBuf cbb = Unpooled.compositeBuffer();

    ByteBuf heapByteBuf = Unpooled.buffer();
    Preconditions.checkState(!heapByteBuf.isDirect());
    heapByteBuf.writeCharSequence("Netty ", Nettys.UTF_8);
    ByteBuf directByteBuf = Unpooled.directBuffer();
    Preconditions.checkState(directByteBuf.isDirect());
    directByteBuf.writeCharSequence("rocks!", Nettys.UTF_8);

    // 添加
    boolean increaseWriterIndex = true; // 同时修改CompositeByteBuf#writerIndex
    cbb.addComponents(increaseWriterIndex, heapByteBuf, directByteBuf);
    // 移除
    // cbb.removeComponent(0)
    // 获取底层ByteBuf
    // cbb.component(cIndex)

    int readerIndexBefore = cbb.readerIndex();
    for (ByteBuf bb : cbb) {
      System.out.println(bb.toString());
    }
    int readerIndexAfter = cbb.readerIndex();
    Preconditions.checkState(readerIndexBefore == readerIndexAfter,
      "toString() should not change readerIndex");

    // 访问数据
    int length = cbb.readableBytes();
    Preconditions.checkState(length == SENTENCE.length(), "length(%s) != all length(%s)", length,
      SENTENCE.length());
    byte[] array = new byte[length];
    cbb.getBytes(cbb.readerIndex(), array);
    readerIndexAfter = cbb.readerIndex();
    Preconditions.checkState(readerIndexBefore == readerIndexAfter,
      "get...() should not change readerIndex");
    System.out.println(Bytes.string(array));
  }

  /**
   * 作为消息数据的payload使用
   */
  static void holder() {
    ByteBuf content = Unpooled.copiedBuffer(SENTENCE, Nettys.UTF_8);
    ByteBufHolder holder = new DefaultHttpContent(content);
    Preconditions.checkNotNull(holder.content());
  }
}
