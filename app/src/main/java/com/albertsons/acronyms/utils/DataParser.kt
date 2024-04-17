package com.albertsons.acronyms.utils

/**
 * Data parser for the input string
 */
object DataParser {

    private const val SUCCESS_MESSAGE = "Success"
    private const val EMPTY_SF_MESSAGE = "Please provide valid acronym."
    private const val SINGLE_CHAR_SF_MESSAGE = "Acronym can't be single character."
    private const val NON_ALPHABET_SF_MESSAGE = "Acronym can have only alphabets."

    /**
     * Validate input string
     */
    fun isValidShorForm(abbreviation: String): Pair<Boolean, String> {
        return if (abbreviation.isEmpty())
            Pair(false, EMPTY_SF_MESSAGE)
        else if (abbreviation.length == 1)
            Pair(false, SINGLE_CHAR_SF_MESSAGE)
        else if (!(abbreviation.matches("^[a-zA-Z]*$".toRegex())))
            Pair(false, NON_ALPHABET_SF_MESSAGE)
        else
            Pair(true, SUCCESS_MESSAGE)
    }

}