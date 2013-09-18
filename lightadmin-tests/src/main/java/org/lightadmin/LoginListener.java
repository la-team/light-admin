package org.lightadmin;

import org.lightadmin.data.Domain;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

public class LoginListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestClass( TestContext testContext ) throws Exception {
		if ( null != ( getTestDomain( testContext ) ) ) {
			getLoginService( testContext ).navigateToDomain( getTestDomain( testContext ) );
		}
	}

	@Override
	public void afterTestClass( TestContext testContext ) throws Exception {
		if ( null != ( getTestDomain( testContext ) ) ) {
			getLoginService( testContext ).logout();
		}
	}

	private LoginService getLoginService( TestContext testContext ) {
		return testContext.getApplicationContext().getBean( LoginService.class );
	}

	private static Domain getTestDomain( TestContext testContext ) {
		final LoginOnce annotation = findAnnotation( testContext.getTestClass(), LoginOnce.class );

		return annotation == null ? null : annotation.domain();
	}

}
