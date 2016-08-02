/**
 * Copyright 2016 Mystes Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.mystes.synapse.mediator.util;

import static fi.mystes.synapse.mediator.util.TestUtil.getResuorceAsString;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class XslTransformerTest {

    XslTransformer transformer;
    String xml;
    String xsl;

    @Before
    public void setUp() throws Exception {
        transformer = new XslTransformer();
        xml = getResuorceAsString("example.xml");
        xsl = getResuorceAsString("example.xsl");
    }

    @Test
    public void shouldTransformGivenXmlUsingGivenXsl() throws Exception {
        final String result = transformer.transform(xml, xsl);
        assertTrue("creates transformed result.", result.contains("<html>"));
    }
}