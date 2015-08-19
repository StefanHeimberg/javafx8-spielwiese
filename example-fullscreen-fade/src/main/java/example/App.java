/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import example.presentation.PresentationView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.stage.Stage;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final int MAX_SCREEN_WIDTH = 1280;
    private static final int MAX_SCREEN_HEIGHT = 720;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");
        
        final PresentationView presentationView = new PresentationView();

        final MenuBar menuBar = new MenuBar(
                new Menu("Media", null,
                        createMenuItem("Image 1", KeyCode.DIGIT1, (ActionEvent event) -> {
                            presentationView.setImage(loadImage("media/image1.jpg"));
                        }),
                        createMenuItem("Image 2", KeyCode.DIGIT2, (ActionEvent event) -> {
                            presentationView.setImage(loadImage("media/image2.jpg"));
                        }),
                        new SeparatorMenuItem(),
                        createMenuItem("Movie 1", KeyCode.DIGIT3, (ActionEvent event) -> {
                            presentationView.setMedia(loadMedia("media/movie1.mp4"));
                        }),
                        createMenuItem("Movie 2", KeyCode.DIGIT4, (ActionEvent event) -> {
                            presentationView.setMedia(loadMedia("media/movie2.mp4"));
                        })
                ),
                new Menu("Window", null,
                        createMenuItem("Fullscreen", KeyCode.F, (ActionEvent event) -> {
                            primaryStage.setFullScreen(!primaryStage.isFullScreen());
                        })
                )
        );
        menuBar.setUseSystemMenuBar(true);

        presentationView.getChildren().add(0, menuBar);
        presentationView.setImage(loadImage("media/default.jpg"));

        primaryStage.setScene(new Scene(presentationView, MAX_SCREEN_WIDTH / 3 * 2, MAX_SCREEN_HEIGHT / 3 * 2));
        primaryStage.show();
    }

    private static Image loadImage(final String filePath) {
        try (final InputStream is = new FileInputStream(filePath)) {
            return new Image(is);
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
