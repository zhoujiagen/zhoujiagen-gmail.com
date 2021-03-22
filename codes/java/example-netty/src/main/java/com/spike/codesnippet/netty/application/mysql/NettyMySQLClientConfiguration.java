package com.spike.codesnippet.netty.application.mysql;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于Netty的MySQL客户端配置.
 */
public class NettyMySQLClientConfiguration {
  private String host;
  private int port;
  private String username;
  private String password;

  private String database;
  private final AtomicInteger sequenceId = new AtomicInteger();

  private String heartbeatSQL = "SELECT 1";

  public static class Builder {
    private static NettyMySQLClientConfiguration INSTANCE = new NettyMySQLClientConfiguration();

    public Builder host(String host) {
      INSTANCE.host = host;
      return this;
    }

    public Builder port(int port) {
      INSTANCE.port = port;
      return this;
    }

    public Builder username(String username) {
      INSTANCE.username = username;
      return this;
    }

    public Builder password(String password) {
      INSTANCE.password = password;
      return this;
    }

    public Builder database(String database) {
      INSTANCE.database = database;
      return this;
    }

    public Builder heartbeatSQL(String heartbeatSQL) {
      INSTANCE.heartbeatSQL = heartbeatSQL;
      return this;
    }

    public NettyMySQLClientConfiguration build() {
      return INSTANCE;
    }

  }

  public static Builder builder() {
    return new Builder();
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getDatabase() {
    return database;
  }

  public AtomicInteger getSequenceId() {
    return sequenceId;
  }

  public String getHeartbeatSQL() {
    return heartbeatSQL;
  }

}
