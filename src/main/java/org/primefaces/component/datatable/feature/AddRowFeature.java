/*
 * The MIT License
 *
 * Copyright (c) 2009-2021 PrimeTek
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
package org.primefaces.component.datatable.feature;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class AddRowFeature implements DataTableFeature {

    @Override
    public void decode(FacesContext context, DataTable table) {
        SelectionFeature feature = (SelectionFeature) DataTable.FEATURES.get(DataTableFeatureKey.SELECT);
        feature.decodeSelectionRowKeys(context, table);
    }

    @Override
    public void encode(FacesContext context, DataTableRenderer renderer, DataTable table) throws IOException {
        String clientId = table.getClientId(context);
        int rowIndex = table.getRowCount() - 1;
        table.setRowIndex(table.getRowCount() - 1);

        if (table.isRowAvailable()) {
            renderer.encodeRow(context, table, clientId, rowIndex);
        }
    }

    @Override
    public boolean shouldDecode(FacesContext context, DataTable table) {
        return isAddRowRequest(context, table)
                && table.isSelectionEnabled();
    }

    @Override
    public boolean shouldEncode(FacesContext context, DataTable table) {
        return isAddRowRequest(context, table);
    }

    protected boolean isAddRowRequest(FacesContext context, DataTable table) {
        return context.getExternalContext().getRequestParameterMap().containsKey(table.getClientId(context) + "_addrow");
    }
}
