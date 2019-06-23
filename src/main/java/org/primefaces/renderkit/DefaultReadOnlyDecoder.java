package org.primefaces.renderkit;

import org.primefaces.component.api.ReadOnlyDecoder;
import org.primefaces.util.ComponentUtils;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public class DefaultReadOnlyDecoder implements ReadOnlyDecoder<UIInput> {

    public static final ReadOnlyDecoder INSTANCE = new DefaultReadOnlyDecoder();

    private DefaultReadOnlyDecoder() {
        // NOOP
    }

    @Override
    public Object decodeReadOnlyValue(FacesContext context, UIInput component) {
        return ComponentUtils.getValueToRender(context, component);
    }
}
