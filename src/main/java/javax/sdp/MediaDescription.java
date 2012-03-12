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
 * MediaDescription.java
 *
 * Created on December 19, 2001, 11:17 AM
 */

package javax.sdp;

import javax.sdp.fields.AttributeField;
import javax.sdp.fields.PreconditionFields;
import java.io.Serializable;
import java.util.List;
// end //


/**
 * A MediaDescription identifies the set of medias that may be received on a specific port or set of ports. It includes:
 * <p/>
 * a mediaType (e.g., audio, video, etc.)
 * a port number (or set of ports)
 * a protocol to be used (e.g., RTP/AVP)
 * a set of media formats which correspond to Attributes associated with the media description.
 * <p/>
 * The following is an example
 * <p/>
 * m=audio 60000 RTP/AVP 0
 * a=rtpmap:0 PCMU/8000
 * <p/>
 * This example identifies that the client can receive audio on port 60000 in format 0 which corresponds to PCMU/8000.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface MediaDescription extends Serializable, Cloneable {

    /**
     * Return the Media field of the description.
     *
     * @return the Media field of the description.
     */
    public Media getMedia();

    /**
     * Set the Media field of the description.
     *
     * @param media to set
     * @throws SdpException if the media field is null
     */
    public void setMedia(Media media)
            throws SdpException;

    /**
     * Returns value of the info field (i=) of this object.
     *
     * @return value of the info field (i=) of this object.
     */
    public Info getInfo();

    /**
     * Sets the i= field of this object.
     *
     * @param i to set
     * @throws SdpException if the info is null
     */
    public void setInfo(Info i)
            throws SdpException;

    /**
     * Returns the connection information associated with this object. This may be null for SessionDescriptions if all Media
     * objects have a connection object and may be null for Media objects if the corresponding session connection is non-null.
     *
     * @return connection
     */
    public Connection getConnection();


    /**
     * Set the connection data for this entity
     *
     * @param conn to set
     * @throws SdpException if the connexion is null
     */
    public void setConnection(Connection conn)
            throws SdpException;

    /**
     * Returns the Bandwidth of the specified type.
     *
     * @param create type of the Bandwidth to return
     * @return the Bandwidth or null if undefined
     */
    public List getBandwidths(boolean create);

    /**
     * set the value of the Bandwidth with the specified type
     *
     * @param bandwidths type of the Bandwidth object whose value is requested
     * @throws SdpException if List is null
     */
    public void setBandwidths(List bandwidths)
            throws SdpException;

    /**
     * Returns the integer value of the specified bandwidth name.
     *
     * @param name the name of the bandwidth type.
     * @return the value of the named bandwidth
     * @throws SdpParseException
     */
    public int getBandwidth(String name)
            throws SdpParseException;

    /**
     * Sets the value of the specified bandwidth type.
     *
     * @param name  the name of the bandwidth type.
     * @param value the value of the named bandwidth type.
     * @throws SdpException if the name is null
     */
    public void setBandwidth(String name,
                             int value)
            throws SdpException;

    /**
     * Removes the specified bandwidth type.
     *
     * @param name the name of the bandwidth type.
     */
    public void removeBandwidth(String name);

    /**
     * Returns the key data.
     *
     * @return the key data.
     */
    public Key getKey();

    /**
     * Sets encryption key information. This consists of a method and an encryption key included inline.
     *
     * @param key the encryption key data; depending on method may be null
     * @throws SdpException if the key is null
     */
    public void setKey(Key key)
            throws SdpException;

    /**
     * Returns the set of attributes for this Description as a List of Attribute objects in the order they were parsed.
     *
     * @param create specifies whether to return null or a new empty List in case no attributes exists for this Description
     * @return attributes for this Description
     */
    public List getAttributes(boolean create);

    /**
     * Adds the specified Attribute to this Description object.
     *
     * @param Attributes the attribute to add
     * @throws SdpException if the attribute is null
     */
    public void setAttributes(List Attributes)
            throws SdpException;

    /**
     * Returns the value of the specified attribute.
     *
     * @param name the name of the attribute.
     * @return the value of the named attribute
     * @throws SdpParseException
     */
    public String getAttribute(String name)
            throws SdpParseException;

    /**
     * Sets the value of the specified attribute
     *
     * @param name  the name of the attribute.
     * @param value the value of the named attribute.
     * @throws SdpException if the parameters are null
     */
    public void setAttribute(String name,
                             String value)
            throws SdpException;

    /**
     * Removes the attribute specified by the value parameter.
     *
     * @param name the name of the attribute.
     */
    public void removeAttribute(String name);

    /**
     * Returns a List containing a string indicating the MIME type for each of the codecs in this description.
     * <p/>
     * A MIME value is computed for each codec in the media description.
     * <p/>
     * The MIME type is computed in the following fashion:
     * The type is the mediaType from the media field.
     * The subType is determined by the protocol.
     * <p/>
     * The result is computed as the string of the form:
     * <p/>
     * type + '/' + subType
     * <p/>
     * The subType portion is computed in the following fashion.
     * RTP/AVP
     * the subType is returned as the codec name. This will either be extracted from the rtpmap attribute or computed.
     * other
     * the protocol is returned as the subType.
     * <p/>
     * If the protocol is RTP/AVP and the rtpmap attribute for a codec is absent, then the codec name will be computed in the
     * following fashion.
     * String indexed in table SdpConstants.avpTypeNames
     * if the value is an int greater than or equal to 0 and less than AVP_DEFINED_STATIC_MAX, and has been assigned a
     * value.
     * SdpConstant.RESERVED
     * if the value is an int greater than or equal to 0 and less than AVP_DEFINED_STATIC_MAX, and has not been
     * assigned a value.
     * SdpConstant.UNASSIGNED
     * An int greater than or equal to AVP_DEFINED_STATIC_MAX and less than AVP_DYNAMIC_MIN - currently
     * unassigned.
     * SdpConstant.DYNAMIC
     * Any int less than 0 or greater than or equal to AVP_DYNAMIC_MIN
     *
     * @return a List containing a string indicating the MIME type for each of the codecs in this description
     * @throws SdpException if there is a problem extracting the parameters.
     */
    public List getMimeTypes()
            throws SdpException;

    /**
     * Returns a List containing a string of parameters for each of the codecs in this description.
     * <p/>
     * A parameter string is computed for each codec.
     * <p/>
     * The parameter string is computed in the following fashion.
     * <p/>
     * The rate is extracted from the rtpmap or static data.
     * <p/>
     * The number of channels is extracted from the rtpmap or static data.
     * <p/>
     * The ptime is extracted from the ptime attribute.
     * <p/>
     * The maxptime is extracted from the maxptime attribute.
     * <p/>
     * Any additional parameters are extracted from the ftmp attribute.
     *
     * @return a List containing a string of parameters for each of the codecs in this description.
     * @throws SdpException if there is a problem extracting the parameters.
     */
    public List getMimeParameters()
            throws SdpException;

    /**
     * Adds dynamic media types to the description.
     *
     * @param payloadNames  a List of String - each one the name of a dynamic payload to be added (usually an integer larger
     *                      than SdpConstants.AVP_DYNAMIC_MIN).
     * @param payloadValues a List of String - each contains the value describing the correlated dynamic payloads to be added
     * @throws SdpException if either List is null or empty.
     *                      if the List sizes are unequal.
     */
    public void addDynamicPayloads(List payloadNames,
                                   List payloadValues)
            throws SdpException;


//////////////////////////////////////////////
// changes made by PT-INOVACAO
//////////////////////////////////////////////

    /**
     * <p>Set PreconditionFields for the Media Description</p>
     * <p/>
     * issued by Miguel Freitas (IT) PTInovacao
     *
     * @param segPrecondition List with values to ser
     * @throws SdpException exception
     */
    public void setPreconditionFields(List segPrecondition) throws SdpException;

    /**
     * <p>Set PreconditionFields for the Media Description</p>
     * <p/>
     * issued by Miguel Freitas (IT) PTInovacao
     *
     * @param segPrecondition PreconditionFields with values to set
     */
    public void setPreconditions(PreconditionFields segPrecondition);

    /**
     * <p>Get all Precondition Fields in the Media Descritpion</p>
     * <p/>
     * issued by Miguel Freitas (IT) PTInovacao
     *
     * @return List precondition fields
     */
    public List getPreconditionFields();

    /**
     * <p>Add Media Attribute based on an AttributeField value</p>
     * <p/>
     * issued by Miguel Freitas (IT) PTInovacao
     *
     * @param at AttributeField
     */
    public void addAttribute(AttributeField at);


}

