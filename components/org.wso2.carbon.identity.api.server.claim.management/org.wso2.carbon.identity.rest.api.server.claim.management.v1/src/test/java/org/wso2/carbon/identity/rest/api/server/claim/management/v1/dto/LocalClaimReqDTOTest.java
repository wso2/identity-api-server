/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class LocalClaimReqDTOTest {

    private LocalClaimReqDTO testObject;
    @Mock
    private LocalClaimResDTO mockClaimResDTO;
    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {

        closeable = MockitoAnnotations.openMocks(this);
        testObject = new LocalClaimReqDTO();
        setupBasicFields(testObject);
    }

    @AfterMethod
    public void tearDown() throws Exception {

        if (closeable != null) {
            closeable.close();
        }
    }

    private void setupBasicFields(LocalClaimReqDTO dto) {

        dto.setClaimURI("http://example.com/claim");
        dto.setDescription("Test Description");
        dto.setDisplayName("Test Display Name");
        dto.setRegEx(".*");
        dto.setReadOnly(false);
        dto.setRequired(true);
        dto.setSupportedByDefault(true);
        dto.setMultiValued(false);
        dto.setDisplayOrder(1);
    }

    @Test
    public void testEquals_WhenBasicFieldsDiffer_ReturnsFalse() {

        when(mockClaimResDTO.getClaimURI()).thenReturn("http://different.com/claim");
        when(mockClaimResDTO.getDescription()).thenReturn("Test Description");
        when(mockClaimResDTO.getDisplayName()).thenReturn("Test Display Name");
        when(mockClaimResDTO.getRegEx()).thenReturn(".*");
        when(mockClaimResDTO.getReadOnly()).thenReturn(false);
        when(mockClaimResDTO.getRequired()).thenReturn(true);
        when(mockClaimResDTO.getSupportedByDefault()).thenReturn(true);
        when(mockClaimResDTO.getMultiValued()).thenReturn(false);
        when(mockClaimResDTO.getDisplayOrder()).thenReturn(1);

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when basic fields differ");
    }

    @Test
    public void testEquals_WhenDescriptionDiffers_ReturnsFalse() {

        when(mockClaimResDTO.getClaimURI()).thenReturn("http://example.com/claim");
        when(mockClaimResDTO.getDescription()).thenReturn("Different Description");
        when(mockClaimResDTO.getDisplayName()).thenReturn("Test Display Name");
        when(mockClaimResDTO.getRegEx()).thenReturn(".*");
        when(mockClaimResDTO.getReadOnly()).thenReturn(false);
        when(mockClaimResDTO.getRequired()).thenReturn(true);
        when(mockClaimResDTO.getSupportedByDefault()).thenReturn(true);
        when(mockClaimResDTO.getMultiValued()).thenReturn(false);
        when(mockClaimResDTO.getDisplayOrder()).thenReturn(1);

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when description differs");
    }

    @Test
    public void testEquals_WhenReadOnlyDiffers_ReturnsFalse() {

        when(mockClaimResDTO.getClaimURI()).thenReturn("http://example.com/claim");
        when(mockClaimResDTO.getDescription()).thenReturn("Test Description");
        when(mockClaimResDTO.getDisplayName()).thenReturn("Test Display Name");
        when(mockClaimResDTO.getRegEx()).thenReturn(".*");
        when(mockClaimResDTO.getReadOnly()).thenReturn(true);
        when(mockClaimResDTO.getRequired()).thenReturn(true);
        when(mockClaimResDTO.getSupportedByDefault()).thenReturn(true);
        when(mockClaimResDTO.getMultiValued()).thenReturn(false);
        when(mockClaimResDTO.getDisplayOrder()).thenReturn(1);

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when readOnly field differs");
    }

    @Test
    public void testEquals_WhenRequiredDiffers_ReturnsFalse() {

        when(mockClaimResDTO.getClaimURI()).thenReturn("http://example.com/claim");
        when(mockClaimResDTO.getDescription()).thenReturn("Test Description");
        when(mockClaimResDTO.getDisplayName()).thenReturn("Test Display Name");
        when(mockClaimResDTO.getRegEx()).thenReturn(".*");
        when(mockClaimResDTO.getReadOnly()).thenReturn(false);
        when(mockClaimResDTO.getRequired()).thenReturn(false);
        when(mockClaimResDTO.getSupportedByDefault()).thenReturn(true);
        when(mockClaimResDTO.getMultiValued()).thenReturn(false);
        when(mockClaimResDTO.getDisplayOrder()).thenReturn(1);

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when required field differs");
    }

    @Test
    public void testEquals_WhenDisplayOrderDiffers_ReturnsFalse() {

        when(mockClaimResDTO.getClaimURI()).thenReturn("http://example.com/claim");
        when(mockClaimResDTO.getDescription()).thenReturn("Test Description");
        when(mockClaimResDTO.getDisplayName()).thenReturn("Test Display Name");
        when(mockClaimResDTO.getRegEx()).thenReturn(".*");
        when(mockClaimResDTO.getReadOnly()).thenReturn(false);
        when(mockClaimResDTO.getRequired()).thenReturn(true);
        when(mockClaimResDTO.getSupportedByDefault()).thenReturn(true);
        when(mockClaimResDTO.getMultiValued()).thenReturn(false);
        when(mockClaimResDTO.getDisplayOrder()).thenReturn(5);

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when displayOrder differs");
    }

    @Test
    public void testEquals_WhenArraysDiffer_ReturnsFalse() {

        setupMatchingBasicFields(mockClaimResDTO);

        String[] differentSubAttributes = {"attr1", "attr2"};
        testObject.setSubAttributes(new String[]{"attr1"});
        when(mockClaimResDTO.getSubAttributes()).thenReturn(differentSubAttributes);

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when arrays differ");
    }

    @Test
    public void testEquals_WhenCanonicalValuesDiffer_ReturnsFalse() {

        setupMatchingBasicFields(mockClaimResDTO);

        testObject.setSubAttributes(null);
        LabelValueDTO labelValueDTO = new LabelValueDTO();
        labelValueDTO.setValue("value1");
        LabelValueDTO labelValueDTOExpected = new LabelValueDTO();
        labelValueDTO.setValue("value2");
        testObject.setCanonicalValues(new LabelValueDTO[]{labelValueDTO});

        when(mockClaimResDTO.getSubAttributes()).thenReturn(null);
        when(mockClaimResDTO.getCanonicalValues()).thenReturn(new LabelValueDTO[]{labelValueDTOExpected});

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when canonical values differ");
    }

    @Test
    public void testEquals_WhenAttributeMappingsDiffer_ReturnsFalse() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);

        AttributeMappingDTO mapping1 = mock(AttributeMappingDTO.class);
        AttributeMappingDTO mapping2 = mock(AttributeMappingDTO.class);

        when(mapping1.toString()).thenReturn("mapping1");
        when(mapping2.toString()).thenReturn("mapping2");

        testObject.setAttributeMapping(Arrays.asList(mapping1));
        when(mockClaimResDTO.getAttributeMapping()).thenReturn(Arrays.asList(mapping2));

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when attribute mappings differ");
    }

    @Test
    public void testEquals_WhenAttributeMappingSizeDiffers_ReturnsFalse() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);

        AttributeMappingDTO mapping = mock(AttributeMappingDTO.class);
        when(mapping.toString()).thenReturn("mapping1");

        testObject.setAttributeMapping(Arrays.asList(mapping));
        when(mockClaimResDTO.getAttributeMapping()).thenReturn(new ArrayList<>());

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when attribute mapping sizes differ");
    }

    @Test
    public void testEquals_WhenPropertiesDiffer_ReturnsFalse() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);
        setupMatchingAttributeMappings(mockClaimResDTO);

        PropertyDTO property1 = mock(PropertyDTO.class);
        PropertyDTO property2 = mock(PropertyDTO.class);

        when(property1.toString()).thenReturn("property1");
        when(property2.toString()).thenReturn("property2");

        testObject.setProperties(Arrays.asList(property1));
        when(mockClaimResDTO.getProperties()).thenReturn(Arrays.asList(property2));

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when properties differ");
    }

    @Test
    public void testEquals_WhenPropertiesSizeDiffer_ReturnsFalse() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);
        setupMatchingAttributeMappings(mockClaimResDTO);

        PropertyDTO property = mock(PropertyDTO.class);
        when(property.toString()).thenReturn("property1");

        testObject.setProperties(Arrays.asList(property));
        when(mockClaimResDTO.getProperties()).thenReturn(new ArrayList<>());

        boolean result = testObject.equals(mockClaimResDTO);
        assertFalse(result, "Should return false when properties size differs");
    }

    @Test
    public void testEquals_WhenAllFieldsMatch_ReturnsTrue() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);
        setupMatchingAttributeMappings(mockClaimResDTO);
        setupMatchingProperties(mockClaimResDTO);

        boolean result = testObject.equals(mockClaimResDTO);
        assertTrue(result, "Should return true when all fields match");
    }

    @Test
    public void testEquals_WithNullEnums_HandlesCorrectly() {

        testObject.setDataType(null);
        testObject.setUniquenessScope(null);
        testObject.setSharedProfileValueResolvingMethod(null);

        when(mockClaimResDTO.getClaimURI()).thenReturn("http://example.com/claim");
        when(mockClaimResDTO.getDescription()).thenReturn("Test Description");
        when(mockClaimResDTO.getDisplayName()).thenReturn("Test Display Name");
        when(mockClaimResDTO.getRegEx()).thenReturn(".*");
        when(mockClaimResDTO.getDataType()).thenReturn(null);
        when(mockClaimResDTO.getUniquenessScope()).thenReturn(null);
        when(mockClaimResDTO.getSharedProfileValueResolvingMethod()).thenReturn(null);
        when(mockClaimResDTO.getReadOnly()).thenReturn(false);
        when(mockClaimResDTO.getRequired()).thenReturn(true);
        when(mockClaimResDTO.getSupportedByDefault()).thenReturn(true);
        when(mockClaimResDTO.getMultiValued()).thenReturn(false);
        when(mockClaimResDTO.getDisplayOrder()).thenReturn(1);
        when(mockClaimResDTO.getProfiles()).thenReturn(null);
        when(mockClaimResDTO.getInputFormat()).thenReturn(null);

        setupMatchingArrays(mockClaimResDTO);
        setupMatchingAttributeMappings(mockClaimResDTO);
        setupMatchingProperties(mockClaimResDTO);

        boolean result = testObject.equals(mockClaimResDTO);
        assertTrue(result, "Should handle null enums correctly");
    }

    @Test
    public void testEquals_WithNullAttributeMappingLists_HandlesCorrectly() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);

        testObject.setAttributeMapping(null);
        testObject.setProperties(null);

        when(mockClaimResDTO.getAttributeMapping()).thenReturn(null);
        when(mockClaimResDTO.getProperties()).thenReturn(null);

        boolean result = testObject.equals(mockClaimResDTO);
        assertTrue(result, "Should handle null lists correctly");
    }

    @Test
    public void testEquals_WithEmptyLists_HandlesCorrectly() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);

        testObject.setAttributeMapping(new ArrayList<>());
        testObject.setProperties(new ArrayList<>());

        when(mockClaimResDTO.getAttributeMapping()).thenReturn(new ArrayList<>());
        when(mockClaimResDTO.getProperties()).thenReturn(new ArrayList<>());

        boolean result = testObject.equals(mockClaimResDTO);
        assertTrue(result, "Should handle empty lists correctly");
    }

    @Test
    public void testEquals_WithNullItemsInList_HandlesCorrectly() {

        setupMatchingBasicFields(mockClaimResDTO);
        setupMatchingArrays(mockClaimResDTO);

        testObject.setAttributeMapping(null);
        testObject.setProperties(new ArrayList<>());

        when(mockClaimResDTO.getAttributeMapping()).thenReturn(null);
        when(mockClaimResDTO.getProperties()).thenReturn(new ArrayList<>());

        boolean result = testObject.equals(mockClaimResDTO);
        assertTrue(result, "Should handle null items in lists correctly");
    }

    @Test
    public void testEquals_WithNullArrays_HandlesCorrectly() {

        setupMatchingBasicFields(mockClaimResDTO);

        testObject.setSubAttributes(null);
        testObject.setCanonicalValues(null);

        when(mockClaimResDTO.getSubAttributes()).thenReturn(null);
        when(mockClaimResDTO.getCanonicalValues()).thenReturn(null);

        setupMatchingAttributeMappings(mockClaimResDTO);
        setupMatchingProperties(mockClaimResDTO);

        boolean result = testObject.equals(mockClaimResDTO);
        assertTrue(result, "Should handle null arrays correctly");
    }

    // Helper methods
    private void setupMatchingBasicFields(LocalClaimResDTO mock) {

        when(mock.getClaimURI()).thenReturn("http://example.com/claim");
        when(mock.getDescription()).thenReturn("Test Description");
        when(mock.getDisplayName()).thenReturn("Test Display Name");
        when(mock.getRegEx()).thenReturn(".*");
        when(mock.getReadOnly()).thenReturn(false);
        when(mock.getRequired()).thenReturn(true);
        when(mock.getSupportedByDefault()).thenReturn(true);
        when(mock.getMultiValued()).thenReturn(false);
        when(mock.getDisplayOrder()).thenReturn(1);
        when(mock.getDataType()).thenReturn(null);
        when(mock.getUniquenessScope()).thenReturn(null);
        when(mock.getSharedProfileValueResolvingMethod()).thenReturn(null);
        when(mock.getProfiles()).thenReturn(null);
        when(mock.getInputFormat()).thenReturn(null);
    }

    private void setupMatchingArrays(LocalClaimResDTO mock) {

        testObject.setSubAttributes(null);
        testObject.setCanonicalValues(null);
        when(mock.getSubAttributes()).thenReturn(null);
        when(mock.getCanonicalValues()).thenReturn(null);
    }

    private void setupMatchingAttributeMappings(LocalClaimResDTO mock) {

        testObject.setAttributeMapping(new ArrayList<>());
        when(mock.getAttributeMapping()).thenReturn(new ArrayList<>());
    }

    private void setupMatchingProperties(LocalClaimResDTO mock) {

        testObject.setProperties(new ArrayList<>());
        when(mock.getProperties()).thenReturn(new ArrayList<>());
    }
}
