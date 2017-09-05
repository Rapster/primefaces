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
package org.primefaces.component.slidemenu;

import org.primefaces.component.menu.AbstractUIMenu;
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
@PFComponent(tagName = "slideMenu",
             description = "SlideMenu displays submenus with a slide animation.",
             widget = true,
             rtl = true,
             parent = AbstractUIMenu.class)
public class SlideMenu extends AbstractSlideMenu implements org.primefaces.component.menu.OverlayMenu {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "A menu model instance to create menu programmatically", type = org.primefaces.model.menu.MenuModel.class)
		model,
		@PFProperty(description = "Inline style of the main container element")
		style,
		@PFProperty(description = "Style class of the main container element")
		styleClass,
		@PFProperty(description = "Text for back link, only applies to sliding menus. Default is \"Back\"", defaultValue = "Back")
		backLabel,
		@PFProperty(description = "Id of component whose click event will show the dynamic positioned menu")
		trigger,
		@PFProperty(description = "Corner of menu to align with trigger element")
		my,
		@PFProperty(description = "Corner of trigger to align with menu element")
		at,
		@PFProperty(description = "Defines positioning, when enabled menu is displayed with absolute positioning relative to the trigger. \n Default is false, meaning static positioning", defaultValue = "false", type = Boolean.class)
		overlay,
		@PFProperty(description = "Event name of component that will show the dynamic positioned menu. Default is click", defaultValue = "click")
		triggerEvent,;
	}


    public static final String STATIC_CONTAINER_CLASS = "ui-menu ui-slidemenu ui-widget ui-widget-content ui-corner-all ui-helper-clearfix";
    public static final String DYNAMIC_CONTAINER_CLASS = "ui-menu ui-slidemenu ui-menu-dynamic ui-widget ui-widget-content ui-corner-all ui-helper-clearfix ui-shadow";
    public static final String WRAPPER_CLASS = "ui-slidemenu-wrapper";
    public static final String CONTENT_CLASS = "ui-slidemenu-content";
    public static final String BACKWARD_CLASS = "ui-slidemenu-backward ui-widget-header ui-corner-all ui-helper-clearfix";
    public static final String BACKWARD_ICON_CLASS = "ui-icon ui-icon-triangle-1-w";
}