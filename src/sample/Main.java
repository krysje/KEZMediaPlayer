package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.print.attribute.standard.Media;
import java.awt.*;
import java.io.FileInputStream;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller mycontroller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setTitle("KamEz media player");


        scene.setFill(Color.BLACK);


        //Adding Keyboard Shortcuts


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE ) {
                    MediaPlayer.Status status = mycontroller.player.getStatus();

                    if (status == MediaPlayer.Status.PLAYING) {
                        mycontroller.player.pause();

                        // playBtn.setText("Play");
                    } else {
                        mycontroller.player.play();
                    }
                        //Stop letting it do anything else
                    keyEvent.consume();
                }

                if (keyEvent.getCode() == KeyCode.UP) {
                    double d = mycontroller.player.getCurrentTime().toSeconds();
                    d = d + 15;

                    mycontroller.player.seek(new Duration(d*1000));
                    keyEvent.consume();

                }

                if (keyEvent.getCode() == KeyCode.DOWN) {
                    double d = mycontroller.player.getCurrentTime().toSeconds();

                    d = d - 15;

                    mycontroller.player.seek(new Duration(d*1000));
                    keyEvent.consume();

                }
                if (keyEvent.getCode() == KeyCode.F) {
                    if(primaryStage.isFullScreen()){
                        primaryStage.setFullScreen(false);
                    }

                    else
                        primaryStage.setFullScreen(true);

                    keyEvent.consume();

                }


            }
        });


//        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent dblClck) {
//                if(dblClck.getClickCount() == 2){
//                    if(primaryStage.isFullScreen()){
//                        primaryStage.setFullScreen(false);
//                    }
//
//                    else
//                        primaryStage.setFullScreen(true);
//
//
//                }}
//        });

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
