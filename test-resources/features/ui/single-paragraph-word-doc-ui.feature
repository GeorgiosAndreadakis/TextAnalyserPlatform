Feature: Document file with single paragraph using the import dialog

  Scenario: A single text passage in a Word document
    Given the started document overview
    When the user selects a file with a text passage and starts the import
    Then after the import the document is available in the system and the overview contains the document