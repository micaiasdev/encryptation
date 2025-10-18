package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;

public class FileSigningController {

  @FXML
  private Button backButton;

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
  private void openFileChooserAndSetPath(TextField field, String title, FileChooser.ExtensionFilter... filters) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    fileChooser.getExtensionFilters().addAll(filters);

    // Obtém a janela atual para exibir o diálogo (necessário para diálogos modais)
    Window stage = field.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      field.setText(selectedFile.getAbsolutePath());
    }
  }

  // --- Métodos de Manipulação de Arquivo ---

  @FXML
  private void handleSelectPrivateKeyFile(ActionEvent event) {
    openFileChooserAndSetPath(privateKeyPathField, "Selecione o Arquivo de Chave Privada",
        new FileChooser.ExtensionFilter("Key Files", "*.key", "*.pem", "*.p8"));
  }

  @FXML
  private void handleSelectFileToSign(ActionEvent event) {
    openFileChooserAndSetPath(filePathToSignField, "Selecione o Arquivo a ser Assinado",
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
    }
  }

  /**
   * Inicia o processo de assinatura digital.
   */
  @FXML
  private void handleSignFile(ActionEvent event) {
    String privateKeyPath = privateKeyPathField.getText();
    String filePath = filePathToSignField.getText();
    String savePath = signatureSavePathField.getText();

    if (privateKeyPath.isEmpty() || filePath.isEmpty() || savePath.isEmpty()) {
      System.out.println("Erro: Todos os caminhos de arquivo devem ser preenchidos.");
      return;
    }

    System.out.println("Iniciando assinatura do arquivo: " + filePath);
    System.out.println("Usando chave privada: " + privateKeyPath);
    System.out.println("Salvando assinatura em: " + savePath);
    // Implemente a lógica de assinatura aqui.
  }

  @FXML
  void backPage() throws IOException {
    App.setRoot("init");
  }
}