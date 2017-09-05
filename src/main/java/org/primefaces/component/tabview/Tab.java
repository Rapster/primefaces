/**
 * Copyright 2009-2017 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.component.tabview;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.component.UINamingContainer;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.faces.render.Renderer;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import java.util.List;
import java.util.ArrayList;
import org.primefaces.util.ComponentUtils;
import org.primefaces.cdk.annotations.*;

@PFComponent(tagName = "tab",
             description = "Tab is a generic container component used by other PrimeFaces components such as tabView or accordionPanel.",
             parent = UIPanel.class)
public class Tab extends AbstractTab {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "Title text of the tab")
		title,
		@PFProperty(description = "Title container inner style")
		titleStyle,
		@PFProperty(description = "Title container style class")
		titleStyleClass,
		@PFProperty(description = "Disables tab element if true", defaultValue = "false", type = Boolean.class)
		disabled,
		@PFProperty(description = "Makes the tab closable by displaying a close icon", defaultValue = "false", type = Boolean.class)
		closable,
		@PFProperty(description = "Tooltip of the tab header")
		titletip,
		@PFProperty(description = "")
		ariaLabel,;
	}


    public void setLoaded(boolean value) {
        getStateHelper().put("loaded", value);
    }

    public boolean isLoaded() {
        Object value = getStateHelper().get("loaded");
        
        return (value == null) ? false : (Boolean) value;
    }
}