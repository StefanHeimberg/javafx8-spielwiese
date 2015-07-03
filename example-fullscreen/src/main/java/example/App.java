/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final String IMAGE = "example/NatGeo01.jpg";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Example");
        primaryStage.setResizable(true);

        final Image i = new Image(getClass().getClassLoader().getResourceAsStream(IMAGE));
        final ImageView iv = new ImageView(i);
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
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
