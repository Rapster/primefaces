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
package org.primefaces.component.gmap;

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
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.ReverseGeocodeEvent;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.GeocodeResult;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.util.Constants;
import org.primefaces.context.RequestContext;
import javax.faces.event.BehaviorEvent;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js"),
	@ResourceDependency(library="primefaces", name="gmap/gmap.js")
})
@PFComponent(tagName = "gmap",
             description = "GMap component is built on Google Maps API Version 3. Gmap is highly integrated with JSF development model and enhanced with Ajax capabilities.",
             widget = true,
             rtl = true,
             parent = UIComponentBase.class)
public class GMap extends AbstractGMap implements javax.faces.component.behavior.ClientBehaviorHolder, org.primefaces.component.api.PrimeClientBehaviorHolder {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "An org.primefaces.model.MapModel instance", type = org.primefaces.model.map.MapModel.class)
		model,
		@PFProperty(description = "Inline style of the map container")
		style,
		@PFProperty(description = "Style class of the map container")
		styleClass,
		@PFProperty(description = "Type of the map. Required", required = true)
		type,
		@PFProperty(description = "Center point of the map. Required", required = true)
		center,
		@PFProperty(description = "Defines the initial zoom level. Default is 8", defaultValue = "8", type = Integer.class, required = true)
		zoom,
		@PFProperty(description = "Controls street view support. Default is false", defaultValue = "false", type = Boolean.class)
		streetView,
		@PFProperty(description = "Disables default UI controls. Default is false", defaultValue = "false", type = Boolean.class)
		disableDefaultUI,
		@PFProperty(description = "Defines visibility of navigation control. Default is true", defaultValue = "true", type = Boolean.class)
		navigationControl,
		@PFProperty(description = "Defines visibility of map type control. Default is true", defaultValue = "true", type = Boolean.class)
		mapTypeControl,
		@PFProperty(description = "Defines draggability of map. Default is true", defaultValue = "true", type = Boolean.class)
		draggable,
		@PFProperty(description = "Disables zooming on mouse double click. Default is false", defaultValue = "false", type = Boolean.class)
		disableDoubleClickZoom,
		@PFProperty(description = "Javascript callback to execute when a point on map is clicked")
		onPointClick,
		@PFProperty(description = "Defines setting center and zoom values auto by included overlays on startup. Default value is false", defaultValue = "false", type = Boolean.class)
		fitBounds,
		@PFProperty(description = "Controls scrollwheel zooming on the map. Default is true", defaultValue = "true", type = Boolean.class)
		scrollWheel,;
	}


    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<? extends BehaviorEvent>>() {{
        put("overlaySelect", OverlaySelectEvent.class);
        put("stateChange", StateChangeEvent.class);
        put("pointSelect", PointSelectEvent.class);
        put("markerDrag", MarkerDragEvent.class);
        put("geocode", GeocodeEvent.class);
        put("reverseGeocode", ReverseGeocodeEvent.class);
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
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        String clientId = this.getClientId(context);

        if(isSelfRequest(context)) {

            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            FacesEvent wrapperEvent = null;

            if(eventName.equals("overlaySelect")) {
                wrapperEvent = new OverlaySelectEvent(this, behaviorEvent.getBehavior(), this.getModel().findOverlay(params.get(clientId + "_overlayId")));

                //if there is info window, update and show it
                GMapInfoWindow infoWindow = getInfoWindow();
                if(infoWindow != null) {
                    RequestContext.getCurrentInstance(getFacesContext()).update(infoWindow.getClientId(context));
                }
            }
            else if(eventName.equals("stateChange")) {
                String[] centerLoc = params.get(clientId + "_center").split(",");
                String[] northeastLoc = params.get(clientId + "_northeast").split(",");
                String[] southwestLoc = params.get(clientId + "_southwest").split(",");
                int zoomLevel = Integer.valueOf(params.get(clientId + "_zoom"));

                LatLng center = new LatLng(Double.valueOf(centerLoc[0]), Double.valueOf(centerLoc[1]));
                LatLng northeast = new LatLng(Double.valueOf(northeastLoc[0]), Double.valueOf(northeastLoc[1]));
                LatLng southwest = new LatLng(Double.valueOf(southwestLoc[0]), Double.valueOf(southwestLoc[1]));

                wrapperEvent = new StateChangeEvent(this, behaviorEvent.getBehavior(), new LatLngBounds(northeast, southwest), zoomLevel, center);
            }
            else if(eventName.equals("pointSelect")) {
                String[] latlng = params.get(clientId + "_pointLatLng").split(",");
                LatLng position = new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1]));

                wrapperEvent = new PointSelectEvent(this, behaviorEvent.getBehavior(), position);
            }
            else if(eventName.equals("markerDrag")) {
                Marker marker = (Marker) this.getModel().findOverlay(params.get(clientId + "_markerId"));
                double lat = Double.valueOf(params.get(clientId + "_lat"));
                double lng = Double.valueOf(params.get(clientId + "_lng"));
                marker.setLatlng(new LatLng(lat, lng));

                wrapperEvent = new MarkerDragEvent(this, behaviorEvent.getBehavior(), marker);
            }
            else if(eventName.equals("geocode")) {
                List<GeocodeResult> results = new ArrayList<GeocodeResult>();
                String query = params.get(clientId + "_query");
                String[] addresses = params.get(clientId + "_addresses").split("_primefaces_");                
                String[] lats = params.get(clientId + "_lat").split(",");
                String[] lngs = params.get(clientId + "_lng").split(",");

                for(int i = 0; i < addresses.length; i++) {
                    results.add(new GeocodeResult(addresses[i], new LatLng(Double.valueOf(lats[i]), Double.valueOf(lngs[i]))));
                }
                
                wrapperEvent = new GeocodeEvent(this, behaviorEvent.getBehavior(), query, results);
            }
            else if(eventName.equals("reverseGeocode")) {                
                List<String> addresses = new ArrayList<String>();
                String[] results = params.get(clientId + "_address").split("_primefaces_");
                for (int i = 0; i < results.length; i++) {
                    addresses.add(results[i]);
                }

                double lat = Double.valueOf(params.get(clientId + "_lat"));
                double lng = Double.valueOf(params.get(clientId + "_lng"));
                LatLng coord = new LatLng(lat, lng);

                wrapperEvent = new ReverseGeocodeEvent(this, behaviorEvent.getBehavior(), coord, addresses);
            }

            wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());

            super.queueEvent(wrapperEvent);
        }
        else {
            super.queueEvent(event);
        }
    }
	
	public GMapInfoWindow getInfoWindow() {
		for(UIComponent kid : getChildren()) {
			if(kid instanceof GMapInfoWindow)
				return (GMapInfoWindow) kid;
		}
		
		return null;
	}
	
    private boolean isSelfRequest(FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }



    
}