package com.tutuka.lib.logger.util;

/**
 * {@link Enum} for exception constants
 */
public enum LogExeptionCodeConstant {
    /**
     *
     */
    CLASSNAMENOTFOUNDEXEPTION("KER-LOG-001"),
    /**
     *
     */
    EMPTYPATTERNEXCEPTION("KER-LOG-004"),
    /**
     *
     */
    FILENAMENOTPROVIDED("KER-LOG-006"),
    /**
     *
     */
    ILLEGALARGUMENTEXCEPTION("KER-LOG-007"),
    /**
     *
     */
    ILLEGALFILEACCESS("KER-LOG-008"),
    /**
     *
     */
    CONFIGURATIONXMLPARSE("KER-LOG-009"),
    /**
     *
     */
    ILLEGALSTATEEXCEPTION("KER-LOG-003"),
    /**
     *
     */
    IMPLEMENTATIONNOTFOUND("KER-LOG-002"),
    /**
     *
     */
    PATTERNSYNTAXEXCEPTION("KER-LOG-005"),
    /**
     *
     */
    CLASSNAMENOTFOUNDEXEPTIONMESSAGE("Class name is empty"),
    /**
     *
     */
    EMPTYPATTERNEXCEPTIONMESSAGEEMPTY("File name pattern is empty"),
    /**
     *
     */
    EMPTYPATTERNEXCEPTIONMESSAGENULL("File name pattern is null"),
    /**
     *
     */
    ILLEGALSTATEEXCEPTIONMESSAGE("FileNamePattern does not contain a valid DateToken"),
    /**
     *
     */
    ILLEGALARGUMENTEXCEPTIONMESSAGE("String value of size is not in expected format"),
    /**
     *
     */
    FILENAMENOTPROVIDEDMESSAGEEMPTY("File name is empty"),
    /**
     *
     */
    FILENAMENOTPROVIDEDMESSAGENULL("File name is null"),
    /**
     *
     */
    IMPLEMENTATIONNOTFOUNDMESSAGE("Log Implementation not found"),
    /**
     *
     */
    ILLEGALFILEACCESSMESSAGE("File location not accessible"),
    /**
     *
     */
    PATTERNSYNTAXEXCEPTIONMESSAGED("Pattern should contain %d{SimpleDateFormat}"),
    /**
     *
     */
    PATTERNSYNTAXEXCEPTIONMESSAGEI("Pattern should contain %i"),
    /**
     *
     */
    PATTERNSYNTAXEXCEPTIONMESSAGENOTI("Pattern should not contain %i"),
    /**
     *
     */
    CONFIGURATIONXMLPARSEMESSAGE("invalid xml configuration");

    /**
     * Value of exception constants constant {@link Enum} value
     */
    private final String value;

    /**
     * Constructor for this class
     *
     * @param value set {@link Enum} value
     */
    LogExeptionCodeConstant(final String value) {
        this.value = value;
    }

    /**
     * Getter for value
     *
     * @return get {@link Enum} value
     */
    public String getValue() {
        return value;
    }
}