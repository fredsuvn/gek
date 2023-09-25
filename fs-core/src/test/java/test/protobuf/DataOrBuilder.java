// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: message.proto

package test.protobuf;

public interface DataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Data)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.Enum em = 1;</code>
   * @return The enum numeric value on the wire for em.
   */
  int getEmValue();
  /**
   * <code>.Enum em = 1;</code>
   * @return The em.
   */
  Enum getEm();

  /**
   * <code>string str = 2;</code>
   * @return The str.
   */
  String getStr();
  /**
   * <code>string str = 2;</code>
   * @return The bytes for str.
   */
  com.google.protobuf.ByteString
      getStrBytes();

  /**
   * <code>int64 num = 3;</code>
   * @return The num.
   */
  long getNum();

  /**
   * <code>repeated string text = 4;</code>
   * @return A list containing the text.
   */
  java.util.List<String>
      getTextList();
  /**
   * <code>repeated string text = 4;</code>
   * @return The count of text.
   */
  int getTextCount();
  /**
   * <code>repeated string text = 4;</code>
   * @param index The index of the element to return.
   * @return The text at the given index.
   */
  String getText(int index);
  /**
   * <code>repeated string text = 4;</code>
   * @param index The index of the value to return.
   * @return The bytes of the text at the given index.
   */
  com.google.protobuf.ByteString
      getTextBytes(int index);

  /**
   * <code>map&lt;string, string&gt; entry = 5;</code>
   */
  int getEntryCount();
  /**
   * <code>map&lt;string, string&gt; entry = 5;</code>
   */
  boolean containsEntry(
      String key);
  /**
   * Use {@link #getEntryMap()} instead.
   */
  @Deprecated
  java.util.Map<String, String>
  getEntry();
  /**
   * <code>map&lt;string, string&gt; entry = 5;</code>
   */
  java.util.Map<String, String>
  getEntryMap();
  /**
   * <code>map&lt;string, string&gt; entry = 5;</code>
   */

  String getEntryOrDefault(
      String key,
      String defaultValue);
  /**
   * <code>map&lt;string, string&gt; entry = 5;</code>
   */

  String getEntryOrThrow(
      String key);

  /**
   * <code>uint32 uint32 = 6;</code>
   * @return The uint32.
   */
  int getUint32();

  /**
   * <code>uint64 uint64 = 7;</code>
   * @return The uint64.
   */
  long getUint64();

  /**
   * <code>fixed32 fixed32 = 8;</code>
   * @return The fixed32.
   */
  int getFixed32();

  /**
   * <code>fixed64 fixed64 = 9;</code>
   * @return The fixed64.
   */
  long getFixed64();

  /**
   * <code>sfixed32 sfixed32 = 10;</code>
   * @return The sfixed32.
   */
  int getSfixed32();

  /**
   * <code>sfixed64 sfixed64 = 11;</code>
   * @return The sfixed64.
   */
  long getSfixed64();

  /**
   * <code>sint32 sint32 = 12;</code>
   * @return The sint32.
   */
  int getSint32();

  /**
   * <code>sint64 sint64 = 13;</code>
   * @return The sint64.
   */
  long getSint64();

  /**
   * <code>bytes bytes = 14;</code>
   * @return The bytes.
   */
  com.google.protobuf.ByteString getBytes();
}
