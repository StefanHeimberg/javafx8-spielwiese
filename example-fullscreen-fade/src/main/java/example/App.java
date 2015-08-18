/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final Logger LOG = Logger.getLogger(App.class.getName());

    private static final String DEFAULT = "media/default.jpg";
    private static final String IMAGE = "media/image%s.jpg";
    private static final String MOVIE = "media/movie%s.mp4";

    private static final int MAX_SCREEN_WIDTH = 1280;
    private static final int MAX_SCREEN_HEIGHT = 720;
    
    public static void main(String[] args) {
        launch(args);
    }

    private final ImageView imageView = new ImageView();
    private final MediaView mediaView1 = new MediaView();
    private final MediaView mediaView2 = new MediaView();
    
    private Node prevNode;
    private MediaView currentMediaView;
    private MediaView nextMediaView;
    
    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");

        final MenuBar menuBar = new MenuBar(
                new Menu("Media", null,
                        createMenuItem("Image 1", KeyCode.DIGIT1, (ActionEvent event) -> {
                            setImage(loadImage(String.format(IMAGE, 1)));
                        }),
                        createMenuItem("Image 2", KeyCode.DIGIT2, (ActionEvent event) -> {
                            setImage(loadImage(String.format(IMAGE, 2)));
                        }),
                        new SeparatorMenuItem(),
                        createMenuItem("Movie 1", KeyCode.DIGIT3, (ActionEvent event) -> {
                            setMedia(loadMedia(String.format(MOVIE, 1)));
                        }),
                        createMenuItem("Movie 2", KeyCode.DIGIT4, (ActionEvent event) -> {
                            setMedia(loadMedia(String.format(MOVIE, 2)));
                        })
                ),
                new Menu("Window", null,
                        createMenuItem("Fullscreen", KeyCode.F, (ActionEvent event) -> {
                            primaryStage.setFullScreen(!primaryStage.isFullScreen());
                        })
                )
        );
        menuBar.setUseSystemMenuBar(true);

        // Resize Binding 
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        mediaView1.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView1.fitWidthProperty().bind(primaryStage.widthProperty());
        mediaView2.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView2.fitWidthProperty().bind(primaryStage.widthProperty());
        
        // Mediaplayer Binding
        //        imageView.imageProperty().addListener((ObservableValue<? extends Image> observable, Image oldValue, Image newValue) -> {
        //            final MediaPlayer mediaPlayer1 = mediaView1.getMediaPlayer();
        //            if(null != mediaPlayer1 && mediaPlayer1.getStatus() == MediaPlayer.Status.PLAYING) {
        //                mediaPlayer1.stop();
        //            }
        //            final MediaPlayer mediaPlayer2 = mediaView2.getMediaPlayer();
        //            if(null != mediaPlayer2 && mediaPlayer2.getStatus() == MediaPlayer.Status.PLAYING) {
        //                mediaPlayer2.stop();
        //            }
        //        });
        

        currentMediaView = mediaView1;
        nextMediaView = mediaView2;

        final Pane pane = new StackPane();
        pane.getChildren().add(menuBar);
        pane.getChildren().add(imageView);
        pane.getChildren().add(mediaView1);
        pane.getChildren().add(mediaView2);

        setImage(loadImage(DEFAULT));

        primaryStage.setScene(new Scene(pane, MAX_SCREEN_WIDTH / 3 * 2, MAX_SCREEN_HEIGHT / 3 * 2));
        primaryStage.show();
    }

    private void setImage(final Image image) {
        final Image oldImage = imageView.getImage();
        final Image newImage = image;

        if (oldImage == null || oldImage == newImage) {
            imageView.setImage(newImage);
        } else {
            new FadeTransitionBuilder()
                    .withDefaults()
                    .withFrom(prevNode)
                    .withToImage(imageView, newImage)
                    .build()
                    .play();
        }
        prevNode = imageView;
    }

    private void setMedia(final Media media) {
        final Media oldMedia;
        if (null == currentMediaView.getMediaPlayer()) {
            oldMedia = null;
        } else {
            oldMedia = currentMediaView.getMediaPlayer().getMedia();
        }

        final Media newMedia = media;

        if (oldMedia == null || oldMedia == newMedia) {
            final MediaPlayer mediaPlayer = new MediaPlayer(newMedia);
            mediaPlayer.setMute(true);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            currentMediaView.setMediaPlayer(mediaPlayer);
        } else {
            new FadeTransitionBuilder()
                    .withDefaults()
                    .withFrom(prevNode)
                    .withToMedia(nextMediaView, newMedia)
                    .build()
                    .play();
        }
        prevNode = nextMediaView;
        nextMediaView = currentMediaView;
        currentMediaView = (MediaView) prevNode;
    }
    
    private static Image loadImage(final String filePath) {
        try (final InputStream imageIS = new FileInputStream(filePath)) {
            if (null == imageIS) {
                // nothing to set. image does not exists
                return null;
            }
            return new Image(imageIS, MAX_SCREEN_WIDTH, MAX_SCREEN_HEIGHT, true, true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static Media loadMedia(final String mediaPath) {
        return new Media(new File(mediaPath).toURI().toString());
    }

    private static MenuItem createMenuItem(final String label, final KeyCode keyCode, final EventHandler<ActionEvent> eventHandler) {
        final MenuItem menuItem = new MenuItem(label);
        menuItem.setAccelerator(new KeyCodeCombination(keyCode, KeyCombination.SHORTCUT_DOWN));
        menuItem.setOnAction(eventHandler);
        return menuItem;
    }

}
