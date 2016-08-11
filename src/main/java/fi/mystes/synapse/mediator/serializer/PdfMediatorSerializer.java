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
package fi.mystes.synapse.mediator.serializer;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorSerializer;
import org.apache.synapse.config.xml.SynapseXPathSerializer;
import org.kohsuke.MetaInfServices;

import fi.mystes.synapse.mediator.PdfMediator;


/**
 * Mediator serializer class to transform mediator instance to OMElement
 * instance.
 * 
 * <pre>
 * &lt;pdf &gt;
 *      &lt;pdfFilePath (value="literal" | expression="xpath")/&gt;
 *      &lt;cssFilePath (value="literal" | expression="xpath")/&gt;
 *      &lt;xslFilePath (value="literal" | expression="xpath")/&gt;
 * &lt;/pdf&gt;
 * </pre>
 */
@MetaInfServices(org.apache.synapse.config.xml.MediatorSerializer.class)
public class PdfMediatorSerializer extends AbstractMediatorSerializer {

    /**
     * Get Pdf Mediator class name
     */
    @Override
    public String getMediatorClassName() {
        return PdfMediator.class.getName();
    }

    /**
     * Performs the mediator serialization by transforming mediator instance into
     * OMElement instance.
     */
    @Override
    public OMElement serializeSpecificMediator(Mediator m) {

        if (!(m instanceof PdfMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        PdfMediator mediator = (PdfMediator) m;
        OMElement pdf = fac.createOMElement("pdf", synNS);
        saveTracingState(pdf, mediator);
        
        setPdfFilePath(mediator, pdf);
        
        setCssFilePath(mediator, pdf);

        setXslFilePath(mediator, pdf);
        
        return pdf;
    }
    
    /**
     * Helper method to set PDF file path on given OMElement pdf.
     * 
     * @param mediator Contains information about PDF file path
     * @param pdf OMElement to set PDF file path to
     */
    private void setPdfFilePath(PdfMediator mediator, OMElement pdf) {
    	OMElement pdfFilePath = fac.createOMElement("pdfFilePath", synNS, pdf);
    	if (mediator.getPdfFilePathExpression() != null) {
    		SynapseXPathSerializer.serializeXPath(mediator.getPdfFilePathExpression(), pdfFilePath, "expression");
    	} else {
    		pdfFilePath.addAttribute(fac.createOMAttribute("value", nullNS, mediator.getPdfFilePath()));
    	}
    }
    
    /**
     * Helper method to set CSS file path on given OMElement pdf.
     * 
     * @param mediator Contains information about CSS file path
     * @param pdf OMElement to set PDF file path to
     */
    private void setCssFilePath(PdfMediator mediator, OMElement pdf) {
    	OMElement cssFilePath = fac.createOMElement("cssFilePath", synNS, pdf);
    	if (mediator.getCssFilePathExpression() != null) {
            SynapseXPathSerializer.serializeXPath(mediator.getCssFilePathExpression(), cssFilePath, "expression");
    	} else {
    		cssFilePath.addAttribute(fac.createOMAttribute("value", nullNS, mediator.getCssFilePath()));
    	}
    }
    
    /**
     * Helper method to set XSL file path on given OMElement pdf.
     * 
     * @param mediator Contains information about XSL file path
     * @param pdf OMElement to set XSL file path to
     */
    private void setXslFilePath(PdfMediator mediator, OMElement pdf) {
    	OMElement xslFilePath = fac.createOMElement("xslFilePath", synNS, pdf);
    	if (mediator.getXslFilePathExpression() != null) {
    		SynapseXPathSerializer.serializeXPath(mediator.getXslFilePathExpression(), xslFilePath, "expression");
    	} else {
    		xslFilePath.addAttribute(fac.createOMAttribute("value", nullNS, mediator.getXslFilePath()));
    	}
    }
}
