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

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Helper class to clean up given HTML content as string to be well formed.
 *
 */
public class HtmlHelper {

	/**
	 * Performs the clean up on given HTML content to be well formed.
	 * 
	 * @param html String content to perform cleaning
	 * 
	 * @return Cleaned and well formed HTML content
	 * 
	 * @throws IOException If any error occurs while performing cleaning
	 */
    public String clean(final String html) throws IOException {
        // Create a buffer to hold the cleaned up HTML
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Clean up the HTML to be well formed
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        TagNode node = cleaner.clean(html);
        // Instead of writing to System.out we now write to the ByteArray buffer
        new PrettyXmlSerializer(props).writeToStream(node, out);

        final String cleanedHtml = clearDocumentTag(out.toString());

        out.flush();
        out.close();
        return cleanedHtml;
    }

    /**
     * Removes the document tag from given HTML content.
     * 
     * @param content HTML content
     * 
     * @return HTML content without document tag
     */
    private String clearDocumentTag(String content) {
        //remove <!DOCUMENT ...>
        return content.replaceAll("<!.*>", "");
    }
}
