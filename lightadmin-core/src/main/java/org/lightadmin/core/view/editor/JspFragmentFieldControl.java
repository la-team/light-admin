package org.lightadmin.core.view.editor;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class JspFragmentFieldControl extends SimpleTagSupport implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private final String jspPath;
	private String field;

	public JspFragmentFieldControl(String jspPath) {
		this.jspPath = jspPath;
	}

	@Override
	public void doTag() throws JspException, IOException {

		addAttribute("field", field);
		prepare();
		PageContext pageContext = (PageContext) getJspContext();
		try {
			pageContext.include(jspPath, true);
		} catch (ServletException e) {
			throw new JspException(e);
		} finally {
			pageContext.removeAttribute("field", PageContext.REQUEST_SCOPE);
		}
	}

	protected void addAttribute(String name, Object value) {
		PageContext pageContext = (PageContext) getJspContext();
		pageContext.setAttribute(name, value, PageContext.REQUEST_SCOPE);
	}

	public void setField(String field) {
		this.field = field;
	}

	protected void prepare() {
	}

}
