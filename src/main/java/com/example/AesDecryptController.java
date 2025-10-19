package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
