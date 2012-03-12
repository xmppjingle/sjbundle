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
 * SdpFactoryException.java
 *
 * Created on January 9, 2002, 6:44 PM
 */

package javax.sdp;

/**
 * The SdpFactoryException encapsulates the information thrown when a problem
 * with configuration with the
 * SdpFactory exists.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public class SdpFactoryException extends SdpException {

    /**
     * Chained exception.
     */
    protected Exception ex;

    /**
     * Creates new SdpFactoryException
     */
    public SdpFactoryException() {
        super();
    }

    /**
     * Create a new FactoryConfigurationException with the String specified as
     * an error message.
     *
     * @param msg msg - the detail message
     */
    public SdpFactoryException(String msg) {
        super(msg);
    }

    /**
     * Create a new FactoryConfigurationException with a given Exception base
     * cause of the error.
     *
     * @param ex ex - the "chained" exception
     */
    public SdpFactoryException(Exception ex) {
        super(ex.getMessage());
        this.ex = ex;
    }

    /**
     * Create a new FactoryConfigurationException with the given Exception base
     * cause and detail message.
     *
     * @param msg msg - the detail message
     * @param ex  ex - the "chained" exception
     */
    public SdpFactoryException(String msg,
                               Exception ex) {
        super(msg);
        this.ex = ex;
    }

    /**
     * Return the message (if any) for this error. If there is no message for
     * the exception and there is an encapsulated
     * exception then the message of that exception will be returned.
     *
     * @return the error message
     */
    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        } else if (ex != null) {
            return ex.getMessage();
        } else {
            return null;
        }
    }

    /**
     * Return the actual exception (if any) that caused this exception to be thrown.
     *
     * @return the encapsulated exception, or null if there is none
     */
    public Exception getException() {
        return ex;
    }
}
