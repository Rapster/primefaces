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
package org.primefaces.component.datascroller;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.model.LazyDataModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class DataScrollerRenderer extends CoreRenderer {

    public static final String CONTAINER_CLASS = "ui-datascroller ui-widget";
    public static final String INLINE_CONTAINER_CLASS = "ui-datascroller ui-datascroller-inline ui-widget";
    public static final String HEADER_CLASS = "ui-datascroller-header ui-widget-header ui-corner-top";
    public static final String CONTENT_CLASS = "ui-datascroller-content ui-widget-content";
    public static final String LIST_CLASS = "ui-datascroller-list";
    public static final String ITEM_CLASS = "ui-datascroller-item";
    public static final String LOADER_CLASS = "ui-datascroller-loader";
    public static final String LOADING_CLASS = "ui-datascroller-loading";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        DataScroller ds = (DataScroller) component;

        if (ds.isLoadRequest()) {
            String clientId = ds.getClientId(context);
            int offset = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get(clientId + "_offset"));

            loadChunk(context, ds, offset, ds.getChunkSize());
        }
        else {
            int chunkSize = ds.getChunkSize();
            if (chunkSize == 0) {
                chunkSize = ds.getRowCount();
            }

            encodeMarkup(context, ds, chunkSize);
            encodeScript(context, ds, chunkSize);
        }
    }

    protected void encodeMarkup(FacesContext context, DataScroller ds, int chunkSize) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = ds.getClientId(context);
        boolean inline = ds.getMode().equals("inline");
        UIComponent header = ds.getFacet("header");
        UIComponent loader = ds.getFacet("loader");
        String contentCornerClass = null;
        String containerClass = inline ? INLINE_CONTAINER_CLASS : CONTAINER_CLASS;

        String style = ds.getStyle();
        String userStyleClass = ds.getStyleClass();
        String styleClass = (userStyleClass == null) ? containerClass : containerClass + " " + userStyleClass;

        writer.startElement("div", ds);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", styleClass, null);
        }

        if (header != null && header.isRendered()) {
            writer.startElement("div", ds);
            writer.writeAttribute("class", HEADER_CLASS, null);
            header.encodeAll(context);
            writer.endElement("div");

            contentCornerClass = "ui-corner-bottom";
        }
        else {
            contentCornerClass = "ui-corner-all";
        }

        writer.startElement("div", ds);
        writer.writeAttribute("class", CONTENT_CLASS + " " + contentCornerClass, null);
        if (inline) {
            writer.writeAttribute("style", "height:" + ds.getScrollHeight() + "px", null);
        }

        writer.startElement("ul", ds);
        writer.writeAttribute("class", LIST_CLASS, null);
        loadChunk(context, ds, 0, chunkSize);
        ds.setRowIndex(-1);
        writer.endElement("ul");

        writer.startElement("div", null);
        writer.writeAttribute("class", LOADER_CLASS, null);
        if (loader != null && loader.isRendered()) {
            loader.encodeAll(context);
        }
        writer.endElement("div");

        writer.endElement("div");

        writer.endElement("div");
    }

    protected void encodeScript(FacesContext context, DataScroller ds, int chunkSize) throws IOException {
        String clientId = ds.getClientId(context);
        String loadEvent = ds.getFacet("loader") == null ? "scroll" : "manual";

        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("DataScroller", ds.resolveWidgetVar(), clientId)
                .attr("chunkSize", chunkSize)
                .attr("totalSize", ds.getRowCount())
                .attr("loadEvent", loadEvent)
                .attr("mode", ds.getMode(), "document")
                .attr("buffer", ds.getBuffer())
                .finish();
    }

    protected void loadChunk(FacesContext context, DataScroller ds, int start, int size) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        if (ds.isLazy()) {
            loadLazyData(ds, start, size);
        }

        for (int i = start; i < (start + size); i++) {
            ds.setRowIndex(i);
            if (!ds.isRowAvailable()) {
                break;
            }

            writer.startElement("li", null);
            writer.writeAttribute("class", ITEM_CLASS, null);
            renderChildren(context, ds);
            writer.endElement("li");
        }
        ds.setRowIndex(-1);
    }

    protected void loadLazyData(DataScroller ds, int start, int size) {
        LazyDataModel lazyModel = (LazyDataModel) ds.getValue();

        if (lazyModel != null) {
            List<?> data = lazyModel.load(start, size, null, null, null);
            lazyModel.setPageSize(size);
            lazyModel.setWrappedData(data);
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
