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
package example.presentation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class PresentationPresenter implements Initializable {

    private static enum ViewEnum {

        IMAGE1, IMAGE2, MEDIA1, MEDIA2;
    }
    
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final ObjectProperty<Media> media = new SimpleObjectProperty<>();
    private final ObjectProperty<Duration> fadeDuration = new SimpleObjectProperty<>(Duration.seconds(1));
    private final BooleanProperty preserveRatio = new SimpleBooleanProperty(false);

    private final FadeTransition fadeOutTransition = new FadeTransition();
    private final FadeTransition fadeInTransition = new FadeTransition();
    private final SequentialTransition transition = new SequentialTransition(fadeOutTransition, fadeInTransition);
    
    private ViewEnum activeView;
    
    @FXML
    private StackPane stackPane;
    
    @FXML
    private ImageView imageView1;
    
    @FXML
    private ImageView imageView2;
    
    @FXML
    private MediaView mediaView1;
    
    @FXML
    private MediaView mediaView2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final ObjectBinding fadeDurationBinding = new ObjectBinding() {
            {
                super.bind(fadeDuration);
            }

            @Override
            protected Object computeValue() {
                return fadeDuration.get().divide(2);
            }
        };

        // default values for fade out transition
        fadeOutTransition.durationProperty().bind(fadeDurationBinding);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.1);

        // default values for fade in transition
        fadeInTransition.durationProperty().bind(fadeDurationBinding);
        fadeInTransition.setFromValue(0.1);
        fadeInTransition.setToValue(1.0);
        fadeInTransition.setOnFinished((ActionEvent event) -> {
            final Node node = fadeOutTransition.getNode();
            node.opacityProperty().set(0.0);
            if (node instanceof MediaView) {
                removeMedia((MediaView) node);
            } else if (node instanceof ImageView) {
                ((ImageView) node).setImage(null);
            }
        });
        
        imageView1.preserveRatioProperty().bind(preserveRatio);
        imageView1.fitHeightProperty().bind(stackPane.heightProperty());
        imageView1.fitWidthProperty().bind(stackPane.widthProperty());
        
        imageView2.preserveRatioProperty().bind(preserveRatio);
        imageView2.fitHeightProperty().bind(stackPane.heightProperty());
        imageView2.fitWidthProperty().bind(stackPane.widthProperty());
        
        mediaView1.preserveRatioProperty().bind(preserveRatio);
        mediaView1.fitHeightProperty().bind(stackPane.heightProperty());
        mediaView1.fitWidthProperty().bind(stackPane.widthProperty());
        
        mediaView2.preserveRatioProperty().bind(preserveRatio);
        mediaView2.fitHeightProperty().bind(stackPane.heightProperty());
        mediaView2.fitWidthProperty().bind(stackPane.widthProperty());
        
        image.addListener((ObservableValue<? extends Image> observable, Image oldValue, Image newValue) -> {
            if (null == activeView) {
                showImage(null, imageView1, newValue);
                activeView = ViewEnum.IMAGE1;
            } else if (activeView == ViewEnum.IMAGE1) {
                showImage(imageView1, imageView2, newValue);
                activeView = ViewEnum.IMAGE2;
            } else if (activeView == ViewEnum.IMAGE2) {
                showImage(imageView2, imageView1, newValue);
                activeView = ViewEnum.IMAGE1;
            } else if (activeView == ViewEnum.MEDIA1) {
                showImage(mediaView1, imageView1, newValue);
                activeView = ViewEnum.IMAGE1;
            } else if (activeView == ViewEnum.MEDIA2) {
                showImage(mediaView2, imageView1, newValue);
                activeView = ViewEnum.IMAGE1;
            } else {
                throw new IllegalStateException("cannot set image...");
            }
        });

        media.addListener((ObservableValue<? extends Media> observable, Media oldValue, Media newValue) -> {
            if (null == activeView) {
                playMedia(null, mediaView1, newValue);
                activeView = ViewEnum.MEDIA1;
            } else if (activeView == ViewEnum.MEDIA1) {
                playMedia(mediaView1, mediaView2, newValue);
                activeView = ViewEnum.MEDIA2;
            } else if (activeView == ViewEnum.MEDIA2) {
                playMedia(mediaView2, mediaView1, newValue);
                activeView = ViewEnum.MEDIA1;
            } else if (activeView == ViewEnum.IMAGE1) {
                playMedia(imageView1, mediaView1, newValue);
                activeView = ViewEnum.MEDIA1;
            } else if (activeView == ViewEnum.IMAGE2) {
                playMedia(imageView2, mediaView1, newValue);
                activeView = ViewEnum.MEDIA1;
            } else {
                throw new IllegalStateException("cannot set media...");
            }
        });
    }

    public boolean isPreserveRatio() {
        return preserveRatio.get();
    }
    
    public void setPreserveRatio(final boolean preserveRatio) {
        this.preserveRatio.set(preserveRatio);
    }
    
    public BooleanProperty preserveRatio() {
        return preserveRatio;
    }
    
    public Duration getFadeDuration() {
        return fadeDuration.get();
    }
    
    public void setFadeDuration(final Duration fadeDuration) {
        this.fadeDuration.set(fadeDuration);
    }
    
    public ObjectProperty<Duration> fadeDurationProperty() {
        return fadeDuration;
    }

    public Image getImage() {
        return image.get();
    }

    public void setImage(final Image image) {
        this.image.set(image);
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public Media getMedia() {
        return media.get();
    }

    public void setMedia(final Media media) {
        this.media.set(media);
    }

    public ObjectProperty<Media> mediaProperty() {
        return media;
    }

    private void showImage(final Node fromView, final ImageView toView, final Image image) {
        transition.stop();
        
        if (null == fromView) {
            toView.setImage(image);
            toView.opacityProperty().set(1.0);
            return;
        }

        fadeOutTransition.setNode(fromView);
        fadeOutTransition.setOnFinished((ActionEvent event) -> {
            toView.setImage(image);
        });

        fadeInTransition.setNode(toView);

        transition.playFromStart();
    }

    private void playMedia(final Node fromView, final MediaView toView, final Media media) {
        transition.stop();
        
        removeMedia(toView);

        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setMute(true);
        mediaPlayer.startTimeProperty().set(Duration.seconds(10));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        toView.setMediaPlayer(mediaPlayer);

        if (null == fromView) {
            mediaPlayer.play();
            toView.opacityProperty().set(1.0);
            return;
        }

        fadeOutTransition.setNode(fromView);
        fadeOutTransition.setOnFinished((ActionEvent event) -> {
            mediaPlayer.play();
        });

        fadeInTransition.setNode(toView);

        transition.playFromStart();
    }

    private static void removeMedia(final MediaView view) {
        if (null != view.getMediaPlayer()) {
            view.getMediaPlayer().stop();
            view.getMediaPlayer().dispose();
            view.setMediaPlayer(null);
        }
    }
    
}
