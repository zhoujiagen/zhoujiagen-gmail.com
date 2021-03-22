package com.spike.codesnippet.netty.application.mysql.packet;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_basic_packets.html
 */
public class MySQLPacket {
  /** int<3> */
  protected int payloadLength;
  /** int<1> */
  protected final int sequenceId;
  /** string<var> */
  protected ByteBuf payload;

  public MySQLPacket(int payloadLength, int sequenceId) {
    this.payloadLength = payloadLength;
    this.sequenceId = sequenceId;
  }

  public int getPayloadLength() {
    return payloadLength;
  }

  public int getSequenceId() {
    return sequenceId;
  }

  public ByteBuf getPayload() {
    return payload;
  }

  public void setPayload(ByteBuf payload) {
    Preconditions.checkArgument(payload.readableBytes() == payloadLength);

    this.payload = payload;
  }

  public void setPayload(byte[] payload) {
    Preconditions.checkArgument(payload.length == payloadLength);

    this.payload.writeBytes(payload);
  }

  public ByteBuf raw() {
    ByteBuf result = Unpooled.buffer();
    result.writeMediumLE(payloadLength);
    result.writeByte(sequenceId);
    result.writeBytes(payload);
    System.err.println(ByteBufUtil.prettyHexDump(result));
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MySQLPacket [payloadLength=");
    builder.append(payloadLength);
    builder.append(", sequenceId=");
    builder.append(sequenceId);
    builder.append(", payload=\n");
    builder.append(ByteBufUtil.prettyHexDump(payload));
    builder.append("]");
    return builder.toString();
  }

}
