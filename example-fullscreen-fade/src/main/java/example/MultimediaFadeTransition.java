/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class MultimediaFadeTransition {
    
    private static final int DEFAULT_DURATION_IN_SECONDS = 1;
    
    private final Node fromNode;
    private final Node toNode;
    private Duration duration = Duration.seconds(DEFAULT_DURATION_IN_SECONDS);
    
    private EventHandler<ActionEvent> fromOnFinished;
    private EventHandler<ActionEvent> toOnFinished;

    public MultimediaFadeTransition(final MediaView node) {
        this.fromNode = node;
        this.toNode = node;
    }

    public MultimediaFadeTransition(final ImageView node) {
        this.fromNode = node;
        this.toNode = node;
    }

    public MultimediaFadeTransition(final MediaView fromNode, final ImageView toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public MultimediaFadeTransition(final ImageView fromNode, final MediaView toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    
    public void setFromOnFinished(EventHandler<ActionEvent> fromOnFinished) {
        this.fromOnFinished = fromOnFinished;
    }

    public void setToOnFinished(EventHandler<ActionEvent> toOnFinished) {
        this.toOnFinished = toOnFinished;
    }
    
    public void play() {
        final FadeTransition fadeOutTransition = new FadeTransition(duration.divide(2), fromNode);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.5);
        fadeOutTransition.setOnFinished(fromOnFinished);

        final FadeTransition fadeInTransition = new FadeTransition(duration.divide(2), toNode);
        fadeInTransition.setFromValue(0.5);
        fadeInTransition.setToValue(1.0);
        fadeInTransition.setOnFinished(toOnFinished);

        new SequentialTransition(fadeOutTransition, fadeInTransition).play();
    }
    
}
