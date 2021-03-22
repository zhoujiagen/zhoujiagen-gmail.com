package com.spike.codesnippet.netty.example.bytebuf;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;

/**
 * 引用计数
 * @author zhoujiagen
 * @see ReferenceCounted
 */
public class ReferenceCouting {
  public static void main(String[] args) {
    ByteBuf bb = Unpooled.buffer();
    Preconditions.checkState(bb.refCnt() == 1);

    boolean released = bb.release();
    Preconditions.checkState(released);
    Preconditions.checkState(bb.refCnt() == 0);
  }
}
