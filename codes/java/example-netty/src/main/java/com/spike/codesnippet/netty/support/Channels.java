package com.spike.codesnippet.netty.support;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;

/**
 * @author zhoujiagen
 * @see java.nio.channels.Channels
 * @see io.netty.channel.socket.DatagramPacket
 */
@SuppressWarnings("deprecation")
public class Channels {
  // ======================================== properties

  // ======================================== methods
  // socket channels
  public static Class<? extends Channel> nio() {
    return NioSocketChannel.class;
  }

  public static Class<? extends Channel> oio() {
    return OioSocketChannel.class;
  }

  public static Class<? extends Channel> epoll() {
    return EpollSocketChannel.class;
  }

  // server socket channels
  public static Class<? extends ServerChannel> nioserver() {
    return NioServerSocketChannel.class;
  }

  public static Class<? extends ServerChannel> oioserver() {
    return OioServerSocketChannel.class;
  }

  public static Class<? extends ServerChannel> epollserver() {
    return EpollServerSocketChannel.class;
  }

  // data gram channels
  public static Class<? extends Channel> niodatagram() {
    return NioDatagramChannel.class;
  }

  public static Class<? extends Channel> oiodatagram() {
    return OioDatagramChannel.class;
  }

  public static Class<? extends Channel> epolldatagram() {
    return EpollDatagramChannel.class;
  }

  // ======================================== classes
}
