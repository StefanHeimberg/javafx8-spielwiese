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
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class App extends Application {

    private static final Logger LOG = Logger.getLogger(App.class.getName());

    private static final String DEFAULT = "media/default.jpg";
    private static final String IMAGE = "media/image%s.jpg";
    private static final String MOVIE = "media/movie%s.mp4";

    private static final int MAX_SCREEN_WIDTH = 1280;
    private static final int MAX_SCREEN_HEIGHT = 720;
    
    public static enum ViewId {
        IMAGE1, IMAGE2, MOVIE1, MOVIE2;
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
    private final SequentialTransition transition = new SequentialTransition();
    
    private ViewId activeView;
    
//    private Node prevNode;
//    private MediaView currentMediaView;
//    private MediaView nextMediaView;
    
    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Fullscreen Fade Example");

        final MenuBar menuBar = new MenuBar(
                new Menu("Media", null,
                        createMenuItem("Image 1", KeyCode.DIGIT1, (ActionEvent event) -> {
                            setImage(loadImage(String.format(IMAGE, 1)));
                        }),
                        createMenuItem("Image 2", KeyCode.DIGIT2, (ActionEvent event) -> {
                            setImage(loadImage(String.format(IMAGE, 2)));
                        }),
                        new SeparatorMenuItem(),
                        createMenuItem("Movie 1", KeyCode.DIGIT3, (ActionEvent event) -> {
                            setMedia(loadMedia(String.format(MOVIE, 1)));
                        }),
                        createMenuItem("Movie 2", KeyCode.DIGIT4, (ActionEvent event) -> {
                            setMedia(loadMedia(String.format(MOVIE, 2)));
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
        
        fadeOutTransition.setDuration(Duration.seconds(0.5));
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.1);
        
        fadeInTransition.setDuration(Duration.seconds(0.5));
        fadeInTransition.setFromValue(0.1);
        fadeInTransition.setToValue(1.0);
        
        transition.getChildren().add(fadeOutTransition);
        transition.getChildren().add(fadeInTransition);
        
        // Resize Binding 
        imageView1.fitHeightProperty().bind(primaryStage.heightProperty());
        imageView1.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView1.opacityProperty().set(0.0);
        
        imageView2.fitHeightProperty().bind(primaryStage.heightProperty());
        imageView2.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView2.opacityProperty().set(0.0);
        
        mediaView1.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView1.fitWidthProperty().bind(primaryStage.widthProperty());
        mediaView1.opacityProperty().set(0.0);
        
        mediaView2.fitHeightProperty().bind(primaryStage.heightProperty());
        mediaView2.fitWidthProperty().bind(primaryStage.widthProperty());
        mediaView2.opacityProperty().set(0.0);
        
        // Mediaplayer Binding
        //        imageView.imageProperty().addListener((ObservableValue<? extends Image> observable, Image oldValue, Image newValue) -> {
        //            final MediaPlayer mediaPlayer1 = mediaView1.getMediaPlayer();
        //            if(null != mediaPlayer1 && mediaPlayer1.getStatus() == MediaPlayer.Status.PLAYING) {
        //                mediaPlayer1.stop();
        //            }
        //            final MediaPlayer mediaPlayer2 = mediaView2.getMediaPlayer();
        //            if(null != mediaPlayer2 && mediaPlayer2.getStatus() == MediaPlayer.Status.PLAYING) {
        //                mediaPlayer2.stop();
        //            }
        //        });
        

//        currentMediaView = mediaView1;
//        nextMediaView = mediaView2;

        final Pane pane = new StackPane();
        pane.getChildren().add(menuBar);
        pane.getChildren().add(imageView1);
        pane.getChildren().add(imageView2);
        pane.getChildren().add(mediaView1);
        pane.getChildren().add(mediaView2);

        //setImage(loadImage(DEFAULT));

        primaryStage.setScene(new Scene(pane, MAX_SCREEN_WIDTH / 3 * 2, MAX_SCREEN_HEIGHT / 3 * 2));
        primaryStage.show();
    }
    
    private void fade(final ImageView fromView, final ImageView toView, final Image image) {
        LOG.log(Level.INFO, "fading from {0} to {1}", new Object[]{fromView.getId(), toView.getId()});
        
        fadeOutTransition.setNode(fromView);
        fadeOutTransition.setOnFinished((ActionEvent event) -> {
            toView.setImage(image);
        });
        
        fadeInTransition.setNode(toView);
        fadeInTransition.setOnFinished((ActionEvent event) -> {
            fromView.setImage(null);
            fromView.opacityProperty().set(0.0);
        });
        
        transition.playFromStart();
    }
    
    private void fade(final ImageView fromView, final MediaView toView, final Media media) {
        LOG.log(Level.INFO, "fading from {0} to {1}", new Object[]{fromView.getId(), toView.getId()});
        
        // FIXME implement
    }
    
    private void fade(final MediaView fromView, final MediaView toView, final Media media) {
        LOG.log(Level.INFO, "fading from {0} to {1}", new Object[]{fromView.getId(), toView.getId()});
        
        // FIXME implement
    }
    
    private void fade(final MediaView fromView, final ImageView toView, final Image image) {
        LOG.log(Level.INFO, "fading from {0} to {1}", new Object[]{fromView.getId(), toView.getId()});
        
        // FIXME implement
    }
    
    private void setImage(final Image image) {
        if(null == activeView) {
            imageView1.setImage(image);
            imageView1.opacityProperty().set(1.0);
            activeView = ViewId.IMAGE1;
            return;
        }
        
        if(activeView == ViewId.IMAGE1) {
            fade(imageView1, imageView2, image);
            activeView = ViewId.IMAGE2;
            return;
        }
        
        if(activeView == ViewId.IMAGE2) {
            fade(imageView2, imageView1, image);
            activeView = ViewId.IMAGE1;
            return;
        }
        
        if(activeView == ViewId.MOVIE1) {
            fade(mediaView1, imageView1, image);
            activeView = ViewId.IMAGE1;
            return;
        }
        
        if(activeView == ViewId.MOVIE2) {
            fade(mediaView2, imageView1, image);
            activeView = ViewId.IMAGE1;
            return;
        }
        
        throw new IllegalStateException("cannot set image...");
    }

    private void setMedia(final Media media) {
//        final Media oldMedia;
//        if (null == currentMediaView.getMediaPlayer()) {
//            oldMedia = null;
//        } else {
//            oldMedia = currentMediaView.getMediaPlayer().getMedia();
//        }
//
//        final Media newMedia = media;
//
//        if (oldMedia == null || oldMedia == newMedia) {
//            final MediaPlayer mediaPlayer = new MediaPlayer(newMedia);
//            mediaPlayer.setMute(true);
//            mediaPlayer.setAutoPlay(true);
//            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//
//            currentMediaView.setMediaPlayer(mediaPlayer);
//        } else {
//            new FadeTransitionBuilder()
//                    .withDefaults()
//                    .withFrom(prevNode)
//                    .withToMedia(nextMediaView, newMedia)
//                    .build()
//                    .play();
//        }
//        prevNode = nextMediaView;
//        nextMediaView = currentMediaView;
//        currentMediaView = (MediaView) prevNode;
    }
    
    private static Image loadImage(final String filePath) {
        try (final InputStream imageIS = new FileInputStream(filePath)) {
            if (null == imageIS) {
                // nothing to set. image does not exists
                return null;
            }
            return new Image(imageIS, MAX_SCREEN_WIDTH, MAX_SCREEN_HEIGHT, true, true);
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
