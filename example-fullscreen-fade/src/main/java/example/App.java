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

import example.presentation.PresentationPresenter;
import example.presentation.PresentationView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.stage.Stage;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final int MAX_SCREEN_WIDTH = 1280;
    private static final int MAX_SCREEN_HEIGHT = 720;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");

        final PresentationView presentationView = new PresentationView();
        final PresentationPresenter presentationPresenter = presentationView.getPresenter();

        final MenuBar menuBar = new MenuBar(
                new Menu("Media", null,
                        createMenuItem("Image 1", KeyCode.DIGIT1, (ActionEvent event) -> {
                            presentationPresenter.imageProperty().set(loadImage("media/image1.jpg"));
                        }),
                        createMenuItem("Image 2", KeyCode.DIGIT2, (ActionEvent event) -> {
                            presentationPresenter.imageProperty().set(loadImage("media/image2.jpg"));
                        }),
                        new SeparatorMenuItem(),
                        createMenuItem("Movie 1", KeyCode.DIGIT3, (ActionEvent event) -> {
                            presentationPresenter.mediaProperty().set(loadMedia("media/movie1.mp4"));
                        }),
                        createMenuItem("Movie 2", KeyCode.DIGIT4, (ActionEvent event) -> {
                            presentationPresenter.mediaProperty().set(loadMedia("media/movie2.mp4"));
                        })
                ),
                new Menu("Window", null,
                        createMenuItem("Fullscreen", KeyCode.F, (ActionEvent event) -> {
                            primaryStage.setFullScreen(!primaryStage.isFullScreen());
                        }),
                        createMenuItem("Switch Preserve Ratio", KeyCode.P, (ActionEvent event) -> {
                            presentationPresenter.preserveRatio().set(!presentationPresenter.preserveRatio().get());
                        })
                )
        );
        menuBar.setUseSystemMenuBar(true);

        presentationPresenter.imageProperty().set(loadImage("media/default.jpg"));

        primaryStage.setScene(new Scene(new StackPane(menuBar, presentationView.getView()), MAX_SCREEN_WIDTH / 3 * 2, MAX_SCREEN_HEIGHT / 3 * 2));
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
