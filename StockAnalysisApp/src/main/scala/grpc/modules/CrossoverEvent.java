// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: challenger.proto

package grpc.modules;

/**
 * Protobuf type {@code Challenger.CrossoverEvent}
 */
public  final class CrossoverEvent extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Challenger.CrossoverEvent)
    CrossoverEventOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CrossoverEvent.newBuilder() to construct.
  private CrossoverEvent(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CrossoverEvent() {
    symbol_ = "";
    securityType_ = 0;
    signalType_ = 0;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new CrossoverEvent();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private CrossoverEvent(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            com.google.protobuf.Timestamp.Builder subBuilder = null;
            if (ts_ != null) {
              subBuilder = ts_.toBuilder();
            }
            ts_ = input.readMessage(com.google.protobuf.Timestamp.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(ts_);
              ts_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            symbol_ = s;
            break;
          }
          case 24: {
            int rawValue = input.readEnum();

            securityType_ = rawValue;
            break;
          }
          case 32: {
            int rawValue = input.readEnum();

            signalType_ = rawValue;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return grpc.modules.ChallengerProto.internal_static_Challenger_CrossoverEvent_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return grpc.modules.ChallengerProto.internal_static_Challenger_CrossoverEvent_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            grpc.modules.CrossoverEvent.class, grpc.modules.CrossoverEvent.Builder.class);
  }

  /**
   * Protobuf enum {@code Challenger.CrossoverEvent.SignalType}
   */
  public enum SignalType
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>Buy = 0;</code>
     */
    Buy(0),
    /**
     * <code>Sell = 1;</code>
     */
    Sell(1),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>Buy = 0;</code>
     */
    public static final int Buy_VALUE = 0;
    /**
     * <code>Sell = 1;</code>
     */
    public static final int Sell_VALUE = 1;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static SignalType valueOf(int value) {
      return forNumber(value);
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     */
    public static SignalType forNumber(int value) {
      switch (value) {
        case 0: return Buy;
        case 1: return Sell;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<SignalType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        SignalType> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<SignalType>() {
            public SignalType findValueByNumber(int number) {
              return SignalType.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return grpc.modules.CrossoverEvent.getDescriptor().getEnumTypes().get(0);
    }

    private static final SignalType[] VALUES = values();

    public static SignalType valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private SignalType(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:Challenger.CrossoverEvent.SignalType)
  }

  public static final int TS_FIELD_NUMBER = 1;
  private com.google.protobuf.Timestamp ts_;
  /**
   * <code>.google.protobuf.Timestamp ts = 1;</code>
   * @return Whether the ts field is set.
   */
  public boolean hasTs() {
    return ts_ != null;
  }
  /**
   * <code>.google.protobuf.Timestamp ts = 1;</code>
   * @return The ts.
   */
  public com.google.protobuf.Timestamp getTs() {
    return ts_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : ts_;
  }
  /**
   * <code>.google.protobuf.Timestamp ts = 1;</code>
   */
  public com.google.protobuf.TimestampOrBuilder getTsOrBuilder() {
    return getTs();
  }

  public static final int SYMBOL_FIELD_NUMBER = 2;
  private volatile java.lang.Object symbol_;
  /**
   * <code>string symbol = 2;</code>
   * @return The symbol.
   */
  public java.lang.String getSymbol() {
    java.lang.Object ref = symbol_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      symbol_ = s;
      return s;
    }
  }
  /**
   * <code>string symbol = 2;</code>
   * @return The bytes for symbol.
   */
  public com.google.protobuf.ByteString
      getSymbolBytes() {
    java.lang.Object ref = symbol_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      symbol_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SECURITY_TYPE_FIELD_NUMBER = 3;
  private int securityType_;
  /**
   * <code>.Challenger.SecurityType security_type = 3;</code>
   * @return The enum numeric value on the wire for securityType.
   */
  public int getSecurityTypeValue() {
    return securityType_;
  }
  /**
   * <code>.Challenger.SecurityType security_type = 3;</code>
   * @return The securityType.
   */
  public grpc.modules.SecurityType getSecurityType() {
    @SuppressWarnings("deprecation")
    grpc.modules.SecurityType result = grpc.modules.SecurityType.valueOf(securityType_);
    return result == null ? grpc.modules.SecurityType.UNRECOGNIZED : result;
  }

  public static final int SIGNAL_TYPE_FIELD_NUMBER = 4;
  private int signalType_;
  /**
   * <code>.Challenger.CrossoverEvent.SignalType signal_type = 4;</code>
   * @return The enum numeric value on the wire for signalType.
   */
  public int getSignalTypeValue() {
    return signalType_;
  }
  /**
   * <code>.Challenger.CrossoverEvent.SignalType signal_type = 4;</code>
   * @return The signalType.
   */
  public grpc.modules.CrossoverEvent.SignalType getSignalType() {
    @SuppressWarnings("deprecation")
    grpc.modules.CrossoverEvent.SignalType result = grpc.modules.CrossoverEvent.SignalType.valueOf(signalType_);
    return result == null ? grpc.modules.CrossoverEvent.SignalType.UNRECOGNIZED : result;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (ts_ != null) {
      output.writeMessage(1, getTs());
    }
    if (!getSymbolBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, symbol_);
    }
    if (securityType_ != grpc.modules.SecurityType.Equity.getNumber()) {
      output.writeEnum(3, securityType_);
    }
    if (signalType_ != grpc.modules.CrossoverEvent.SignalType.Buy.getNumber()) {
      output.writeEnum(4, signalType_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (ts_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getTs());
    }
    if (!getSymbolBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, symbol_);
    }
    if (securityType_ != grpc.modules.SecurityType.Equity.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(3, securityType_);
    }
    if (signalType_ != grpc.modules.CrossoverEvent.SignalType.Buy.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(4, signalType_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof grpc.modules.CrossoverEvent)) {
      return super.equals(obj);
    }
    grpc.modules.CrossoverEvent other = (grpc.modules.CrossoverEvent) obj;

    if (hasTs() != other.hasTs()) return false;
    if (hasTs()) {
      if (!getTs()
          .equals(other.getTs())) return false;
    }
    if (!getSymbol()
        .equals(other.getSymbol())) return false;
    if (securityType_ != other.securityType_) return false;
    if (signalType_ != other.signalType_) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasTs()) {
      hash = (37 * hash) + TS_FIELD_NUMBER;
      hash = (53 * hash) + getTs().hashCode();
    }
    hash = (37 * hash) + SYMBOL_FIELD_NUMBER;
    hash = (53 * hash) + getSymbol().hashCode();
    hash = (37 * hash) + SECURITY_TYPE_FIELD_NUMBER;
    hash = (53 * hash) + securityType_;
    hash = (37 * hash) + SIGNAL_TYPE_FIELD_NUMBER;
    hash = (53 * hash) + signalType_;
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static grpc.modules.CrossoverEvent parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.CrossoverEvent parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.CrossoverEvent parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.CrossoverEvent parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.CrossoverEvent parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.CrossoverEvent parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.CrossoverEvent parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static grpc.modules.CrossoverEvent parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static grpc.modules.CrossoverEvent parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static grpc.modules.CrossoverEvent parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static grpc.modules.CrossoverEvent parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static grpc.modules.CrossoverEvent parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(grpc.modules.CrossoverEvent prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code Challenger.CrossoverEvent}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Challenger.CrossoverEvent)
      grpc.modules.CrossoverEventOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_CrossoverEvent_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_CrossoverEvent_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              grpc.modules.CrossoverEvent.class, grpc.modules.CrossoverEvent.Builder.class);
    }

    // Construct using grpc.modules.CrossoverEvent.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (tsBuilder_ == null) {
        ts_ = null;
      } else {
        ts_ = null;
        tsBuilder_ = null;
      }
      symbol_ = "";

      securityType_ = 0;

      signalType_ = 0;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_CrossoverEvent_descriptor;
    }

    @java.lang.Override
    public grpc.modules.CrossoverEvent getDefaultInstanceForType() {
      return grpc.modules.CrossoverEvent.getDefaultInstance();
    }

    @java.lang.Override
    public grpc.modules.CrossoverEvent build() {
      grpc.modules.CrossoverEvent result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public grpc.modules.CrossoverEvent buildPartial() {
      grpc.modules.CrossoverEvent result = new grpc.modules.CrossoverEvent(this);
      if (tsBuilder_ == null) {
        result.ts_ = ts_;
      } else {
        result.ts_ = tsBuilder_.build();
      }
      result.symbol_ = symbol_;
      result.securityType_ = securityType_;
      result.signalType_ = signalType_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof grpc.modules.CrossoverEvent) {
        return mergeFrom((grpc.modules.CrossoverEvent)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(grpc.modules.CrossoverEvent other) {
      if (other == grpc.modules.CrossoverEvent.getDefaultInstance()) return this;
      if (other.hasTs()) {
        mergeTs(other.getTs());
      }
      if (!other.getSymbol().isEmpty()) {
        symbol_ = other.symbol_;
        onChanged();
      }
      if (other.securityType_ != 0) {
        setSecurityTypeValue(other.getSecurityTypeValue());
      }
      if (other.signalType_ != 0) {
        setSignalTypeValue(other.getSignalTypeValue());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      grpc.modules.CrossoverEvent parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (grpc.modules.CrossoverEvent) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.google.protobuf.Timestamp ts_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> tsBuilder_;
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     * @return Whether the ts field is set.
     */
    public boolean hasTs() {
      return tsBuilder_ != null || ts_ != null;
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     * @return The ts.
     */
    public com.google.protobuf.Timestamp getTs() {
      if (tsBuilder_ == null) {
        return ts_ == null ? com.google.protobuf.Timestamp.getDefaultInstance() : ts_;
      } else {
        return tsBuilder_.getMessage();
      }
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     */
    public Builder setTs(com.google.protobuf.Timestamp value) {
      if (tsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ts_ = value;
        onChanged();
      } else {
        tsBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     */
    public Builder setTs(
        com.google.protobuf.Timestamp.Builder builderForValue) {
      if (tsBuilder_ == null) {
        ts_ = builderForValue.build();
        onChanged();
      } else {
        tsBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     */
    public Builder mergeTs(com.google.protobuf.Timestamp value) {
      if (tsBuilder_ == null) {
        if (ts_ != null) {
          ts_ =
            com.google.protobuf.Timestamp.newBuilder(ts_).mergeFrom(value).buildPartial();
        } else {
          ts_ = value;
        }
        onChanged();
      } else {
        tsBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     */
    public Builder clearTs() {
      if (tsBuilder_ == null) {
        ts_ = null;
        onChanged();
      } else {
        ts_ = null;
        tsBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     */
    public com.google.protobuf.Timestamp.Builder getTsBuilder() {
      
      onChanged();
      return getTsFieldBuilder().getBuilder();
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     */
    public com.google.protobuf.TimestampOrBuilder getTsOrBuilder() {
      if (tsBuilder_ != null) {
        return tsBuilder_.getMessageOrBuilder();
      } else {
        return ts_ == null ?
            com.google.protobuf.Timestamp.getDefaultInstance() : ts_;
      }
    }
    /**
     * <code>.google.protobuf.Timestamp ts = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder> 
        getTsFieldBuilder() {
      if (tsBuilder_ == null) {
        tsBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.protobuf.Timestamp, com.google.protobuf.Timestamp.Builder, com.google.protobuf.TimestampOrBuilder>(
                getTs(),
                getParentForChildren(),
                isClean());
        ts_ = null;
      }
      return tsBuilder_;
    }

    private java.lang.Object symbol_ = "";
    /**
     * <code>string symbol = 2;</code>
     * @return The symbol.
     */
    public java.lang.String getSymbol() {
      java.lang.Object ref = symbol_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        symbol_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string symbol = 2;</code>
     * @return The bytes for symbol.
     */
    public com.google.protobuf.ByteString
        getSymbolBytes() {
      java.lang.Object ref = symbol_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        symbol_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string symbol = 2;</code>
     * @param value The symbol to set.
     * @return This builder for chaining.
     */
    public Builder setSymbol(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      symbol_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string symbol = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearSymbol() {
      
      symbol_ = getDefaultInstance().getSymbol();
      onChanged();
      return this;
    }
    /**
     * <code>string symbol = 2;</code>
     * @param value The bytes for symbol to set.
     * @return This builder for chaining.
     */
    public Builder setSymbolBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      symbol_ = value;
      onChanged();
      return this;
    }

    private int securityType_ = 0;
    /**
     * <code>.Challenger.SecurityType security_type = 3;</code>
     * @return The enum numeric value on the wire for securityType.
     */
    public int getSecurityTypeValue() {
      return securityType_;
    }
    /**
     * <code>.Challenger.SecurityType security_type = 3;</code>
     * @param value The enum numeric value on the wire for securityType to set.
     * @return This builder for chaining.
     */
    public Builder setSecurityTypeValue(int value) {
      securityType_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.Challenger.SecurityType security_type = 3;</code>
     * @return The securityType.
     */
    public grpc.modules.SecurityType getSecurityType() {
      @SuppressWarnings("deprecation")
      grpc.modules.SecurityType result = grpc.modules.SecurityType.valueOf(securityType_);
      return result == null ? grpc.modules.SecurityType.UNRECOGNIZED : result;
    }
    /**
     * <code>.Challenger.SecurityType security_type = 3;</code>
     * @param value The securityType to set.
     * @return This builder for chaining.
     */
    public Builder setSecurityType(grpc.modules.SecurityType value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      securityType_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.Challenger.SecurityType security_type = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearSecurityType() {
      
      securityType_ = 0;
      onChanged();
      return this;
    }

    private int signalType_ = 0;
    /**
     * <code>.Challenger.CrossoverEvent.SignalType signal_type = 4;</code>
     * @return The enum numeric value on the wire for signalType.
     */
    public int getSignalTypeValue() {
      return signalType_;
    }
    /**
     * <code>.Challenger.CrossoverEvent.SignalType signal_type = 4;</code>
     * @param value The enum numeric value on the wire for signalType to set.
     * @return This builder for chaining.
     */
    public Builder setSignalTypeValue(int value) {
      signalType_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.Challenger.CrossoverEvent.SignalType signal_type = 4;</code>
     * @return The signalType.
     */
    public grpc.modules.CrossoverEvent.SignalType getSignalType() {
      @SuppressWarnings("deprecation")
      grpc.modules.CrossoverEvent.SignalType result = grpc.modules.CrossoverEvent.SignalType.valueOf(signalType_);
      return result == null ? grpc.modules.CrossoverEvent.SignalType.UNRECOGNIZED : result;
    }
    /**
     * <code>.Challenger.CrossoverEvent.SignalType signal_type = 4;</code>
     * @param value The signalType to set.
     * @return This builder for chaining.
     */
    public Builder setSignalType(grpc.modules.CrossoverEvent.SignalType value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      signalType_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.Challenger.CrossoverEvent.SignalType signal_type = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearSignalType() {
      
      signalType_ = 0;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:Challenger.CrossoverEvent)
  }

  // @@protoc_insertion_point(class_scope:Challenger.CrossoverEvent)
  private static final grpc.modules.CrossoverEvent DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new grpc.modules.CrossoverEvent();
  }

  public static grpc.modules.CrossoverEvent getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CrossoverEvent>
      PARSER = new com.google.protobuf.AbstractParser<CrossoverEvent>() {
    @java.lang.Override
    public CrossoverEvent parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new CrossoverEvent(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<CrossoverEvent> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<CrossoverEvent> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public grpc.modules.CrossoverEvent getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

