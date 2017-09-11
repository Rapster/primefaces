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
package org.primefaces.component.separator;

import javax.faces.component.UIComponentBase;
import org.primefaces.model.menu.Separator;
import org.primefaces.component.separator.Separator;
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

@PFComponent(tagName = "separator",
             description = "Seperator displays a horizontal line to separate content.",
             parent = UIComponentBase.class)
public class UISeparator extends AbstractUISeparator implements Separator, Separator {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "Advisory tooltip informaton")
		title,
		@PFProperty(description = "Inline style of the separator")
		style,
		@PFProperty(description = "Style class of the separator")
		styleClass,;
	}


    public final static String DEFAULT_STYLE_CLASS = "ui-separator ui-state-default ui-corner-all";
}