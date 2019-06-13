@api
Feature: Import Word documents

  Scenario: A single text passage in a Word document
    Given the text document simple-text-passage.docx
    When the user starts the import for the given file
    Then there is a text passage in the system with substring 'The central concept of a document store'

  Scenario: Two text passages in a Word document
    Given the text document p_p.docx
    When the user starts the import for the given file
    Then there is a text passage in the system with substring 'extracting knowledge from a document can be hard work'
    And there is a text passage in the system with substring 'share the results of the analytical process'

  @section
  Scenario: A section in a Word document
    Given the text document h1_p.docx
    When the user starts the import for the given file
    Then there is a section in the system with title 'About the Text Analyzer Platform'

  @section
  Scenario: A section in a Word document
    Given the text document h1_p_h2_p.docx
    When the user starts the import for the given file
    Then there is a section in the system with title 'A Meaningless Second Level Heading'

  @section
  Scenario: A section in a Word document
    Given the text document h1_p_h1_p_h2_p_h3_p_h1_p.docx
    When the user starts the import for the given file
    Then there is a section in the system with title 'Third-Order Section of Second-Order Section of 2nd First-Order Section'
    Then there is a section in the system with title '3rd First-Order Section'
