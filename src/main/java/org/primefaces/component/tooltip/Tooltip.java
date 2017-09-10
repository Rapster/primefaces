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
package org.primefaces.component.tooltip;

import javax.faces.component.UIOutput;
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
	@ResourceDependency(library="primefaces", name="components.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js")
})
@PFComponent(tagName = "tooltip",
             description = "Tooltip goes beyond the legacy html title attribute by providing custom effects, events, html content and advance theme support.",
             widget = true,
             parent = UIOutput.class)
public class Tooltip extends AbstractTooltip {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "Event displaying the tooltip")
		showEvent,
		@PFProperty(description = "Effect to be used for displaying", defaultValue = "fade")
		showEffect,
		@PFProperty(description = "Delay time to show tooltip in milliseconds", defaultValue = "150", type = Integer.class)
		showDelay,
		@PFProperty(description = "Event hiding the tooltip")
		hideEvent,
		@PFProperty(description = "Effect to be used for hiding", defaultValue = "fade")
		hideEffect,
		@PFProperty(description = "Delay time to hide tooltip in milliseconds", defaultValue = "0", type = Integer.class)
		hideDelay,
		@PFProperty(description = "Id of the component to attach the tooltip")
		forValue("for"),
		@PFProperty(description = "Inline style of the tooltip")
		style,
		@PFProperty(description = "Style class of the tooltip")
		styleClass,
		@PFProperty(description = "jquery selector for global tooltip")
		globalSelector,
		@PFProperty(description = "Defines whether html would be escaped or not, defaults to true", defaultValue = "true", type = Boolean.class)
		escape,
		@PFProperty(description = "Tooltip position follows pointer on mousemove", defaultValue = "false", type = Boolean.class)
		trackMouse,
		@PFProperty(description = "Client side callback to execute before tooltip is shown. Returning false will prevent display")
		beforeShow,
		@PFProperty(description = "Client side callback to execute after tooltip is hidden")
		onHide,
		@PFProperty(description = "Client side callback to execute after tooltip is shown")
		onShow,
		@PFProperty(description = "Position of the tooltip, valid values are right, left, top and bottom", defaultValue = "right")
		position,;

		String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {}

		public String toString() {
			return toString != null ? toString : super.toString();
		}
	}


    public static String CONTAINER_CLASS = "ui-tooltip ui-widget";
}