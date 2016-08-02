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
package fi.mystes.synapse.mediator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.OMAttributeImpl;
import org.apache.axiom.om.impl.llom.factory.OMLinkedListMetaFactory;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.impl.llom.soap11.SOAP11BodyImpl;
import org.apache.axiom.soap.impl.llom.soap11.SOAP11Factory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;
import org.junit.Before;
import org.junit.Test;

import com.lowagie.text.DocumentException;

import fi.mystes.synapse.mediator.util.HtmlToPdf;
import fi.mystes.synapse.mediator.util.MediatorUtil;
import fi.mystes.synapse.mediator.util.XslTransformer;

public class PdfMediatorTest {

	public static final String CSS_PATH = "cssPath";
	public static final String PDF_FILE = "pdfPath/file.pdf";
	public static final String XSL_PATH = "xslPath";
	private PdfMediator mediator;

	private MessageContext mc = mock(MessageContext.class);
	private SOAPEnvelope envelope;
	private SOAPBody body;

	private SOAPFactory soapFactory;
	private OMElement rootElement;
	private OMLinkedListMetaFactory omFactory = new OMLinkedListMetaFactory();

	private XslTransformer xslTransformer = mock(XslTransformer.class);
	private HtmlToPdf htmlToPdf =  mock(HtmlToPdf.class);
	private MediatorUtil mediatorUtil = mock(MediatorUtil.class);

	private String css = "css";
	private String html = "html";
	private String xsl = "xsl";


	@Before
	public void setUp() throws IOException, JaxenException {
		//instantiate mediator with mock objects
		mediator = new PdfMediator(xslTransformer, htmlToPdf, mediatorUtil);
		mediator.setCssFilePath(CSS_PATH);
		mediator.setXslFilePath(XSL_PATH);
		soapFactory = new SOAP11Factory(omFactory);
		envelope = soapFactory.createSOAPEnvelope();
		body = new SOAP11BodyImpl(envelope, soapFactory);
		rootElement = soapFactory.createOMElement("foo", null);
		body.addChild(rootElement);
		soapFactory.createOMElement(new QName("pdfFilePath"), rootElement).setText(PDF_FILE);
		rootElement.getFirstElement().addAttribute(new OMAttributeImpl("pathToFile", null, PDF_FILE, soapFactory));
		prepareMockOperations();
	}

	@Test
	public void shouldCreatePdfFetchingPdfFilePathFromElement() throws IOException, DocumentException, JaxenException {
		mediator.setPdfFilePathExpression(new SynapseXPath("//pdfFilePath"));
		verifyAssertions();
	}
	
	@Test
	public void shouldCreatePdfFetchingPdfFilePathFromElementTextNode() throws IOException, DocumentException, JaxenException {
		mediator.setPdfFilePathExpression(new SynapseXPath("//pdfFilePath/text()"));
		verifyAssertions();
	}
	
	@Test
	public void shouldCreatePdfFetchingPdfFilePathFromElementAttribute() throws IOException, DocumentException, JaxenException {
		mediator.setPdfFilePathExpression(new SynapseXPath("//pdfFilePath/@pathToFile"));
		verifyAssertions();
	}

	@Test
	public void shouldCreatePdfFetchingPdfFilePathFromPropertyUsingContextOM() throws IOException, DocumentException, JaxenException {
		mediator.setPdfFilePathExpression(new SynapseXPath("$ctx:pdfFileProperty"));
		verifyAssertions();
	}

	@Test
	public void shouldCreatePdfFetchingPdfFilePathFromPropertyUsingGetPropertyFunction() throws IOException, DocumentException, JaxenException {
		mediator.setPdfFilePathExpression(new SynapseXPath("get-property('pdfFileProperty')"));
		verifyAssertions();
	}
	
	private void prepareMockOperations () {
		when(mediatorUtil.getPayload(mc)).thenReturn(rootElement.toString());
		when(mediatorUtil.getResource(XSL_PATH, mc)).thenReturn(xsl);
		when(mediatorUtil.getResource(CSS_PATH, mc)).thenReturn(css);
		when(xslTransformer.transform(rootElement.toString(), xsl)).thenReturn(html);
		when(mc.getEnvelope()).thenReturn(envelope);
		when(mc.getProperty("pdfFileProperty")).thenReturn(PDF_FILE);
	}

	private void verifyAssertions() throws IOException, DocumentException {
		assertTrue(mediator.mediate(mc));

		verify(mediatorUtil).getResource(XSL_PATH, mc);
		verify(mediatorUtil).getResource(CSS_PATH, mc);
		verify(xslTransformer).transform(rootElement.toString(), xsl);
		verify(htmlToPdf).create(html, css, PDF_FILE);
	}

}
