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
package org.primefaces.component.ribbon;

import org.primefaces.component.tabview.TabView;
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
	@ResourceDependency(library="primefaces", name="ribbon/ribbon.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js"),
	@ResourceDependency(library="primefaces", name="ribbon/ribbon.js")
})
@PFComponent(tagName = "ribbon",
             description = "Ribbon is container component to group different sets of controls.")
public abstract class RibbonCore extends TabView implements IRibbon {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "Inline style of the container element")
		style,
		@PFProperty(description = "Style class of the container element")
		styleClass,
		@PFProperty(description = "Index of the active tab. Default is 0", defaultValue = "0", type = Integer.class)
		activeIndex,;
	}


    public static final String CONTAINER_CLASS = "ui-ribbon ui-tabs ui-tabs-top ui-widget ui-widget-content ui-corner-top ui-hidden-container";
    public static final String NAVIGATOR_CLASS = "ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all";
    public static final String INACTIVE_TAB_HEADER_CLASS = "ui-state-default ui-corner-top";
    public static final String ACTIVE_TAB_HEADER_CLASS = "ui-state-default ui-tabs-selected ui-state-active ui-corner-top";
    public static final String PANELS_CLASS = "ui-tabs-panels";
    public static final String ACTIVE_TAB_CONTENT_CLASS = "ui-tabs-panel ui-widget-content ui-corner-bottom";
    public static final String INACTIVE_TAB_CONTENT_CLASS = "ui-tabs-panel ui-widget-content ui-corner-bottom ui-helper-hidden";
    public static final String GROUPS_CLASS = "ui-ribbon-groups ui-helper-reset ui-helper-clearfix ui-widget-content";
    public static final String GROUP_CLASS = "ui-ribbon-group";
    public static final String GROUP_CONTENT_CLASS = "ui-ribbon-group-content";
    public static final String GROUP_LABEL_CLASS = "ui-ribbon-group-label";
}