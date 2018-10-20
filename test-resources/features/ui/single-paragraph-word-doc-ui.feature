Feature: Document file with single paragraph using the import dialog

  Scenario: A single text passage in a Word document
    Given a started import dialog
    When the user selects a file containing a single text passage and starts the import
    Then the file will be imported, the text will be available in the system and the ui shows the single passage