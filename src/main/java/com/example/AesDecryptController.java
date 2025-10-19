package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class AesDecryptController {

  @FXML
  private Button backButton;

  @FXML
  private Button decryptButton;

  @FXML
  private TextField filePathField;

  @FXML
  private ComboBox<?> inputFormatComboBox;

  @FXML
  private TextField ivField;

  @FXML
  private ComboBox<?> ivFormatComboBox;

  @FXML
  private TextField keyField;

  @FXML
  private ComboBox<?> keyFormatComboBox;

  @FXML
  private ComboBox<?> modeComboBox;

  @FXML
  private ComboBox<?> outputFormatComboBox;

  @FXML
  private Button selectFileButton;

  @FXML
  void backButton(ActionEvent event) throws IOException {
    App.setRoot("init");
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
  void modifyValidateBackgroundField(TextField textField, Boolean validation) {
    if (validation)
      textField.setStyle("fx-background-color:#ffc1c1ff");
    else
      textField.setStyle("fx-background-color:#ffffffff");
  }

  @FXML
  Boolean hexFieldValidate(TextField textField, String fieldType) {
    if (fieldType.equals("KEY")) {
      int fieldBytesLen = HexFormat.of().parseHex(textField.getText()).length;
      return (fieldBytesLen == 16 || fieldBytesLen == 24 || fieldBytesLen == 32) && isHex(textField.getText());
    } else {
      int fieldBytesLen = HexFormat.of().parseHex(textField.getText()).length;
      return fieldBytesLen == 16 && isHex(textField.getText());
    }
  }

  @FXML
  void wrongKeyFieldValid(KeyEvent event) {
    // HEXADECIMAL
    // UTF_8
  }

  @FXML
  void wrongKeyFieldValidOnAction(ActionEvent event) {

  }

  @FXML
  void wrongIvFieldValid(KeyEvent event) {

  }

  @FXML
  void wrongIvFieldValidOnAction(ActionEvent event) {

  }

  @FXML
  void keyFieldValid() {

  }

  @FXML
  void ivParameterFiledValid() {

  }

  @FXML
  void handleDecryptFile(ActionEvent event) {

  }

  @FXML
  void handleSelectFile(ActionEvent event) {

  }

}
