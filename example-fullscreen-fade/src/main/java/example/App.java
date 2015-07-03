/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example;

import java.io.InputStream;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
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

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");
        primaryStage.setResizable(true);

        final ImageView iv = new ImageView();
        iv.fitHeightProperty().bind(primaryStage.heightProperty());
        iv.fitWidthProperty().bind(primaryStage.widthProperty());

        final Pane root = new Pane();
        root.getChildren().add(iv);

        final Scene scene = new Scene(root, 800, 600);
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
        final ClassLoader cl = getClass().getClassLoader();
        final InputStream imageIS = cl.getResourceAsStream(String.format(IMAGE, nr));

        if (null == imageIS) {
            // nothing to set. image does not exists
            return;
        }

        final Image oldImage = iv.getImage();
        final Image newImage = new Image(imageIS);

        if (oldImage == null) {
            // nothing to fade...
            iv.setImage(newImage);
            return;
        }

        final SequentialTransition transition = createTransition(iv, newImage);
        transition.play();
    }

    private SequentialTransition createTransition(final ImageView iv, final Image img) {
        final FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), iv);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.3);
        fadeOutTransition.setOnFinished((ActionEvent arg0) -> {
            iv.setImage(img);
        });

        final FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(1), iv);
        fadeInTransition.setFromValue(0.3);
        fadeInTransition.setToValue(1.0);
        final SequentialTransition sequentialTransition = SequentialTransitionBuilder
                .create()
                .children(fadeOutTransition, fadeInTransition)
                .build();

        return sequentialTransition;
    }

}
