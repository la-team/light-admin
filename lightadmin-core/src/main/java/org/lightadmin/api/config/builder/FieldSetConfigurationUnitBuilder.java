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
package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface FieldSetConfigurationUnitBuilder extends ConfigurationUnitBuilder<FieldSetConfigurationUnit> {

    FieldSetConfigurationUnitBuilder field(String fieldName);

    FieldSetConfigurationUnitBuilder caption(String caption);

    FieldSetConfigurationUnitBuilder dynamic(String expression);

    FieldSetConfigurationUnitBuilder renderable(FieldValueRenderer<?> renderer);

    FieldSetConfigurationUnitBuilder enumeration(EnumElement... elements);

}
