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
package org.lightadmin.core.view.editor;

import org.springframework.data.mapping.PersistentProperty;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.Serializable;

import static javax.servlet.jsp.PageContext.REQUEST_SCOPE;

public class JspFragmentFieldControl extends SimpleTagSupport implements Serializable, Cloneable {

    private final String jspPath;

    protected PersistentProperty persistentProperty;
    protected String field;

    public JspFragmentFieldControl(String jspPath) {
        this.jspPath = jspPath;
    }

    @Override
    public void doTag() throws JspException, IOException {
        addAttribute("field", field);
        addAttribute("attributeMetadata", persistentProperty);
        prepare();
        PageContext pageContext = (PageContext) getJspContext();
        try {
            pageContext.include(jspPath, true);
        } catch (ServletException e) {
            throw new JspException(e);
        } finally {
            pageContext.removeAttribute("field", REQUEST_SCOPE);
            pageContext.removeAttribute("attributeMetadata", REQUEST_SCOPE);
        }
    }

    protected void addAttribute(String name, Object value) {
        PageContext pageContext = (PageContext) getJspContext();
        pageContext.setAttribute(name, value, REQUEST_SCOPE);
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setPersistentProperty(PersistentProperty persistentProperty) {
        this.persistentProperty = persistentProperty;
    }

    protected void prepare() {
    }
}