package org.lightadmin.core.view.editor;

public class EnumFieldEditControl extends JspFragmentFieldControl {

	private static final long serialVersionUID = 1L;

	private final String[] enumValues;

	public EnumFieldEditControl(String... enumValues) {
		super("/views/editors/enum-field-edit-control.jsp");
		this.enumValues = enumValues;
	}

	@Override
	protected void prepare() {
		addAttribute("enumValues", enumValues);
	}

}
