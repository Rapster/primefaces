package org.primefaces.component.api;

import javax.faces.context.FacesContext;

public interface ReadOnlyDecoder<T> {

    Object decodeReadOnlyValue(FacesContext context, T component);
}
