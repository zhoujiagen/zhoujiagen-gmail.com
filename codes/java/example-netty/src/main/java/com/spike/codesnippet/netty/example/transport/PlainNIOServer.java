package com.spike.codesnippet.netty.example.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.codesnippet.netty.support.ByteBufs;
import com.spike.codesnippet.netty.support.Nettys;

/**
 * NIO Server which echo 'Hi!' use Java {@link ServerSocketChannel} and {@link Selector}
 * @author zhoujiagen
 */
public class PlainNIOServer implements TransportServer {
  private static final Logger LOG = LoggerFactory.getLogger(PlainNIOServer.class);

  public static void main(String[] args) throws IOException {
    new PlainNIOServer().start(Nettys.DEFAULT_HOST, Nettys.DEFAULT_PORT);
  }

  @Override
  public void start(String host, int port) throws IOException {

    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
      serverSocketChannel.configureBlocking(false);// non blocking

      ServerSocket serverSocket = serverSocketChannel.socket();
      serverSocket.bind(Nettys.SOCKET_ADDRESS(host, port));

      try (Selector selector = Selector.open();) {
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        final ByteBuffer msg = ByteBufs.wrapNIO("Hi!\r\n");

        while (true) {
          try {
            selector.select(); // performs a blocking selection operation
          } catch (IOException e) {
            LOG.error("IO ERROR", e);
            break;
          }

          Set<SelectionKey> selectionKeys = selector.selectedKeys();
          Iterator<SelectionKey> it = selectionKeys.iterator();
          SelectionKey selectionKey = null;
          while (it.hasNext()) {
            selectionKey = it.next();
            it.remove(); // remove current key

            // handle selection key represented operation logic
            if (selectionKey.isAcceptable()) {

              // case 1
              ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
              SocketChannel sc = ssc.accept();
              LOG.info("ACCEPT CONNECTION: {}", sc);
              sc.configureBlocking(false);// non blocking
              Object attachment = msg;
              sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, attachment);

            } else if (selectionKey.isWritable()) {

              // case 2
              try (SocketChannel sc = (SocketChannel) selectionKey.channel();) {
                ByteBuffer bb = (ByteBuffer) selectionKey.attachment();
                while (bb.hasRemaining()) {
                  if (sc.write(bb) == 0) {
                    break;
                  }
                }
              }
            }

          }
        }
      }

    } catch (IOException e) {
      LOG.error("Something wrong happened", e);
      throw e;
    }
  }

}
