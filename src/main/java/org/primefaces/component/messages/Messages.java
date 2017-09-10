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
package org.primefaces.component.messages;

import javax.faces.component.UIMessages;
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

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="components.css")
})
@PFComponent(tagName = "messages",
             description = "Message is a pre-skinned extended version of the standard JSF message component with extensions.",
             parent = UIMessages.class)
public class Messages extends AbstractMessages implements org.primefaces.component.api.AutoUpdatable, org.primefaces.component.api.UINotification {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "", defaultValue = "false", type = Boolean.class)
		autoUpdate,
		@PFProperty(description = "Defines whether html would be escaped or not, defaults to true", defaultValue = "true", type = Boolean.class)
		escape,
		@PFProperty(description = "Comma seperated list of severities to display only")
		severity,
		@PFProperty(description = "Adds a close icon to hide the messages", defaultValue = "false", type = Boolean.class)
		closable,
		@PFProperty(description = "Inline style of the component")
		style,
		@PFProperty(description = "Style class of the component")
		styleClass,
		@PFProperty(description = "Defines if severity icons would be displayed", defaultValue = "true", type = Boolean.class)
		showIcon,
		@PFProperty(description = "Type of the \"for\" attribute. Valid values are \"key\" and \"expression\"")
		forType,;
	}


    public static final String CONTAINER_CLASS = "ui-messages ui-widget";
    public static final String ICONLESS_CONTAINER_CLASS = "ui-messages ui-messages-noicon ui-widget";
    public static final String CLOSE_LINK_CLASS = "ui-messages-close";
    public static final String CLOSE_ICON_CLASS = "ui-icon ui-icon-close";
    public static final String SEVERITY_PREFIX_CLASS = "ui-messages-";
}