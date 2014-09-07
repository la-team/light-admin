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
package org.lightadmin.jhipster.config.administration;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.jhipster.domain.User;

@SuppressWarnings("unused")
public class UserAdministration extends AdministrationConfiguration<User> {

    @Override
    public ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("Users management").build();
    }

    @Override
    public EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder
                .nameField("login")
                .pluralName("Users")
                .singularName("User")
                .build();
    }

    @Override
    public FieldSetConfigurationUnit listView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("login").caption("Login")
                .field("firstName").caption("First Name")
                .field("lastName").caption("Last Name")
                .field("email").caption("Email")
                .field("activated").caption("Activated")
                .build();
    }

    @Override
    public FieldSetConfigurationUnit quickView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("login").caption("Login")
                .field("firstName").caption("First Name")
                .field("lastName").caption("Last Name")
                .field("email").caption("Email")
                .field("activated").caption("Activated")
                .field("langKey").caption("Lang Key")
                .field("activationKey").caption("Actication Key")
                .build();
    }

    @Override
    public FieldSetConfigurationUnit showView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("login").caption("Login")
                .field("firstName").caption("First Name")
                .field("lastName").caption("Last Name")
                .field("email").caption("Email")
                .field("activated").caption("Activated")
                .field("langKey").caption("Lang Key")
                .field("activationKey").caption("Actication Key")
                .field("authorities").caption("Authorities")
                .field("persistentTokens").caption("Persistent Tokens")
                .build();
    }

    @Override
    public FieldSetConfigurationUnit formView(PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("login").caption("Login")
                .field("password").caption("Password")
                .field("firstName").caption("First Name")
                .field("lastName").caption("Last Name")
                .field("email").caption("Email")
                .field("activated").caption("Activated")
                .field("langKey").caption("Lang Key")
                .field("activationKey").caption("Actication Key")
                .field("authorities").caption("Authorities")
                .field("persistentTokens").caption("Persistent Tokens")
                .field("createdDate").caption("Created Date")
                .field("createdBy").caption("Created By")
                .field("lastModifiedBy").caption("Last Modified By")
                .field("lastModifiedDate").caption("Last Modified Date")
                .build();
    }

    @Override
    public FiltersConfigurationUnit filters(FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Login", "login")
                .filter("Active", "activated")
                .build();
    }
}