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

import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorFactory;
import org.apache.synapse.config.xml.SynapseXPathFactory;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.jaxen.JaxenException;
import org.kohsuke.MetaInfServices;

import fi.mystes.synapse.mediator.PdfMediator;

/**
 * Factory for {@link PdfMediator} instances.
 * 
 * <pre>
 * &lt;pdf &gt;
 *      &lt;pdfFilePath (value="literal" | expression="xpath")/&gt;
 *      &lt;cssFilePath (value="literal" | expression="xpath")/&gt;
 *      &lt;xslFilePath (value="literal" | expression="xpath")/&gt;
 * &lt;/pdf&gt;
 * </pre>
 */
@MetaInfServices(org.apache.synapse.config.xml.MediatorFactory.class)
public class PdfMediatorFactory extends AbstractMediatorFactory {

	public static final QName TAG_NAME = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "pdf");

	public static final QName Q_PDF_FILE_PATH = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "pdfFilePath");
	public static final QName Q_CSS_FILE_PATH = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "cssFilePath");
	public static final QName Q_XSL_FILE_PATH = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "xslFilePath");

	/**
	 * The QName of PDF mediator element in the XML config
	 * 
	 * @return QName of wrapper mediator
	 */
	@Override
	public QName getTagQName() {
		return TAG_NAME;
	}

	/**
	 * Specific mediator factory implementation to build the
	 * org.apache.synapse.Mediator by the given XML configuration
	 * 
	 * @param OMElement
	 *            element configuration element describing the properties of the
	 *            mediator
	 * @param properties
	 *            bag of properties to pass in any information to the factory
	 * 
	 * @return built wrapper mediator
	 */
	@Override
	public Mediator createSpecificMediator(OMElement elem, Properties properties) {

		PdfMediator pdf = new PdfMediator();

		setPdfFilePath(elem, pdf);

		setCssFilePath(elem, pdf);

		setXslFilePath(elem, pdf);

		return pdf;
	}

	/**
	 * Helper method to set PDF file path to PDF mediator.
	 * 
	 * @param pdfElement Contains necessary element for PDF file path setting
	 * @param pdf Mediator to set PDF file path to
	 */
	private void setPdfFilePath(OMElement pdfElement, PdfMediator pdf){
		OMElement pdfFilePathElement = pdfElement.getFirstChildWithName(Q_PDF_FILE_PATH);
		if (pdfFilePathElement == null) {
			handleException("pdfFilePath element is required");
		}

		OMAttribute expressionAttribute = pdfFilePathElement.getAttribute(ATT_EXPRN);
		if (expressionAttribute == null) {
			OMAttribute valueAttribute = pdfFilePathElement.getAttribute(ATT_VALUE);
			if (valueAttribute == null) {
				handleException("Either expression or value attribute is required for pdfFilePath");
			}
			pdf.setPdfFilePath(valueAttribute.getAttributeValue());
		} else {
			try {
				pdf.setPdfFilePathExpression(SynapseXPathFactory.getSynapseXPath(pdfFilePathElement, ATT_EXPRN));
			} catch (JaxenException e) {
				handleException("Invalid pdfFilePath expression : " + pdfFilePathElement.getAttributeValue(ATT_EXPRN));
			}
		}
	}

	/**
	 * Helper method to set CSS file path to PDF mediator.
	 * 
	 * @param pdfElement Contains necessary element for CSS file path setting
	 * @param pdf Mediator to set CSS file path to
	 */
	private void setCssFilePath(OMElement pdfElement, PdfMediator pdf){
		OMElement cssFilePathElement = pdfElement.getFirstChildWithName(Q_CSS_FILE_PATH);
		if (cssFilePathElement == null) {
			handleException("cssFilePath element is required");
		}

		OMAttribute expressionAttribute = cssFilePathElement.getAttribute(ATT_EXPRN);
		if (expressionAttribute == null) {
			OMAttribute valueAttribute = cssFilePathElement.getAttribute(ATT_VALUE);
			if (valueAttribute == null) {
				handleException("Either expression or value attribute is required for cssFilePath");
			}
			pdf.setCssFilePath(valueAttribute.getAttributeValue());
		} else {
			try {
				pdf.setCssFilePathExpression(SynapseXPathFactory.getSynapseXPath(cssFilePathElement, ATT_EXPRN));
			} catch (JaxenException e) {
				handleException("Invalid cssFilePath expression : " + cssFilePathElement.getAttributeValue(ATT_EXPRN));
			}
		}
	}

	/**
	 * Helper method to set XLS file path to PDF mediator.
	 * 
	 * @param pdfElement Contains necessary element for XSL file path setting
	 * @param pdf Mediator to set XSL file path to
	 */
	private void setXslFilePath(OMElement pdfElement, PdfMediator pdf){
		OMElement xslFilePathElement = pdfElement.getFirstChildWithName(Q_XSL_FILE_PATH);
		if (xslFilePathElement == null) {
			handleException("xslFilePath element is required");
		}

		OMAttribute expressionAttribute = xslFilePathElement.getAttribute(ATT_EXPRN);
		if (expressionAttribute == null) {
			OMAttribute valueAttribute = xslFilePathElement.getAttribute(ATT_VALUE);
			if (valueAttribute == null) {
				handleException("Either expression or value attribute is required for xslFilePath");
			}
			pdf.setXslFilePath(valueAttribute.getAttributeValue());
		} else {
			try {
				pdf.setXslFilePathExpression(SynapseXPathFactory.getSynapseXPath(xslFilePathElement, ATT_EXPRN));
			} catch (JaxenException e) {
				handleException("Invalid xslFilePathElement expression : " + xslFilePathElement.getAttributeValue(ATT_EXPRN));
			}
		}
	}

}
