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
package org.primefaces.component.rowtoggler;

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
import org.primefaces.component.datatable.DataTable;

@PFComponent(tagName = "rowToggler",
             description = "RowToggler is a helper component for datatable.",
             parent = UIComponentBase.class)
public class RowToggler extends AbstractRowToggler {

	@PFPropertyKeys(base = {})
	public enum PropertyKeys {

		@PFProperty(description = "Expand text to display instead of icon to")
		expandLabel,
		@PFProperty(description = "Collapse text to display instead of icon")
		collapseLabel,
		@PFProperty(description = "Position of the element in the tabbing order", defaultValue = "0")
		tabindex,;
	}


    public final static String COLLAPSED_ICON = "ui-icon ui-icon-circle-triangle-e";
    public final static String EXPANDED_ICON = "ui-icon ui-icon-circle-triangle-s";

    public final static String ROW_TOGGLER = "primefaces.rowtoggler.aria.ROW_TOGGLER";

    private DataTable parentTable = null;

    public DataTable getParentTable(FacesContext context) {
        if(parentTable == null) {
            UIComponent parent = this.getParent();

            while(parent != null) {
                if(parent instanceof DataTable) {
                    parentTable = (DataTable) parent;
                    break;
                }

                parent = parent.getParent();
            }
        }

        return parentTable;
    }
}