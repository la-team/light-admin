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
package org.lightadmin.page.fieldDisplay.listView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.EntityWithUUIDConfiguration;
import org.lightadmin.data.Domain;

import static org.lightadmin.util.DomainAsserts.assertTableRowData;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
//Covers LA-127: https://github.com/la-team/light-admin/issues/127
@RunWithConfiguration( EntityWithUUIDConfiguration.class )
@LoginOnce( domain = Domain.ENTITIES_WITH_UUIDS )
public class EntityWithUUIDTest extends SeleniumIntegrationTest {

    @Test
    public void displayingUUIDs() {
        assertTableRowData( expectedResult2, getStartPage().getDataTable(), 1 );
    }

    private String[] expectedResult2 = { "5276eced-7e41-4933-94a0-77557e7d5b37", "test" };
}
