/**
 * The MIT License
 *
 * Copyright (c) 2009-2019 PrimeTek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.model;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * Generic comparator for column sorting.
 */
public class BeanPropertyComparator implements Comparator {

    private String var;
    private boolean caseSensitive = false;
    private Locale locale;
    private Collator collator;
    private int nullSortOrder;
    private SortMeta sortMeta;

    public BeanPropertyComparator(String var, SortMeta sortMeta, boolean caseSensitive, Locale locale, int nullSortOrder) {
        this.sortMeta = sortMeta;
        this.var = var;
        this.caseSensitive = caseSensitive;
        this.locale = locale;
        this.collator = Collator.getInstance(locale);
        this.nullSortOrder = nullSortOrder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object obj1, Object obj2) {
        FacesContext context = FacesContext.getCurrentInstance();
        ValueExpression sortBy = sortMeta.getSortBy();

        try {
            context.getExternalContext().getRequestMap().put(var, obj1);
            Object value1 = sortBy != null
                    ? sortBy.getValue(context.getELContext())
                    : getValueFromVarField(var, sortMeta.getSortField());

            context.getExternalContext().getRequestMap().put(var, obj2);
            Object value2 = sortBy != null
                    ? sortBy.getValue(context.getELContext())
                    : getValueFromVarField(var, sortMeta.getSortField());

            int result;

            //Empty check
            if (value1 == null && value2 == null) {
                return 0;
            }
            else if (value1 == null) {
                result = nullSortOrder;
            }
            else if (value2 == null) {
                result = -1 * nullSortOrder;
            }
            else if (sortMeta.getSortFunction() == null) {
                if (value1 instanceof String && value2 instanceof String) {
                    if (this.caseSensitive) {
                        result = collator.compare(value1, value2);
                    }
                    else {
                        String str1 = (((String) value1).toLowerCase(locale));
                        String str2 = (((String) value2).toLowerCase(locale));

                        result = collator.compare(str1, str2);
                    }
                }
                else {
                    result = ((Comparable) value1).compareTo(value2);
                }
            }
            else {
                result = (Integer) sortMeta.getSortFunction().invoke(context.getELContext(), new Object[]{value1, value2});
            }

            return SortOrder.ASCENDING.equals(sortMeta.getSortOrder()) ? result : -1 * result;

        }
        catch (Exception e) {
            throw new FacesException(e);
        }
    }

    private Object getValueFromVarField(String var, String field) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory()
                .createValueExpression(context.getELContext(),"#{" + var + "." + field + "}", String.class)
                .getValue(context.getELContext());
    }
}
