// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: terrain/terrain.proto

package intergroup.terrain;

public final class Terrain {
  private Terrain() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * Protobuf enum {@code intergroup.terrain.Kind}
   */
  public enum Kind
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>HILLS = 0;</code>
     */
    HILLS(0),
    /**
     * <code>PASTURE = 1;</code>
     */
    PASTURE(1),
    /**
     * <code>MOUNTAINS = 2;</code>
     */
    MOUNTAINS(2),
    /**
     * <code>FIELDS = 3;</code>
     */
    FIELDS(3),
    /**
     * <code>FOREST = 4;</code>
     */
    FOREST(4),
    /**
     * <code>DESERT = 5;</code>
     */
    DESERT(5),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>HILLS = 0;</code>
     */
    public static final int HILLS_VALUE = 0;
    /**
     * <code>PASTURE = 1;</code>
     */
    public static final int PASTURE_VALUE = 1;
    /**
     * <code>MOUNTAINS = 2;</code>
     */
    public static final int MOUNTAINS_VALUE = 2;
    /**
     * <code>FIELDS = 3;</code>
     */
    public static final int FIELDS_VALUE = 3;
    /**
     * <code>FOREST = 4;</code>
     */
    public static final int FOREST_VALUE = 4;
    /**
     * <code>DESERT = 5;</code>
     */
    public static final int DESERT_VALUE = 5;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static Kind valueOf(int value) {
      return forNumber(value);
    }

    public static Kind forNumber(int value) {
      switch (value) {
        case 0: return HILLS;
        case 1: return PASTURE;
        case 2: return MOUNTAINS;
        case 3: return FIELDS;
        case 4: return FOREST;
        case 5: return DESERT;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Kind>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        Kind> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Kind>() {
            public Kind findValueByNumber(int number) {
              return Kind.forNumber(number);
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
      return intergroup.terrain.Terrain.getDescriptor().getEnumTypes().get(0);
    }

    private static final Kind[] VALUES = values();

    public static Kind valueOf(
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

    private Kind(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:intergroup.terrain.Kind)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025terrain/terrain.proto\022\022intergroup.terr" +
      "ain*Q\n\004Kind\022\t\n\005HILLS\020\000\022\013\n\007PASTURE\020\001\022\r\n\tM" +
      "OUNTAINS\020\002\022\n\n\006FIELDS\020\003\022\n\n\006FOREST\020\004\022\n\n\006DE" +
      "SERT\020\005b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}