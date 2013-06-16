package org.lightadmin.core.view.editor;

import java.util.List;

import org.lightadmin.core.config.domain.common.EnumElement;

public class EnumFieldEditControl extends JspFragmentFieldControl {

	private static final long serialVersionUID = 1L;

	private final List<EnumElement> elements;

	public EnumFieldEditControl(List<EnumElement> elements) {
		super("/views/editors/enum-field-edit-control.jsp");
		this.elements = elements;
	}

	@Override
	protected void prepare() {
		addAttribute("elements", elements);
	}

}
