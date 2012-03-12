/*
 * Conditions Of Use
 *
 *  This software was developed by employees of the National Institute of
 *  Standards and Technology (NIST), an agency of the Federal Government.
 *  Pursuant to title 15 Untied States Code Section 105, works of NIST
 *  employees are not subject to copyright protection in the United States
 *  and are considered to be in the public domain.  As a result, a formal
 *  license is not needed to use the software.
 *
 *  This software is provided by NIST as a service and is expressly
 *  provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 *  OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 *  AND DATA ACCURACY.  NIST does not warrant or make any representations
 *  regarding the use of the software or the results thereof, including but
 *  not limited to the correctness, accuracy, reliability or usefulness of
 *  the software.
 *
 *  Permission to use this software is contingent upon your acceptance
 *  of the terms of this agreement
 */

/*
 * SdpException.java
 *
 * Created on December 18, 2001, 11:08 AM
 */

package javax.sdp;

/**
 * The SdpParseException encapsulates the information thrown when an error occurs
 * during SDP parsing.
 *
 * @author deruelle
 * @version 1.0
 */
public class SdpParseException extends SdpException {

    private int lineNumber;
    private int charOffset;

    /**
     * Constructs a new SdpParseException when the parser needs to throw an exception
     * indicating a parsing failure.
     *
     * @param lineNumber SDP line number that caused the exception.
     * @param charOffset offset of the characeter that caused the exception.
     * @param message    a String containing the text of the exception message
     * @param rootCause  the Throwable exception that interfered with the Codelet's
     *                   normal operation, making this Codelet exception necessary.
     */
    public SdpParseException(int lineNumber, int charOffset, String message,
                             Throwable rootCause) {
        super(message, rootCause);
        this.lineNumber = lineNumber;
        this.charOffset = charOffset;
    }

    /**
     * Constructs a new SdpParseException when the parser needs to throw an exception
     * indicating a parsing failure.
     *
     * @param lineNumber SDP line number that caused the exception.
     * @param charOffset offset of the characeter that caused the exception.
     * @param message    a String containing the text of the exception message
     */
    public SdpParseException(int lineNumber, int charOffset, String message) {
        super(message);
        this.lineNumber = lineNumber;
        this.charOffset = charOffset;
    }

    /**
     * Returns the line number where the error occured
     *
     * @return the line number where the error occured
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the char offset where the error occured.
     *
     * @return the char offset where the error occured.
     */
    public int getCharOffset() {
        return charOffset;
    }


    /**
     * Returns the message stored when the exception was created.
     *
     * @return the message stored when the exception was created.
     */
    public String getMessage() {
        return super.getMessage();
    }

}
