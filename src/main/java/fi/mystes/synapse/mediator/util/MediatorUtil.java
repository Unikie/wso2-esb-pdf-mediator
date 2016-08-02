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

import org.apache.axiom.om.OMText;
import org.apache.axiom.soap.SOAPBody;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.Entry;
import org.apache.synapse.registry.Registry;

import java.util.Properties;

/**
 * Helper class providing simple accessing methods for fetching data from Message Context.
 * 
 */
public class MediatorUtil {

	/**
	 * Reads payload as string from given Message Context
	 * 
	 * @param messageContext Contains XML payload
	 * 
	 * @return String payload
	 */
    public String getPayload(MessageContext messageContext) {
        return messageContext.getEnvelope().getBody().getFirstElement().toString();
    }

    /**
     * Reads SOAP Body from given Message Context.
     * 
     * @param messageContext Contains SOAP Body
     * 
     * @return SOAPBody object read from given Message Context
     */
    public SOAPBody getBody(MessageContext messageContext) {
        return messageContext.getEnvelope().getBody();
    }

    /**
     * Reads property value from Message Context.
     * 
     * @param key Property name
     * @param messageContext To read property value from
     * 
     * @return Value of property
     */
    public String getProperty(String key, MessageContext messageContext) {
        return messageContext.getProperty(key).toString();
    }

    /**
     * Reads resource from Synapse registry.
     * 
     * @param path Path to the registry
     * @param messageContext Contains access to registry
     * 
     * @return Content of the resource
     */
    public String getResource(String path, MessageContext messageContext) {
        Entry entry = createEntry(path);
        final Object o = resourceFromRegistry(entry, messageContext);
        if (o instanceof OMText) {
            return ((OMText)o).getText();
        }
        //OMElement
        return o.toString();
    }

    /**
     * Gets resource from Synapse registry.
     * 
     * @param entry Resource entry to get from registry
     * @param messageContext Contains access to registry
     * 
     * @return Matched resource
     */
    private Object resourceFromRegistry(Entry entry, MessageContext messageContext) {
        return getRegistry(messageContext).getResource(entry, new Properties());
    }

    /**
     * Creates registry entry with given path.
     * 
     * @param path Access entry to particular resource
     * 
     * @return Entry for the registry resource
     */
    private Entry createEntry(String path) {
        Entry entry = new Entry();
        entry.setType(Entry.REMOTE_ENTRY);
        entry.setKey(path);
        return entry;
    }

    /**
     * Gets registry from Synapse configuration.
     * 
     * @param messageContext Contains access to Synapse configuration
     * 
     * @return Synapse registry
     */
    private Registry getRegistry(MessageContext messageContext) {
        return messageContext.getEnvironment().getSynapseConfiguration().getRegistry();
    }

}
