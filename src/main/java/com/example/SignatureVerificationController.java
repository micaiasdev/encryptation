package com.example;

import com.crypto.Rsa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HexFormat;

public class SignatureVerificationController {

  @FXML
  private Path filePathKey;

  @FXML
  private Path filePathSign;

  @FXML
  private Path filePathMessage;

  @FXML
  private Button backButton;

  @FXML
  private TextField publicKeyPathField;

  @FXML
  private Button selectPublicKeyButton;

  @FXML
  private TextField originalFilePathField;

  @FXML
  private Button selectOriginalFileButton;

  @FXML
  private TextField signatureFilePathField;

  @FXML
  private Button selectSignatureFileButton;

  @FXML
  private Label verificationResultLabel; // Para exibir o resultado

  @FXML
  private Button verifySignatureButton;

  @FXML
  void backButton() throws IOException {
    App.setRoot("init");
  }

  // Helper para abrir o FileChooser e definir o campo de texto
  private void openFileChooserAndSetPath(String typePath, TextField field, String title,
      FileChooser.ExtensionFilter... filters) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().addAll(filters);

    Window stage = field.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      field.setText(selectedFile.getAbsolutePath());
      if (typePath.equalsIgnoreCase("KEY")) {
        filePathKey = Paths.get(selectedFile.getAbsolutePath());
      } else if (typePath.equalsIgnoreCase("SIGN")) {
        filePathSign = Paths.get(selectedFile.getAbsolutePath());
      } else {
        filePathMessage = Paths.get(selectedFile.getAbsolutePath());
      }
    }
  }

  @FXML
  private void handleSelectPublicKeyFile(ActionEvent event) {
    openFileChooserAndSetPath("KEY", publicKeyPathField, "Selecione o Arquivo de Chave Pública",
        new FileChooser.ExtensionFilter("Public Key Files", "*.pub", "*.pem"));
    verificationResultLabel.setText("Resultado da Verificação: Aguardando...");
  }

  @FXML
  private void handleSelectOriginalFile(ActionEvent event) {
    openFileChooserAndSetPath("Message", originalFilePathField, "Selecione o Arquivo em Claro (Original)",
        new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*"));
    verificationResultLabel.setText("Resultado da Verificação: Aguardando...");
  }

  @FXML
  private void handleSelectSignatureFile(ActionEvent event) {
    openFileChooserAndSetPath("SIGN", signatureFilePathField, "Selecione o Arquivo da Assinatura Digital",
        new FileChooser.ExtensionFilter("Signature Files", "*.sig", "*.bin", "*.txt"));
    verificationResultLabel.setText("Resultado da Verificação: Aguardando...");
  }

  @FXML
  boolean isHex(String s) {
    if (s == null)
      return false;
    s = s.trim();
    if (s.startsWith("0x") || s.startsWith("0X"))
      s = s.substring(2);
    s = s.replaceAll("[\\s:-]", "");
    if (s.isEmpty())
      return false;
    try {
      java.util.HexFormat.of().parseHex(s);
      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

  @FXML
  public static boolean isBase64(String s) {
    if (s == null)
      return false;
    String t = s.trim();
    int comma = t.indexOf(',');
    if (comma >= 0 && t.substring(0, comma).toLowerCase().contains("base64")) {
      t = t.substring(comma + 1);
    }
    t = t.replaceAll("\\s+", "");
    if (t.isEmpty())
      return false;
    if (t.length() > 10 * 1024 * 1024)
      return false; // proteção contra OOM

    // Tentar decoder — aceita Standard e URL-safe
    try {
      java.util.Base64.getDecoder().decode(t);
      return true;
    } catch (IllegalArgumentException e) {
      try {
        java.util.Base64.getUrlDecoder().decode(t);
        return true;
      } catch (IllegalArgumentException ex) {
        return false;
      }
    }
  }

  private byte[] readSignatureBytes(java.nio.file.Path path) throws java.io.IOException {
    byte[] raw = java.nio.file.Files.readAllBytes(path);

    // detecta se parece binário (bytes não imprimíveis)
    boolean hasNonPrintable = false;
    for (byte b : raw) {
      int v = b & 0xFF;
      if (v < 32 && v != '\n' && v != '\r' && v != '\t') {
        hasNonPrintable = true;
        break;
      }
    }
    if (hasNonPrintable) {
      // já é binário -> retorno direto
      return raw;
    }

    // texto legível -> tentar Base64 primeiro, depois HEX, senão retornar os bytes
    // textuais
    String s = new String(raw, java.nio.charset.StandardCharsets.US_ASCII).trim();
    s = s.replaceAll("\\s+", ""); // remover quebras e espaços

    // tentar Base64
    try {
      return java.util.Base64.getDecoder().decode(s);
    } catch (IllegalArgumentException ex) {
      // não é base64
    }

    // tentar HEX
    try {
      return java.util.HexFormat.of().parseHex(s);
    } catch (IllegalArgumentException ex) {
      // não é hex
    }

    // fallback: retorno dos bytes lidos (texto como bytes)
    return raw;
  }

  /**
   * Inicia o processo de verificação da assinatura.
   */
  @FXML
  private void handleVerifySignature(ActionEvent event) {
    String publicKeyPath = publicKeyPathField.getText();
    String originalPath = originalFilePathField.getText();
    String signaturePath = signatureFilePathField.getText();

    if (publicKeyPath.isEmpty() || originalPath.isEmpty() || signaturePath.isEmpty()) {
      verificationResultLabel.setText("Erro: Preencha todos os caminhos de arquivo.");
      verificationResultLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("ERRO");
      alert.setContentText("SELECIONE TODOS OS ARQUIVOS");
      return;
    }

    System.out.println("Verificando assinatura...");
    try {
      Rsa rsa = new Rsa();
      byte[] file = Files.readAllBytes(filePathMessage);
      System.out.println("READ signature from: " + filePathSign.toAbsolutePath());
      byte[] signature = readSignatureBytes(filePathSign);
      System.out.println("Signature read length = " + signature.length);
      try {
        System.out.println("Signature read hex prefix = "
            + HexFormat.of().formatHex(signature).substring(0, Math.min(64, signature.length * 2)));
      } catch (Exception ex) {
        // ignore
      }
      PublicKey key = rsa.loadPublicKey(filePathKey.toString());

      if (key == null) {
        verificationResultLabel.setText("Erro: chave pública inválida (null)");
        verificationResultLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        return;
      }

      // opção: checar tamanho esperado da assinatura (ex.: RSA-2048 -> 256 bytes)
      if (signature.length < 64) { // limite heurístico
        System.out
            .println("AVISO: assinatura muito curta -> possivelmente texto/format inválido. len=" + signature.length);
      }

      boolean isCoherent = rsa.signatureVerify(file, signature, key);

      if (isCoherent) {
        verificationResultLabel.setText("Resultado da Verificação: ASSINATURA COERENTE!");
        verificationResultLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
      } else {
        verificationResultLabel.setText("Resultado da Verificação: ASSINATURA INVÁLIDA!");
        verificationResultLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}