package com.spike.codesnippet.netty.application.mysql.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_packets_protocol_handshake_v10.html
 */
public class MySQLHandshakeV10 extends MySQLPacket {
  /** int<1> */
  private int protocolVersion = 10;
  /** string<NUL> */
  private String serverVersion;
  /** int<4>, connection id */
  private int threadId;
  /** string[8] */
  private byte[] authPluginDataPart1;
  /** int<1> */
  private int filler;
  /** int<2> */
  private int capabilityFlags1;
  /** int<1> */
  private int characterSet;
  /** int<2> */
  private int statusFlags;
  /** int<2> */
  private int capabilityFlags2;
  /** CLIENT_PLUGIN_AUTH, int<1> */
  private int authPluginDataLen; // else 0x00
  /** string[10], All 0s. */
  private String reserved;
  /** $length = MAX(13, length of auth-plugin-data - 8) */
  private byte[] authPluginDataPart2;
  /** string<NUL> */
  private String authPluginName;

  /// Derived fields
  private byte[] authPluginData;

  public MySQLHandshakeV10(int payloadLength, int sequenceId, ByteBuf payload) {
    super(payloadLength, sequenceId);
    this.payload = payload;

    // # int<1>
    // protocol_version = int.from_bytes(payload[current_index:current_index + 1],
    // byteorder='little')
    this.protocolVersion = payload.readByte();
    // print('protocol_version', protocol_version)
    // current_index += 1
    // # string<NUL>
    // (server_version, next_index) = parse_String_NULL(payload, start_index=current_index)
    // current_index = next_index
    // print('server_version', server_version)
    int indexOfNUL = payload.indexOf(payload.readerIndex(),
      payload.readerIndex() + payload.readableBytes(), (byte) 0);
    this.serverVersion =
        payload.readBytes(indexOfNUL - payload.readerIndex()).toString(CharsetUtil.UTF_8);
    payload.readerIndex(indexOfNUL + 1);
    // # int<4>
    // thread_id = int.from_bytes(payload[current_index: current_index + 4], byteorder='little')
    // print('thread_id', thread_id)
    // current_index = current_index + 4
    this.threadId = payload.readIntLE();
    // # string[8]
    // auth_plugin_data_part_1 = payload[current_index: current_index + 8]
    // print('auth_plugin_data_part_1', auth_plugin_data_part_1)
    // current_index += 8
    this.authPluginDataPart1 = new byte[8];
    payload.readBytes(this.authPluginDataPart1, 0, 8);
    // # int<1>
    // current_index += 1
    this.filler = payload.readByte();
    // # int<2>
    // capability_flags_1 = payload[current_index: current_index + 2]
    // print('capability_flags_1', capability_flags_1)
    // current_index += 2
    this.capabilityFlags1 = payload.readShortLE();
    // # int<1>
    // character_set = payload[current_index:current_index + 1]
    // print('character_set', character_set)
    // current_index += 1
    this.characterSet = payload.readByte();
    // # int<2>
    // status_flags = payload[current_index:current_index + 2]
    // print('status_flags', status_flags)
    // current_index += 2
    this.statusFlags = payload.readShortLE();
    // # int<2>
    // capability_flags_2 = payload[current_index:current_index + 2]
    // print('capability_flags_2', capability_flags_2)
    // current_index += 2
    this.capabilityFlags2 = payload.readShortLE();
    // # int<1>
    // auth_plugin_data_len = int.from_bytes(payload[current_index:current_index + 1],
    // byteorder='little')
    // print('auth_plugin_data_len', auth_plugin_data_len)
    // current_index += 1
    this.authPluginDataLen = payload.readUnsignedByte();
    // # string[10]
    // reserved = payload[current_index:current_index + 10]
    // print('reserved', reserved)
    // current_index += 10
    this.reserved = payload.readBytes(10).toString(CharsetUtil.UTF_8);
    // # $length = $len=MAX(13, length of auth-plugin-data - 8)
    // _length = max(13, auth_plugin_data_len - 8)
    // auth_plugin_data_part_2 = payload[current_index:current_index + _length]
    // print('auth_plugin_data_part_2', auth_plugin_data_part_2)
    // current_index += _length
    int authPluginDataPart2Length = Math.max(13, authPluginDataLen - 8);
    this.authPluginDataPart2 = new byte[authPluginDataPart2Length];
    payload.readBytes(this.authPluginDataPart2, 0, authPluginDataPart2Length);
    // # NULL
    // auth_plugin_name = payload[current_index:]
    // print('auth_plugin_name', auth_plugin_name)
    //
    // auth_plugin_data = auth_plugin_data_part_1 +
    // auth_plugin_data_part_2[:len(auth_plugin_data_part_2) - 1]
    // print('auth_plugin_data', auth_plugin_data)
    // return auth_plugin_data

    this.authPluginName =
        payload.readBytes(payload.readableBytes() - 1).toString(CharsetUtil.UTF_8);

    // Derived fields

    // 注意: 是20字节!!!
    this.authPluginData =
        new byte[this.authPluginDataPart1.length + this.authPluginDataPart2.length - 1];
    System.arraycopy(this.authPluginDataPart1, 0, this.authPluginData, 0,
      this.authPluginDataPart1.length);
    System.arraycopy(this.authPluginDataPart2, 0, this.authPluginData,
      this.authPluginDataPart1.length, this.authPluginDataPart2.length - 1);
  }

  public int getProtocolVersion() {
    return protocolVersion;
  }

  public void setProtocolVersion(int protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  public String getServerVersion() {
    return serverVersion;
  }

  public void setServerVersion(String serverVersion) {
    this.serverVersion = serverVersion;
  }

  public int getThreadId() {
    return threadId;
  }

  public void setThreadId(int threadId) {
    this.threadId = threadId;
  }

  public byte[] getAuthPluginDataPart1() {
    return authPluginDataPart1;
  }

  public void setAuthPluginDataPart1(byte[] authPluginDataPart1) {
    this.authPluginDataPart1 = authPluginDataPart1;
  }

  public int getFiller() {
    return filler;
  }

  public void setFiller(int filler) {
    this.filler = filler;
  }

  public int getCapabilityFlags1() {
    return capabilityFlags1;
  }

  public void setCapabilityFlags1(int capabilityFlags1) {
    this.capabilityFlags1 = capabilityFlags1;
  }

  public int getCharacterSet() {
    return characterSet;
  }

  public void setCharacterSet(int characterSet) {
    this.characterSet = characterSet;
  }

  public int getStatusFlags() {
    return statusFlags;
  }

  public void setStatusFlags(int statusFlags) {
    this.statusFlags = statusFlags;
  }

  public int getCapabilityFlags2() {
    return capabilityFlags2;
  }

  public void setCapabilityFlags2(int capabilityFlags2) {
    this.capabilityFlags2 = capabilityFlags2;
  }

  public int getAuthPluginDataLen() {
    return authPluginDataLen;
  }

  public void setAuthPluginDataLen(int authPluginDataLen) {
    this.authPluginDataLen = authPluginDataLen;
  }

  public String getReserved() {
    return reserved;
  }

  public void setReserved(String reserved) {
    this.reserved = reserved;
  }

  public byte[] getAuthPluginDataPart2() {
    return authPluginDataPart2;
  }

  public void setAuthPluginDataPart2(byte[] authPluginDataPart2) {
    this.authPluginDataPart2 = authPluginDataPart2;
  }

  public String getAuthPluginName() {
    return authPluginName;
  }

  public void setAuthPluginName(String authPluginName) {
    this.authPluginName = authPluginName;
  }

  public byte[] getAuthPluginData() {
    return authPluginData;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MySQLHandshakeV10 [protocolVersion=");
    builder.append(protocolVersion);
    builder.append(", serverVersion=");
    builder.append(serverVersion);
    builder.append(", threadId=");
    builder.append(threadId);
    builder.append(", authPluginDataPart1=\n");
    builder.append(ByteBufUtil.prettyHexDump(Unpooled.copiedBuffer(authPluginDataPart1)));
    builder.append(", filler=");
    builder.append(filler);
    builder.append(", capabilityFlags1=");
    builder.append(capabilityFlags1);
    builder.append(", characterSet=");
    builder.append(characterSet);
    builder.append(", statusFlags=");
    builder.append(statusFlags);
    builder.append(", capabilityFlags2=");
    builder.append(capabilityFlags2);
    builder.append(", authPluginDataLen=");
    builder.append(authPluginDataLen);
    builder.append(", reserved=");
    builder.append(reserved);
    builder.append(", authPluginDataPart2=\n");
    builder.append(ByteBufUtil.prettyHexDump(Unpooled.copiedBuffer(authPluginDataPart2)));
    builder.append(", authPluginName=");
    builder.append(authPluginName);
    builder.append("]");
    return builder.toString();
  }

}
