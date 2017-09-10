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
package org.primefaces.component.orderlist;

import javax.faces.component.UIInput;
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
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.util.Constants;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.BehaviorEvent;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="components.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js")
})
@PFComponent(tagName = "orderList",
             description = "OrderList is used to sort a collection.",
             widget = true,
             parent = UIInput.class)
public class OrderList extends AbstractOrderList implements javax.faces.component.behavior.ClientBehaviorHolder, org.primefaces.component.api.PrimeClientBehaviorHolder {

	@PFPropertyKeys(base = {org.primefaces.component.api.propertykeys.UIInputPropertyKeys.class})
	public enum PropertyKeys {

		@PFProperty(description = "Name of the iterator")
		var,
		@PFProperty(description = "Label of an item")
		itemLabel,
		@PFProperty(description = "Value of an item", type = Object.class)
		itemValue,
		@PFProperty(description = "Style of the main container")
		style,
		@PFProperty(description = "Style class of the main container")
		styleClass,
		@PFProperty(description = "Disables the component", defaultValue = "false", type = Boolean.class)
		disabled,
		@PFProperty(description = "Name of the animation to display")
		effect,
		@PFProperty(description = "Text of move up button", defaultValue = "Move Up")
		moveUpLabel,
		@PFProperty(description = "Text of move top button", defaultValue = "Move Top")
		moveTopLabel,
		@PFProperty(description = "Text of move down button", defaultValue = "Move Down")
		moveDownLabel,
		@PFProperty(description = "Text of move bottom button", defaultValue = "Move Bottom")
		moveBottomLabel,
		@PFProperty(description = "Location of the order controls, valid values are \"left\" (default), \"right\" and none", defaultValue = "left")
		controlsLocation,
		@PFProperty(description = "In responsive mode, orderList adjusts itself based on screen width", defaultValue = "false", type = Boolean.class)
		responsive,;
	}


    public static final String CONTAINER_CLASS = "ui-orderlist ui-grid ui-widget";
    public static final String LIST_CLASS = "ui-widget-content ui-orderlist-list";
    public static final String CONTROLS_CLASS = "ui-orderlist-controls ui-grid-col-2";
    public static final String CAPTION_CLASS = "ui-orderlist-caption ui-widget-header ui-corner-top";
    public static final String ITEM_CLASS = "ui-orderlist-item ui-corner-all";
    public static final String MOVE_UP_BUTTON_CLASS = "ui-orderlist-button-move-up";
    public static final String MOVE_DOWN_BUTTON_CLASS = "ui-orderlist-button-move-down";
    public static final String MOVE_TOP_BUTTON_CLASS = "ui-orderlist-button-move-top";
    public static final String MOVE_BOTTOM_BUTTON_CLASS = "ui-orderlist-button-move-bottom";
    public static final String MOVE_UP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-n";
    public static final String MOVE_DOWN_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-s";
    public static final String MOVE_TOP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-n";
    public static final String MOVE_BOTTOM_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-s";

    private Map<String,AjaxBehaviorEvent> customEvents = new HashMap<String,AjaxBehaviorEvent>();
    
    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<? extends BehaviorEvent>>() {{
        put("select", SelectEvent.class);
        put("unselect", UnselectEvent.class);
        put("reorder", null);
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

    private Map<String,AjaxBehaviorEvent> getCustomEvents() {
        if(customEvents == null) {
            customEvents = new HashMap<String,AjaxBehaviorEvent>();
        }

        return customEvents;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();

        if(ComponentUtils.isRequestSource(this, context) && (event instanceof AjaxBehaviorEvent)) {
            String eventName = context.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            customEvents.put(eventName, (AjaxBehaviorEvent) event);
        } 
        else {
            super.queueEvent(event);
        }
    }

    @Override
    public void validate(FacesContext context) {
        super.validate(context);

        if(isValid() && customEvents != null) {
            for(Iterator<String> customEventIter = customEvents.keySet().iterator(); customEventIter.hasNext();) {
                String eventName = customEventIter.next();
                AjaxBehaviorEvent behaviorEvent = customEvents.get(eventName);
                Map<String,String> params = context.getExternalContext().getRequestParameterMap();
                String clientId = this.getClientId(context);
                List<?> list = (List) this.getValue();
                FacesEvent wrapperEvent = null;

                if(eventName.equals("select")) {
                    int itemIndex = Integer.parseInt(params.get(clientId + "_itemIndex"));
                    boolean metaKey = Boolean.valueOf(params.get(clientId + "_metaKey"));
                    boolean ctrlKey = Boolean.valueOf(params.get(clientId + "_ctrlKey"));
                    wrapperEvent = new SelectEvent(this, behaviorEvent.getBehavior(), list.get(itemIndex), metaKey, ctrlKey);
                }
                else if(eventName.equals("unselect")) {
                    int itemIndex = Integer.parseInt(params.get(clientId + "_itemIndex"));
                    wrapperEvent = new UnselectEvent(this, behaviorEvent.getBehavior(), list.get(itemIndex));
                }
                else if(eventName.equals("reorder")) {
                    wrapperEvent = behaviorEvent;
                }

                wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());
                
                super.queueEvent(wrapperEvent);
            }
        }
    }

}