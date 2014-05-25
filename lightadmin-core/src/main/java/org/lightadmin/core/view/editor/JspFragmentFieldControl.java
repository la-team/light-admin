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

    private static final long serialVersionUID = 1L;

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
