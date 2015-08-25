/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
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
package example.menubar;

import example.image.ImageService;
import example.media.MediaService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javax.inject.Inject;

public class MenubarPresenter {

    @Inject
    private ImageService imageService;

    @Inject
    private MediaService mediaService;

    private final ObjectProperty<Image> image = new SimpleObjectProperty<>(this, "image");
    private final ObjectProperty<Media> media = new SimpleObjectProperty<>(this, "media");
    private final BooleanProperty fullscreen = new SimpleBooleanProperty(this, "fullscreen");
    private final BooleanProperty preserveRatio = new SimpleBooleanProperty(this, "preserveRatio");
    private final BooleanProperty mute = new SimpleBooleanProperty(this, "mute");

    public final ReadOnlyObjectProperty<Image> imageProperty() {
        return image;
    }

    public final ReadOnlyObjectProperty<Media> mediaProperty() {
        return media;
    }

    public ReadOnlyBooleanProperty fullscreenProperty() {
        return fullscreen;
    }

    public ReadOnlyBooleanProperty preserveRatioProperty() {
        return preserveRatio;
    }

    public ReadOnlyBooleanProperty muteProperty() {
        return mute;
    }

    @FXML
    public void handleImage1Action(final ActionEvent event) {
        image.set(imageService.loadImageFromPath("media/image1.jpg"));
    }

    @FXML
    public void handleImage2Action(final ActionEvent event) {
        image.set(imageService.loadImageFromPath("media/image2.jpg"));
    }

    @FXML
    public void handleImage2LongRunningAction(final ActionEvent event) {
        final Service<Image> service = new Service<Image>() {
            @Override
            protected Task<Image> createTask() {
                return new Task<Image>() {
                    @Override
                    protected Image call() throws Exception {
                        Thread.sleep(2000l);
                        return imageService.loadImageFromPath("media/image2.jpg");
                    };
                };
            }
        };
        service.setOnSucceeded((final WorkerStateEvent event1) -> {
            image.set((Image)event1.getSource().getValue());
        });
        service.start();
    }

    @FXML
    public void handleMovie1Action(final ActionEvent event) {
        media.set(mediaService.loadMediaFromFilePath("media/movie1.mp4"));
    }

    @FXML
    public void handleMovie2Action(final ActionEvent event) {
        media.set(mediaService.loadMediaFromFilePath("media/movie2.mp4"));
    }

    @FXML
    public void handleFullscreenAction(final ActionEvent event) {
        fullscreen.set(!fullscreen.get());
    }

    @FXML
    public void handlePreserveRatioAction(final ActionEvent event) {
        preserveRatio.set(!preserveRatio.get());
    }

    @FXML
    public void handleMuteAction(final ActionEvent event) {
        mute.set(!mute.get());
    }

}
