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
/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
 *******************************************************************************/
package javax.sdp.fields;

import gov.nist.core.Host;
import gov.nist.core.Separators;

import javax.sdp.SdpException;
import javax.sdp.SdpParseException;

/**
 * Origin Field SDP header
 *
 * @author Olivier Deruelle <deruelle@antd.gov.nist.gov>
 * @author M. Ranganathan   <br/>
 * @version JSR141-PUBLIC-REVIEW (subject to change).
 */
public class OriginField extends SDPField implements javax.sdp.Origin {
    protected String username;
    //protected long sessId;
    //protected long sessVersion;
    protected String nettype; // IN
    protected String addrtype; // IPV4/6
    protected Host address;
    private String sessIdString;
    private String sessVersionString;

    public OriginField() {
        super(ORIGIN_FIELD);
    }

    /**
     * Returns the name of the session originator.
     *
     * @return the string username.
     * @throws SdpParseException
     */
    public String getUsername() throws SdpParseException {
        return username;
    }

    /**
     * Get the sessionID member.
     *
     * @return
     */
    public long getSessId() {

        return Long.parseLong(this.sessIdString);
    }

    public String getSessIdAsString() {
        return this.sessIdString;
    }

    /**
     * Get the sessionVersion member.
     *
     * @return
     */
    public long getSessVersion() {

        return Long.parseLong(sessVersionString);
    }

    public String getSessVersionAsString() {
        return this.sessVersionString;
    }

    /**
     * Get the netType member.
     *
     * @return
     */
    public String getNettype() {
        return nettype;
    }

    /**
     * Get the address type member.
     *
     * @return
     */
    public String getAddrtype() {
        return addrtype;
    }

    /**
     * Get the host member.
     *
     * @return
     */
    public Host getHost() {
        return address;
    }

    /**
     * Set the sessId member
     *
     * @param s
     */
    public void setSessId(long s) {
        this.sessIdString = Long.toString(s);
    }

    /**
     * This is a work around for some implementations that do not set a long
     * session id.
     *
     * @param sessId
     */
    public void setSessionId(String sessId) {
        this.sessIdString = sessId;
    }

    /**
     * Set the sessVersion member
     *
     * @param s
     */
    public void setSessVersion(long s) {
        sessVersionString = Long.toString(s);
    }

    /**
     * Set the session version as a string.
     *
     * @param s
     */
    public void setSessVersion(String s) {
        this.sessVersionString = s;
    }

    /**
     * Set the nettype member
     *
     * @param n
     */
    public void setNettype(String n) {
        nettype = n;
    }

    /**
     * Set the addrtype member
     *
     * @param a
     */
    public void setAddrtype(String a) {
        addrtype = a;
    }

    /**
     * Set the address member
     *
     * @param a
     */
    public void setAddress(Host a) {
        address = a;
    }

    /**
     * Sets the name of the session originator.
     *
     * @param user the string username.
     * @throws SdpException if the parameter is null
     */
    public void setUsername(String user) throws SdpException {
        if (user == null) {
            throw new SdpException("The user parameter is null");
        } else {
            this.username = user;
        }
    }

    /**
     * Returns the unique identity of the session.
     *
     * @return the session id.
     * @throws SdpParseException
     */
    public long getSessionId() throws SdpParseException {
        return getSessId();
    }

    /**
     * Sets the unique identity of the session.
     *
     * @param id the session id.
     * @throws SdpException if the id is <0
     */
    public void setSessionId(long id) throws SdpException {
        if (id < 0) {
            throw new SdpException("The is parameter is <0");
        } else {
            setSessId(id);
        }
    }

    /**
     * Returns the unique version of the session.
     *
     * @return the session version.
     * @throws SdpException
     */
    public long getSessionVersion() throws SdpParseException {
        return getSessVersion();
    }

    /**
     * Sets the unique version of the session.
     *
     * @param version the session version.
     * @throws SdpException if the version is <0
     */
    public void setSessionVersion(long version) throws SdpException {
        if (version < 0) {
            throw new SdpException("The version parameter is <0");
        } else {
            setSessVersion(version);
        }
    }

    /**
     * Returns the type of the network for this Connection.
     *
     * @return the string network type.
     * @throws SdpParseException
     */
    public String getAddress() throws SdpParseException {
        Host addr = getHost();
        if (addr == null) {
            return null;
        } else {
            return addr.getAddress();
        }
    }

    /**
     * Returns the type of the address for this Connection.
     *
     * @return the string address type.
     * @throws SdpParseException
     */
    public String getAddressType() throws SdpParseException {
        return getAddrtype();
    }

    /**
     * Returns the type of the network for this Connection
     *
     * @return the string network type.
     * @throws SdpParseException
     */
    public String getNetworkType() throws SdpParseException {
        return getNettype();
    }

    /**
     * Sets the type of the address for this Connection.
     *
     * @param addr string address type.
     * @throws SdpException if the addr is null
     */
    public void setAddress(String addr) throws SdpException {
        if (addr == null) {
            throw new SdpException("The addr parameter is null");
        } else {
            Host host = getHost();
            if (host == null) {
                host = new Host();
            }
            host.setAddress(addr);
            setAddress(host);
        }
    }

    /**
     * Returns the type of the network for this Connection.
     *
     * @param type the string network type.
     * @throws SdpException if the type is null
     */
    public void setAddressType(String type) throws SdpException {
        if (type == null) {
            throw new SdpException("The type parameter is <0");
        } else {
            setAddrtype(type);
        }
    }

    /**
     * Sets the type of the network for this Connection.
     *
     * @param type the string network type.
     * @throws SdpException if the type is null
     */
    public void setNetworkType(String type) throws SdpException {
        if (type == null) {
            throw new SdpException("The type parameter is <0");
        } else {
            setNettype(type);
        }
    }

    /**
     * Get the string encoded version of this object
     *
     * @since v1.0
     */
    public String encode() {

        return ORIGIN_FIELD
                + username
                + Separators.SP
                + sessIdString
                + Separators.SP
                + sessVersionString
                + Separators.SP
                + nettype
                + Separators.SP
                + addrtype
                + Separators.SP
                + address.encode()
                + Separators.NEWLINE;
    }

    public Object clone() {
        OriginField retval = (OriginField) super.clone();
        if (this.address != null) {
            retval.address = (Host) this.address.clone();
        }
        return retval;
    }

}

