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
package org.primefaces.component.diagram;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.faces.event.FacesEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.util.Constants;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import java.util.Map;
import java.util.HashMap;
import javax.faces.event.BehaviorEvent;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="components.css"),
	@ResourceDependency(library="primefaces", name="diagram/diagram.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js"),
	@ResourceDependency(library="primefaces", name="diagram/diagram.js")
})
@PFComponent(tagName = "diagram",
             description = "",
             widget = true,
             parent = UIOutput.class)
public class Diagram extends AbstractDiagram implements javax.faces.component.behavior.ClientBehaviorHolder, org.primefaces.component.api.PrimeClientBehaviorHolder {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "Name of the iterator variable used to refer each data")
		var,
		@PFProperty(description = "Inline style of the component")
		style,
		@PFProperty(description = "Style class of the component")
		styleClass,;
	}


    public static final String CONTAINER_CLASS = "ui-diagram ui-widget";
    public static final String ELEMENT_CLASS = "ui-diagram-element";
    public static final String DRAGGABLE_ELEMENT_CLASS = "ui-diagram-draggable";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<? extends BehaviorEvent>>() {{
        put("connect", ConnectEvent.class);
        put("disconnect", DisconnectEvent.class);
        put("connectionChange", ConnectionChangeEvent.class);
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

    public boolean isConnectRequest(FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context) + "_connect");
    }

    public boolean isDisconnectRequest(FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context) + "_disconnect");
    }

    public boolean isConnectionChangeRequest(FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context) + "_connectionChange");
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();

        if(ComponentUtils.isRequestSource(this, context) && event instanceof AjaxBehaviorEvent) {
            Map<String,String> params = context.getExternalContext().getRequestParameterMap();
            String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            String clientId = this.getClientId(context);
            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            DiagramModel model = (DiagramModel) this.getValue();

            if(model != null) {
                if(eventName.equals("connect") || eventName.equals("disconnect")) {
                    FacesEvent facesEvent = null;
                    Element sourceElement = model.findElement(params.get(clientId + "_sourceId"));
                    Element targetElement = model.findElement(params.get(clientId + "_targetId"));
                    EndPoint sourceEndPoint = model.findEndPoint(sourceElement, params.get(clientId + "_sourceEndPointId"));
                    EndPoint targetEndPoint = model.findEndPoint(targetElement, params.get(clientId + "_targetEndPointId"));

                    if(eventName.equals("connect"))
                        facesEvent = new ConnectEvent(this, behaviorEvent.getBehavior(), sourceElement, targetElement, sourceEndPoint, targetEndPoint);
                    else if(eventName.equals("disconnect"))
                        facesEvent = new DisconnectEvent(this, behaviorEvent.getBehavior(), sourceElement, targetElement, sourceEndPoint, targetEndPoint);

                    facesEvent.setPhaseId(behaviorEvent.getPhaseId());
                    super.queueEvent(facesEvent);
                }
                else if(eventName.equals("connectionChange")) {
                    Element originalSourceElement = model.findElement(params.get(clientId + "_originalSourceId"));
                    Element newSourceElement = model.findElement(params.get(clientId + "_newSourceId"));
                    Element originalTargetElement = model.findElement(params.get(clientId + "_originalTargetId"));
                    Element newTargetElement = model.findElement(params.get(clientId + "_newTargetId"));
                    EndPoint originalSourceEndPoint = model.findEndPoint(originalSourceElement, params.get(clientId + "_originalSourceEndPointId"));
                    EndPoint newSourceEndPoint = model.findEndPoint(newSourceElement, params.get(clientId + "_newSourceEndPointId"));
                    EndPoint originalTargetEndPoint = model.findEndPoint(originalTargetElement, params.get(clientId + "_originalTargetEndPointId"));
                    EndPoint newTargetEndPoint = model.findEndPoint(newTargetElement, params.get(clientId + "_targetEndPointId"));

                    ConnectionChangeEvent connectionChangeEvent = new ConnectionChangeEvent(this, behaviorEvent.getBehavior(), 
                            originalSourceElement, newSourceElement, originalTargetElement, newTargetElement,
                            originalSourceEndPoint, newSourceEndPoint, originalTargetEndPoint, newTargetEndPoint);

                    connectionChangeEvent.setPhaseId(behaviorEvent.getPhaseId());
                    super.queueEvent(connectionChangeEvent);
                }
            }
        }
        else {
            super.queueEvent(event);
        }
    }
}