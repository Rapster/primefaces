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
package org.primefaces.component.layout;

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
import javax.faces.component.UIComponent;
import org.primefaces.component.layout.LayoutUnit;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.primefaces.util.Constants;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ResizeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.BehaviorEvent;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="components.css"),
	@ResourceDependency(library="primefaces", name="layout/layout.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js"),
	@ResourceDependency(library="primefaces", name="layout/layout.js")
})
@PFComponent(tagName = "layout",
             description = "Layout component features a highly customizable borderLayout model making it very easy to create complex layouts even if you're not familar with web design.",
             widget = true,
             parent = UIPanel.class)
public class Layout extends AbstractLayout implements javax.faces.component.behavior.ClientBehaviorHolder, org.primefaces.component.api.PrimeClientBehaviorHolder {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "Specifies whether layout should span all page or not", defaultValue = "false", type = Boolean.class)
		fullPage,
		@PFProperty(description = "Style to apply to container element, this is only applicable to element based layouts")
		style,
		@PFProperty(description = "Style class to apply to container element, this is only applicable to element based layouts")
		styleClass,
		@PFProperty(description = "A server side listener to process a ResizeEvent")
		onResize,
		@PFProperty(description = "A server side listener to process a CloseEvent")
		onClose,
		@PFProperty(description = "A server side listener to process a ToggleEvent")
		onToggle,
		@PFProperty(description = "Title label for the resizer element")
		resizeTitle,
		@PFProperty(description = "Title label for the collapse button of collapsible units", defaultValue = "Collapse")
		collapseTitle,
		@PFProperty(description = "Title label for the expand button of closable units")
		expandTitle,
		@PFProperty(description = "Title label for the close button of closable units", defaultValue = "Close")
		closeTitle,
		@PFProperty(description = "When enabled, layout state is saved in a cookie for the session", defaultValue = "false", type = Boolean.class)
		stateful,;
	}


    public final static String UNIT_CLASS = "ui-layout-unit ui-widget ui-widget-content ui-corner-all";
    public final static String UNIT_HEADER_CLASS = "ui-layout-unit-header ui-widget-header ui-corner-all";
    public final static String UNIT_CONTENT_CLASS = "ui-layout-unit-content ui-widget-content";
    public final static String UNIT_FOOTER_CLASS = "ui-layout-unit-footer ui-widget-header ui-corner-all";
    public final static String UNIT_HEADER_TITLE_CLASS = "ui-layout-unit-header-title";
    public final static String UNIT_FOOTER_TITLE_CLASS = "ui-layout-unit-footer-title";
    public final static String UNIT_HEADER_ICON_CLASS = "ui-layout-unit-header-icon ui-state-default ui-corner-all";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<? extends BehaviorEvent>>() {{
        put("toggle", ToggleEvent.class);
        put("close", CloseEvent.class);
        put("resize", ResizeEvent.class);
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

	protected LayoutUnit getLayoutUnitByPosition(String name) {
		for(UIComponent child : getChildren()) {
			if(child instanceof LayoutUnit) {
				LayoutUnit layoutUnit = (LayoutUnit) child;
				
				if(layoutUnit.getPosition().equalsIgnoreCase(name))
					return layoutUnit;
			}
		}
		
		return null;
	}
	
    public boolean isNested() {
        return this.getParent() instanceof LayoutUnit;
    }

    public boolean isElementLayout() {
        return !isNested() && !isFullPage();
    }

    @Override
    public void processDecodes(FacesContext context) {
        if(isSelfRequest(context)) {
            this.decode(context);
        }
        else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        if(!isSelfRequest(context)) {
            super.processValidators(context);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        if(!isSelfRequest(context)) {
            super.processUpdates(context);
        }
    }

    private boolean isSelfRequest(FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        String clientId = this.getClientId(context);

        if(isSelfRequest(context)) {

            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            FacesEvent wrapperEvent = null;

            if(eventName.equals("toggle")) {
                boolean collapsed = Boolean.valueOf(params.get(clientId + "_collapsed"));
                LayoutUnit unit = getLayoutUnitByPosition(params.get(clientId + "_unit"));
                Visibility visibility = collapsed ? Visibility.HIDDEN : Visibility.VISIBLE;
                unit.setCollapsed(collapsed);
                
                wrapperEvent = new ToggleEvent(unit, behaviorEvent.getBehavior(), visibility);
            }
            else if(eventName.equals("close")) {
                LayoutUnit unit = getLayoutUnitByPosition(params.get(clientId + "_unit"));
                unit.setVisible(false);

                wrapperEvent = new CloseEvent(unit, behaviorEvent.getBehavior());
            }
            else if(eventName.equals("resize")) {
                LayoutUnit unit = getLayoutUnitByPosition(params.get(clientId + "_unit"));
                String position = unit.getPosition();
                int width = Integer.valueOf(params.get(clientId + "_width"));
                int height = Integer.valueOf(params.get(clientId + "_height"));

                if(position.equals("west") || position.equals("east")) {
                    unit.setSize(String.valueOf(width));
                } else if(position.equals("north") || position.equals("south")) {
                    unit.setSize(String.valueOf(height));
                }

                wrapperEvent = new ResizeEvent(unit, behaviorEvent.getBehavior(), width, height);
            }

            wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());

            super.queueEvent(wrapperEvent);
        }
        else {
            super.queueEvent(event);
        }
    }
}