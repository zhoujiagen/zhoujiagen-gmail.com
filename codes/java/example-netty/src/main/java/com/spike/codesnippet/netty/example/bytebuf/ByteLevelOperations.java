package com.spike.codesnippet.netty.example.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import com.google.common.base.Preconditions;
import com.spike.codesnippet.netty.support.ByteBufs;
import com.spike.codesnippet.netty.support.Bytes;
import com.spike.codesnippet.netty.support.Nettys;

/**
 * {@link ByteBuf}字节层次的操作
 * 
 * <pre>
 * <li>{@link #RANDOM_ACCESS_INDEXING(ByteBuf)}      随机访问索引
 * <li>{@link #SEQUENTIAL_ACCESS_INDEXING(ByteBuf)}  顺序访问索引
 * <li>{@link #DISCARDABLE_BYTES(ByteBuf)}           可忽略的字节
 * <li>{@link #READABLE_BYTES(ByteBuf)}              可读的字节
 * <li>{@link #WRITABLE_BYTES(ByteBuf)}              可写的字节
 * <li>{@link #INDEX_MANAGEMENT(ByteBuf)}            索引管理
 * <li>{@link #SEARCH_OPERATIONS(ByteBuf)}           搜索操作
 * <li>{@link #DERIVED_BUFFERS(ByteBuf)}             视图
 * <li>{@link #READ_WRITE_OPERATIONS(ByteBuf)}       读写操作
 * </pre>
 * 
 * @author zhoujiagen
 */
public class ByteLevelOperations {

  private static final String SENTENCE = "Netty rocks!";
  private static final ByteBuf BYTE_BUF = ByteBufs.wrap(SENTENCE);

  public static void main(String[] args) {
    // before
    System.out.println(ByteBufs.introspect(BYTE_BUF));

    // operations
    // RANDOM_ACCESS_INDEXING(BYTE_BUF);
    // SEQUENTIAL_ACCESS_INDEXING(BYTE_BUF);
    // DISCARDABLE_BYTES(BYTE_BUF);
    // READABLE_BYTES(BYTE_BUF);
    // WRITABLE_BYTES(BYTE_BUF);
    // INDEX_MANAGEMENT(BYTE_BUF);
    // SEARCH_OPERATIONS(BYTE_BUF);
    // DERIVED_BUFFERS(BYTE_BUF);
    READ_WRITE_OPERATIONS(BYTE_BUF);

    // after
    System.out.println(ByteBufs.introspect(BYTE_BUF));
  }

  static void RANDOM_ACCESS_INDEXING(ByteBuf bb) {
    for (int i = 0, len = bb.capacity(); i < len; i++) {
      byte b = bb.getByte(i);
      System.out.println(b + "\t" + (char) b);
    }
  }

  static void SEQUENTIAL_ACCESS_INDEXING(ByteBuf bb) {
    Preconditions.checkState(bb.readerIndex() == 0);
    BYTE_BUF.readByte();
    Preconditions.checkState(bb.readerIndex() == 1);

    Preconditions.checkState(bb.writerIndex() == SENTENCE.length());
    String str = " YES!";
    BYTE_BUF.writeCharSequence(str, Nettys.UTF_8);
    Preconditions.checkState(bb.writerIndex() == SENTENCE.length() + str.length());

    Preconditions.checkState(bb.readerIndex() <= bb.writerIndex());
    Preconditions.checkState(bb.writerIndex() <= bb.capacity());
  }

  static void DISCARDABLE_BYTES(ByteBuf bb) {
    Preconditions.checkState(bb.readerIndex() == 0);
    int n = 5;
    byte[] dst = new byte[n];
    bb.readBytes(dst);
    Preconditions.checkState(bb.readerIndex() == n);

    int beforeWriterIndex = bb.writerIndex();
    bb.discardReadBytes(); // 忽略已读字节
    Preconditions.checkState(bb.readerIndex() == 0);
    Preconditions.checkState(bb.writerIndex() == beforeWriterIndex - n);
  }

  static void READABLE_BYTES(ByteBuf bb) {
    Preconditions.checkState(bb.readerIndex() == 0);

    bb.readByte();
    Preconditions.checkState(bb.readerIndex() == 1);
    bb.skipBytes(1);
    Preconditions.checkState(bb.readerIndex() == 2);

    bb.readerIndex(0); // 重置readerIndex

    String str = "hello";
    ByteBuf dst = ByteBufs.wrap(str);
    System.out.println("INTROSPECT(dst)=\n" + ByteBufs.introspect(dst));

    Preconditions.checkState(dst.writerIndex() == str.length());

    // 目标可写入的字节数量
    int dstWritableBytes = dst.writableBytes();

    bb.readBytes(dst); // 从bb读

    Preconditions.checkState(bb.readerIndex() == dstWritableBytes,
      "bb.readerIndex(%s) != dstWritableBytes(%s)", bb.readerIndex(), dstWritableBytes);
    // 修改dst的writerIndex
    Preconditions.checkState(dst.writerIndex() == dstWritableBytes + str.length(),
      "dst.writerIndex(%s) != len of SENTENCE(%s) + len of str(%s)", dst.writerIndex(),
      SENTENCE.length(), str.length());
  }

  static void WRITABLE_BYTES(ByteBuf bb) {
    int writableBytes = bb.writableBytes(); // 可写的字节数量
    int beforeCapacity = bb.capacity();

    byte[] src = new byte[writableBytes + 1];
    bb.writeBytes(src);
    // 会自动扩容(capacity)
    Preconditions.checkArgument(bb.capacity() > beforeCapacity);
  }

  static void INDEX_MANAGEMENT(ByteBuf bb) {
    // 标记和重置读写索引
    Preconditions.checkState(bb.readerIndex() == 0);
    Preconditions.checkState(bb.writerIndex() == SENTENCE.length());

    bb.markReaderIndex();
    bb.markWriterIndex();
    bb.readByte();
    String str = "hello";
    bb.writeCharSequence(str, Nettys.UTF_8);
    Preconditions.checkState(bb.readerIndex() == 1);
    Preconditions.checkState(bb.writerIndex() == SENTENCE.length() + str.length());

    bb.resetReaderIndex();
    bb.resetWriterIndex();
    Preconditions.checkState(bb.readerIndex() == 0);
    Preconditions.checkState(bb.writerIndex() == SENTENCE.length());

    // 设置索引
    bb.readerIndex(1);
    bb.writerIndex(10);
    Preconditions.checkState(bb.readerIndex() == 1);
    Preconditions.checkState(bb.writerIndex() == 10);

    // 清空
    bb.clear();
    Preconditions.checkState(bb.readerIndex() == 0);
    Preconditions.checkState(bb.writerIndex() == 0);
  }

  static void SEARCH_OPERATIONS(ByteBuf bb) {
    // 搜索
    // ByteProcessor processor = ByteProcessor.FIND_CR;
    ByteProcessor processor = new ByteProcessor() {
      @Override
      public boolean process(byte value) throws Exception {
        // 注意是abort
        return value != Bytes.wrap('!');
      }
    };
    int index = bb.forEachByte(processor);
    Preconditions.checkState(SENTENCE.indexOf("!") == index);
  }

  /**
   * <pre>
   * 1 返回新的实例, 有自己的读写索引, 但共享内部存储
   * {@link ByteBuf#duplicate()}
   * {@link ByteBuf#slice()}
   * {@link ByteBuf#slice(int, int)}
   * {@link ByteBuf#order(java.nio.ByteOrder)}
   * {@link ByteBuf#readSlice(int)}
   * {@link Unpooled#unmodifiableBuffer(ByteBuf)}
   * 
   * 2 深度拷贝
   * {@link ByteBuf#copy()}
   * </pre>
   * 
   * @param bb
   */
  static void DERIVED_BUFFERS(ByteBuf bb) {
    int length = bb.writerIndex() - 1;
    ByteBuf sliced = bb.slice(0, length);
    Preconditions.checkState(sliced.readerIndex() == 0);
    Preconditions.checkState(sliced.writerIndex() == length);

    // 共享内部存储
    sliced.setByte(0, Bytes.wrap('n'));// source: 'N'
    Preconditions.checkState(bb.getByte(0) == sliced.getByte(0));

    // 深度拷贝
    ByteBuf copy = bb.copy();
    copy.setByte(1, Bytes.wrap('n'));// source: 'e'
    Preconditions.checkState(bb.getByte(1) != copy.getByte(1));
  }

  /**
   * <pre>
   * {@link ByteBuf#getByte(int)}和 {@link ByteBuf#setByte(int, int)}不修改读写索引
   * 
   * {@link ByteBuf#readByte()},  {@link ByteBuf#writeByte(int)}, {@link ByteBuf#skipBytes(int)}修改读写索引
   * </pre>
   * 
   * @param bb
   */
  static void READ_WRITE_OPERATIONS(ByteBuf bb) {
    Preconditions.checkState(bb.readerIndex() == 0);
    Preconditions.checkState(bb.writerIndex() == SENTENCE.length());

    // get.../set...
    bb.getByte(0);
    bb.setByte(0, 'c');
    Preconditions.checkState(bb.readerIndex() == 0);
    Preconditions.checkState(bb.writerIndex() == SENTENCE.length());

    // read.../write...
    bb.readByte();
    bb.writeByte('c');
    Preconditions.checkState(bb.readerIndex() == 1);
    Preconditions.checkState(bb.writerIndex() == SENTENCE.length() + 1);
  }
}
