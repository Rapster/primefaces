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

import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.ColumnBase;
import org.primefaces.component.datatable.DataTableBase;

import java.io.Serializable;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

public class SortMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String columnKey;
    private String sortField;
    private SortOrder sortOrder = SortOrder.UNSORTED;
    private ValueExpression sortBy;
    private MethodExpression sortFunction;
    private boolean active;

    public SortMeta() {
        // NOOP
    }

    public SortMeta(String columnKey, String sortField, SortOrder sortOrder, MethodExpression sortFunction, ValueExpression sortBy, boolean active) {
        this.columnKey = columnKey;
        this.sortField = sortField;
        this.sortOrder = sortOrder == null ? SortOrder.ASCENDING : sortOrder;
        this.sortFunction = sortFunction;
        this.sortBy = sortBy;
        this.active = active;
    }

    public SortMeta(SortMeta sortMeta) {
        this(sortMeta.columnKey, sortMeta.sortField, sortMeta.sortOrder, sortMeta.sortFunction, sortMeta.sortBy, sortMeta.active);
    }

    public static final SortMeta of(UIColumn column) {
        return new SortMeta(column.getColumnKey(),
                column.getField(),
                SortOrder.of(column.getOrder()),
                column.getSortFunction(),
                column.getValueExpression(ColumnBase.PropertyKeys.sortBy.name()),
                column.isActive());
    }

    public String getColumnKey() {
        return columnKey;
    }

    public String getSortField() {
        return sortField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public MethodExpression getSortFunction() {
        return sortFunction;
    }

    public ValueExpression getSortBy() {
        return sortBy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "SortMeta{" +
                "columnKey='" + columnKey + '\'' +
                ", sortField='" + sortField + '\'' +
                ", sortOrder=" + sortOrder +
                ", sortBy=" + sortBy +
                ", sortFunction=" + sortFunction +
                ", active=" + active +
                '}';
    }
}
