package org.primefaces.component.api;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public interface ReadOnlyDecoder {

    Object decodeReadOnlyValue(FacesContext context, UIInput component);
}
