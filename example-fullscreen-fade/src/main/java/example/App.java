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

import com.airhacks.afterburner.injection.Injector;
import example.image.ImageService;
import example.menubar.MenubarPresenter;
import example.menubar.MenubarView;
import example.presentation.PresentationPresenter;
import example.presentation.PresentationView;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final int MAX_SCREEN_WIDTH = 1280;
    private static final int MAX_SCREEN_HEIGHT = 720;
    
    @Inject
    private ImageService imageService;
    
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>(this, "image");

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Injector.injectMembers(App.class, this);
        
        primaryStage.setTitle("Fullscreen Fade Example");
        
        final MenubarView menubarView = new MenubarView();
        final MenubarPresenter menubarPresenter = menubarView.getPresenter();
        
        menubarPresenter.fullscreenProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            primaryStage.setFullScreen(newValue);
        });
        menubarPresenter.imageProperty().addListener((ObservableValue<? extends Image> observable, Image oldValue, Image newValue) -> {
            image.set(newValue);
        });
        
        final PresentationView presentationView = new PresentationView();
        final PresentationPresenter presentationPresenter = presentationView.getPresenter();
        
        presentationPresenter.imageProperty().bind(image);
        presentationPresenter.mediaProperty().bind(menubarPresenter.mediaProperty());
        presentationPresenter.preserveRatio().bind(menubarPresenter.preserveRatioProperty());
        presentationPresenter.muteProperty().bind(menubarPresenter.muteProperty());
        
        image.set(imageService.loadImageFromPath("example/image/default.jpg"));
        
        final StackPane stackPane = presentationView.getView();
        stackPane.getChildren().add(menubarView.getView());

        primaryStage.setScene(new Scene(stackPane, MAX_SCREEN_WIDTH / 3 * 2, MAX_SCREEN_HEIGHT / 3 * 2));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }
    
}
