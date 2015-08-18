/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final Logger LOG = Logger.getLogger(App.class.getName());

    private static final int MAX_SCREEN_WIDTH = 1280;
    private static final int MAX_SCREEN_HEIGHT = 720;

    private static final Duration FADE_DURATION = Duration.seconds(1);

    public static enum ViewEnum {

        IMAGE1, IMAGE2, MEDIA1, MEDIA2;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private final ImageView imageView1 = new ImageView();
    private final ImageView imageView2 = new ImageView();
    private final MediaView mediaView1 = new MediaView();
    private final MediaView mediaView2 = new MediaView();

    private final FadeTransition fadeOutTransition = new FadeTransition();
    private final FadeTransition fadeInTransition = new FadeTransition();
    private final SequentialTransition transition = new SequentialTransition(fadeOutTransition, fadeInTransition);

    private ViewEnum activeView;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");

        final MenuBar menuBar = new MenuBar(
                new Menu("Media", null,
                        createMenuItem("Image 1", KeyCode.DIGIT1, (ActionEvent event) -> {
                            setImage(loadImage("media/image1.jpg"));
                        }),
                        createMenuItem("Image 2", KeyCode.DIGIT2, (ActionEvent event) -> {
                            setImage(loadImage("media/image2.jpg"));
                        }),
                        new SeparatorMenuItem(),
                        createMenuItem("Movie 1", KeyCode.DIGIT3, (ActionEvent event) -> {
                            setMedia(loadMedia("media/movie1.mp4"));
                        }),
                        createMenuItem("Movie 2", KeyCode.DIGIT4, (ActionEvent event) -> {
                            setMedia(loadMedia("media/movie2.mp4"));
                        })
                ),
                new Menu("Window", null,
                        createMenuItem("Fullscreen", KeyCode.F, (ActionEvent event) -> {
                            primaryStage.setFullScreen(!primaryStage.isFullScreen());
                        })
                )
        );
        menuBar.setUseSystemMenuBar(true);

        imageView1.setId("image-view-1");
        imageView2.setId("image-view-2");
        mediaView1.setId("media-view-1");
        mediaView2.setId("media-view-2");

        // default values for fade out transition
        fadeOutTransition.setDuration(FADE_DURATION.divide(2));
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.1);

        // default values for fade in transition
        fadeInTransition.setDuration(FADE_DURATION.divide(2));
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

        // all views transparent by default
        imageView1.opacityProperty().set(0.0);
        imageView2.opacityProperty().set(0.0);
        mediaView1.opacityProperty().set(0.0);
        mediaView2.opacityProperty().set(0.0);

        // Bind all views to the stage height and width. for resizing the window 
        imageView1.fitHeightProperty().bind(primaryStage.heightProperty());
        imageView1.fitWidthProperty().bind(primaryStage.widthProperty());

        imageView2.fitHeightProperty().bind(primaryStage.heightProperty());
        imageView2.fitWidthProperty().bind(primaryStage.widthProperty());

        mediaView1.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView1.fitWidthProperty().bind(primaryStage.widthProperty());

        mediaView2.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView2.fitWidthProperty().bind(primaryStage.widthProperty());

        // create stacked pane
        final Pane pane = new StackPane();
        pane.getChildren().add(menuBar);
        pane.getChildren().add(imageView1);
        pane.getChildren().add(imageView2);
        pane.getChildren().add(mediaView1);
        pane.getChildren().add(mediaView2);

        setImage(loadImage("media/default.jpg"));

        primaryStage.setScene(new Scene(pane, MAX_SCREEN_WIDTH / 3 * 2, MAX_SCREEN_HEIGHT / 3 * 2));
        primaryStage.show();
    }

    public void setImage(final Image image) {
        if (null == activeView) {
            showImage(null, imageView1, image);
            activeView = ViewEnum.IMAGE1;
        } else if (activeView == ViewEnum.IMAGE1) {
            showImage(imageView1, imageView2, image);
            activeView = ViewEnum.IMAGE2;
        } else if (activeView == ViewEnum.IMAGE2) {
            showImage(imageView2, imageView1, image);
            activeView = ViewEnum.IMAGE1;
        } else if (activeView == ViewEnum.MEDIA1) {
            showImage(mediaView1, imageView1, image);
            activeView = ViewEnum.IMAGE1;
        } else if (activeView == ViewEnum.MEDIA2) {
            showImage(mediaView2, imageView1, image);
            activeView = ViewEnum.IMAGE1;
        } else {
            throw new IllegalStateException("cannot set image...");
        }
    }

    public void setMedia(final Media media) {
        if (null == activeView) {
            playMedia(null, mediaView1, media);
            activeView = ViewEnum.MEDIA1;
        } else if (activeView == ViewEnum.MEDIA1) {
            playMedia(mediaView1, mediaView2, media);
            activeView = ViewEnum.MEDIA2;
        } else if (activeView == ViewEnum.MEDIA2) {
            playMedia(mediaView2, mediaView1, media);
            activeView = ViewEnum.MEDIA1;
        } else if (activeView == ViewEnum.IMAGE1) {
            playMedia(imageView1, mediaView1, media);
            activeView = ViewEnum.MEDIA1;
        } else if (activeView == ViewEnum.IMAGE2) {
            playMedia(imageView2, mediaView1, media);
            activeView = ViewEnum.MEDIA1;
        } else {
            throw new IllegalStateException("cannot set media...");
        }
    }

    private void showImage(final Node fromView, final ImageView toView, final Image image) {
        if (null == fromView) {
            LOG.log(Level.INFO, "show image on {0}", toView.getId());

            toView.setImage(image);
            toView.opacityProperty().set(1.0);
            return;
        }

        LOG.log(Level.INFO, "fading from {0} to {1}", new Object[]{fromView.getId(), toView.getId()});

        fadeOutTransition.setNode(fromView);
        fadeOutTransition.setOnFinished((ActionEvent event) -> {
            toView.setImage(image);
        });

        fadeInTransition.setNode(toView);

        transition.playFromStart();
    }

    private void playMedia(final Node fromView, final MediaView toView, final Media media) {
        final MediaPlayer mediaPlayer = createAndSetMediaPlayer(toView, media);

        if (null == fromView) {
            LOG.log(Level.INFO, "playing media on {0}", toView.getId());

            mediaPlayer.play();
            toView.opacityProperty().set(1.0);
            return;
        }

        LOG.log(Level.INFO, "fading from {0} to {1}", new Object[]{fromView.getId(), toView.getId()});

        fadeOutTransition.setNode(fromView);
        fadeOutTransition.setOnFinished((ActionEvent event) -> {
            mediaPlayer.play();
        });

        fadeInTransition.setNode(toView);

        transition.playFromStart();
    }

    private static Image loadImage(final String filePath) {
        try (final InputStream is = new FileInputStream(filePath)) {
            return new Image(is, MAX_SCREEN_WIDTH, MAX_SCREEN_HEIGHT, true, true);
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

    private static MediaPlayer createAndSetMediaPlayer(final MediaView view, final Media media) {
        removeMedia(view);

        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setMute(true);
        mediaPlayer.startTimeProperty().set(Duration.seconds(10));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        view.setMediaPlayer(mediaPlayer);

        return mediaPlayer;
    }

    private static void removeMedia(final MediaView view) {
        if (null != view.getMediaPlayer()) {
            view.getMediaPlayer().stop();
            view.getMediaPlayer().dispose();
            view.setMediaPlayer(null);
        }
    }

}
