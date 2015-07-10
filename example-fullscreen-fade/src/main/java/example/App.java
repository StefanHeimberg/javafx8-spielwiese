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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
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

    private static int currentImageNr = -1;
    private static int currentMovieNr = -1;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");
        primaryStage.setResizable(true);

        // https://blog.codecentric.de/en/2015/04/tweaking-the-menu-bar-of-javafx-8-applications-on-os-x
        // https://github.com/codecentric/NSMenuFX
        final ImageView iv = new ImageView();
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.fitHeightProperty().bind(primaryStage.heightProperty());
        iv.fitWidthProperty().bind(primaryStage.widthProperty());

        final MediaView mv = new MediaView();
        mv.setPreserveRatio(true);
        mv.setSmooth(true);
        mv.fitHeightProperty().bind(primaryStage.heightProperty());
        mv.fitWidthProperty().bind(primaryStage.widthProperty());

        final MenuItem image1MenuItem = new MenuItem("Image 1");
        image1MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHORTCUT_DOWN));
        image1MenuItem.setOnAction((ActionEvent event) -> {
            setImage(1, iv);
        });

        final MenuItem image2MenuItem = new MenuItem("Image 2");
        image2MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.SHORTCUT_DOWN));
        image2MenuItem.setOnAction((ActionEvent event) -> {
            setImage(2, iv);
        });

        final MenuItem movie1MenuItem = new MenuItem("Movie 1");
        movie1MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.SHORTCUT_DOWN));
        movie1MenuItem.setOnAction((ActionEvent event) -> {
            setMovie(1, mv);
        });

        final MenuItem movie2MenuItem = new MenuItem("Movie 2");
        movie2MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.SHORTCUT_DOWN));
        movie2MenuItem.setOnAction((ActionEvent event) -> {
            setMovie(2, mv);
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

        final Scene scene = new Scene(new Pane(menuBar, iv, mv), SCREEN_WIDTH / 3 * 2, SCREEN_HEIGHT / 3 * 2);
        scene.setFill(Color.WHITE);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setImage(final int nr, final ImageView iv) {
        final Image oldImage = iv.getImage();
        final Image newImage;

        if (currentImageNr == nr) {
            newImage = oldImage;
        } else {
            currentImageNr = nr;
            try (final InputStream imageIS = new FileInputStream(String.format(IMAGE, nr))) {
                if (null == imageIS) {
                    // nothing to set. image does not exists
                    return;
                }
                newImage = new Image(imageIS, SCREEN_WIDTH, SCREEN_HEIGHT, true, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (oldImage == null || oldImage == newImage) {
            iv.setImage(newImage);
        } else {
            final MultimediaFadeTransition mfd = new MultimediaFadeTransition(iv);
            mfd.setFromOnFinished((ActionEvent event) -> {
                iv.setImage(newImage);
            });
            mfd.play();
        }
    }

    private void setMovie(final int nr, final MediaView mv) {
        final Media oldMedia;
        if (null == mv.getMediaPlayer()) {
            oldMedia = null;
        } else {
            oldMedia = mv.getMediaPlayer().getMedia();
        }

        final Media newMedia;

        if (currentMovieNr == nr) {
            newMedia = oldMedia;
        } else {
            currentMovieNr = nr;
            final File mediaFile = new File(String.format(MOVIE, nr));
            LOG.log(Level.INFO, "mediaFile: {0}", mediaFile);
            newMedia = new Media(mediaFile.toURI().toString());
        }

        final MediaPlayer mediaPlayer = new MediaPlayer(newMedia);
        mediaPlayer.setMute(true);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        if (oldMedia == null || oldMedia == newMedia) {
            mv.setMediaPlayer(mediaPlayer);
        } else {
            final MultimediaFadeTransition mfd = new MultimediaFadeTransition(mv);
            mfd.setFromOnFinished((ActionEvent event) -> {
                mv.setMediaPlayer(mediaPlayer);
            });
            mfd.play();
        }
    }

}
