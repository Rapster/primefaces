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
package org.primefaces.component.selectcheckboxmenu;

import javax.faces.component.html.HtmlSelectManyCheckbox;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.faces.event.FacesEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.util.Constants;
import java.util.HashMap;
import javax.faces.event.BehaviorEvent;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="components.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js")
})
@PFComponent(tagName = "selectCheckboxMenu",
             description = "",
             widget = true,
             rtl = true,
             parent = HtmlSelectManyCheckbox.class)
public class SelectCheckboxMenu extends AbstractSelectCheckboxMenu implements org.primefaces.component.api.PrimeClientBehaviorHolder {

	@PFPropertyKeys(base = {org.primefaces.component.api.propertykeys.UIInputPropertyKeys.class})
	public enum PropertyKeys {

		@PFProperty(description = "", defaultValue = "java.lang.Integer.MAX_VALUE", type = Integer.class)
		scrollHeight,
		@PFProperty(description = "Client side callback to execute when overlay is displayed")
		onShow,
		@PFProperty(description = "Client side callback to execute when overlay is hidden")
		onHide,
		@PFProperty(description = "Renders an input field as a filter when enabled", defaultValue = "false", type = Boolean.class)
		filter,
		@PFProperty(description = "Match mode for filtering, valid values are startsWith (default), contains, endsWith and custom")
		filterMatchMode,
		@PFProperty(description = "Client side function to use in custom filterMatchMode")
		filterFunction,
		@PFProperty(description = "Defines if filtering would be case sensitive. Default is false", defaultValue = "false", type = Boolean.class)
		caseSensitive,
		@PFProperty(description = "")
		panelStyle,
		@PFProperty(description = "")
		panelStyleClass,
		@PFProperty(description = "Appends the overlay to the element defined by search expression. Defaults to document body")
		appendTo,
		@PFProperty(description = "Position of the element in the tabbing order")
		tabindex,
		@PFProperty(description = "Advisory tooltip information")
		title,
		@PFProperty(description = "When enabled, the header of panel is displayed. Default is true", defaultValue = "true", type = Boolean.class)
		showHeader,
		@PFProperty(description = "When enabled, the selected items are displayed on label. Default is false", defaultValue = "false", type = Boolean.class)
		updateLabel,
		@PFProperty(description = "", defaultValue = "false", type = Boolean.class)
		multiple,;
	}


    public final static String STYLE_CLASS = "ui-selectcheckboxmenu ui-widget ui-state-default ui-corner-all";
    public final static String LABEL_CONTAINER_CLASS = "ui-selectcheckboxmenu-label-container";
    public final static String LABEL_CLASS = "ui-selectcheckboxmenu-label ui-corner-all";
    public final static String TRIGGER_CLASS = "ui-selectcheckboxmenu-trigger ui-state-default ui-corner-right";
    public final static String PANEL_CLASS = "ui-selectcheckboxmenu-panel ui-widget-content ui-corner-all ui-helper-hidden";
    public final static String LIST_CLASS = "ui-selectcheckboxmenu-items ui-selectcheckboxmenu-list ui-widget-content ui-widget ui-corner-all ui-helper-reset";
    public final static String ITEM_CLASS = "ui-selectcheckboxmenu-item ui-selectcheckboxmenu-list-item ui-corner-all ui-helper-clearfix";
    public final static String MULTIPLE_CLASS = "ui-selectcheckboxmenu-multiple";
    public final static String MULTIPLE_CONTAINER_CLASS = "ui-selectcheckboxmenu-multiple-container ui-widget ui-inputfield ui-state-default ui-corner-all";
    public final static String TOKEN_DISPLAY_CLASS = "ui-selectcheckboxmenu-token ui-state-active ui-corner-all";
    public final static String TOKEN_LABEL_CLASS = "ui-selectcheckboxmenu-token-label";
    public final static String TOKEN_ICON_CLASS = "ui-selectcheckboxmenu-token-icon ui-icon ui-icon-close";

    private final static String DEFAULT_EVENT = "change";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<? extends BehaviorEvent>>() {{
        put("change", null);
        put("toggleSelect", ToggleSelectEvent.class);
    }});

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
         return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return DEFAULT_EVENT;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();
        String eventName = context.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        
        if(event instanceof AjaxBehaviorEvent && eventName.equals("toggleSelect")) {
            Map<String,String> params = context.getExternalContext().getRequestParameterMap();
            String clientId = this.getClientId(context);
            boolean checked = Boolean.valueOf(params.get(clientId + "_checked"));
            ToggleSelectEvent toggleSelectEvent = new ToggleSelectEvent(this, ((AjaxBehaviorEvent) event).getBehavior(), checked);
            toggleSelectEvent.setPhaseId(event.getPhaseId());

            super.queueEvent(toggleSelectEvent);
        }
        else {
            super.queueEvent(event);
        }
    }
}