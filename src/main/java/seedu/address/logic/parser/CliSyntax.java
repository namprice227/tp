package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {
    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_RELATIONSHIP = new Prefix("r/");
    public static final Prefix PREFIX_ALLERGY = new Prefix("ta/");
    public static final Prefix PREFIX_CONDITION = new Prefix("tc/");
    public static final Prefix PREFIX_INSURANCE = new Prefix("ti/");
}
