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