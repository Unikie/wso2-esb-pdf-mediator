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
package fi.mystes.synapse.mediator.factory;

import static org.junit.Assert.assertTrue;

import fi.mystes.synapse.mediator.factory.PdfMediatorFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.junit.Before;
import org.junit.Test;

import fi.mystes.synapse.mediator.PdfMediator;

public class PdfMediatorFactoryTest {

	private PdfMediatorFactory factory;
	private OMElement mediatorElement;
	private final OMFactory omFactory = OMAbstractFactory.getOMFactory();

	@Before
	public void setUp() {
		factory = new PdfMediatorFactory();
		mediatorElement = new OMElementImpl(factory.getTagQName(), null, omFactory);
	}

	@Test
	public void shouldFailPdfMediatorInitiationDueToMissingRequiredPdfFilePathElement() {
		try {
			factory.createSpecificMediator(mediatorElement, null);
		} catch (Exception e) {
			assertTrue("Should the following exception occur: pdfFilePath element is required",
					e.getMessage().equals("pdfFilePath element is required"));
		}
	}

	@Test
	public void shouldFailPdfMediatorInitiationDueToMissingRequiredCssFilePathElement() {
		try {
			omFactory.createOMElement(PdfMediatorFactory.Q_PDF_FILE_PATH, mediatorElement)
						.addAttribute("value", "/tmp/pdfFile.pdf", null);
			factory.createSpecificMediator(mediatorElement, null);
		} catch (Exception e) {
			assertTrue("Should the following exception occur: cssFilePath element is required",
					e.getMessage().equals("cssFilePath element is required"));
		}
	}

	@Test
	public void shouldFailPdfMediatorInitiationDueToMissingRequiredXslFilePathElement() {
		try {
			omFactory.createOMElement(PdfMediatorFactory.Q_PDF_FILE_PATH, mediatorElement)
						.addAttribute("value", "/tmp/pdfFile.pdf", null);
			omFactory.createOMElement(PdfMediatorFactory.Q_CSS_FILE_PATH, mediatorElement)
						.addAttribute("value", "/tmp/cssFile.css", null);
			factory.createSpecificMediator(mediatorElement, null);
		} catch (Exception e) {
			assertTrue("Should the following exception occur: xslFilePath element is required",
					e.getMessage().equals("xslFilePath element is required"));
		}
	}

	@Test
	public void shouldInitiatePdfMediatorWithAllChildren() {
		String pdfFilePath =  "/tmp/pdfFile.pdf";
		omFactory.createOMElement(PdfMediatorFactory.Q_PDF_FILE_PATH, mediatorElement)
					.addAttribute("value",pdfFilePath, null);
		String cssFilePath = "/tmp/cssFile.css";
		omFactory.createOMElement(PdfMediatorFactory.Q_CSS_FILE_PATH, mediatorElement)
					.addAttribute("value", cssFilePath, null);
		String xslFilePath = "/tmp/xslFile.xsl";
		omFactory.createOMElement(PdfMediatorFactory.Q_XSL_FILE_PATH, mediatorElement)
					.addAttribute("value", xslFilePath, null);
		
		PdfMediator pdfMediator = (PdfMediator)factory.createSpecificMediator(mediatorElement, null);
		assertTrue("pdfFilePath should be set", pdfMediator.getPdfFilePath().equals(pdfFilePath));
		assertTrue("cssFilePath should be set", pdfMediator.getCssFilePath().equals(cssFilePath));
		assertTrue("xslFilePath should be set", pdfMediator.getXslFilePath().equals(xslFilePath));
	}
	
}
