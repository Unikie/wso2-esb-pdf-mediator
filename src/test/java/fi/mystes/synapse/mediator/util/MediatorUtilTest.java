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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.registry.Registry;
import org.junit.Before;
import org.junit.Test;
import org.apache.synapse.config.Entry;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MediatorUtilTest {

    public static final String PAYLOAD = "<payload/>";

    public static final String PROPERTY_VALUE = "property";
    public static final String KEY = "key";
    public static final String RESOURCE_PATH = "resource path";
    public static final String RESOURCE_VALUE = "resource value";

    MediatorUtil mu = new MediatorUtil();

    private MessageContext mc = mock(MessageContext.class);
    private SOAPEnvelope envelope = mock(SOAPEnvelope.class);
    private SOAPBody body = mock(SOAPBody.class);
    private OMElement firstElement = mock(OMElement.class);
    private SynapseEnvironment synapseEnvironment = mock(SynapseEnvironment.class);
    private SynapseConfiguration synapseConfiguration = mock(SynapseConfiguration.class);
    private Registry registry = mock(Registry.class);


    @Before
    public void setUp() throws IOException {
        when(mc.getEnvelope()).thenReturn(envelope);

        when(envelope.getBody()).thenReturn(body);
        when(body.getFirstElement()).thenReturn(firstElement);
        when(firstElement.toString()).thenReturn(PAYLOAD);

        when(mc.getEnvironment()).thenReturn(synapseEnvironment);
        when(synapseEnvironment.getSynapseConfiguration()).thenReturn(synapseConfiguration);
        when(synapseConfiguration.getRegistry()).thenReturn(registry);

    }

    @Test
    public void shouldGetPayload() {
        assertEquals("payload is returned", PAYLOAD, mu.getPayload(mc));
        verify(mc).getEnvelope();
        verify(envelope).getBody();
        verify(body).getFirstElement();
    }

    @Test
    public void shouldGetProperty() {
        when(mc.getProperty(KEY)).thenReturn(PROPERTY_VALUE);
        assertEquals("property value is returned", PROPERTY_VALUE, mu.getProperty(KEY,mc));
        verify(mc).getProperty(KEY);
    }

    @Test
    public void shouldGetResource() {
        when(registry.getResource(any(Entry.class),any(Properties.class))).thenReturn(RESOURCE_VALUE);
        assertEquals("resource is returned", RESOURCE_VALUE, mu.getResource(RESOURCE_PATH, mc));
        verify(mc).getEnvironment();
        verify(synapseEnvironment).getSynapseConfiguration();
        verify(synapseConfiguration).getRegistry();

    }
}
