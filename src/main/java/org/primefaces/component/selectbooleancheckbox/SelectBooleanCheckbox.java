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
package org.primefaces.component.selectbooleancheckbox;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;
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
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js")
})
@PFComponent(tagName = "selectBooleanCheckbox",
             description = "",
             widget = true,
             rtl = true,
             parent = HtmlSelectBooleanCheckbox.class)
public class SelectBooleanCheckbox extends AbstractSelectBooleanCheckbox implements org.primefaces.component.api.InputHolder {

	@PFPropertyKeys(base = {org.primefaces.component.api.propertykeys.UIInputPropertyKeys.class})
	public enum PropertyKeys {

		@PFProperty(description = "Label to display next to checkbox")
		itemLabel,;
	}


    public final static String STYLE_CLASS = "ui-checkbox ui-widget";
    public final static String CHECKBOX_BOX_CLASS = "ui-checkbox-box ui-widget ui-corner-all ui-state-default";
    public final static String CHECKBOX_INPUT_WRAPPER_CLASS = "ui-helper-hidden";
    public final static String CHECKBOX_ICON_CLASS = "ui-checkbox-icon";
    public final static String CHECKBOX_CHECKED_ICON_CLASS = "ui-icon ui-icon-check";
    public final static String LABEL_CLASS = "ui-checkbox-label";
    
    public final static String MOBILE_STYLE_CLASS = "ui-checkbox";
    public final static String MOBILE_LABEL_OFF_CLASS = "ui-btn ui-btn-inherit ui-btn-icon-left ui-corner-all ui-checkbox-off";
    public final static String MOBILE_LABEL_ON_CLASS = "ui-btn ui-btn-inherit ui-btn-icon-left ui-corner-all ui-checkbox-on";

    public String getInputClientId() {
        return this.getClientId(getFacesContext()) + "_input";
    }

    public String getValidatableInputClientId() {
        return this.getInputClientId();
    }

    public void setLabelledBy(String labelledBy) {
        getStateHelper().put("labelledby", labelledBy);
    }
    public String getLabelledBy() {
        return (String) getStateHelper().get("labelledby");
    }
}