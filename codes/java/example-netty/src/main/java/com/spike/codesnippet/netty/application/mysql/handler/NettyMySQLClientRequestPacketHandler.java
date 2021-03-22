package com.spike.codesnippet.netty.application.mysql.handler;

import com.spike.codesnippet.netty.application.mysql.NettyMySQLClientConstants;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * WARN: 因要编码sequenceId, 未使用.
 */
public class NettyMySQLClientRequestPacketHandler extends LengthFieldPrepender {

  public NettyMySQLClientRequestPacketHandler() {
    super(NettyMySQLClientConstants.BYTE_ORDER,
        NettyMySQLClientConstants.PACKET_PAYLOAD_LENGTH_BYTE, 0, false);
  }

}
