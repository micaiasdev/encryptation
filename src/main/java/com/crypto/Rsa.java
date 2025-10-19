package com.crypto;

import java.io.FileReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class Rsa {
  String algorithm = "SHA256withRSA";
  String versionSHA;

  public Rsa(String versionSha) throws Exception {
    this.algorithm = String.format("SHA%swithRSA", versionSha);
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  public Rsa() {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  public Signature signature(byte[] data, PrivateKey privateKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA", "BC");
    signature.initSign(privateKey);
    signature.update(data);
    return signature;
  }

  public boolean signatureVerify(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
    Signature signatureInstance = Signature.getInstance("SHA256withRSA", "BC");
    signatureInstance.initVerify(publicKey);
    signatureInstance.update(data);
    return signatureInstance.verify(signature);
  }

  public PrivateKey loadPrivateKey(String pathFile) throws Exception {
    try (FileReader fileReader = new FileReader(pathFile);
        PEMParser pemParser = new PEMParser(fileReader)) {
      Object object = pemParser.readObject();

      if (object instanceof PrivateKeyInfo) {
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        return converter.getPrivateKey(privateKeyInfo);
      } else {
        throw new IllegalArgumentException("Arquivo não compatível");
      }

    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public PrivateKey loadPrivateKey() throws Exception {
    try (FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/crypto/keys/Private.pem");
        PEMParser pemParser = new PEMParser(fileReader)) {
      Object object = pemParser.readObject();

      if (object instanceof PrivateKeyInfo) {
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        return converter.getPrivateKey(privateKeyInfo);
      } else {
        throw new IllegalArgumentException("Arquivo não compatível");
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  public PublicKey loadPublicKey() throws Exception {
    try (FileReader fileReader = new FileReader(System.getProperty("user.dir") + "/crypto/keys/Public.pem");
        PEMParser pemParser = new PEMParser(fileReader)) {
      Object object = pemParser.readObject();

      if (object instanceof SubjectPublicKeyInfo) {
        SubjectPublicKeyInfo subjectPublicKeyInfo = (SubjectPublicKeyInfo) object;
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        return converter.getPublicKey(subjectPublicKeyInfo);
      } else {
        throw new IllegalArgumentException("Arquivo não compatível");
      }

    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public byte[] signatureBytes(byte[] data, PrivateKey privateKey) throws Exception {
    Signature sig = Signature.getInstance("SHA256withRSA", "BC");
    sig.initSign(privateKey);
    sig.update(data);
    return sig.sign();
  }

  public PublicKey loadPublicKey(String pathFile) throws Exception {
    try (java.io.FileReader fileReader = new java.io.FileReader(pathFile);
        org.bouncycastle.openssl.PEMParser pemParser = new org.bouncycastle.openssl.PEMParser(fileReader)) {

      Object object = pemParser.readObject();
      System.out.println("PEM object class (public) = " + (object == null ? "null" : object.getClass().getName()));

      org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter converter = new org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter()
          .setProvider("BC");

      if (object instanceof org.bouncycastle.asn1.x509.SubjectPublicKeyInfo) {
        org.bouncycastle.asn1.x509.SubjectPublicKeyInfo subjectPublicKeyInfo = (org.bouncycastle.asn1.x509.SubjectPublicKeyInfo) object;
        return converter.getPublicKey(subjectPublicKeyInfo);
      } else if (object instanceof org.bouncycastle.cert.X509CertificateHolder) {
        org.bouncycastle.cert.X509CertificateHolder cert = (org.bouncycastle.cert.X509CertificateHolder) object;
        return converter.getPublicKey(cert.getSubjectPublicKeyInfo());
      } else {
        throw new IllegalArgumentException("Arquivo não compatível (public) - tipo retornado: " +
            (object == null ? "null" : object.getClass().getName()));
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
