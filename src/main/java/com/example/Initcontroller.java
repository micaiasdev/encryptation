package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import java.io.IOException;

import javafx.event.ActionEvent;;

public class Initcontroller {

  @FXML
  private MenuItem Dencrypt_AES;

  @FXML
  private MenuItem Encrypt_AES;

  @FXML
  private Button next_button;

  @FXML
  private MenuItem rsa_keys_gen;

  @FXML
  private MenuItem sign_rsa;

  @FXML
  private SplitMenuButton split_menu_button;

  @FXML
  private MenuItem verify_sign_rsa;

  private MenuItem selecItem;

  @FXML
  public void initialize() {
    split_menu_button.getItems().forEach(item -> item.setOnAction(this::onMenuItemSelected));
  }

  private void onMenuItemSelected(ActionEvent event) {
    this.selecItem = (MenuItem) event.getSource();
    String texto = this.selecItem.getText();
    split_menu_button.setText(texto);

  }

  public void nextPage() throws IOException {
    switch (this.selecItem.getText()) {
      case "Encriptação AES":
        App.setRoot("aes_encrypt_page");
        break;
      case "Decriptação AES":
        App.setRoot("aes_decrypt_page");
        break;
      case "Gerar chaves RSA":
        App.setRoot("rsa_key_gen");
        break;
      case "Assinatura RSA":
        App.setRoot("signature");
        break;
      case "Verificar assinatura RSA":
        App.setRoot("signature_verify");
        break;
      default:
        break;
    }

  }

}
