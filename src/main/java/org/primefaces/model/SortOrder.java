/**
 * The MIT License
 * <p>
 * Copyright (c) 2009-2019 PrimeTek
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.model;

import java.util.Arrays;
import java.util.List;

public enum SortOrder {
    ASCENDING("asc", "1", 1),
    DESCENDING("desc", "-1", -1),
    UNSORTED("", null, "0", 0);

    private List<Object> values;

    SortOrder(Object... values) {
        this.values = Arrays.asList(values);
    }

    public static SortOrder of(Object order) {
        SortOrder s = UNSORTED;
        for (SortOrder o : values()) {
            if (o.values.contains(order)) {
                s = o;
                break;
            }
        }
        return s;
    }
}
