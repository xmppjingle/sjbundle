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
 * Media.java
 *
 * Created on December 19, 2001, 10:28 AM
 */

package javax.sdp;

import java.util.List;

/**
 * A Media represents an m= field contained within a MediaDescription. The Media
 * identifies information about the format(s) of the
 * media associated with the MediaDescription.
 * <p/>
 * The Media field includes:
 * <p/>
 * a mediaType (e.g. audio, video, etc.)
 * a port number (or set of ports)
 * a protocol to be used (e.g. RTP/AVP)
 * a set of media formats which correspond to Attributes associated with the
 * media description.
 * <p/>
 * Here is an example:
 * <p/>
 * m=audio 60000 RTP/AVP 0
 * a=rtpmap:0 PCMU/8000
 * <p/>
 * This example identifies that the client can receive audio on port 60000 in
 * format 0 which corresponds to PCMU/8000.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface Media extends Field {

    /**
     * Returns the type (audio,video etc) of the media defined by this description.
     *
     * @return the string media type.
     * @throws SdpParseException
     */
    public String getMediaType()
            throws SdpParseException;

    /**
     * Sets the type (audio,video etc) of the media defined by this description.
     *
     * @param mediaType to set
     * @throws SdpException if mediaType is null
     */
    public void setMediaType(String mediaType)
            throws SdpException;

    /**
     * Returns the port of the media defined by this description
     *
     * @return the integer media port.
     * @throws SdpParseException
     */
    public int getMediaPort()
            throws SdpParseException;

    /**
     * Sets the port of the media defined by this description
     *
     * @param port to set
     * @throws SdpException
     */
    public void setMediaPort(int port)
            throws SdpException;

    /**
     * Returns the number of ports associated with this media description
     *
     * @return the integer port count.
     * @throws SdpParseException
     */
    public int getPortCount()
            throws SdpParseException;

    /**
     * Sets the number of ports associated with this media description.
     *
     * @param portCount portCount - the integer port count.
     * @throws SdpException
     */
    public void setPortCount(int portCount)
            throws SdpException;

    /**
     * Returns the protocol over which this media should be transmitted.
     *
     * @return the String protocol, e.g. RTP/AVP.
     * @throws SdpParseException
     */
    public String getProtocol()
            throws SdpParseException;

    /**
     * Sets the protocol over which this media should be transmitted.
     *
     * @param protocol - the String protocol, e.g. RTP/AVP.
     * @throws SdpException if the protocol is null
     */
    public void setProtocol(String protocol)
            throws SdpException;

    /**
     * Returns an List of the media formats supported by this description.
     * Each element in this List will be an String value which matches one of
     * the a=rtpmap: attribute fields of the media description.
     *
     * @param create to set
     * @return the List.
     * @throws SdpException
     * @throws SdpParseException
     */
    public List getMediaFormats(boolean create)
            throws SdpParseException;

    /**
     * Adds a media format to the media description.
     * Each element in this List should be an String value which matches one of the
     * a=rtpmap: attribute fields of the media description.
     *
     * @param mediaFormats the format to add.
     * @throws SdpException if the List is null
     */
    public void setMediaFormats(List mediaFormats)
            throws SdpException;

    /**
     * Generates a string description of this object.
     *
     * @return the description.
     */
    public String toString();

}

