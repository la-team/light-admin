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
package org.lightadmin.core.view.tags;

import org.lightadmin.core.web.ApplicationController;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public abstract class AbstractAutowiredTag extends SimpleTagSupport {

    @Override
    public void setJspContext(JspContext context) {
        super.setJspContext(context);

        AutowireCapableBeanFactory beanFactory = (AutowireCapableBeanFactory) context.findAttribute(ApplicationController.BEAN_FACTORY_KEY);
        beanFactory.autowireBean(this);
    }
}