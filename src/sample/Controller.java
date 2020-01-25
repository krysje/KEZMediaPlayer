package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.floor;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;

public class Controller implements Initializable{


    MediaPlayer player;
    Duration duration;

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox vBox;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button playBtn;

    @FXML
    private Button preBtn;

    @FXML
    private Button nxtBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Button skipb15;

    @FXML
    private Button skipa15;

    @FXML
    private Button fast;

    @FXML
    private Button slow;

    @FXML
    private Button reload;

    @FXML
    private Slider timeSlider;

    @FXML
    private Button rateField;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label time;

    @FXML
    private Label timeRemaining;

    @FXML
    private MenuBar menuBar;



    @FXML
    void openFileMenu(ActionEvent event) {
        try {
            //System.out.println("open btn clicked");
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);

            Media m = new Media(file.toURI().toURL().toString());


            if(player != null){
                player.dispose();
            }

            player = new MediaPlayer(m);

            mediaView.setMediaPlayer(player);
            player.setAutoPlay(true);
            player.setRate(1);



            //Preserve height and width ratio
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(),"height"));
            mediaView.setPreserveRatio(true);

            //volume slider
            player.setVolume(0.6);
            volumeSlider.setValue(player.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    player.setVolume(volumeSlider.getValue()/100);
                }
            });


            //System.out.println(player.getRate());

            //time slider

            player.setOnReady(()->{
                //when player gets ready..
                duration = player.getMedia().getDuration();
                updateValues();

                Duration timeRem = m.getDuration();
                // System.out.println(floor(m.getDuration().toSeconds()));

                timeRemaining.setText(formatTimeDuration(timeRem));



                mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent dblClck) {
                        if(dblClck.getClickCount() == 2){
                            if(menuBar.isVisible() && vBox.isVisible()) {
                                menuBar.setVisible(false);
                                vBox.setVisible(false);
                            }

                            else if(!menuBar.isVisible() && !vBox.isVisible()){
                                menuBar.setVisible(true);
                                vBox.setVisible(true);
                            }}}
                });


                timeSlider.setMin(0);
                timeSlider.setMax(player.getMedia().getDuration().toMinutes());
                // System.out.println(player.getMedia().getDuration().toMinutes());
                timeSlider.setValue(0);
                try{
                    playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.png"))));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            });


            // Listener on player..
            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    Duration d = player.getCurrentTime();

                    timeSlider.setValue(d.toMinutes());
                }
            });

            //Listener on Time
            player.currentTimeProperty().addListener((Observable ov) -> {
                updateValues();
            });


            //Skip Property
            //Listener on time Slider

            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if(timeSlider.isPressed()){

                        double val = timeSlider.getValue();
                        val = val*60;
                        //System.out.println(val*60*1000);
                        player.seek(new Duration(val*1000));
                    }}
            });



        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateValues() {
        if (time != null) {
            runLater(() -> {
                Duration currentTime = player.getCurrentTime();
                time.setText(formatTime(currentTime));

            });
        }
    }



    private static String formatTimeDuration(Duration duration) {


        int intDuration = (int) floor(duration.toSeconds());

        int durationHours = intDuration / (60 * 60);


        if (durationHours > 0) {
            intDuration = intDuration - durationHours * 60 * 60;
        }


        int durationMinutes = intDuration / 60;

        int durationSeconds = intDuration  - durationMinutes * 60;


        if (durationHours > 0) {
            return format("%d:%02d:%02d", durationHours, durationMinutes, durationSeconds);
        } else {
            return format("%02d:%02d",
                    durationMinutes, durationSeconds);
        }
    }





    private static String formatTime(Duration elapsed) {
        int intElapsed = (int) floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;

        int elapsedSeconds = intElapsed - elapsedMinutes * 60;

        if (elapsedHours > 0) {
            return format("%d:%02d:%02d", elapsedHours,
                    elapsedMinutes, elapsedSeconds);
        } else {
            return format("%02d:%02d", elapsedMinutes,
                    elapsedSeconds);
        }
    }



    @FXML
    void QuitBtnClick(ActionEvent event) {
        System.exit(0);
    }



    @FXML
    void play(ActionEvent event) {

        try {
            MediaPlayer.Status status = player.getStatus();



            if (status == MediaPlayer.Status.PLAYING) {
                player.pause();
                // playBtn.setText("Play");
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
            } else {
                player.play();
                //player.setRate(1);
                // playBtn.setText("Pause");
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.png"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
//            preBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/previous.png"))));
//            preBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/previous.png"))));
//            nxtBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/next.png"))));
//            stopBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/stop.png"))));
//            skipb15.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/skipb15.png"))));
//            skipa15.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/skipa15.png"))));
//            slow.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/slow.png"))));
//            fast.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/fast.png"))));
//
//
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void fwdBtnClick(ActionEvent event) {

        double d = player.getCurrentTime().toSeconds();

        d = d + 15;

        player.seek(new Duration(d*1000));

    }


    @FXML
    void rwndBtnClick(ActionEvent event) {
        double d = player.getCurrentTime().toSeconds();
        d = d - 15;

        player.seek(new Duration(d*1000));

    }

    @FXML
    void stopBtnClick(ActionEvent event) {
        player.stop();
        try {
            playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void fastBtnClick(ActionEvent event) {
        player.setRate(player.getRate()+0.25);
        rateField.setText(player.getRate()+"x");
    }

    @FXML
    void slowBtnClick(ActionEvent event) {
        player.setRate(player.getRate()-0.25);
        rateField.setText(player.getRate()+"x");
    }

    @FXML
    void rateBtnClick(ActionEvent event) {
        player.setRate(1);
        rateField.setText("1x");
    }


    @FXML
    void reloadBtnClick(ActionEvent event) {
        player.seek(player.getStartTime());
    }


}