package org.primefaces.renderkit;

import org.primefaces.component.api.ReadOnlyDecoder;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SelectManyReadOnlyDecoder implements ReadOnlyDecoder {

    public static final ReadOnlyDecoder INSTANCE = new SelectManyReadOnlyDecoder();

    private SelectManyReadOnlyDecoder() {
        // NOOP
    }

    @Override
    public Object decodeReadOnlyValue(FacesContext context, UIInput component) {
        List<SelectItem> items = SelectRenderer.getSelectItems(context, component);
        Collection<String> values = new ArrayList<>();
        Collection<Object> inputs = convertInputValues(component);

        findLabelForMatchingValue(items, values, inputs);
        return values;
    }

    protected void findLabelForMatchingValue(List<SelectItem> items, Collection<String> values, Collection<Object> inputs) {
        for (SelectItem item : items) {
            if (item instanceof SelectItemGroup) {
                findLabelForMatchingValue(Arrays.asList(((SelectItemGroup) item).getSelectItems()), values, inputs);
            } else {
                if (inputs.contains(item.getValue())) {
                    values.add(item.getLabel());
                }
            }
        }
    }


    protected Collection<Object> convertInputValues(UIInput component) {
        Object value = component.getValue();
        return value.getClass().isArray()
                ? Arrays.asList(value)
                : (Collection) value;
    }
}
