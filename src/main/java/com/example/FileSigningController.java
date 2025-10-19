package com.example;

import com.crypto.Rsa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class FileSigningController {

  @FXML
  private Button backButton;

  @FXML
  private Path filePathSave;

  @FXML
  private Path filePathKeySelected;

  @FXML
  private Path filePathSelected;

  @FXML
  private TextField privateKeyPathField;

  @FXML
  private Button selectPrivateKeyButton;

  @FXML
  private TextField filePathToSignField;

  @FXML
  private Button selectFileToSignButton;

  @FXML
  private TextField signatureSavePathField;

  @FXML
  private Button selectSignatureSavePathButton;

  @FXML
  private Button signFileButton;

  // Helper para abrir o FileChooser e definir o campo de texto
  private void openFileChooserAndSetPath(String pathType, TextField field, String title,
      FileChooser.ExtensionFilter... filters) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().addAll(filters);

    Window stage = field.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      field.setText(selectedFile.getAbsolutePath());
      if (pathType.equalsIgnoreCase("KEY"))
        filePathKeySelected = Paths.get(selectedFile.getAbsolutePath());
      else
        filePathSelected = Paths.get(selectedFile.getAbsolutePath());
    }
  }

  // --- Métodos de Manipulação de Arquivo ---

  @FXML
  private void handleSelectPrivateKeyFile(ActionEvent event) {
    openFileChooserAndSetPath("KEY", privateKeyPathField, "Selecione o Arquivo de Chave Privada",
        new FileChooser.ExtensionFilter("Key Files", "*.key", "*.pem", "*.p8"));
  }

  @FXML
  private void handleSelectFileToSign(ActionEvent event) {
    openFileChooserAndSetPath("FILE", filePathToSignField, "Selecione o Arquivo a ser Assinado",
        new FileChooser.ExtensionFilter("Todos os Arquivos", "*.txt*"));
  }

  @FXML
  private void handleSelectSignatureSavePath(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Salvar Assinatura Digital");
    fileChooser.setInitialFileName("arquivo_assinatura.sig");

    Window stage = signatureSavePathField.getScene().getWindow();
    File saveFile = fileChooser.showSaveDialog(stage);

    if (saveFile != null) {
      signatureSavePathField.setText(saveFile.getAbsolutePath());
      filePathSave = Paths.get(saveFile.getAbsolutePath());
    }
  }

  @FXML
  void alertGenerate(String title, String content) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(content);
    alert.showAndWait();

  }

  @FXML
  private void handleSignFile(ActionEvent event) {
    String privateKeyPath = privateKeyPathField.getText();
    String filePath = filePathToSignField.getText();
    String savePath = signatureSavePathField.getText();

    if (privateKeyPath.isEmpty() || filePath.isEmpty() || savePath.isEmpty()) {
      alertGenerate("ARQUIVOS NÃO SELECIONADO", "SELECIONE TODOS OS CAMINHOS");
      return;
    }

    System.out.println("Iniciando assinatura do arquivo: " + filePath);
    System.out.println("Usando chave privada: " + privateKeyPath);
    System.out.println("Salvando assinatura em: " + savePath);

    try {
      Rsa rsa = new Rsa();
      PrivateKey privateKey = rsa.loadPrivateKey(filePathKeySelected.toString());

      Signature signature = rsa.signature(Files.readAllBytes(filePathSelected), privateKey);
      Files.write(filePathSave, signature.sign());

      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("SUCESSO!");
      alert.setContentText("ARQUIVO SALVO COM SUCESSO");
      alert.showAndWait();
    } catch (Exception e) {
      alertGenerate("ERRO NA ASSINATURA", e.toString());
    }
  }

  @FXML
  void backPage() throws IOException {
    App.setRoot("init");
  }
}