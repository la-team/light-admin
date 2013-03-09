package org.lightadmin;

import org.lightadmin.data.Domain;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

public class LoginListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestClass( TestContext testContext ) throws Exception {
		getStartPageAwareTestContext( testContext ).loginAndNavigateToDomain( getTestDomain( testContext ) );
	}

	@Override
	public void afterTestClass( TestContext testContext ) throws Exception {
		getStartPageAwareTestContext( testContext ).logout();
	}

	private LoginService getStartPageAwareTestContext( TestContext testContext ) {
		return testContext.getApplicationContext().getBean( LoginService.class );
	}

	private Domain getTestDomain( TestContext testContext ) {
		final LoginOnce annotation = findAnnotation( testContext.getTestClass(), LoginOnce.class );

		return annotation == null ? null : annotation.domain();
	}

}
