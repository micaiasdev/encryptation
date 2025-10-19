package com.example;

import com.crypto.Generatekeys;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser; // Provavelmente DirectoryChooser é mais adequado
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RsaKeyGenController {
  @FXML
  private Path filePathSave;

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

  @FXML
  void selectSavePathCryptoFile() throws IOException {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Selecione a pasta para salvar o arquivo");

    Window stage = generateKeyButton.getScene().getWindow();
    File saveFile = directoryChooser.showDialog(stage);

    if (saveFile != null) {
      filePathSave = Paths.get(saveFile.getAbsolutePath());
      savePathField.setText(saveFile.getAbsolutePath());
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
  private void handleGenerateKeys(ActionEvent event) {
    int keySize = Integer.parseInt(keySizeComboBox.getValue());
    String savePath = savePathField.getText();

    if (savePath.isEmpty() || savePath.equals("Nenhum diretório selecionado")) {
      System.out.println("Erro: Selecione um diretório para salvar as chaves.");
      return;
    }

    try {
      Generatekeys generatekeys = new Generatekeys(keySize);
      generatekeys.createPEMPrivateKey(filePathSave);
      generatekeys.createPEMPublicKey(filePathSave);
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("SUCESSO");
      alert.setContentText("ARQUIVOS SALVOS COM SUCESSO");
      alert.showAndWait();
    } catch (Exception e) {
      alertGenerate("ERRO", e.toString());
      ;
    }

  }

  @FXML
  void backButton() throws IOException {
    App.setRoot("init");
  }
}
