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
import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final Logger LOG = Logger.getLogger(App.class.getName());

    private static final String IMAGE = "media/image%s.jpg";
    private static final String MOVIE = "media/movie%s.mp4";

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    public static void main(String[] args) {
        launch(args);
    }

    private ImageView imageView;
    private MediaView mediaView1;
    private MediaView mediaView2;
    private Node prevNode;
    private MediaView currentMediaView;
    private MediaView nextMediaView;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");
        primaryStage.setResizable(true);

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());

        mediaView1 = new MediaView();
        mediaView1.setPreserveRatio(true);
        mediaView1.setSmooth(true);
        mediaView1.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView1.fitWidthProperty().bind(primaryStage.widthProperty());

        mediaView2 = new MediaView();
        mediaView2.setPreserveRatio(true);
        mediaView2.setSmooth(true);
        mediaView2.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView2.fitWidthProperty().bind(primaryStage.widthProperty());

        currentMediaView = mediaView1;
        nextMediaView = mediaView2;

        final MenuItem image1MenuItem = new MenuItem("Image 1");
        image1MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHORTCUT_DOWN));
        image1MenuItem.setOnAction((ActionEvent event) -> {
            setImage(loadImage(1));
        });

        final MenuItem image2MenuItem = new MenuItem("Image 2");
        image2MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.SHORTCUT_DOWN));
        image2MenuItem.setOnAction((ActionEvent event) -> {
            setImage(loadImage(2));
        });

        final MenuItem movie1MenuItem = new MenuItem("Movie 1");
        movie1MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.SHORTCUT_DOWN));
        movie1MenuItem.setOnAction((ActionEvent event) -> {
            setMedia(loadMedia(1));
        });

        final MenuItem movie2MenuItem = new MenuItem("Movie 2");
        movie2MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.SHORTCUT_DOWN));
        movie2MenuItem.setOnAction((ActionEvent event) -> {
            setMedia(loadMedia(2));
        });

        final MenuItem fullscreen = new MenuItem("Fullscreen");
        fullscreen.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN));
        fullscreen.setOnAction((ActionEvent event) -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
        });

        final Menu mediaMenu = new Menu("Media");
        mediaMenu.getItems().add(image1MenuItem);
        mediaMenu.getItems().add(image2MenuItem);
        mediaMenu.getItems().add(new SeparatorMenuItem());
        mediaMenu.getItems().add(movie1MenuItem);
        mediaMenu.getItems().add(movie2MenuItem);

        final Menu windowMenu = new Menu("Window");
        windowMenu.getItems().add(fullscreen);

        final MenuBar menuBar = new MenuBar(mediaMenu, windowMenu);
        menuBar.useSystemMenuBarProperty().set(true);

        final Scene scene = new Scene(new Pane(menuBar, imageView, mediaView1, mediaView2), SCREEN_WIDTH / 3 * 2, SCREEN_HEIGHT / 3 * 2);
        scene.setFill(Color.WHITE);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Image loadImage(final int nr) {
        try (final InputStream imageIS = new FileInputStream(String.format(IMAGE, nr))) {
            if (null == imageIS) {
                // nothing to set. image does not exists
                return null;
            }
            return new Image(imageIS, SCREEN_WIDTH, SCREEN_HEIGHT, true, true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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

    private Media loadMedia(final int nr) {
        final File mediaFile = new File(String.format(MOVIE, nr));
        return new Media(mediaFile.toURI().toString());
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

}
