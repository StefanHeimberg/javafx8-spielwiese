/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public final class FadeTransitionBuilder {

    private Duration duration;
    private Node fromNode;
    private Node toNode;
    private Object toObject;
    private EventHandler<ActionEvent> fromOnFinishEventHandler;

    public FadeTransitionBuilder withDefaults() {
        duration = Duration.seconds(1);
        return this;
    }

    public FadeTransitionBuilder withDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public FadeTransitionBuilder withFrom(Node node) {
        fromNode = node;
        return this;
    }
    
    public FadeTransitionBuilder withFromOnFinish(EventHandler<ActionEvent> fromOnFinishEventHandler) {
        this.fromOnFinishEventHandler = fromOnFinishEventHandler;
        return this;
    }

    public FadeTransitionBuilder withToImage(ImageView view, Image image) {
        toNode = view;
        toObject = image;
        return this;
    }

    public FadeTransitionBuilder withToMedia(MediaView view, Media media) {
        toNode = view;
        toObject = media;
        return this;
    }

    public Transition build() {
        final SequentialTransition transition = new SequentialTransition();

//        if (toNode instanceof MediaView) {
//            final Transition dummyTransition = new Transition() {
//                @Override
//                protected void interpolate(double frac) {
//                }
//            };
//            dummyTransition.setOnFinished((ActionEvent event) -> {
//                System.out.println("from: "  + fromNode + " to: " + toNode);
//            });
//            transition.getChildren().add(dummyTransition);
//        }

        final FadeTransition fadeOutTransition = new FadeTransition(duration.divide(2), fromNode);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.5);
        if(null != fromOnFinishEventHandler) {
            fadeOutTransition.setOnFinished(fromOnFinishEventHandler);
        }
//        fadeOutTransition.setOnFinished((ActionEvent event) -> {
//            if (fromNode instanceof MediaView) {
//                final MediaView fromMediaView = (MediaView) fromNode;
//                final MediaPlayer fromMediaPlayer = fromMediaView.getMediaPlayer();
//                if(null != fromMediaPlayer) {
//                    fromMediaPlayer.stop();
//                }
//            }
//            if (toNode instanceof MediaView) {
//                final MediaView toMediaView = (MediaView) toNode;
//                final MediaPlayer toMediaPlayer = toMediaView.getMediaPlayer();
//                if(null != toMediaPlayer) {
//                    toMediaPlayer.stop();
//                }
//            }
//            if (toNode instanceof ImageView) {
//                final ImageView toImageView = (ImageView) toNode;
//                final Image toImage = (Image) toObject;
//                toImageView.setImage(toImage);
//            }
//            if(fromNode != toNode) {
//                fromNode.setVisible(false);
//                toNode.setVisible(true);
//            }
//        });
        transition.getChildren().add(fadeOutTransition);

        final FadeTransition fadeInTransition = new FadeTransition(duration.divide(2), toNode);
        fadeInTransition.setFromValue(0.5);
        fadeInTransition.setToValue(1.0);
        transition.getChildren().add(fadeInTransition);

        return transition;
    }

}
