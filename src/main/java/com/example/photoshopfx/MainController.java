package com.example.photoshopfx;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    public int num = 1;
    @FXML
    public Menu menuTransformacao;

    @FXML
    public MenuItem onSalvarId;

    @FXML
    public MenuItem onSalvarComoId;

    @FXML
    private boolean imageChanged=false;

    @FXML
    public ImageView imagemView;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(num == 1)
            travar();
    }

    public void onAbrir(ActionEvent actionEvent) {
        FileChooser fileChooser;
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory((new File("/home/victor/Downloads")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todas as Imagens", "*.jpeg","*.jpg","*.GIF","*.png")
                ,new FileChooser.ExtensionFilter("JPG", "*.jpg")
                ,new FileChooser.ExtensionFilter("PNG", "*.png")
                ,new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file!=null){
            Image image;
            image = new Image(file.toURI().toString());
            imagemView.setImage(image);
            imagemView.setFitHeight(1000);
            imagemView.setFitWidth(1000); // OU imagemView.setFitWidth(1000)
            destravar();
        }
        else{
            travar();
        }
    }

    public void onSalvarComo(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File arq = fc.showSaveDialog(null);
        if(arq != null){
            BufferedImage bimg;
            bimg = SwingFXUtils.fromFXImage(imagemView.getImage(),null);
            BufferedImage copy = new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = copy.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
            g2d.drawImage(bimg, 0, 0, null);
            g2d.dispose();
            try{
                ImageIO.write(bimg,"png",arq);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("A imagem foi salva com sucesso!");
                alert.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onSalvar(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Deseja realmente Salvar imagem a imagem?");
        ButtonType salvar = new ButtonType("Salvar");
        ButtonType cancelar = new ButtonType("Cancelar");
        alert.getButtonTypes().setAll(salvar,cancelar);
        alert.showAndWait().ifPresent(response -> {
            if (response == salvar) {
                File arq = new File("/home/victor/Downloads/copia.jpg");
                BufferedImage bimg;
                bimg = SwingFXUtils.fromFXImage(imagemView.getImage(),null);
                BufferedImage copy = new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = copy.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
                g2d.drawImage(bimg, 0, 0, null);
                g2d.dispose();
                try {
                    ImageIO.write(bimg,"png",arq);
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setContentText("A imagem foi salva com sucesso!");
                    alert2.showAndWait();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                alert.close();
        });
    }

    public void onSair(ActionEvent actionEvent) {
        if(!imageChanged)
            Platform.exit();
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Salvar Alterações");
            alert.setHeaderText("Você tem alterações não salvas na imagem.");
            alert.setContentText("Deseja salvar?");
            ButtonType save = new ButtonType("Salvar");
            ButtonType notsave = new ButtonType("Não Salvar");
            ButtonType cancel = new ButtonType("Cancelar");

            alert.getButtonTypes().setAll(save,notsave,cancel);
            alert.showAndWait().ifPresent(response ->{
                if(response == save)
                    onSalvarComo(actionEvent);
                else if (response == notsave)
                    Platform.exit();
                else
                    actionEvent.consume();
            });
        }
    }

    public void onTonsCinza(ActionEvent actionEvent){
        Image image = imagemView.getImage();
        image = Conversora.tonsCinza(image);
        imagemView.setImage(image);
        imageChanged = true;
    }

    public void onPretoBranco(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.pretoBranco(image);
        imagemView.setImage(image);
        imageChanged = true;
    }

    public void onEspelharHorizontal(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.inverter(image);
        imagemView.setImage(image);
        imageChanged = true;
    }

    public void onEspelhaVertical(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.vertical(image);
        imagemView.setImage(image);
        imageChanged = true;
    }

    public void onNegativo(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.negativo(image);
        imagemView.setImage(image);
        imageChanged = true;
    }

    public void onSobre(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("Informções do aplicativo e desenvolvedores");
        alert.setContentText("Informções do aplicativo:\nAplicativo de PhotoShop, no qual você pode fazer alterações na imagem.\n\nInformações de desenolvedores:\nDesenvolvido por: Victor \nCom o auxilio do professor Silvio");
        alert.showAndWait();
    }

    public void travar(){
        menuTransformacao.setDisable(true);
        onSalvarComoId.setDisable(true);
        onSalvarId.setDisable(true);
    }

    public void destravar(){
        menuTransformacao.setDisable(false);
        onSalvarComoId.setDisable(false);
        onSalvarId.setDisable(false);
    }

    public void onDetectarBorda(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.detectarBordasij(image);
        imagemView.setImage(image);
    }

    public void onBlur(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.detectarBlur(image);
        imagemView.setImage(image);
    }

    public void onThreshold(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.detectarThereshold(image);
        imagemView.setImage(image);
    }

    public void onDilate(ActionEvent actionEvent) {
        Image image = imagemView.getImage();
        image = Conversora.detectarDilate(image);
        imagemView.setImage(image);
    }

    public void btAbrir(ActionEvent actionEvent) {
        onAbrir(actionEvent);
    }

    public void btSalvar(ActionEvent actionEvent) {
        onSalvar(actionEvent);
    }

    public void btHorizontal(ActionEvent actionEvent) {
        onEspelharHorizontal(actionEvent);
    }

    public void btVertical(ActionEvent actionEvent) {
        onEspelhaVertical(actionEvent);
    }

    public void btInfo(ActionEvent actionEvent) {
        onSobre(actionEvent);
    }
}