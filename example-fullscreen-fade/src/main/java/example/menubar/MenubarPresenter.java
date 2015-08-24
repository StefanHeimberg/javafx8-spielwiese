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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

public class MenubarPresenter {
    
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>(this, "image");
    private final ObjectProperty<Media> media = new SimpleObjectProperty<>(this, "media");
    private final BooleanProperty fullscreen = new SimpleBooleanProperty(this, "fullscreen", false);
    private final BooleanProperty preserveRatio = new SimpleBooleanProperty(this, "preserveRatio", false);

    public final ReadOnlyObjectProperty<Image> imageProperty() {
        return image;
    }

    public final ReadOnlyObjectProperty<Media> mediaProperty() {
        return media;
    }

    public ReadOnlyBooleanProperty fullscreenProperty() {
        return fullscreen;
    }

    public BooleanProperty preserveRatioProperty() {
        return preserveRatio;
    }
    
    @FXML
    public void handleImage1Action(final ActionEvent event) {
        image.set(loadImage("media/image1.jpg"));
    }
    
    @FXML
    public void handleImage2Action(final ActionEvent event) {
        image.set(loadImage("media/image2.jpg"));
    }
    
    @FXML
    public void handleMovie1Action(final ActionEvent event) {
        media.set(loadMedia("media/movie1.mp4"));
    }
    
    @FXML
    public void handleMovie2Action(final ActionEvent event) {
        media.set(loadMedia("media/movie2.mp4"));
    }
    
    @FXML
    public void handleFullscreenAction(final ActionEvent event) {
        fullscreen.set(!fullscreen.get());
    }
    
    @FXML
    public void handlePreserveRatioAction(final ActionEvent event) {
        preserveRatio.set(!preserveRatio.get());
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

}
