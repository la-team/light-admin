package org.lightadmin.core.config.domain.sidebar;

import java.io.Serializable;

public class SidebarMetadata implements Serializable {

    private final String name;
    private final String jspFilePath;

    public SidebarMetadata(String jspFilePath) {
        this(null, jspFilePath);
    }

    public SidebarMetadata(String name, String jspFilePath) {
        this.name = name;
        this.jspFilePath = jspFilePath;
    }

    public String getName() {
        return name;
    }

    public String getJspFilePath() {
        return jspFilePath;
    }
}