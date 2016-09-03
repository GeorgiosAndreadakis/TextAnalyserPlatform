/*
 * MIT License
 *
 * Copyright (c) 2016 Georgios Andreadakis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.tap.accepttest.importdoc;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.tap.accepttest.importdoc.util.ClasspathUtil;
import org.tap.accepttest.importdoc.util.FileHandlingUtil;
import org.tap.application.importdoc.ApplicationContext;
import org.tap.application.importdoc.DocImporter;
import org.tap.application.importdoc.DocImporterImpl;
import org.tap.domain.Document;
import org.tap.domain.DocumentRepository;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Cucumber step class for acceptance tests regardingthe import of documents with a single text passage.
 *
 * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
 */
public class SimpleTextPassageInWordFileSteps {

    private Path docFile;
    private DocumentRepository documentRepository = mock(DocumentRepository.class);

    @Before
    public void beforeScenario() {
        System.out.println("*** Running tests in folder: " + (new File("")).getAbsolutePath());
        System.out.println("Default File System: " + FileSystems.getDefault());
        ClasspathUtil.showClasspath();

    }

    @Given("^a word file which contains a single text passage$")
    public void given_a_word_file_with_a_single_text_passage() throws Exception {
        docFile = FileHandlingUtil.readFromClasspath("importdoc/simple-text-passage.docx");
    }

    @When("^the user starts the import for the given file$")
    public void when_the_user_starts_the_import_for_the_given_file() {
        ApplicationContext context = new ApplicationContext(documentRepository);
        DocImporter docImporter = new DocImporterImpl(context);
        docImporter.importFile(docFile);
    }

    @Then("^the file will be imported and the text is in the system available$")
    public void then_the_file_will_be_imported_and_the_text_is_in_the_system_available() throws Throwable {
        verify(documentRepository, times(1)).save(any(Document.class));
    }

}
