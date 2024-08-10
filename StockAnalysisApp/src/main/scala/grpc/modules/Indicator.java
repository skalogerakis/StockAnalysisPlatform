// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: challenger.proto

package grpc.modules;

/**
 * Protobuf type {@code Challenger.Indicator}
 */
public  final class Indicator extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Challenger.Indicator)
    IndicatorOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Indicator.newBuilder() to construct.
  private Indicator(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Indicator() {
    symbol_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Indicator();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Indicator(
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
            java.lang.String s = input.readStringRequireUtf8();

            symbol_ = s;
            break;
          }
          case 21: {

            ema38_ = input.readFloat();
            break;
          }
          case 29: {

            ema100_ = input.readFloat();
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
    return grpc.modules.ChallengerProto.internal_static_Challenger_Indicator_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return grpc.modules.ChallengerProto.internal_static_Challenger_Indicator_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            grpc.modules.Indicator.class, grpc.modules.Indicator.Builder.class);
  }

  public static final int SYMBOL_FIELD_NUMBER = 1;
  private volatile java.lang.Object symbol_;
  /**
   * <code>string symbol = 1;</code>
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
   * <code>string symbol = 1;</code>
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

  public static final int EMA_38_FIELD_NUMBER = 2;
  private float ema38_;
  /**
   * <code>float ema_38 = 2;</code>
   * @return The ema38.
   */
  public float getEma38() {
    return ema38_;
  }

  public static final int EMA_100_FIELD_NUMBER = 3;
  private float ema100_;
  /**
   * <code>float ema_100 = 3;</code>
   * @return The ema100.
   */
  public float getEma100() {
    return ema100_;
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
    if (!getSymbolBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, symbol_);
    }
    if (ema38_ != 0F) {
      output.writeFloat(2, ema38_);
    }
    if (ema100_ != 0F) {
      output.writeFloat(3, ema100_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getSymbolBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, symbol_);
    }
    if (ema38_ != 0F) {
      size += com.google.protobuf.CodedOutputStream
        .computeFloatSize(2, ema38_);
    }
    if (ema100_ != 0F) {
      size += com.google.protobuf.CodedOutputStream
        .computeFloatSize(3, ema100_);
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
    if (!(obj instanceof grpc.modules.Indicator)) {
      return super.equals(obj);
    }
    grpc.modules.Indicator other = (grpc.modules.Indicator) obj;

    if (!getSymbol()
        .equals(other.getSymbol())) return false;
    if (java.lang.Float.floatToIntBits(getEma38())
        != java.lang.Float.floatToIntBits(
            other.getEma38())) return false;
    if (java.lang.Float.floatToIntBits(getEma100())
        != java.lang.Float.floatToIntBits(
            other.getEma100())) return false;
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
    hash = (37 * hash) + SYMBOL_FIELD_NUMBER;
    hash = (53 * hash) + getSymbol().hashCode();
    hash = (37 * hash) + EMA_38_FIELD_NUMBER;
    hash = (53 * hash) + java.lang.Float.floatToIntBits(
        getEma38());
    hash = (37 * hash) + EMA_100_FIELD_NUMBER;
    hash = (53 * hash) + java.lang.Float.floatToIntBits(
        getEma100());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static grpc.modules.Indicator parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.Indicator parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.Indicator parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.Indicator parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.Indicator parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.Indicator parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.Indicator parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static grpc.modules.Indicator parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static grpc.modules.Indicator parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static grpc.modules.Indicator parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static grpc.modules.Indicator parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static grpc.modules.Indicator parseFrom(
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
  public static Builder newBuilder(grpc.modules.Indicator prototype) {
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
   * Protobuf type {@code Challenger.Indicator}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Challenger.Indicator)
      grpc.modules.IndicatorOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_Indicator_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_Indicator_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              grpc.modules.Indicator.class, grpc.modules.Indicator.Builder.class);
    }

    // Construct using grpc.modules.Indicator.newBuilder()
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
      symbol_ = "";

      ema38_ = 0F;

      ema100_ = 0F;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_Indicator_descriptor;
    }

    @java.lang.Override
    public grpc.modules.Indicator getDefaultInstanceForType() {
      return grpc.modules.Indicator.getDefaultInstance();
    }

    @java.lang.Override
    public grpc.modules.Indicator build() {
      grpc.modules.Indicator result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public grpc.modules.Indicator buildPartial() {
      grpc.modules.Indicator result = new grpc.modules.Indicator(this);
      result.symbol_ = symbol_;
      result.ema38_ = ema38_;
      result.ema100_ = ema100_;
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
      if (other instanceof grpc.modules.Indicator) {
        return mergeFrom((grpc.modules.Indicator)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(grpc.modules.Indicator other) {
      if (other == grpc.modules.Indicator.getDefaultInstance()) return this;
      if (!other.getSymbol().isEmpty()) {
        symbol_ = other.symbol_;
        onChanged();
      }
      if (other.getEma38() != 0F) {
        setEma38(other.getEma38());
      }
      if (other.getEma100() != 0F) {
        setEma100(other.getEma100());
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
      grpc.modules.Indicator parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (grpc.modules.Indicator) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object symbol_ = "";
    /**
     * <code>string symbol = 1;</code>
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
     * <code>string symbol = 1;</code>
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
     * <code>string symbol = 1;</code>
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
     * <code>string symbol = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearSymbol() {
      
      symbol_ = getDefaultInstance().getSymbol();
      onChanged();
      return this;
    }
    /**
     * <code>string symbol = 1;</code>
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

    private float ema38_ ;
    /**
     * <code>float ema_38 = 2;</code>
     * @return The ema38.
     */
    public float getEma38() {
      return ema38_;
    }
    /**
     * <code>float ema_38 = 2;</code>
     * @param value The ema38 to set.
     * @return This builder for chaining.
     */
    public Builder setEma38(float value) {
      
      ema38_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>float ema_38 = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearEma38() {
      
      ema38_ = 0F;
      onChanged();
      return this;
    }

    private float ema100_ ;
    /**
     * <code>float ema_100 = 3;</code>
     * @return The ema100.
     */
    public float getEma100() {
      return ema100_;
    }
    /**
     * <code>float ema_100 = 3;</code>
     * @param value The ema100 to set.
     * @return This builder for chaining.
     */
    public Builder setEma100(float value) {
      
      ema100_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>float ema_100 = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearEma100() {
      
      ema100_ = 0F;
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


    // @@protoc_insertion_point(builder_scope:Challenger.Indicator)
  }

  // @@protoc_insertion_point(class_scope:Challenger.Indicator)
  private static final grpc.modules.Indicator DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new grpc.modules.Indicator();
  }

  public static grpc.modules.Indicator getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Indicator>
      PARSER = new com.google.protobuf.AbstractParser<Indicator>() {
    @java.lang.Override
    public Indicator parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Indicator(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Indicator> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Indicator> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public grpc.modules.Indicator getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

