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
package org.primefaces.component.organigram;

import javax.faces.component.UIComponentBase;
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
import org.primefaces.event.organigram.OrganigramNodeCollapseEvent;
import org.primefaces.event.organigram.OrganigramNodeDragDropEvent;
import org.primefaces.event.organigram.OrganigramNodeExpandEvent;
import org.primefaces.event.organigram.OrganigramNodeSelectEvent;
import org.primefaces.model.OrganigramNode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="components.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="organigram/organigram.js"),
	@ResourceDependency(library="primefaces", name="organigram/organigram.css")
})
@PFComponent(tagName = "organigram",
             description = "",
             widget = true,
             rtl = true,
             parent = UIComponentBase.class)
public class Organigram extends AbstractOrganigram implements javax.faces.component.behavior.ClientBehaviorHolder, org.primefaces.component.api.PrimeClientBehaviorHolder {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "The model", type = org.primefaces.model.OrganigramNode.class)
		value,
		@PFProperty(description = "Name of the request-scoped variable that'll be used to refer each treenode data during rendering")
		var,
		@PFProperty(description = "OrganigramNode to reference the selections", type = org.primefaces.model.OrganigramNode.class)
		selection,
		@PFProperty(description = "Style of the main container element of organigram")
		style,
		@PFProperty(description = "Style class of the main container element of organigram")
		styleClass,
		@PFProperty(description = "The height of the connector line for leaf nodes", defaultValue = "10", type = Integer.class)
		leafNodeConnectorHeight,
		@PFProperty(description = "Defines if zoom controls are rendered", defaultValue = "false", type = Boolean.class)
		zoom,
		@PFProperty(description = "Auto scroll to the selected node on rendering if enabled", defaultValue = "false", type = Boolean.class)
		autoScrollToSelection,;
	}



    private static final String DEFAULT_EVENT = "select";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<? extends BehaviorEvent>>() {{
        put("select", OrganigramNodeSelectEvent.class);
        put("expand", OrganigramNodeExpandEvent.class);
        put("collapse", OrganigramNodeCollapseEvent.class);
        put("dragdrop", OrganigramNodeDragDropEvent.class);
        put("contextmenu", OrganigramNodeSelectEvent.class);
    }});

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
         return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames()
    {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName()
    {
        return DEFAULT_EVENT;
    }

    @Override
    public void queueEvent(FacesEvent event)
    {
        FacesContext context = getFacesContext();

        if (ComponentUtils.isRequestSource(this, context) && event instanceof AjaxBehaviorEvent)
        {
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            String clientId = this.getClientId(context);
            FacesEvent wrapperEvent = null;
            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("expand"))
            {
                OrganigramNode node = findTreeNode(getValue(), params.get(clientId + "_expandNode"));
                node.setExpanded(true);

                wrapperEvent = new OrganigramNodeExpandEvent(this, behaviorEvent.getBehavior(), node);
            }
            else if (eventName.equals("collapse"))
            {
                OrganigramNode node = findTreeNode(getValue(), params.get(clientId + "_collapseNode"));
                node.setExpanded(false);

                wrapperEvent = new OrganigramNodeCollapseEvent(this, behaviorEvent.getBehavior(), node);
            }
            else if (eventName.equals("select") || eventName.equals("contextmenu"))
            {
                OrganigramNode node = findTreeNode(getValue(), params.get(clientId + "_selectNode"));

                wrapperEvent = new OrganigramNodeSelectEvent(this, behaviorEvent.getBehavior(), node);
            }
            else if (eventName.equals("dragdrop"))
            {
                OrganigramNode dragNode = findTreeNode(getValue(), params.get(clientId + "_dragNode"));
                OrganigramNode dropNode = findTreeNode(getValue(), params.get(clientId + "_dropNode"));

                // remove node from current parent
                if (dragNode != null && dropNode != null)
                {
                    OrganigramNode sourceNode = dragNode.getParent();

                    if (sourceNode != null)
                    {
                        sourceNode.getChildren().remove(dragNode);
                    }

                    // set new parent
                    dragNode.setParent(dropNode);

                    wrapperEvent = new OrganigramNodeDragDropEvent(this, behaviorEvent.getBehavior(), dragNode, dropNode, sourceNode);
                }
            }

            wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());

            super.queueEvent(wrapperEvent);
        }
        else
        {
            super.queueEvent(event);
        }
    }


    public OrganigramNode findTreeNode(OrganigramNode searchRoot, String rowKey)
    {
        if (rowKey != null && rowKey.equals("root"))
        {
            return getValue();
        }

        return OrganigramHelper.findTreeNode(searchRoot, rowKey);
    }
}