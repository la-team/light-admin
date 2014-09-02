package org.lightadmin.core.config.domain.field;

import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertFalse;

public class FieldMetadataComparatorTest {

    @Test
    public void testCompare() {
        Set<FieldMetadata> fields = new TreeSet<FieldMetadata>(new FieldMetadataUtils.FieldMetadataComparator());
        fields.add(new CustomFieldMetadata("custom", null));
        fields.add(new PersistentFieldMetadata("persistent", "persistent"));
        fields.add(new PersistentFieldMetadata("persistent-key", "persistent-key", true));
        fields.add(new CustomFieldMetadata("custom-2", null));
        fields.add(new PersistentFieldMetadata("persistent-2", "persistent-2"));
        fields.add(new TransientFieldMetadata("transient", "transient"));

        assertFalse(fields.isEmpty());
    }
}