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
 * TimeDescription.java
 *
 * Created on January 9, 2002, 11:13 AM
 */

package javax.sdp;

import java.io.Serializable;
import java.util.List;

/**
 * A TimeDescription represents the fields present within a SDP time description.
 * <p/>
 * Quoting from RFC 2327:
 * <p/>
 * Multiple "t=" fields may be used if a session is active at multiple
 * irregularly spaced times; each additional
 * "t=" field specifies an additional period of time for which the session
 * will be active. If the session is active at
 * regular times, an "r=" field (see below) should be used in addition to and
 * following a "t=" field - in which
 * case the "t=" field specifies the start and stop times of the repeat sequence.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface TimeDescription extends Serializable, Cloneable {

    /**
     * Constant used to translate between NTP time used in SDP and "native" Java time.
     * NTP time is defined as the
     * number of seconds relative to midnight, January 1, 1900 and Java time is
     * measured in number of milliseconds
     * since midnight, January 1, 1970 UTC (see System#currentTimeMillis()}).
     * <p/>
     * The value of this constant is 2208988800L. It can be used to convert between
     * NTP and Java time using the
     * following formulas:
     * <p/>
     * ntpTime = javaTime/1000 * SdpConstants.NTP_CONST;
     * javaTime = (ntpTime - SdpConstants.NTP_CONST) * 1000;
     * <p/>
     * <p/>
     * The Network Time Protocol (NTP) is defined in RFC 1305.
     */
    public static final long NTP_CONST = 2208988800L;

    /**
     * Returns the Time field.
     *
     * @return Time
     * @throws SdpParseException
     */
    public Time getTime() throws SdpParseException;

    /**
     * Sets the Time field.
     *
     * @param t Time to set
     * @throws SdpException if the time is null
     */
    public void setTime(Time t)
            throws SdpException;

    /**
     * Returns the list of repeat times (r= fields) specified in the SessionDescription.
     *
     * @param create boolean to set
     * @return List
     */
    public List getRepeatTimes(boolean create);

    /**
     * Returns the list of repeat times (r= fields) specified in the SessionDescription.
     *
     * @param repeatTimes List to set
     * @throws SdpException if the parameter is null
     */
    public void setRepeatTimes(List repeatTimes)
            throws SdpException;

}

