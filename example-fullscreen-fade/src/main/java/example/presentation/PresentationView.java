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
package example.presentation;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class PresentationView {
    
    private final FXMLLoader loader;

    public PresentationView() {
        loader = new FXMLLoader(getClass().getResource("presentation.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot load fxml", ex);
        }
    }
    
    public Parent getView() {
        return loader.getRoot();
    }

    public PresentationPresenter getPresenter() {
        return loader.getController();
    }
    
}
