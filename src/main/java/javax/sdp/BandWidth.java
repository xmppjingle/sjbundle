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
 * BandWidth.java
 *
 * Created on December 18, 2001, 3:59 PM
 */

package javax.sdp;

/**
 * A Bandwidth represents the b= fields contained within either a MediaDescription or a
 * SessionDescription.
 * <p/>
 * This specifies the proposed bandwidth to be used by the session or media, and is optional. Multiple
 * bandwidth specifiers of different types may be associated with the same SessionDescription. Each
 * consists of a token type and an integer value measuring bandwidth in kilobits per second.
 * <p/>
 * RFC 2327 defines two bandwidth types (or modifiers):
 * <p/>
 * CT
 * Conference Total: An implicit maximum bandwidth is associated with each TTL on the Mbone or
 * within a particular multicast administrative scope region (the Mbone bandwidth vs. TTL limits are given
 * in the MBone FAQ). If the bandwidth of a session or media in a session is different from the
 * bandwidth implicit from the scope, a 'b=CT:...' line should be supplied for the session giving the
 * proposed upper limit to the bandwidth used. The primary purpose of this is to give an approximate
 * idea as to whether two or more conferences can co-exist simultaneously.
 * AS
 * Application-Specific Maximum: The bandwidth is interpreted to be application-specific, i.e., will be the
 * application's concept of maximum bandwidth. Normally this will coincide with what is set on the
 * application's "maximum bandwidth" control if applicable.
 * <p/>
 * Note that CT gives a total bandwidth figure for all the media at all sites. AS gives a bandwidth figure for a
 * single media at a single site, although there may be many sites sending simultaneously.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface BandWidth extends Field {

    /**
     * "Conference Total" bandwidth modifier, "CT".
     */
    public static final String CT = "CT";

    /**
     * "Application Specific" bandwidth modifier, "AS".
     */
    public static final String AS = "AS";

    /**
     * Returns the bandwidth type.
     *
     * @return
     * @throws SdpParseException
     */
    public String getType() throws SdpParseException;

    /**
     * Sets the bandwidth type.
     *
     * @param type
     * @throws SdpException
     */
    public void setType(String type) throws SdpException;

    /**
     * Returns the bandwidth value measured in kilobits per second.
     *
     * @return
     * @throws SdpParseException
     */
    public int getValue() throws SdpParseException;

    /**
     * Sets the bandwidth value.
     *
     * @param value
     * @throws SdpException
     */
    public void setValue(int value) throws SdpException;

}

