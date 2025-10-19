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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;

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
    openFileChooserAndSetPath("SIGN", originalFilePathField, "Selecione o Arquivo em Claro (Original)",
        new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*"));
    verificationResultLabel.setText("Resultado da Verificação: Aguardando...");
  }

  @FXML
  private void handleSelectSignatureFile(ActionEvent event) {
    openFileChooserAndSetPath("Message", signatureFilePathField, "Selecione o Arquivo da Assinatura Digital",
        new FileChooser.ExtensionFilter("Signature Files", "*.sig", "*.bin", "*.txt"));
    verificationResultLabel.setText("Resultado da Verificação: Aguardando...");
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
      byte[] signature = Files.readAllBytes(filePathSign);
      PublicKey key = rsa.loadPublicKey(filePathKey.toString());

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