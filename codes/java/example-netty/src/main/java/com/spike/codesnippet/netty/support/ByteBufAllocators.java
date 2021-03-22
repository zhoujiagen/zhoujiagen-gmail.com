package com.spike.codesnippet.netty.support;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

public class ByteBufAllocators {
  // ======================================== properties

  // ======================================== methods
  /**
   * pooling {@link ByteBuf}分配器
   * @return
   */
  public static PooledByteBufAllocator POOLED() {
    return new PooledByteBufAllocator();
  }

  /**
   * unpooling {@link ByteBuf}分配器
   * @param preferDirect
   * @return
   */
  public static UnpooledByteBufAllocator UNPOOLED(boolean preferDirect) {
    return new UnpooledByteBufAllocator(preferDirect);
  }
}
