package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser; // Provavelmente DirectoryChooser é mais adequado
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;

public class RsaKeyGenController {

  @FXML
  private ComboBox<String> keySizeComboBox;

  @FXML
  private TextField savePathField;

  @FXML
  private Button selectSavePathButton;

  @FXML
  private Button generateKeyButton;

  // Botão de navegação
  @FXML
  private Button backButton;

  @FXML
  public void initialize() {
    // Garantir que o valor padrão esteja selecionado se não for definido no FXML
    if (keySizeComboBox.getValue() == null && keySizeComboBox.getItems().size() > 0) {
      keySizeComboBox.getSelectionModel().selectFirst();
    }
  }

  /**
   * Manipula a seleção do diretório para salvar as chaves.
   */
  @FXML
  private void handleSelectSavePath(ActionEvent event) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Selecione o Diretório para Salvar as Chaves RSA");

    Window stage = selectSavePathButton.getScene().getWindow();
    File selectedDirectory = directoryChooser.showDialog(stage);

    if (selectedDirectory != null) {
      savePathField.setText(selectedDirectory.getAbsolutePath());
    } else {
      savePathField.setText("Nenhum diretório selecionado");
    }
  }

  /**
   * Inicia o processo de geração das chaves RSA.
   */
  @FXML
  private void handleGenerateKeys(ActionEvent event) {
    String keySize = keySizeComboBox.getValue();
    String savePath = savePathField.getText();

    if (savePath.isEmpty() || savePath.equals("Nenhum diretório selecionado")) {
      System.out.println("Erro: Selecione um diretório para salvar as chaves.");
      return;
    }

    System.out.println("Gerando chaves RSA de " + keySize + " bits em: " + savePath);
    // Implemente a lógica de geração e salvamento de chaves RSA aqui.
  }

  @FXML
  void backButton() throws IOException {
    App.setRoot("init");
  }
}
