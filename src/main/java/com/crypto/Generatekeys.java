package com.crypto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class Generatekeys {
  private KeyPairGenerator keyPairGenerator;
  private KeyPair keyPair;
  public String standardFilePath = System.getProperty("user.dir") + "/src/main/java/com/crypto/keys/%s.pem";

  public Generatekeys(int lenKey) throws Exception {
    // Security.addProvider(new
    // org.bouncycastle.jce.provider.BouncyCastleProvider());
    this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(lenKey);
    this.keyPair = keyPairGenerator.genKeyPair();
  }

  public byte[] getPublicKey() throws Exception {
    return this.keyPair.getPublic().getEncoded();
  }

  public byte[] getPrivateKey() throws Exception {
    return this.keyPair.getPrivate().getEncoded();
  }

  public void createPEMPrivateKey(Path filePath) throws Exception {
    String pem_format = "-----BEGIN PRIVATE KEY----- %s\n-----END PRIVATE KEY-----";
    Path path_file = Paths.get(filePath.toString() + "/privateKey.pem");
    String stringPrivateKey = Base64.getEncoder().encodeToString(getPrivateKey());
    Files.writeString(path_file, String.format(pem_format, stringPrivateKey));
  }

  public void createPEMPrivateKey() throws Exception {
    String pem_format = "-----BEGIN PRIVATE KEY-----\n %s\n-----END PRIVATE KEY-----";
    String path_file = String.format(this.standardFilePath, "Private");
    String stringPrivateKey = Base64.getEncoder().encodeToString(getPrivateKey());
    Files.writeString(Paths.get(path_file), String.format(pem_format, stringPrivateKey));
  }

  public void createPEMPublicKey(Path filePath) throws Exception {
    String pem_format = "-----BEGIN PUBLIC KEY----- %s\n-----END PUBLIC KEY-----";
    Path path_file = Paths.get(filePath.toString() + "/publicKey.pem");
    String stringPrivateKey = Base64.getEncoder().encodeToString(getPublicKey());
    Files.writeString(path_file, String.format(pem_format, stringPrivateKey));
  }

  public void createPEMPublicKey() throws Exception {
    String pem_format = "-----BEGIN PUBLIC KEY-----\n %s\n-----END PUBLIC KEY-----";
    String path_file = String.format(this.standardFilePath, "Public");
    String stringPrivateKey = Base64.getEncoder().encodeToString(getPublicKey());
    Files.writeString(Paths.get(path_file), String.format(pem_format, stringPrivateKey));
  }

  public static void main(String[] args) throws Exception {
    Generatekeys generateKeys = new Generatekeys(1024);
    System.out.println(generateKeys.standardFilePath);
    generateKeys.createPEMPrivateKey();
    generateKeys.createPEMPublicKey();
  }
}
