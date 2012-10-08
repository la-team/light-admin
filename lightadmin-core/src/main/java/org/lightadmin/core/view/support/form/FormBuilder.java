package org.lightadmin.core.view.support.form;

import org.lightadmin.core.view.support.fragment.Fragment;
import org.lightadmin.core.view.support.renderer.Renderer;

public interface FormBuilder {

	FormBuilder field( String fieldName );

	Fragment fragment();

	FormBuilder renderer( Renderer renderer );
}