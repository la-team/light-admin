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
package org.lightadmin.core.config.domain.unit.visitor;

import static org.springframework.core.GenericTypeResolver.resolveTypeArgument;
import static org.springframework.util.ClassUtils.isAssignableValue;

public abstract class ParametrizedConfigurationUnitVisitor<T extends VisitableConfigurationUnit> implements ConfigurationUnitVisitor<VisitableConfigurationUnit> {

    private Class<?> TYPE = resolveTypeArgument(getClass(), ParametrizedConfigurationUnitVisitor.class);

    @Override
    @SuppressWarnings("unchecked")
    public void visit(VisitableConfigurationUnit configurationUnit) {
        if (isAssignableValue(TYPE, configurationUnit)) {
            visitInternal((T) configurationUnit);
        }
    }

    protected abstract void visitInternal(T configurationUnit);
}