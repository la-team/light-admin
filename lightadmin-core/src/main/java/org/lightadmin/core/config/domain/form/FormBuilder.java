package org.lightadmin.core.config.domain.form;

import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.renderer.Renderer;

public interface FormBuilder {

	FormBuilder field( String fieldName );

	Fragment fragment();

	FormBuilder renderer( Renderer renderer );
}