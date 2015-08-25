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
package example.menubar;

import com.airhacks.afterburner.views.FXMLView;
import javafx.scene.control.MenuBar;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class MenubarView extends FXMLView {

    @Override
    public MenubarPresenter getPresenter() {
        return (MenubarPresenter) super.getPresenter();
    }

    @Override
    public MenuBar getView() {
        return (MenuBar) super.getView();
    }
    
}
