package com.hba.common.messaging;

import java.util.Objects;

public class Message {
  private String id;
  private boolean success;
  private Object payload;

  public Message() {}

  private Message(boolean success, Object payload) {
    this.id = null;
    this.success = success;
    this.payload = payload;
  }

  private Message(String id, boolean success, Object payload) {
    this.id = id;
    this.success = success;
    this.payload = payload;
  }

  public static Message withSuccess() {
    return new Message(true, null);
  }

  public static Message withSuccess(Object payload) {
    return new Message(true, payload);
  }

  public static Message withSuccess(String id, Object payload) {
    return new Message(id, true, payload);
  }

  public static Message withFailure() {
    return new Message(false, null);
  }

  public static Message withFailure(Object payload) {
    return new Message(false, payload);
  }

  public static Message withFailure(String id, Object payload) {
    return new Message(id, false, payload);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "Message{" + "id='" + id + '\'' + ", success=" + success + ", payload=" + payload + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Message)) return false;
    Message message = (Message) o;
    return Objects.equals(id, message.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}