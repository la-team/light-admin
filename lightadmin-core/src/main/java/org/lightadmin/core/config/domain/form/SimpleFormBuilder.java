package org.lightadmin.core.config.domain.form;

import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.renderer.Renderer;

public class SimpleFormBuilder implements FormBuilder {

	@Override
	public FormBuilder field( final String fieldName ) {
		return null;
	}

	@Override
	public Fragment fragment() {
		return null;
	}

	@Override
	public FormBuilder renderer( final Renderer renderer ) {
		return null;
	}
}