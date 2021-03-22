package com.spike.codesnippet.netty.application.mysql;

import java.nio.ByteOrder;

/**
 * 基于Netty的MySQL客户端中使用的常量.
 */
public interface NettyMySQLClientConstants {
  /** 报文最大大小: 16MB. */
  int PACKET_MAX_SIZE_BYTE = 16 * 1024 * 1024;
  /** int<3> payload_length */
  int PACKET_PAYLOAD_LENGTH_BYTE = 3;
  /** int<1> sequence_id */
  int PACKET_SEQUENCE_ID_SIZE_BYTE = 1;

  byte BYTE_NUL = 0x00;

  /** 未知的报文负载长度. */
  int PAYLOAD_LENGTH_UNKNOWN = -1;

  String AUTH_PLUGIN_NAME = "mysql_native_password";

  /** 字节序. */
  ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

}
