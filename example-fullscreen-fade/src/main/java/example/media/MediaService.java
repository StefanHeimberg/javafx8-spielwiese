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
package example.media;

import java.net.URISyntaxException;
import javafx.scene.media.Media;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class MediaService {
    
    public Media loadMediaFromFilePath(final String filePath) {
        try {
            final ClassLoader cl = getClass().getClassLoader();
            return new Media(cl.getResource(filePath).toURI().toString());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
    
}
