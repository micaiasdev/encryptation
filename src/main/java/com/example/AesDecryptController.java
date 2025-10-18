package com.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
  void handleDecryptFile(ActionEvent event) {

  }

  @FXML
  void handleSelectFile(ActionEvent event) {

  }

}
