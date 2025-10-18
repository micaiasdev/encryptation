package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;

public class SignatureVerificationController {

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
  private void openFileChooserAndSetPath(TextField field, String title, FileChooser.ExtensionFilter... filters) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().addAll(filters);

    Window stage = field.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      field.setText(selectedFile.getAbsolutePath());
    }
  }

  // --- Métodos de Manipulação de Arquivo ---

  @FXML
  private void handleSelectPublicKeyFile(ActionEvent event) {
    openFileChooserAndSetPath(publicKeyPathField, "Selecione o Arquivo de Chave Pública",
        new FileChooser.ExtensionFilter("Public Key Files", "*.pub", "*.pem"));
    verificationResultLabel.setText("Resultado da Verificação: Aguardando...");
  }

  @FXML
  private void handleSelectOriginalFile(ActionEvent event) {
    openFileChooserAndSetPath(originalFilePathField, "Selecione o Arquivo em Claro (Original)",
        new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*"));
    verificationResultLabel.setText("Resultado da Verificação: Aguardando...");
  }

  @FXML
  private void handleSelectSignatureFile(ActionEvent event) {
    openFileChooserAndSetPath(signatureFilePathField, "Selecione o Arquivo da Assinatura Digital",
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
      return;
    }

    System.out.println("Verificando assinatura...");
    // Implemente a lógica de verificação aqui.
    boolean isCoherent = Math.random() < 0.5; // Exemplo de lógica fictícia

    if (isCoherent) {
      verificationResultLabel.setText("Resultado da Verificação: ASSINATURA COERENTE!");
      verificationResultLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
    } else {
      verificationResultLabel.setText("Resultado da Verificação: ASSINATURA INVÁLIDA!");
      verificationResultLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
    }
  }
}