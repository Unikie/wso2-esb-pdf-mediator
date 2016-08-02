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


import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Helper class to transform XML content with desired XSL style sheet.
 *
 */
public class XslTransformer {
	private final Log log = LogFactory.getLog(XslTransformer.class);

	/**
	 * Performs XML transformation with given XSL.
	 * 
	 * @param xml Content to transform
	 * @param xsl Transformation style sheet
	 * 
	 * @return Transformed content
	 */
    public String transform(String xml, String xsl) {
    	log.info("Starting XML content transformation");
        try {
            DOMSource source = createDomSource(xml);

            Transformer xslTransformer = createTransformerFor(xsl);

            final OutputStream outputStream = new ByteArrayOutputStream();
            Result result = new StreamResult(outputStream);

            xslTransformer.transform(source, result);

            return outputStream.toString();

        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    /**
     * Created DOMSource object from given XML string content.
     * 
     * @param xml Content to transform to DOMSoure object
     * 
     * @return DOMSource object containing given XML content
     * 
     * @throws ParserConfigurationException If document builder initialization fails
     * @throws SAXException If given XML content parsing fails
     * @throws IOException If given XML content parsing fails
     */
    private DOMSource createDomSource(final String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = createDocumentBuilder();
        Document document = builder.parse(IOUtils.toInputStream(xml));
        return new DOMSource(document);
    }

    /**
     * Creates document builder for XML content parsing.
     * 
     * @return Initiated document builder
     * 
     * @throws ParserConfigurationException If document builder initialization fails
     */
    private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }

    /**
     * Creates XSL transformer.
     * 
     * @param xsl XSL style sheet to initialize transformer with
     * 
     * @return Initiated XSL transformer
     * 
     * @throws TransformerConfigurationException If transformer initialization fails
     */
    private Transformer createTransformerFor(String xsl) throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        return transformerFactory.newTransformer(new StreamSource(IOUtils.toInputStream(xsl)));
    }

}

