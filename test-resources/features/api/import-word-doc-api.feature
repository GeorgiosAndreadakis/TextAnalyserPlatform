Feature: Import Word documents

  Scenario: A single text passage in a Word document
    Given a word file which contains a single text passage
    When the user starts the import for the given file
    Then the file will be imported and the text is in the system available


  Scenario: Two text passages in a Word document
    Given the text document p_p.docx
    When the user starts the import for the given file
    Then there is a text passage in the system with substring 'extracting knowledge from a document can be hard work'
    And there is a text passage in the system with substring 'share the results of the analytical process'