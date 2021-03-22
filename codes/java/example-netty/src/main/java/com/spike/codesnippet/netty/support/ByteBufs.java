package com.spike.codesnippet.netty.support;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.google.common.base.Strings;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

public class ByteBufs {

  // ======================================== properties

  // ======================================== methods

  /**
   * {@link ByteBuf}的引用计数减1.
   * @param bbs
   * @see io.netty.util.ReferenceCountUtil
   */
  public static void RELEASE(ByteBuf... bbs) {
    if (bbs == null) return;

    for (ByteBuf bb : bbs) {
      if (bb.refCnt() > 0) {
        bb.release();
      }
    }
  }

  /**
   * {@link ByteBuf}的读写索引归0.
   * @param bbs
   */
  public static void CLEAR(ByteBuf... bbs) {
    if (bbs == null) return;

    for (ByteBuf bb : bbs) {
      bb.clear();
    }
  }

  /**
   * 查看{@link ByteBuf}的属性
   * @param bb
   * @return
   * @see ByteBuf#toString()
   */
  public static String introspect(ByteBuf bb) {
    StringBuilder sb = new StringBuilder();

    sb.append(Strings.repeat("=", 50) + "\n");
    sb.append("isDirect=" + bb.isDirect());
    sb.append(", isReadOnly=" + bb.isReadOnly());
    sb.append(", isReadable=" + bb.isReadable());
    sb.append(", isWritable=" + bb.isWritable());

    sb.append("\nreaderIndex=" + bb.readerIndex());
    sb.append(", writerIndex=" + bb.writerIndex());
    sb.append(", capacity=" + bb.capacity());
    sb.append(", maxCapacity=" + bb.maxCapacity());

    sb.append("\nhexDump=" + ByteBufUtil.hexDump(bb));
    if (bb.isReadable()) {
      sb.append(
        "\ncontent=" + bb.getCharSequence(bb.readerIndex(), bb.readableBytes(), CharsetUtil.UTF_8));
    }
    sb.append("\n" + Strings.repeat("=", 50) + "\n");

    return sb.toString();
  }

  /**
   * 将字符串格式消息转换为{@link ByteBuf}
   * @param message
   * @return
   */
  public static ByteBuf wrap(String message) {
    if (message == null) return Unpooled.EMPTY_BUFFER;

    return Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
  }

  /**
   * 将字符串格式消息转换为不可释放{@link ByteBuf}
   * @param message
   * @return
   */
  public static ByteBuf wrapUnreleasable(String message) {
    if (message == null) return Unpooled.unreleasableBuffer(Unpooled.EMPTY_BUFFER);

    return Unpooled.unreleasableBuffer(wrap(message));
  }

  /**
   * 将字符串格式消息转换为{@link ByteBuffer}
   * @param message
   * @return
   */
  public static ByteBuffer wrapNIO(String message) {
    if (message == null) return ByteBuffer.allocate(0);

    return ByteBuffer.wrap(message.getBytes(CharsetUtil.UTF_8));
  }

  /**
   * 将{@link ByteBuf}转换为{@link java.nio.ByteBuffer}
   * <p>
   * 对返回结果的修改不影响原有{@link ByteBuf}
   * @param bb
   * @return
   */
  public static ByteBuffer wrapNIO(ByteBuf bb) {
    if (bb == null) return null;

    return bb.nioBuffer();
  }

  /**
   * 不修改{@link ByteBuf#readerIndex()}和{@link ByteBuf#writerIndex()}, 获取{@link ByteBuf}中字符串格式的内容
   * @param bb
   * @param charset 默认为CharsetUtil.UTF_8
   * @return
   */
  public static String string(final ByteBuf bb, Charset charset) {
    if (bb == null) return StringUtil.EMPTY_STRING;

    if (charset == null) {
      charset = CharsetUtil.UTF_8;
    }
    return bb.toString(charset);
  }

  /**
   * 获取消息的字符串格式内容
   * @param message
   * @return
   * @see #string(ByteBuf, Charset)
   */
  public static String string(final Object message) {
    String result = StringUtil.EMPTY_STRING;
    if (message == null) return result;

    if (message instanceof ByteBuf) {
      ByteBuf msg = (ByteBuf) message;
      result = msg.toString(CharsetUtil.UTF_8);
    } else {
      result = message.toString();
    }

    return result;
  }
}
