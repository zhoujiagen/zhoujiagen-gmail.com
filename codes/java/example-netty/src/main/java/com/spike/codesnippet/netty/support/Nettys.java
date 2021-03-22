package com.spike.codesnippet.netty.support;

import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class Nettys {
  // private static final Logger LOG = LoggerFactory.getLogger(Nettys.class);

  // ======================================== properties
  /** 默认的主机 */
  public static final String DEFAULT_HOST = "127.0.0.1";
  /** 默认的端口 */
  public static final int DEFAULT_PORT = 8888;
  /** 默认的Socket地址 */
  public static final SocketAddress DEFAULT_ADDRESS = //
      new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT);
  /** 编码UTF_8 */
  public static final Charset UTF_8 = CharsetUtil.UTF_8;

  // ======================================== methods
  /**
   * 获取{@link InetSocketAddress}
   * @param host
   * @param port
   * @return
   */
  public static InetSocketAddress SOCKET_ADDRESS(String host, int port) {
    return new InetSocketAddress(host, port);
  }

}
