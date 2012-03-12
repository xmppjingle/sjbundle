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
 * Key.java
 *
 * Created on December 19, 2001, 10:10 AM
 */

package javax.sdp;

/**
 * A Key represents the k= field contained within either a MediaDescription or a SessionDescription.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface Key extends Field {

    /**
     * Returns the name of this attribute
     *
     * @return the name of this attribute
     * @throws SdpParseException
     */
    public String getMethod()
            throws SdpParseException;

    /**
     * Sets the id of this attribute.
     *
     * @param name to set
     * @throws SdpException if the name is null
     */
    public void setMethod(String name)
            throws SdpException;

    /**
     * Determines if this attribute has an associated value.
     *
     * @return if this attribute has an associated value.
     * @throws SdpParseException
     */
    public boolean hasKey()
            throws SdpParseException;

    /**
     * Returns the value of this attribute.
     *
     * @return the value of this attribute
     * @throws SdpParseException
     */
    public String getKey()
            throws SdpParseException;

    /**
     * Sets the value of this attribute.
     *
     * @param key to set
     * @throws SdpException if key is null
     */
    public void setKey(String key)
            throws SdpException;
}

