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
package org.lightadmin.core.config.domain.unit;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class DomainTypeConfigurationUnit implements ConfigurationUnit {

    private final Class<?> domainType;

    protected DomainTypeConfigurationUnit(final Class<?> domainType) {
        this.domainType = domainType;
    }

    public Class<?> getDomainType() {
        return domainType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DomainTypeConfigurationUnit that = (DomainTypeConfigurationUnit) o;

        if (getDomainConfigurationUnitType() != that.getDomainConfigurationUnitType()) {
            return false;
        }

        return domainType.equals(that.domainType);
    }

    @Override
    public int hashCode() {
        int result = domainType.hashCode();
        result = 31 * result + getDomainConfigurationUnitType().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("domainType", domainType).append("domainConfigurationUnitType", getDomainConfigurationUnitType()).toString();
    }
}