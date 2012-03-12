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
 * Attribute.java
 *
 * Created on December 18, 2001, 10:55 AM
 */

package javax.sdp;

/**
 * An Attribute represents an a= fields contained within either a MediaDescription or a
 * SessionDescription.
 * <p/>
 * An Attribute can be just an identity/name or a name-value pair.
 * <p/>
 * Here are some examples:
 * <p/>
 * a=recvonly
 * identifies a rcvonly attribute with just a name
 * a=rtpmap:0 PCMU/8000
 * identifies the media format 0 has having the value PCMU/8000.
 * <p/>
 * If a value is present, it must be preceeded by the : character.
 *
 * @author deruelle
 * @version 1.0
 */
public interface Attribute extends Field {

    /**
     * Returns the name of this attribute
     *
     * @return a String identity.
     * @throws SdpParseException if the name is not well formatted.
     */
    public String getName() throws SdpParseException;

    /**
     * Sets the id of this attribute.
     *
     * @param name the string name/id of the attribute.
     * @throws SdpException if the name is null
     */
    public void setName(String name) throws SdpException;

    /**
     * Determines if this attribute has an associated value.
     *
     * @return true if the attribute has a value.
     * @throws SdpParseException if the value is not well formatted.
     */
    public boolean hasValue() throws SdpParseException;

    /**
     * Returns the value of this attribute.
     *
     * @return the value; null if the attribute has no associated value.
     * @throws SdpParseException if the value is not well formatted.
     */
    public String getValue() throws SdpParseException;

    /**
     * Sets the value of this attribute.
     *
     * @param value the - attribute value
     * @throws SdpException if the value is null.
     */
    public void setValue(String value) throws SdpException;

}

