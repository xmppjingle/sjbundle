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
 * The SdpException defines a general exception for the SDP classes to throw when they encounter a difficulty.
 *
 * @author deruelle
 * @version 1.0
 */
public class SdpException extends Exception {


    /**
     * Creates new SdpException
     */
    public SdpException() {
        super();
    }

    /**
     * Constructs a new SdpException with the message you specify.
     *
     * @param message a String specifying the text of the exception message
     */
    public SdpException(String message) {
        super(message);
    }

    /**
     * Constructs a new SdpException when the Codelet needs to throw an
     * exception and include a message about another exception that interfered
     * with its normal operation.
     *
     * @param message   a String specifying the text of the exception message
     * @param rootCause the Throwable exception that interfered with the
     *                  Codelet's normal operation, making this Codelet exception necessary
     */
    public SdpException(String message,
                        Throwable rootCause) {
        super(rootCause.getMessage() + ";" + message);
    }

    /**
     * Constructs a new SdpException as a result of a system exception and uses
     * the localized system exception message.
     *
     * @param rootCause the system exception that makes this SdpException necessary
     */
    public SdpException(Throwable rootCause) {
        super(rootCause.getLocalizedMessage());
    }

    /**
     * Returns the Throwable system exception that makes this SdpException necessary.
     *
     * @return Throwable
     */
    public Throwable getRootCause() {
        return fillInStackTrace();
    }

}
