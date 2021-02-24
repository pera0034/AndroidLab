package com.example.androidlab.models;

public class Message {

  private final String text;
  private final Type type;

  public Message(String messageText, Type type) {
    this.text = messageText;
    this.type = type;
  }

  public String getText() {
    return text;
  }

  public Type getType() {
    return type;
  }

  public enum Type {
    SENT,
    RECEIVED
  }
}
