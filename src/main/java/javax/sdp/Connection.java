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
 * Connection.java
 *
 * Created on December 18, 2001, 4:20 PM
 */

package javax.sdp;

/**
 * A Connection represents the c= field associated with a SessionDescription or with an individual
 * MediaDescription and is used to identify a network address on which media can be received.
 * <p/>
 * The Connection in the SessionDescription applies to all MediaDescriptions unless a
 * MediaDescription specifically overrides it. The Connection identifies the network type (IN for internet),
 * address type (IP4 or IP6), the start of an address range, the time to live of the session and the number of
 * addresses in the range. Both the time to live and number of addresses are optional.
 * <p/>
 * A Connection could therefore be of one these forms:
 * <p/>
 * c=IN IP4 myhost.somewhere.com (no ttl and only one address)
 * c=IN IP4 myhost.somewhere.com/5 (a ttl of 5)
 * c=IN IP4 myhost.somewhere.com/5/2 (a ttl of 5 and 2 addresses)
 * <p/>
 * This implementation does not explicitly support ttl and number of addresses.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface Connection extends Field {

    /**
     * The Internet network type, "IN".
     */
    public static final String IN = "IN";

    /**
     * The IPv4 address type, "IP4".
     */
    public static final String IP4 = "IP4";

    /**
     * The IPv6 address type, "IP6".
     */
    public static final String IP6 = "IP6";

    /**
     * Returns the type of the network for this Connection.
     *
     * @return
     * @throws SdpParseException
     */
    public String getAddress() throws SdpParseException;

    /**
     * Returns the type of the address for this Connection.
     *
     * @return
     * @throws SdpParseException
     */
    public String getAddressType() throws SdpParseException;

    /**
     * Returns the type of the network for this Connection.
     *
     * @return
     * @throws SdpParseException
     */
    public String getNetworkType() throws SdpParseException;

    /**
     * Sets the type of the address for this Connection.
     *
     * @param addr
     * @throws SdpException
     */
    public void setAddress(String addr) throws SdpException;

    /**
     * Returns the type of the network for this Connection.
     *
     * @param type
     * @throws SdpException
     */
    public void setAddressType(String type) throws SdpException;

    /**
     * Sets the type of the network for this Connection.
     *
     * @param type
     * @throws SdpException
     */
    public void setNetworkType(String type) throws SdpException;

}

