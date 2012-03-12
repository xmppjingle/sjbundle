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
 * Origin.java
 *
 * Created on December 20, 2001, 2:30 PM
 */

package javax.sdp;


/**
 * An Origin represents the o= fields contained within a SessionDescription.
 * <p/>
 * The Origin field identifies the originator of the session.
 * <p/>
 * This is not necessarily the same entity who is involved in the session.
 * <p/>
 * The Origin contains:
 * <p/>
 * the name of the user originating the session,
 * a unique session identifier, and
 * a unique version for the session.
 * <p/>
 * These fields should uniquely identify the session.
 * <p/>
 * The Origin also includes:
 * <p/>
 * the network type,
 * address type, and
 * address of the originator.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface Origin extends Field {

    /**
     * Returns the name of the session originator.
     *
     * @return the string username.
     * @throws SdpParseException
     */
    public String getUsername()
            throws SdpParseException;

    /**
     * Sets the name of the session originator.
     *
     * @param user the string username.
     * @throws SdpException if the parameter is null
     */
    public void setUsername(String user)
            throws SdpException;

    /**
     * Returns the unique identity of the session.
     *
     * @return the session id.
     * @throws SdpParseException
     */
    public long getSessionId()
            throws SdpParseException;

    /**
     * Sets the unique identity of the session.
     *
     * @param id the session id.
     * @throws SdpException if the id is <0
     */
    public void setSessionId(long id)
            throws SdpException;

    /**
     * Returns the unique version of the session.
     *
     * @return the session version.
     * @throws SdpException
     * @throws SdpParseException
     */
    public long getSessionVersion()
            throws SdpParseException;

    /**
     * Sets the unique version of the session.
     *
     * @param version the session version.
     * @throws SdpException if the version is <0
     */
    public void setSessionVersion(long version)
            throws SdpException;

    /**
     * Returns the type of the network for this Connection.
     *
     * @return the string network type.
     * @throws SdpParseException
     */
    public String getAddress()
            throws SdpParseException;

    /**
     * Returns the type of the address for this Connection.
     *
     * @return the string address type.
     * @throws SdpParseException
     */
    public String getAddressType()
            throws SdpParseException;

    /**
     * Returns the type of the network for this Connection
     *
     * @return the string network type.
     * @throws SdpParseException
     */
    public String getNetworkType()
            throws SdpParseException;

    /**
     * Sets the type of the address for this Connection.
     *
     * @param addr string address type.
     * @throws SdpException if the addr is null
     */
    public void setAddress(String addr)
            throws SdpException;

    /**
     * Returns the type of the network for this Connection.
     *
     * @param type the string network type.
     * @throws SdpException if the type is null
     */
    public void setAddressType(String type)
            throws SdpException;

    /**
     * Sets the type of the network for this Connection.
     *
     * @param type the string network type.
     * @throws SdpException if the type is null
     */
    public void setNetworkType(String type)
            throws SdpException;
}

