@ui
Feature: Document file with single paragraph using the import dialog

  Scenario: A single text passage in a Word document
    Given the started document overview
    When the user selects the file simple-text-passage.docx and starts the import
    Then after the import the document is available in the system and the overview shows the document