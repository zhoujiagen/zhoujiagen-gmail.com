package com.spike.codesnippet.netty.example.transport;

import java.io.IOException;

public interface TransportServer {
  /**
   * 启动Server
   * @param host
   * @param port
   */
  void start(String host, int port) throws IOException;
}
