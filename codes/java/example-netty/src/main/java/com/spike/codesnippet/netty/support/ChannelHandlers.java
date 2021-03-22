package com.spike.codesnippet.netty.support;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.spike.codesnippet.netty.support.ChannelHandlers.SimpleWebSocketFrame.FrameType;

public class ChannelHandlers {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelHandlers.class);

  // ======================================== properties

  public static final String SSL_NAME = "ssl";
  public static final String DECODER_NAME = "decoder";
  public static final String ENCODER_NAME = "encoder";
  public static final String CODEC_NAME = "codec";
  public static final String AGGREGATOR_NAME = "aggregator";
  public static final int CONTENT_LENGTH_512B = 512 * 1024;
  public static final int CONTENT_LENGTH_64B = 64 * 1024;
  public static final String DECOMPRESSOR_NAME = "decompressor";
  public static final String COMPRESSOR_NAME = "compressor";

  // ======================================== methods

  /**
   * 依次将{@link ChannelHandler}添加到{@link ChannelPipeline}中, 并返回{@link ChannelHandler}
   * @param handlers
   * @return
   */
  public static ChannelHandler CONSTRUCT(final ChannelHandler... handlers) {
    ChannelHandler channelHandler = new ChannelInitializer<Channel>() {
      @Override
      protected void initChannel(Channel ch) throws Exception {
        // add handler to channel pipeline
        ch.pipeline().addLast(handlers);
      }
    };

    return channelHandler;
  }

  public static ChannelHandler SIMPLE() {
    return new SimpleChannelInboundHandler<ByteBuf>() {
      @Override
      protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        LOG.info("RECEIVE DATA: " + ByteBufs.introspect(msg));
      }
    };
  }

  public static <T> ChannelHandler SPAWN_BOOTSTRAP(String remoteHost, int remotePort,
      NettyCallable<ByteBuf, T> callable) {
    return new ChannelHandlerBootstrap<T>(remoteHost, remotePort, callable);
  }

  // ======================================== classes
  /**
   * 覆盖了{@link ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}的实现, 不需要将消息传递给
   * {@link ChannelPipeline}中下一个 {@link ChannelHandler}时需要释放消息资源.
   * <p>
   * 一般位于{@link ChannelPipeline} inbound的末尾.
   */
  @Sharable
  public static class DiscardChannelInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      // RELEASE POOLED BYTEBUF
      ReferenceCountUtil.release(msg);

      // DO NOT PASS TO NEXT CHANNELHANDLER
      // ctx.fireChannelRead(msg);
    }
  }

  /**
   * {@link SimpleChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}会自动释放消息资源.
   * @see DiscardChannelInboundHandler
   */
  @Sharable
  public static class AlternativeDiscardChannelInboundHandler
      extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      // DO NOTHING
    }
  }

  /**
   * <p>
   * 一般位于{@link ChannelPipeline} outbound的末尾.
   */
  @Sharable
  public static class DiscardChannelOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
        throws Exception {
      // RELEASE POOLED BYTEBUF
      ReferenceCountUtil.release(msg);
      // NOTIFY CHANNELPROMISE/CHANNELFUTURELISTENR
      promise.setSuccess();
    }
  }

  /**
   * 缓存了{@link ChannelHandlerContext}的{@link ChannelHandler}
   * <p>
   * 因{@link ChannelHandlerContext}在{@link ChannelHandler}添加到{@link ChannelPipeline}时创建,一旦创建便不再改变.
   * <p>
   * 因{@link ChannelHandler}添加到多个{@link ChannelPipeline}中, 会关联多个{@link ChannelHandlerContext},
   * 所以这里不能添加{@link Sharable}注解.
   */
  public static class CacheContextChannelHandler extends ChannelHandlerAdapter {
    private ChannelHandlerContext context;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      this.context = ctx;
    }

    public void send(Object msg) {
      this.context.writeAndFlush(msg);
    }
  }

  /**
   * 处理inbound异常.
   * <p>
   * 默认的{@link ChannelHandler}处理异常实现会将异常传递给下一个{@link ChannelHandler}, 如果没有处理, Netty会记录日志.
   */
  public static class InboundExceptionChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      // DO NOT PASS TO NEXT CHANNELHANDLER
      // ctx.fireExceptionCaught(cause);
      // LOGGING THE EXCEPTION
      LOG.error("Something wrong happened", cause);
      // CLOSE THE CHANNEL
      ctx.close();
    }
  }

  /**
   * 处理outbound异常.
   * <p>
   * 另一种方式是在{@link ChannelFuture}上添加{@link ChannelFutureListener}
   */
  public static class OutboundExceptionChannelHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
        throws Exception {
      // ADD CHANNEL FUTURE LISTENER
      promise.addListener(new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
          if (!future.isSuccess()) {
            // LOGGING EXCEPTION
            LOG.error("Something wrong happened", future.cause());
            // CLOSE THE CHANNEL
            future.channel().close();
          }
        }
      });
      ctx.write(msg, promise);
    }
  }

  /**
   * 在{@link Channel}中spawn {@link Bootstrap}的{@link ChannelHandler}.
   */
  public static class ChannelHandlerBootstrap<T> extends SimpleChannelInboundHandler<ByteBuf> {

    private SocketAddress remoteAddress;
    private ChannelFuture channelFuture;
    private NettyCallable<ByteBuf, T> callable;

    public ChannelHandlerBootstrap(String remoteHost, int remotePort,
        NettyCallable<ByteBuf, T> callable) {
      Preconditions.checkNotNull(remoteHost);
      remoteAddress = Nettys.SOCKET_ADDRESS(remoteHost, remotePort);
      this.callable = callable;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      // SPAWN BOOTSTRAP IN A CHANNEL
      Bootstrap bootstrap = Bootstraps.CLIENT(ctx.channel().eventLoop(), Channels.nio(),
        remoteAddress, ChannelHandlers.SIMPLE());
      channelFuture = bootstrap.connect();
      // FOR DEBUG
      channelFuture.addListener(ChannelFutures.DEFAULT_CHANNEL_FUTURE_LISTENER());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
      if (channelFuture.isDone()) {
        T value = callable.call(ctx, msg);
        LOG.info("RESULT: {}", value.toString());
      }

    }
  }

  /**
   * @param MSG message
   * @param RES result
   */
  public static interface NettyCallable<MSG, RES> {
    RES call(ChannelHandlerContext ctx, MSG msg);
  }

  /**
   * {@link WebSocketFrame}与{@link SimpleWebSocketFrame}的转换工具类
   * @author zhoujiagen
   */
  public static final class WebSocketFrameConverters {
    public static WebSocketFrame convert(SimpleWebSocketFrame simpleWebSocketFrame,
        boolean retain) {
      if (simpleWebSocketFrame == null) return null;

      WebSocketFrame result = null;

      FrameType frameType = simpleWebSocketFrame.getFrameType();
      ByteBuf payload = simpleWebSocketFrame.getData().duplicate();
      if (retain) {
        payload.retain();
      }

      if (FrameType.BINARY.equals(frameType)) {
        result = new BinaryWebSocketFrame(payload);
      } else if (FrameType.TEXT.equals(frameType)) {
        result = new TextWebSocketFrame(payload);
      } else if (FrameType.CLOSE.equals(frameType)) {
        result = new CloseWebSocketFrame(true, 0, payload);
      } else if (FrameType.CONTINUATION.equals(frameType)) {
        result = new ContinuationWebSocketFrame(payload);
      } else if (FrameType.PING.equals(frameType)) {
        result = new PingWebSocketFrame(payload);
      } else if (FrameType.PONG.equals(frameType)) {
        result = new PongWebSocketFrame(payload);
      }

      if (result == null) {
        throw new IllegalArgumentException("不支持的SimpleWebSocketFrame: " + simpleWebSocketFrame);
      }
      return result;
    }

    public static SimpleWebSocketFrame convert(WebSocketFrame webSocketFrame, boolean retain) {
      if (webSocketFrame == null) return null;
      SimpleWebSocketFrame result = null;

      ByteBuf payload = webSocketFrame.content();
      if (retain) {
        payload.retain();
      }

      if (webSocketFrame instanceof BinaryWebSocketFrame) {
        result = new SimpleWebSocketFrame(FrameType.BINARY, payload);
      } else if (webSocketFrame instanceof TextWebSocketFrame) {
        result = new SimpleWebSocketFrame(FrameType.TEXT, payload);
      } else if (webSocketFrame instanceof CloseWebSocketFrame) {
        result = new SimpleWebSocketFrame(FrameType.CLOSE, payload);
      } else if (webSocketFrame instanceof ContinuationWebSocketFrame) {
        result = new SimpleWebSocketFrame(FrameType.CONTINUATION, payload);
      } else if (webSocketFrame instanceof PingWebSocketFrame) {
        result = new SimpleWebSocketFrame(FrameType.PING, payload);
      } else if (webSocketFrame instanceof PongWebSocketFrame) {
        result = new SimpleWebSocketFrame(FrameType.PONG, payload);
      }

      if (result == null) {
        throw new IllegalArgumentException("不支持的WebSocketFrame: " + webSocketFrame);
      }
      return result;

    }
  }

  /**
   * 自定义WebSocket帧抽象
   */
  public static final class SimpleWebSocketFrame {
    public enum FrameType {
      BINARY, CLOSE, PING, PONG, TEXT, CONTINUATION
    }

    private final FrameType frameType;
    private final ByteBuf data;

    public SimpleWebSocketFrame(FrameType frameType, ByteBuf data) {
      this.frameType = frameType;
      this.data = data;
    }

    public FrameType getFrameType() {
      return frameType;
    }

    public ByteBuf getData() {
      return data;
    }

    @Override
    public String toString() {
      return "SimpleWebSocketFrame [frameType=" + frameType + ", data=" + ByteBufs.string(data)
          + "]";
    }
  }

}
