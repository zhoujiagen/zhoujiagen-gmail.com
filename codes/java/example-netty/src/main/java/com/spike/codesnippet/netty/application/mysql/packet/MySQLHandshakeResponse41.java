package com.spike.codesnippet.netty.application.mysql.packet;

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.spike.codesnippet.netty.application.mysql.NettyMySQLClientConfiguration;
import com.spike.codesnippet.netty.application.mysql.NettyMySQLClientConstants;
import com.spike.codesnippet.netty.support.Bytes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_packets_protocol_handshake_response.html
 */
public class MySQLHandshakeResponse41 extends MySQLPacket {

  private final NettyMySQLClientConfiguration configuration;

  /** int<4> */
  private byte[] clientFlag;
  /** int<4> */
  private int maxPacketSize;
  /** int<1> */
  private byte characterSet;
  /** string[23] */
  private byte[] filler;
  /** string<NUL> */
  private String username;

  /// CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA
  /** int<1> */
  private byte authResponseLength;
  /** string<length> */
  private byte[] authResponse;

  /// CLIENT_CONNECT_WITH_DB
  /** string<NUL> */
  private String database;

  /// CLIENT_PLUGIN_AUTH
  /** string<NUL> */
  private String clientPluginName;

  /// CLIENT_CONNECT_ATTRS
  /** int<lenenc>, string<lenenc>, string<lenenc> */
  private Map<String, String> clientConnectAttrs = Maps.newHashMap();

  public MySQLHandshakeResponse41(int sequenceId, NettyMySQLClientConfiguration configuration,
      byte[] authPluginData) {
    super(NettyMySQLClientConstants.PAYLOAD_LENGTH_UNKNOWN, sequenceId);
    this.configuration = configuration;
    // Preconditions.checkArgument(authPluginData.length == 20);

    super.payload = Unpooled.buffer();

    // clientFlag
    this.clientFlag = new byte[] { 0x0d, (byte) 0xa2, 0x3a, 0x00 };
    super.payload.writeBytes(this.clientFlag);
    // maxPacketSize
    final byte[] maxPacketSizeArray = new byte[] { 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff };
    this.maxPacketSize = Unpooled.copiedBuffer(maxPacketSizeArray).readIntLE();
    super.payload.writeInt(this.maxPacketSize);
    // character_set
    this.characterSet = 0x2d;
    super.payload.writeByte(this.characterSet);
    // filler
    this.filler = Bytes.repeats((byte) 0x00, 23);
    super.payload.writeZero(23);
    // username
    this.username = configuration.getUsername();
    super.payload.writeBytes(Bytes.nullEndString(this.username));

    // authResponseLength, authResponse
    // SHA1(password) XOR SHA1("20-bytes random data from server" < concat > SHA1(SHA1(password)))
    HashFunction hashFunction = Hashing.sha1();
    byte[] password = configuration.getPassword().getBytes();
    byte[] passwordSHA1 = hashFunction.hashBytes(password).asBytes();
    byte[] concat = com.google.common.primitives.Bytes.concat(authPluginData,
      hashFunction.hashBytes(passwordSHA1).asBytes());
    byte[] concatSHA1 = hashFunction.hashBytes(concat).asBytes();
    this.authResponse = Bytes.xor(passwordSHA1, concatSHA1);
    this.authResponseLength = (byte) authResponse.length;
    payload.writeBytes(Bytes.lengthEncoded(this.authResponse));

    // database
    this.database = this.configuration.getDatabase();
    payload.writeBytes(Bytes.nullEndString(this.database));

    // client_plugin_name
    this.clientPluginName = NettyMySQLClientConstants.AUTH_PLUGIN_NAME;
    payload.writeBytes(Bytes.nullEndString(this.clientPluginName));

    // CLIENT_CONNECT_ATTRS
    this.clientConnectAttrs.put("clientId", "zhoujiagen");
    ByteBuf clientConnectAttrByteBuf = Unpooled.buffer();
    for (String key : clientConnectAttrs.keySet()) {
      clientConnectAttrByteBuf.writeBytes(Bytes.lengthEncoded(key.getBytes()));
      clientConnectAttrByteBuf.writeBytes(//
        Bytes.lengthEncoded(clientConnectAttrs.get(key).getBytes()));
    }
    payload.writeByte(clientConnectAttrByteBuf.readableBytes());
    payload.writeBytes(clientConnectAttrByteBuf);

    super.payloadLength = payload.readableBytes();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MySQLHandshakeResponse41 [payloadLength=");
    builder.append(payloadLength);
    builder.append(", sequenceId=");
    builder.append(sequenceId);
    builder.append(", clientFlag=");
    builder.append(Arrays.toString(clientFlag));
    builder.append(", maxPacketSize=");
    builder.append(maxPacketSize);
    builder.append(", characterSet=");
    builder.append(characterSet);
    builder.append(", filler=");
    builder.append(Arrays.toString(filler));
    builder.append(", username=");
    builder.append(username);
    builder.append(", authResponseLength=");
    builder.append(authResponseLength);
    builder.append(", authResponse=");
    builder.append(Arrays.toString(authResponse));
    builder.append(", database=");
    builder.append(database);
    builder.append(", clientPluginName=");
    builder.append(clientPluginName);
    builder.append(", clientConnectAttrs=");
    builder.append(clientConnectAttrs);
    builder.append("]");
    return builder.toString();
  }

}
