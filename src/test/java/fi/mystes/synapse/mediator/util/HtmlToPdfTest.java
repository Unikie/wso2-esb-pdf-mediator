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

import static fi.mystes.synapse.mediator.util.TestUtil.getResuorceAsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;

public class HtmlToPdfTest {

    String html;
    String css;
    String pdfFileLocation;
    String fileName;
    String createdFilePath;
    File file;

    @Before
    public void setUp() throws IOException {

        html = getResuorceAsString("example.html");
        css = getResuorceAsString("example.css");
        pdfFileLocation = System.getProperty("java.io.tmpdir");
        fileName = "example.pdf";
        createdFilePath = pdfFileLocation +"/" + fileName;

        file = new File(createdFilePath);
    }

    @Test
    public void test() throws IOException, DocumentException {
        assertFalse("file doesn't exists", Files.exists(file.toPath()));
        new HtmlToPdf().create(html, css, createdFilePath);
        assertTrue("file exists", file.exists());
        
        PdfReader pdfReader = new PdfReader(createdFilePath);
        String content = new String(pdfReader.getPageContent(1));
        
        assertTrue("Header 1 should exist in PDF file", content.contains("Header 1"));
        assertTrue("Header 2 should exist in PDF file", content.contains("Header 2"));
        assertTrue("Header 3 should exist in PDF file", content.contains("Header 3"));
        
        assertTrue("Row 1 should exist in PDF file", content.contains("Row 1"));
        assertTrue("Row 2 should exist in PDF file", content.contains("Row 2"));
        assertTrue("Row 3 should exist in PDF file", content.contains("Row 3"));
        assertTrue("Row 4 should exist in PDF file", content.contains("Row 4"));
        
        assertTrue("Column 1 should exist in PDF file", content.contains("Column 1"));
        assertTrue("Column 2 should exist in PDF file", content.contains("Column 2"));
        assertTrue("Column 3 should exist in PDF file", content.contains("Column 3"));
        
        assertTrue("Example page should exist in PDF file", content.contains("Example page"));
    }

    @After
    public void tearDown() throws IOException {
        assertTrue("delete test file", Files.deleteIfExists(new File(createdFilePath).toPath()));
    }
}
