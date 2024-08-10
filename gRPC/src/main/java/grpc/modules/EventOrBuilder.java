// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: challenger.proto

package grpc.modules;

public interface EventOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Challenger.Event)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string symbol = 1;</code>
   * @return The symbol.
   */
  java.lang.String getSymbol();
  /**
   * <code>string symbol = 1;</code>
   * @return The bytes for symbol.
   */
  com.google.protobuf.ByteString
      getSymbolBytes();

  /**
   * <code>.Challenger.SecurityType security_type = 2;</code>
   * @return The enum numeric value on the wire for securityType.
   */
  int getSecurityTypeValue();
  /**
   * <code>.Challenger.SecurityType security_type = 2;</code>
   * @return The securityType.
   */
  grpc.modules.SecurityType getSecurityType();

  /**
   * <code>float last_trade_price = 3;</code>
   * @return The lastTradePrice.
   */
  float getLastTradePrice();

  /**
   * <code>.google.protobuf.Timestamp last_trade = 4;</code>
   * @return Whether the lastTrade field is set.
   */
  boolean hasLastTrade();
  /**
   * <code>.google.protobuf.Timestamp last_trade = 4;</code>
   * @return The lastTrade.
   */
  com.google.protobuf.Timestamp getLastTrade();
  /**
   * <code>.google.protobuf.Timestamp last_trade = 4;</code>
   */
  com.google.protobuf.TimestampOrBuilder getLastTradeOrBuilder();
}
