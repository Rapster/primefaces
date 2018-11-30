package org.primefaces.component.api;

import org.primefaces.renderkit.DefaultReadOnlyDecoder;
import org.primefaces.util.LangUtils;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UIItemsReadOnlyDecoder implements ReadOnlyDecoder {

    public static final ReadOnlyDecoder INSTANCE = new UIItemsReadOnlyDecoder();

    private UIItemsReadOnlyDecoder() {
        // NOOP
    }

    @Override
    public Object decodeReadOnlyValue(FacesContext context, UIInput component) {
        UIItemsComponent comp = (UIItemsComponent) component;
        String var = ((UIItemsComponent) component).getVar();
        if (!LangUtils.isValueBlank(var)) {
            List<String> values = new ArrayList<>();
            Object inputValue = component.getValue();
            if (inputValue.getClass().isArray()) {
                inputValue = Arrays.asList(inputValue);
            }

            for (Object value : (Collection) inputValue) {
                context.getExternalContext().getRequestMap().put(var, value);
                values.add(comp.getItemLabel());
            }
            context.getExternalContext().getRequestMap().remove(var);
            return values;
        } else {
            return DefaultReadOnlyDecoder.INSTANCE.decodeReadOnlyValue(context, component);
        }
    }
}
