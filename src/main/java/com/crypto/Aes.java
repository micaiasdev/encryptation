package com.crypto;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.example.MyFile;

public class Aes {
  private SecretKey secretKey;
  private IvParameterSpec ivParameterSpec;
  private Cipher cipher;

  public Aes(byte[] key, byte[] ivParameterSpec, String operation_mode) throws Exception {
    // this.operation_mode = operation_mode;
    this.cipher = Cipher.getInstance(String.format("AES/%s/PKCS5Padding", operation_mode));
    this.secretKey = new SecretKeySpec(key, "AES");
    this.ivParameterSpec = new IvParameterSpec(ivParameterSpec);
  }

  public Aes(byte[] key, String operation_mode) throws Exception {
    // this.operation_mode = operation_mode;
    this.cipher = Cipher.getInstance(String.format("AES/%s/PKCS5Padding", operation_mode));
    this.secretKey = new SecretKeySpec(key, "AES");
  }

  public byte[] encypt(byte[] data) throws Exception {
    byte[] encrypt_data;
    this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.ivParameterSpec);
    encrypt_data = this.cipher.doFinal(data);
    return encrypt_data;
  }

  public byte[] encyptECB(byte[] data) throws Exception {
    byte[] encrypt_data;
    this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
    encrypt_data = this.cipher.doFinal(data);
    return encrypt_data;
  }

  public byte[] decrypt(byte[] data) throws Exception {
    byte[] decrypt_data;
    this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey, this.ivParameterSpec);
    decrypt_data = this.cipher.doFinal(data);
    return decrypt_data;
  }

  public byte[] decryptECB(byte[] data) throws Exception {
    byte[] decrypt_data;
    this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
    decrypt_data = this.cipher.doFinal(data);
    return decrypt_data;
  }

}
