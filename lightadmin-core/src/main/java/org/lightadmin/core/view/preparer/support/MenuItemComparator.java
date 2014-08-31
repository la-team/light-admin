/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.view.preparer.support;

import org.apache.tiles.beans.MenuItem;

import java.util.Comparator;

public class MenuItemComparator implements Comparator<MenuItem> {

    public static final MenuItemComparator INSTANCE = new MenuItemComparator();

    @Override
    public int compare(final MenuItem menuItem, final MenuItem menuItem2) {
        return menuItem.getValue().compareTo(menuItem2.getValue());
    }
}