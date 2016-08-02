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


import com.lowagie.text.DocumentException;

import fi.mystes.synapse.mediator.util.HtmlToPdf;
import fi.mystes.synapse.mediator.util.MediatorUtil;
import fi.mystes.synapse.mediator.util.XslTransformer;

import java.io.IOException;
import java.util.List;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

/**
 * PDF mediator to output XML contents into PDF file.
 * 
 * <pre>
 * &lt;pdf &gt;
 *      &lt;pdfFilePath (value="literal" | expression="xpath")/&gt;
 *      &lt;cssFilePath (value="literal" | expression="xpath")/&gt;
 *      &lt;xslFilePath (value="literal" | expression="xpath")/&gt;
 * &lt;/pdf&gt;
 * </pre>
 */
public class PdfMediator extends AbstractMediator {

    private XslTransformer xslTransformer;
    private HtmlToPdf htmlToPdf;
    private MediatorUtil mediatorUtil;

    // paths are defined in mediator params
    private String pdfFilePath;
    private SynapseXPath pdfFilePathExpression;
    
    private String xslFilePath;
    private SynapseXPath xslFilePathExpression;
    
    private String cssFilePath;
    private SynapseXPath cssFilePathExpression;

    /**
     * Default constructor.
     */
    public PdfMediator() {
        this.xslTransformer = new XslTransformer();
        this.htmlToPdf = new HtmlToPdf();
        this.mediatorUtil = new MediatorUtil();
    }

    /**
     * Constructor for testing to inject mocks.
     * 
     * @param xslTransformer
     * @param htmlToPdf
     * @param mediatorUtil
     */
    public PdfMediator(XslTransformer xslTransformer, HtmlToPdf htmlToPdf, MediatorUtil mediatorUtil) {
        this.xslTransformer = xslTransformer;
        this.htmlToPdf = htmlToPdf;
        this.mediatorUtil = mediatorUtil;
    }
    
    public boolean mediate(MessageContext messageContext) {
		log.info("called PDF mediate.");

		try {
			String xslFile = getXslFile(messageContext);
			String xsl = mediatorUtil.getResource(xslFile, messageContext);

			String cssFile = getCssFile(messageContext);
			String css = mediatorUtil.getResource(cssFile, messageContext);

			String xmlPayload = mediatorUtil.getPayload(messageContext);
			String html = xslTransformer.transform(xmlPayload, xsl);
			String pdfFile = getPdfFile(messageContext);

			htmlToPdf.create(html, css, pdfFile);
		} catch (IOException e) {
			handleException("Failed to create PDF.", e, messageContext);
		} catch (DocumentException  e) {
            handleException("Failed to create PDF.", e, messageContext);
        }

        return true;
    }
    
    /**
     * 
     * @return
     */
    public String getPdfFilePath() {
        return pdfFilePath;
    }
    
    /**
     * 
     * @param pdfFilePath
     */
    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }
    
    /**
     * 
     * @return
     */
    public String getXslFilePath() {
        return xslFilePath;
    }

    /**
     * 
     * @param xslFilePath
     */
    public void setXslFilePath(String xslFilePath) {
        this.xslFilePath = xslFilePath;
    }

    /**
     * 
     * @return
     */
    public String getCssFilePath() {
        return cssFilePath;
    }

    /**
     * 
     * @param cssFilePath
     */
    public void setCssFilePath(String cssFilePath) {
        this.cssFilePath = cssFilePath;
    }

    /**
     * 
     * @return
     */
	public SynapseXPath getPdfFilePathExpression() {
		return pdfFilePathExpression;
	}

	/**
	 * 
	 * @param pdfFilePathExpression
	 */
	public void setPdfFilePathExpression(SynapseXPath pdfFilePathExpression) {
		this.pdfFilePathExpression = pdfFilePathExpression;
	}

	/**
	 * 
	 * @return
	 */
	public SynapseXPath getXslFilePathExpression() {
		return xslFilePathExpression;
	}

	/**
	 * 
	 * @param xslFilePathExpression
	 */
	public void setXslFilePathExpression(SynapseXPath xslFilePathExpression) {
		this.xslFilePathExpression = xslFilePathExpression;
	}

	/**
	 * 
	 * @return
	 */
	public SynapseXPath getCssFilePathExpression() {
		return cssFilePathExpression;
	}

	/**
	 * 
	 * @param cssFilePathExpression
	 */
	public void setCssFilePathExpression(SynapseXPath cssFilePathExpression) {
		this.cssFilePathExpression = cssFilePathExpression;
	}
    
	/**
	 * 
	 * @param messageContext
	 * @return
	 */
	private String getXslFile(MessageContext messageContext) {
		return fetchValue(messageContext, xslFilePath, xslFilePathExpression, "xslFilePath");
	}
	
	/**
	 * 
	 * @param messageContext
	 * @return
	 */
	private String getCssFile(MessageContext messageContext) {
		return fetchValue(messageContext, cssFilePath, cssFilePathExpression, "cssFilePath");
	}
	
	/**
	 * 
	 * @param messageContext
	 * @return
	 */
	private String getPdfFile(MessageContext messageContext) {
		return fetchValue(messageContext, pdfFilePath, pdfFilePathExpression, "pdfFilePath");
	}
	
	/**
	 * Fetches value by evaluating given expression.
	 * 
	 * @param messageContext Context containing payload to evaluate XPath expression to
	 * @param defaultValue Value taken from 'value' attribute
	 * @param expression XPath expression to evaluate
	 * @param fieldName Contains one of the following: pdfFilePath, cssFilePath, xslFilePath
	 * 
	 * @return Fetched value or already set value from 'value' attribute
	 */
	private String fetchValue(MessageContext messageContext, String defaultValue, SynapseXPath expression, String fieldName) {
        if (defaultValue == null) {
        	try {
        		defaultValue = evaluateXPathExpression(expression, messageContext);
				if (defaultValue == null) {
	        		throw new JaxenException("No value found with " + fieldName + "'s expression");
	        	}
			} catch (JaxenException e) {
				handleException("Invalid " + fieldName + " expression", e, messageContext);
			}
        }
		return defaultValue;
	}
	
	/**
	 * Evaluates given XPath expression to given message context.
	 * 
	 * @param expression XPath expression to evaluate
	 * @param messageContext Contains payload which XPath expression will be evaluated to
	 * 
	 * @return Value extracted with expression evaluation
	 * 
	 * @throws JaxenException If expression evaluation fails
	 */
	private String evaluateXPathExpression(SynapseXPath expression, MessageContext messageContext) throws JaxenException {
		Object result = expression.evaluate(messageContext);
		return extractValueFromPayload(expression, messageContext, result);
	}
	
	/**
     * Helper method to extract value from payload within given message context.
     * 
     * @param xpath
     *            Evaluated XPath
     * @param messageContext
     *            Contains payload
     * @param evaluationResult
     *            Already evaluated XPath result
     * @return Extracted value from payload, otherwise null
     */
    private String extractValueFromPayload(SynapseXPath xpath, MessageContext messageContext, Object evaluationResult) {
        if (evaluationResult instanceof String) {
            return (String) evaluationResult;
        } else if (evaluationResult instanceof OMElement) {
            return ((OMElement) evaluationResult).getText();
        } else if (evaluationResult instanceof OMAttribute) {
            return ((OMAttribute) evaluationResult).getAttributeValue();
        } else if (evaluationResult instanceof OMText) {
            return ((OMText) evaluationResult).getText();
        } else if (evaluationResult != null) {
            if (evaluationResult instanceof List) {
                List<?> resultList = (List<?>) evaluationResult;
                if (resultList.size() > 1) {
                    handleException("More than one result found with xpath " + xpath + ", refusing to proceed.",
                            messageContext);
                } else {
                    if (!resultList.isEmpty()) {
                        return extractValueFromPayload(xpath, messageContext, resultList.get(0));
                    } else {
                        getLog(messageContext).traceOrDebug("VfsMediator: Resolving resulted in null");
                        return null;
                    }
                }
            }
            handleException(
                    "Unsupported result type for " + xpath + ": " + evaluationResult.getClass().getCanonicalName(),
                    messageContext);
            return null;
        }
        getLog(messageContext).traceOrDebug("VfsMediator: Resolving resulted in null");
        return null;
    }
}
