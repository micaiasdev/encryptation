package com.example;

import com.crypto.Aes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class AesEncryptController {
  @FXML
  private MyFile file;

  @FXML
  private Button backPageButton;

  @FXML
  private Button encryptButton;

  @FXML
  private TextField filePathField;

  @FXML
  private Path filePathSelected;

  @FXML
  private Path filePathSave;

  @FXML
  private ComboBox<String> inputFormatComboBox;

  @FXML
  private TextField ivField;

  @FXML
  private ComboBox<String> ivFormatComboBox;

  @FXML
  private TextField keyField;

  @FXML
  private ComboBox<String> keyFormatComboBox;

  @FXML
  private ComboBox<String> modeComboBox;

  @FXML
  private ComboBox<String> outputFormatComboBox;

  @FXML
  private Button selectFileButton;

  @FXML
  void backPage(ActionEvent event) throws IOException {
    App.setRoot("init");
  }

  @FXML
  String getKeyFormatSelected() {
    return keyFormatComboBox.getValue().toUpperCase();
  }

  @FXML
  String getIvParameterFormatSelected() {
    return ivFormatComboBox.getValue().toUpperCase();
  }

  @FXML
  String getModeEncryptFormatSelected() {
    return modeComboBox.getValue().toUpperCase();
  }

  @FXML
  String getInputFormatSelected() {
    return inputFormatComboBox.getValue().toUpperCase();
  }

  @FXML
  String getOutputFormatSelected() {
    return outputFormatComboBox.getValue().toUpperCase();
  }

  @FXML
  void wrongValueField(TextField textField, int multiple, String typeField) {
    int bytesTextFieldLen = textField.getText().getBytes(StandardCharsets.UTF_8).length;
    Boolean additional;
    if (multiple == 2)
      additional = !isHex(textField.getText());
    else
      additional = false;
    Boolean verifyKey = !(bytesTextFieldLen == 16 * multiple || bytesTextFieldLen == 24 * multiple
        || bytesTextFieldLen == 32 * multiple);
    Boolean verifyIv = !(bytesTextFieldLen == 16 * multiple);

    if (typeField.equals("KEY")) {
      if (verifyKey || additional) {
        textField.setStyle("-fx-background-color: #ffc1c1ff");
      } else
        textField.setStyle("-fx-background-color: #ffffffff");
    } else {
      if (verifyIv || additional)
        textField.setStyle("-fx-background-color: #ffc1c1ff");
      else
        textField.setStyle("-fx-background-color: #ffffffff");
    }
  }

  @FXML
  void wrongKeyField(KeyEvent event) {
    if (keyFormatComboBox.getValue().equalsIgnoreCase("UTF-8"))
      wrongValueField(keyField, 1, "KEY");
    else {
      wrongValueField(keyField, 2, "KEY");
      System.out.println(isHex(keyField.getText()));
    }
  }

  @FXML
  void wrongIvField(KeyEvent event) {
    if (ivFormatComboBox.getValue().equalsIgnoreCase(("UTF-8")))
      wrongValueField(ivField, 1, "IV");
    else
      wrongValueField(ivField, 2, "IV");
  }

  @FXML
  void wrongKeyFieldAction(ActionEvent event) {
    if (keyFormatComboBox.getValue().equals("UTF-8"))
      wrongValueField(keyField, 1, "KEY");
    else
      wrongValueField(keyField, 2, "KEY");
  }

  @FXML
  void wrongIvFieldAction(ActionEvent event) {
    if (ivFormatComboBox.getValue().equals("UTF-8"))
      wrongValueField(ivField, 1, "IV");
    else
      wrongValueField(ivField, 2, "IV");
  }

  @FXML
  Boolean validKeyField() {
    int byteskeyLen = keyField.getText().getBytes(StandardCharsets.UTF_8).length;
    if (getKeyFormatSelected().equals("UTF-8")) {
      if (!(byteskeyLen == 16 || byteskeyLen == 24 || byteskeyLen == 32))
        return false;
      else
        return true;
    } else {
      if (!(byteskeyLen == 32 || byteskeyLen == 48 || byteskeyLen == 64) || !isHex(keyField.getText()))
        return false;
      else
        return true;
    }
  }

  @FXML
  Boolean validIvField() {
    int bytesIvLen = ivField.getText().getBytes(StandardCharsets.UTF_8).length;
    if (getIvParameterFormatSelected().equals("UTF-8")) {
      if (!(bytesIvLen == 16)) {
        return false;
      } else {
        return true;
      }
    } else {
      if (!(bytesIvLen == 32) || !isHex(ivField.getText())) {
        return false;
      } else
        return true;
    }
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

  @FXML
  Boolean validFileFormat() throws IOException {
    if (filePathSelected == null)
      return false;
    String fileContent = new String(Files.readAllBytes(filePathSelected), java.nio.charset.StandardCharsets.US_ASCII);
    String inputFormat = inputFormatComboBox == null ? null : inputFormatComboBox.getValue();
    if (inputFormat == null)
      return false;

    if (inputFormat.equalsIgnoreCase("HEX")) {
      return isHex(fileContent);
    } else {
      return isBase64(fileContent);
    }
  }

  @FXML
  void alertGenerate(String title, String content) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(content);
    alert.showAndWait();

  }

  void validAllInputs() throws IOException {
    try {
      if (!validFileFormat()) {
        alertGenerate("Formato de entrada inválido",
            "Verifique se você selecionou corretamente o formato de entrada.\nFormatos suportados: Hexadecimal e Base64");
        throw new IllegalArgumentException("Invalid File Format");
      }
      if (!validKeyField()) {
        alertGenerate("Tamanho de chave inválido",
            "O tamanho da chave é inválido. O AES aceita 128, 192 ou 256 bits (16, 24 ou 32 bytes). Verifique o tamanho informado.");
        throw new IllegalArgumentException("Invalid key");
      }
      if (!validIvField()) {
        alertGenerate("IV inválido",
            "O vetor de inicialização (IV) tem tamanho inválido. Para AES utilize 16 bytes (128 bits). Verifique o valor fornecido.");
        throw new IllegalArgumentException("Iv Parameter Invalid");
      }
    } catch (NullPointerException e) {
      alertGenerate("Arquivo não selecionado", "Selecione um arquivo");
    }
  }

  @FXML
  void handleSelectFile(ActionEvent event) {
    try {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Selecione o Arquivo para Fazer a Encriptação");
      Window stage = selectFileButton.getScene().getWindow();
      File selectedFile = fileChooser.showOpenDialog(stage);
      if (selectedFile == null)
        return;
      filePathField.setText(selectedFile.getAbsolutePath());
      filePathSelected = Paths.get(selectedFile.getAbsolutePath());
    } catch (Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Arquivo não selecionado");
      alert.setContentText("Selecione um arquivo para continuar");
      System.out.println(e);
    }
  }

  @FXML
  void selectSavePathCryptoFile() throws IOException {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Selecione a pasta para salvar o arquivo");
    fileChooser.setInitialFileName("Arquivo Cryptografado.txt");

    Window stage = encryptButton.getScene().getWindow();
    File saveFile = fileChooser.showSaveDialog(stage);

    if (saveFile != null)
      filePathSave = Paths.get(saveFile.getAbsolutePath());
  }

  @FXML
  void encryptionFile() throws Exception {
    try {
      Aes aes;
      validAllInputs();
      MyFile file = new MyFile(filePathSelected);
      if (getKeyFormatSelected().equals("HEX")) {
        if (getModeEncryptFormatSelected().equals("CBC"))
          aes = new Aes(HexFormat.of().parseHex(keyField.getText()),
              HexFormat.of().parseHex(ivField.getText()), getModeEncryptFormatSelected());
        else
          aes = new Aes(HexFormat.of().parseHex(keyField.getText()), getModeEncryptFormatSelected());
      } else {
        if (getModeEncryptFormatSelected().equals("CBC"))
          aes = new Aes(HexFormat.of().parseHex(keyField.getText()),
              keyField.getText().getBytes(StandardCharsets.UTF_8), getModeEncryptFormatSelected());
        else
          aes = new Aes(keyField.getText().getBytes(StandardCharsets.UTF_8), getModeEncryptFormatSelected());
      }
      MyFile encryptFile = new MyFile(aes.encypt(file.getContent()));
      selectSavePathCryptoFile();
      if (getOutputFormatSelected().equalsIgnoreCase("HEX")) {
        Files.writeString(filePathSave, encryptFile.getHexEncode());
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Arquivo salvo com sucesso");
        alert.showAndWait();
      } else {
        Files.writeString(filePathSave, encryptFile.getBase64Encode());
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Arquivo salvo com sucesso");
        alert.showAndWait();
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e);
    }

  }

}
