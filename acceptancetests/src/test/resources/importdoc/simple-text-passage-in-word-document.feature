Feature: Import document file

  Scenario: A single text passage in a Word document
    Given a word file which contains a single text passage
    When the user starts the import for the given file
    Then the file will be imported and the text is in the system available