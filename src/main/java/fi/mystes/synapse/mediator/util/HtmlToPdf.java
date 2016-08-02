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


import com.lowagie.text.DocumentException;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Helper class to export given HTML content into desired PDF file.
 *
 */
public class HtmlToPdf {

	/**
	 * Creates PDF file with given HTML content.
	 * 
	 * @param html Content to export to PDF file
	 * @param css Rules to style HTML content
	 * @param pdfFile File name with path to export HTML content to
	 * 
	 * @throws IOException If HTML content clean up or PDF file creation fails
	 * @throws DocumentException If writing contents to PDF file fails
	 */
    public void create(String html, final String css, final String pdfFile) throws IOException, DocumentException {
        String cleanedHtmlContent = new HtmlHelper().clean(html);

        ITextRenderer renderer = new ITextRenderer();

        String content = injectCssToHtml(css, cleanedHtmlContent);

        renderer.setDocumentFromString(content);
        renderer.layout();
        OutputStream outputStream = new FileOutputStream(pdfFile);
        renderer.createPDF(outputStream);

        // Finishing up
        renderer.finishPDF();
    }

    /**
     * Replaces 'link' tags including css-file references with 'style' tag and given CSS content.
     * 
     * @param css Style sheet content to be injected to HTML content
     * @param cleanedHtmlContent HTML content to inject CSS content to
     * 
     * @return Modified HTML content
     */
    private String injectCssToHtml(String css, String cleanedHtmlContent) {
        return cleanedHtmlContent.replaceAll("<link.*.css.*>", String.format("%s%s%s", "<style>", css, "</style>"));
    }


}

