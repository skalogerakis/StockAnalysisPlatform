// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: challenger.proto

package grpc.modules;

/**
 * Protobuf type {@code Challenger.BenchmarkConfiguration}
 */
public  final class BenchmarkConfiguration extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Challenger.BenchmarkConfiguration)
    BenchmarkConfigurationOrBuilder {
private static final long serialVersionUID = 0L;
  // Use BenchmarkConfiguration.newBuilder() to construct.
  private BenchmarkConfiguration(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BenchmarkConfiguration() {
    token_ = "";
    benchmarkName_ = "";
    benchmarkType_ = "";
    queries_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new BenchmarkConfiguration();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private BenchmarkConfiguration(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
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

            token_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            benchmarkName_ = s;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            benchmarkType_ = s;
            break;
          }
          case 32: {
            int rawValue = input.readEnum();
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              queries_ = new java.util.ArrayList<java.lang.Integer>();
              mutable_bitField0_ |= 0x00000001;
            }
            queries_.add(rawValue);
            break;
          }
          case 34: {
            int length = input.readRawVarint32();
            int oldLimit = input.pushLimit(length);
            while(input.getBytesUntilLimit() > 0) {
              int rawValue = input.readEnum();
              if (!((mutable_bitField0_ & 0x00000001) != 0)) {
                queries_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000001;
              }
              queries_.add(rawValue);
            }
            input.popLimit(oldLimit);
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
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        queries_ = java.util.Collections.unmodifiableList(queries_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return grpc.modules.ChallengerProto.internal_static_Challenger_BenchmarkConfiguration_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return grpc.modules.ChallengerProto.internal_static_Challenger_BenchmarkConfiguration_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            grpc.modules.BenchmarkConfiguration.class, grpc.modules.BenchmarkConfiguration.Builder.class);
  }

  public static final int TOKEN_FIELD_NUMBER = 1;
  private volatile java.lang.Object token_;
  /**
   * <pre>
   *Token from the webapp for authentication
   * </pre>
   *
   * <code>string token = 1;</code>
   * @return The token.
   */
  public java.lang.String getToken() {
    java.lang.Object ref = token_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      token_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *Token from the webapp for authentication
   * </pre>
   *
   * <code>string token = 1;</code>
   * @return The bytes for token.
   */
  public com.google.protobuf.ByteString
      getTokenBytes() {
    java.lang.Object ref = token_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      token_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int BENCHMARK_NAME_FIELD_NUMBER = 2;
  private volatile java.lang.Object benchmarkName_;
  /**
   * <pre>
   *chosen by the team, listed in the results
   * </pre>
   *
   * <code>string benchmark_name = 2;</code>
   * @return The benchmarkName.
   */
  public java.lang.String getBenchmarkName() {
    java.lang.Object ref = benchmarkName_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      benchmarkName_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *chosen by the team, listed in the results
   * </pre>
   *
   * <code>string benchmark_name = 2;</code>
   * @return The bytes for benchmarkName.
   */
  public com.google.protobuf.ByteString
      getBenchmarkNameBytes() {
    java.lang.Object ref = benchmarkName_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      benchmarkName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int BENCHMARK_TYPE_FIELD_NUMBER = 3;
  private volatile java.lang.Object benchmarkType_;
  /**
   * <pre>
   *benchmark type, e.g., test
   * </pre>
   *
   * <code>string benchmark_type = 3;</code>
   * @return The benchmarkType.
   */
  public java.lang.String getBenchmarkType() {
    java.lang.Object ref = benchmarkType_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      benchmarkType_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *benchmark type, e.g., test
   * </pre>
   *
   * <code>string benchmark_type = 3;</code>
   * @return The bytes for benchmarkType.
   */
  public com.google.protobuf.ByteString
      getBenchmarkTypeBytes() {
    java.lang.Object ref = benchmarkType_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      benchmarkType_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int QUERIES_FIELD_NUMBER = 4;
  private java.util.List<java.lang.Integer> queries_;
  private static final com.google.protobuf.Internal.ListAdapter.Converter<
      java.lang.Integer, grpc.modules.Query> queries_converter_ =
          new com.google.protobuf.Internal.ListAdapter.Converter<
              java.lang.Integer, grpc.modules.Query>() {
            public grpc.modules.Query convert(java.lang.Integer from) {
              @SuppressWarnings("deprecation")
              grpc.modules.Query result = grpc.modules.Query.valueOf(from);
              return result == null ? grpc.modules.Query.UNRECOGNIZED : result;
            }
          };
  /**
   * <code>repeated .Challenger.Query queries = 4;</code>
   * @return A list containing the queries.
   */
  public java.util.List<grpc.modules.Query> getQueriesList() {
    return new com.google.protobuf.Internal.ListAdapter<
        java.lang.Integer, grpc.modules.Query>(queries_, queries_converter_);
  }
  /**
   * <code>repeated .Challenger.Query queries = 4;</code>
   * @return The count of queries.
   */
  public int getQueriesCount() {
    return queries_.size();
  }
  /**
   * <code>repeated .Challenger.Query queries = 4;</code>
   * @param index The index of the element to return.
   * @return The queries at the given index.
   */
  public grpc.modules.Query getQueries(int index) {
    return queries_converter_.convert(queries_.get(index));
  }
  /**
   * <code>repeated .Challenger.Query queries = 4;</code>
   * @return A list containing the enum numeric values on the wire for queries.
   */
  public java.util.List<java.lang.Integer>
  getQueriesValueList() {
    return queries_;
  }
  /**
   * <code>repeated .Challenger.Query queries = 4;</code>
   * @param index The index of the value to return.
   * @return The enum numeric value on the wire of queries at the given index.
   */
  public int getQueriesValue(int index) {
    return queries_.get(index);
  }
  private int queriesMemoizedSerializedSize;

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
    getSerializedSize();
    if (!getTokenBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, token_);
    }
    if (!getBenchmarkNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, benchmarkName_);
    }
    if (!getBenchmarkTypeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, benchmarkType_);
    }
    if (getQueriesList().size() > 0) {
      output.writeUInt32NoTag(34);
      output.writeUInt32NoTag(queriesMemoizedSerializedSize);
    }
    for (int i = 0; i < queries_.size(); i++) {
      output.writeEnumNoTag(queries_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getTokenBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, token_);
    }
    if (!getBenchmarkNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, benchmarkName_);
    }
    if (!getBenchmarkTypeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, benchmarkType_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < queries_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeEnumSizeNoTag(queries_.get(i));
      }
      size += dataSize;
      if (!getQueriesList().isEmpty()) {  size += 1;
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32SizeNoTag(dataSize);
      }queriesMemoizedSerializedSize = dataSize;
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
    if (!(obj instanceof grpc.modules.BenchmarkConfiguration)) {
      return super.equals(obj);
    }
    grpc.modules.BenchmarkConfiguration other = (grpc.modules.BenchmarkConfiguration) obj;

    if (!getToken()
        .equals(other.getToken())) return false;
    if (!getBenchmarkName()
        .equals(other.getBenchmarkName())) return false;
    if (!getBenchmarkType()
        .equals(other.getBenchmarkType())) return false;
    if (!queries_.equals(other.queries_)) return false;
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
    hash = (37 * hash) + TOKEN_FIELD_NUMBER;
    hash = (53 * hash) + getToken().hashCode();
    hash = (37 * hash) + BENCHMARK_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getBenchmarkName().hashCode();
    hash = (37 * hash) + BENCHMARK_TYPE_FIELD_NUMBER;
    hash = (53 * hash) + getBenchmarkType().hashCode();
    if (getQueriesCount() > 0) {
      hash = (37 * hash) + QUERIES_FIELD_NUMBER;
      hash = (53 * hash) + queries_.hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static grpc.modules.BenchmarkConfiguration parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static grpc.modules.BenchmarkConfiguration parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static grpc.modules.BenchmarkConfiguration parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static grpc.modules.BenchmarkConfiguration parseFrom(
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
  public static Builder newBuilder(grpc.modules.BenchmarkConfiguration prototype) {
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
   * Protobuf type {@code Challenger.BenchmarkConfiguration}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Challenger.BenchmarkConfiguration)
      grpc.modules.BenchmarkConfigurationOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_BenchmarkConfiguration_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_BenchmarkConfiguration_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              grpc.modules.BenchmarkConfiguration.class, grpc.modules.BenchmarkConfiguration.Builder.class);
    }

    // Construct using grpc.modules.BenchmarkConfiguration.newBuilder()
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
      token_ = "";

      benchmarkName_ = "";

      benchmarkType_ = "";

      queries_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return grpc.modules.ChallengerProto.internal_static_Challenger_BenchmarkConfiguration_descriptor;
    }

    @java.lang.Override
    public grpc.modules.BenchmarkConfiguration getDefaultInstanceForType() {
      return grpc.modules.BenchmarkConfiguration.getDefaultInstance();
    }

    @java.lang.Override
    public grpc.modules.BenchmarkConfiguration build() {
      grpc.modules.BenchmarkConfiguration result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public grpc.modules.BenchmarkConfiguration buildPartial() {
      grpc.modules.BenchmarkConfiguration result = new grpc.modules.BenchmarkConfiguration(this);
      int from_bitField0_ = bitField0_;
      result.token_ = token_;
      result.benchmarkName_ = benchmarkName_;
      result.benchmarkType_ = benchmarkType_;
      if (((bitField0_ & 0x00000001) != 0)) {
        queries_ = java.util.Collections.unmodifiableList(queries_);
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.queries_ = queries_;
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
      if (other instanceof grpc.modules.BenchmarkConfiguration) {
        return mergeFrom((grpc.modules.BenchmarkConfiguration)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(grpc.modules.BenchmarkConfiguration other) {
      if (other == grpc.modules.BenchmarkConfiguration.getDefaultInstance()) return this;
      if (!other.getToken().isEmpty()) {
        token_ = other.token_;
        onChanged();
      }
      if (!other.getBenchmarkName().isEmpty()) {
        benchmarkName_ = other.benchmarkName_;
        onChanged();
      }
      if (!other.getBenchmarkType().isEmpty()) {
        benchmarkType_ = other.benchmarkType_;
        onChanged();
      }
      if (!other.queries_.isEmpty()) {
        if (queries_.isEmpty()) {
          queries_ = other.queries_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureQueriesIsMutable();
          queries_.addAll(other.queries_);
        }
        onChanged();
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
      grpc.modules.BenchmarkConfiguration parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (grpc.modules.BenchmarkConfiguration) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object token_ = "";
    /**
     * <pre>
     *Token from the webapp for authentication
     * </pre>
     *
     * <code>string token = 1;</code>
     * @return The token.
     */
    public java.lang.String getToken() {
      java.lang.Object ref = token_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        token_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *Token from the webapp for authentication
     * </pre>
     *
     * <code>string token = 1;</code>
     * @return The bytes for token.
     */
    public com.google.protobuf.ByteString
        getTokenBytes() {
      java.lang.Object ref = token_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        token_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *Token from the webapp for authentication
     * </pre>
     *
     * <code>string token = 1;</code>
     * @param value The token to set.
     * @return This builder for chaining.
     */
    public Builder setToken(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      token_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *Token from the webapp for authentication
     * </pre>
     *
     * <code>string token = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearToken() {
      
      token_ = getDefaultInstance().getToken();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *Token from the webapp for authentication
     * </pre>
     *
     * <code>string token = 1;</code>
     * @param value The bytes for token to set.
     * @return This builder for chaining.
     */
    public Builder setTokenBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      token_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object benchmarkName_ = "";
    /**
     * <pre>
     *chosen by the team, listed in the results
     * </pre>
     *
     * <code>string benchmark_name = 2;</code>
     * @return The benchmarkName.
     */
    public java.lang.String getBenchmarkName() {
      java.lang.Object ref = benchmarkName_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        benchmarkName_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *chosen by the team, listed in the results
     * </pre>
     *
     * <code>string benchmark_name = 2;</code>
     * @return The bytes for benchmarkName.
     */
    public com.google.protobuf.ByteString
        getBenchmarkNameBytes() {
      java.lang.Object ref = benchmarkName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        benchmarkName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *chosen by the team, listed in the results
     * </pre>
     *
     * <code>string benchmark_name = 2;</code>
     * @param value The benchmarkName to set.
     * @return This builder for chaining.
     */
    public Builder setBenchmarkName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      benchmarkName_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *chosen by the team, listed in the results
     * </pre>
     *
     * <code>string benchmark_name = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearBenchmarkName() {
      
      benchmarkName_ = getDefaultInstance().getBenchmarkName();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *chosen by the team, listed in the results
     * </pre>
     *
     * <code>string benchmark_name = 2;</code>
     * @param value The bytes for benchmarkName to set.
     * @return This builder for chaining.
     */
    public Builder setBenchmarkNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      benchmarkName_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object benchmarkType_ = "";
    /**
     * <pre>
     *benchmark type, e.g., test
     * </pre>
     *
     * <code>string benchmark_type = 3;</code>
     * @return The benchmarkType.
     */
    public java.lang.String getBenchmarkType() {
      java.lang.Object ref = benchmarkType_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        benchmarkType_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *benchmark type, e.g., test
     * </pre>
     *
     * <code>string benchmark_type = 3;</code>
     * @return The bytes for benchmarkType.
     */
    public com.google.protobuf.ByteString
        getBenchmarkTypeBytes() {
      java.lang.Object ref = benchmarkType_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        benchmarkType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *benchmark type, e.g., test
     * </pre>
     *
     * <code>string benchmark_type = 3;</code>
     * @param value The benchmarkType to set.
     * @return This builder for chaining.
     */
    public Builder setBenchmarkType(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      benchmarkType_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *benchmark type, e.g., test
     * </pre>
     *
     * <code>string benchmark_type = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearBenchmarkType() {
      
      benchmarkType_ = getDefaultInstance().getBenchmarkType();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *benchmark type, e.g., test
     * </pre>
     *
     * <code>string benchmark_type = 3;</code>
     * @param value The bytes for benchmarkType to set.
     * @return This builder for chaining.
     */
    public Builder setBenchmarkTypeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      benchmarkType_ = value;
      onChanged();
      return this;
    }

    private java.util.List<java.lang.Integer> queries_ =
      java.util.Collections.emptyList();
    private void ensureQueriesIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        queries_ = new java.util.ArrayList<java.lang.Integer>(queries_);
        bitField0_ |= 0x00000001;
      }
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @return A list containing the queries.
     */
    public java.util.List<grpc.modules.Query> getQueriesList() {
      return new com.google.protobuf.Internal.ListAdapter<
          java.lang.Integer, grpc.modules.Query>(queries_, queries_converter_);
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @return The count of queries.
     */
    public int getQueriesCount() {
      return queries_.size();
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param index The index of the element to return.
     * @return The queries at the given index.
     */
    public grpc.modules.Query getQueries(int index) {
      return queries_converter_.convert(queries_.get(index));
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param index The index to set the value at.
     * @param value The queries to set.
     * @return This builder for chaining.
     */
    public Builder setQueries(
        int index, grpc.modules.Query value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureQueriesIsMutable();
      queries_.set(index, value.getNumber());
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param value The queries to add.
     * @return This builder for chaining.
     */
    public Builder addQueries(grpc.modules.Query value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureQueriesIsMutable();
      queries_.add(value.getNumber());
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param values The queries to add.
     * @return This builder for chaining.
     */
    public Builder addAllQueries(
        java.lang.Iterable<? extends grpc.modules.Query> values) {
      ensureQueriesIsMutable();
      for (grpc.modules.Query value : values) {
        queries_.add(value.getNumber());
      }
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearQueries() {
      queries_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @return A list containing the enum numeric values on the wire for queries.
     */
    public java.util.List<java.lang.Integer>
    getQueriesValueList() {
      return java.util.Collections.unmodifiableList(queries_);
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param index The index of the value to return.
     * @return The enum numeric value on the wire of queries at the given index.
     */
    public int getQueriesValue(int index) {
      return queries_.get(index);
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param index The index of the value to return.
     * @return The enum numeric value on the wire of queries at the given index.
     * @return This builder for chaining.
     */
    public Builder setQueriesValue(
        int index, int value) {
      ensureQueriesIsMutable();
      queries_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param value The enum numeric value on the wire for queries to add.
     * @return This builder for chaining.
     */
    public Builder addQueriesValue(int value) {
      ensureQueriesIsMutable();
      queries_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Challenger.Query queries = 4;</code>
     * @param values The enum numeric values on the wire for queries to add.
     * @return This builder for chaining.
     */
    public Builder addAllQueriesValue(
        java.lang.Iterable<java.lang.Integer> values) {
      ensureQueriesIsMutable();
      for (int value : values) {
        queries_.add(value);
      }
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


    // @@protoc_insertion_point(builder_scope:Challenger.BenchmarkConfiguration)
  }

  // @@protoc_insertion_point(class_scope:Challenger.BenchmarkConfiguration)
  private static final grpc.modules.BenchmarkConfiguration DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new grpc.modules.BenchmarkConfiguration();
  }

  public static grpc.modules.BenchmarkConfiguration getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BenchmarkConfiguration>
      PARSER = new com.google.protobuf.AbstractParser<BenchmarkConfiguration>() {
    @java.lang.Override
    public BenchmarkConfiguration parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new BenchmarkConfiguration(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BenchmarkConfiguration> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BenchmarkConfiguration> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public grpc.modules.BenchmarkConfiguration getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

