package com.spike.codesnippet.netty.example.transport;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.codesnippet.netty.support.Bytes;
import com.spike.codesnippet.netty.support.Nettys;

/**
 * OIO Server which echo 'Hi!' using Java {@link ServerSocket}
 * @author zhoujiagen
 */
public class PlainOIOServer implements TransportServer {

  private static final Logger LOG = LoggerFactory.getLogger(PlainOIOServer.class);

  private static final ExecutorService ES = Executors.newFixedThreadPool(10);

  public static void main(String[] args) throws IOException {
    new PlainOIOServer().start(Nettys.DEFAULT_HOST, Nettys.DEFAULT_PORT);
  }

  @Override
  public void start(String host, int port) throws IOException {
    try (final ServerSocket serverSocket = new ServerSocket();) {
      serverSocket.bind(Nettys.SOCKET_ADDRESS(host, port));

      while (true) {
        // blocks until a connection is made
        final Socket socket = serverSocket.accept();
        LOG.info("ACCEPT CONNECTION: {}", socket);

        ES.submit(new Runnable() {
          @Override
          public void run() {
            try (OutputStream os = socket.getOutputStream();) {
              os.write(Bytes.wrap("Hi!\r\n"));
              os.flush();
            } catch (IOException e) {
              LOG.error("write to client socket failed", e);
            } finally {
              try {
                socket.close();
              } catch (IOException e) {
                LOG.error("close client socket failed", e);
              }
            }
          }
        });
      }
    }
  }
}
