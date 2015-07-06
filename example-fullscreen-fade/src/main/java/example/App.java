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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final String IMAGE = "example/NatGeo%s.jpg";

    public static void main(String[] args) {
        launch(args);
    }

    private static int currentImageNr = -1;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");
        primaryStage.setResizable(true);

        final ImageView iv = new ImageView();
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.fitHeightProperty().bind(primaryStage.heightProperty());
        iv.fitWidthProperty().bind(primaryStage.widthProperty());

        final Pane root = new Pane();
        root.getChildren().add(iv);

        final Scene scene = new Scene(root, 1280/3*2, 720/3*2);
        scene.setFill(Color.WHITE);

        final KeyCodeCombination fullscreenKeyCodeCombination = new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN);

        scene.getAccelerators().put(fullscreenKeyCodeCombination, () -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
        });

        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case DIGIT1:
                    setImage(1, iv);
                    break;
                case DIGIT2:
                    setImage(2, iv);
                    break;
                case DIGIT3:
                    setImage(3, iv);
                    break;
                case DIGIT4:
                    setImage(4, iv);
                    break;
            }
        });

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
                newImage = new Image(imageIS, 1280, 720, true, true);
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

    private void fade(final Node node, EventHandler<ActionEvent> finishAction) {
        final FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), node);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.3);
        fadeOutTransition.setOnFinished(finishAction);

        final FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(1), node);
        fadeInTransition.setFromValue(0.3);
        fadeInTransition.setToValue(1.0);

        final SequentialTransition sequentialTransition = new SequentialTransition(fadeOutTransition, fadeInTransition);
        sequentialTransition.play();
    }

}
