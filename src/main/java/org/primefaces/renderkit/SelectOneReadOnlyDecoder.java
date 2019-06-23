package org.primefaces.renderkit;

import org.primefaces.component.api.ReadOnlyDecoder;

import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.util.Arrays;
import java.util.List;

public class SelectOneReadOnlyDecoder implements ReadOnlyDecoder<UISelectOne> {

    public static final ReadOnlyDecoder INSTANCE = new SelectOneReadOnlyDecoder();

    private SelectOneReadOnlyDecoder() {
        // NOOP
    }


    @Override
    public Object decodeReadOnlyValue(FacesContext context, UISelectOne component) {
        List<SelectItem> items = SelectRenderer.getSelectItems(context, component);
        Object value = component.getValue();
        return findLabelForMatchingValue(items, value);
    }

    protected String findLabelForMatchingValue(List<SelectItem> items, Object value) {
        for (SelectItem item : items) {
            if (item instanceof SelectItemGroup) {
                String res = findLabelForMatchingValue(Arrays.asList(((SelectItemGroup) item).getSelectItems()), value);
                if (res != null) {
                    return res;
                }
            }
            else if (item.getValue().equals(value)) {
                return item.getLabel();
            }
        }

        return null;
    }
}
