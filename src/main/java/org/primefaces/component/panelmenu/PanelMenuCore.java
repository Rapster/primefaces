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
package org.primefaces.component.panelmenu;

import org.primefaces.component.menu.AbstractMenu;
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
@PFComponent(tagName = "panelMenu",
             description = "PanelMenu is a hybrid of accordion-tree components used for navigations and actions.",
             widget = true,
             rtl = true)
public abstract class PanelMenuCore extends AbstractMenu implements IPanelMenu {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "A menu model instance to create menu programmatically", type = org.primefaces.model.menu.MenuModel.class)
		model,
		@PFProperty(description = "Inline style of the container element")
		style,
		@PFProperty(description = "Style class of the container element")
		styleClass,
		@PFProperty(description = "When enabled, menu state is saved in a cookie for the session. Default is true", defaultValue = "true", type = Boolean.class)
		stateful,;
	}


    public final static String CONTAINER_CLASS = "ui-panelmenu ui-widget";
    public final static String INACTIVE_HEADER_CLASS = "ui-widget ui-panelmenu-header ui-state-default ui-corner-all";
    public final static String ACTIVE_HEADER_CLASS = "ui-widget ui-panelmenu-header ui-state-default ui-state-active ui-corner-top";
    public final static String INACTIVE_TAB_HEADER_ICON_CLASS = "ui-icon ui-icon-triangle-1-e";
    public final static String ACTIVE_TAB_HEADER_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";
    public final static String INACTIVE_ROOT_SUBMENU_CONTENT = "ui-panelmenu-content ui-widget-content ui-helper-hidden";
    public final static String ACTIVE_ROOT_SUBMENU_CONTENT = "ui-panelmenu-content ui-widget-content";
    public final static String MENUITEM_CLASS = "ui-menuitem ui-widget ui-corner-all";
    public final static String DESCENDANT_SUBMENU_CLASS = "ui-widget ui-menuitem ui-corner-all ui-menu-parent";
    public final static String DESCENDANT_SUBMENU_EXPANDED_ICON_CLASS = "ui-panelmenu-icon ui-icon ui-icon-triangle-1-s";
    public final static String DESCENDANT_SUBMENU_COLLAPSED_ICON_CLASS = "ui-panelmenu-icon ui-icon ui-icon-triangle-1-e";
    public static final String DESCENDANT_SUBMENU_EXPANDED_LIST_CLASS = "ui-menu-list ui-helper-reset";
    public static final String DESCENDANT_SUBMENU_COLLAPSED_LIST_CLASS = "ui-menu-list ui-helper-reset ui-helper-hidden";
    public final static String PANEL_CLASS = "ui-panelmenu-panel";
    public final static String MENUITEM_LINK_WITH_ICON_CLASS = "ui-menuitem-link ui-menuitem-link-hasicon ui-corner-all";

}