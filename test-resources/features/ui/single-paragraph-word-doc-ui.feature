Feature: Document file with single paragraph using the import dialog

  Scenario: A single text passage in a Word document
    Given a started import dialog
    When the user selects a file with a text passage and starts the import
    Then after the import the file will be available in the system and the ui shows the document