/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example;

import java.io.IOException;
import java.io.InputStream;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final String IMAGE = "example/NatGeo%s.jpg";
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    public static void main(String[] args) {
        launch(args);
    }

    private static int currentImageNr = -1;

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
        
        final MenuItem natGeo1MenuItem = new MenuItem("National Geographics 1");
        natGeo1MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHORTCUT_DOWN));
        natGeo1MenuItem.setOnAction((ActionEvent event) -> {
            setImage(1, iv);
        });
        
        final MenuItem natGeo2MenuItem = new MenuItem("National Geographics 2");
        natGeo2MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.SHORTCUT_DOWN));
        natGeo2MenuItem.setOnAction((ActionEvent event) -> {
            setImage(2, iv);
        });
        
        final MenuItem natGeo3MenuItem = new MenuItem("National Geographics 3");
        natGeo3MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.SHORTCUT_DOWN));
        natGeo3MenuItem.setOnAction((ActionEvent event) -> {
            setImage(3, iv);
        });
        
        final MenuItem natGeo4MenuItem = new MenuItem("National Geographics 4");
        natGeo4MenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.SHORTCUT_DOWN));
        natGeo4MenuItem.setOnAction((ActionEvent event) -> {
            setImage(4, iv);
        });
        
        final MenuItem fullscreen = new MenuItem("Fullscreen");
        fullscreen.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN));
        fullscreen.setOnAction((ActionEvent event) -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
        });
        
        final Menu mediaMenu = new Menu("Media");
        mediaMenu.getItems().add(natGeo1MenuItem);
        mediaMenu.getItems().add(natGeo2MenuItem);
        mediaMenu.getItems().add(natGeo3MenuItem);
        mediaMenu.getItems().add(natGeo4MenuItem);
        mediaMenu.getItems().add(new SeparatorMenuItem());
        
        final Menu windowMenu = new Menu("Window");
        windowMenu.getItems().add(fullscreen);
        
        final MenuBar menuBar = new MenuBar(mediaMenu, windowMenu);
        menuBar.useSystemMenuBarProperty().set(true);
        
        final Pane root = new Pane();
        root.getChildren().add(menuBar);
        root.getChildren().add(iv);

        final Scene scene = new Scene(root, SCREEN_WIDTH / 3 * 2, SCREEN_HEIGHT / 3 * 2);
        scene.setFill(Color.WHITE);

        setImage(1, iv);
        
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
            final ClassLoader cl = getClass().getClassLoader();
            try (final InputStream imageIS = cl.getResourceAsStream(String.format(IMAGE, nr))) {
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
            fade(iv, (ActionEvent event) -> {
                iv.setImage(newImage);
            });
        }
    }
    
    private void setMovie(final int nr, final MediaView mv) {
        final MediaPlayer mediaPlayer = mv.getMediaPlayer();
        
        final Media oldMedia = mediaPlayer.getMedia();
        final Media newMedia;
        
        // FIXME implement
    }

    private void fade(final Node node, EventHandler<ActionEvent> finishAction) {
        final FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), node);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.5);
        fadeOutTransition.setOnFinished(finishAction);

        final FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(1), node);
        fadeInTransition.setFromValue(0.5);
        fadeInTransition.setToValue(1.0);

        final SequentialTransition sequentialTransition = new SequentialTransition(fadeOutTransition, fadeInTransition);
        sequentialTransition.play();
    }

}
