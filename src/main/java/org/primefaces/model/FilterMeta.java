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

import java.io.Serializable;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.column.ColumnBase;

public class FilterMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String filterField;
    private String columnKey;
    private transient UIColumn column;
    private ValueExpression filterByVE;
    private MatchMode filterMatchMode;
    private Object filterValue;

    public FilterMeta() {
        // NOOP
    }

    public FilterMeta(String filterField, Object filterValue) {
        this.filterField = filterField;
        this.filterValue = filterValue;
    }

    public FilterMeta(FilterMeta filterMeta) {
        this.filterField = filterMeta.filterField;
        this.filterValue = filterMeta.filterValue;
        this.columnKey = filterMeta.columnKey;
        this.column = filterMeta.column;
        this.filterByVE = filterMeta.filterByVE;
        this.filterMatchMode = filterMeta.filterMatchMode;
    }

    public static final FilterMeta of(UIColumn column) {
        FilterMeta f = new FilterMeta();
        f.filterField = column.getField();
        f.columnKey = column.getColumnKey();
        f.filterByVE = column.getValueExpression(ColumnBase.PropertyKeys.filterBy.name());
        f.filterMatchMode = MatchMode.of(column.getFilterMatchMode());
        f.filterValue = column.getFilterValue();
        f.column = column;
        return f;
    }

    public String getFilterField() {
        return filterField;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public ValueExpression getFilterByVE() {
        return filterByVE;
    }

    public MatchMode getFilterMatchMode() {
        return filterMatchMode;
    }

    public Object getFilterValue() {
        return filterValue;
    }

    public UIColumn getColumn() {
        return column;
    }

    public void setColumn(UIColumn column) {
        this.column = column;
    }

    private Object getValueFromVarField(String var, String field) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory()
                .createValueExpression(context.getELContext(),"#{" + var + "." + field + "}", String.class)
                .getValue(context.getELContext());
    }

    @Override
    public String toString() {
        return "FilterMeta{" +
                "filterField='" + filterField + '\'' +
                ", columnKey='" + columnKey + '\'' +
                ", column=" + column +
                ", filterByVE=" + filterByVE +
                ", filterMatchMode=" + filterMatchMode +
                ", filterValue=" + filterValue +
                '}';
    }
}
