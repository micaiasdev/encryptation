package com.example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HexFormat;

public class MyFile {
  private byte[] content;

  public MyFile(String content) throws UnsupportedEncodingException, IOException {
    this.content = content.getBytes(StandardCharsets.UTF_8);
  }

  public MyFile(byte[] content) throws IOException {
    this.content = content;
  }

  public MyFile(Path filePath) throws IOException {
    this.content = Files.readAllBytes(filePath);
  }

  public String getHexEncode() {
    return HexFormat.of().formatHex(content);
  }

  public String getBase64Encode() {
    return Base64.getEncoder().encodeToString(content);
  }

  public void setBase64Content() {
    this.content = Base64.getDecoder().decode(content);
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] bytesContent) {
    this.content = bytesContent;
  }

}
