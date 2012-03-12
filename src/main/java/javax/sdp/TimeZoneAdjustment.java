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
 * TimeZoneAdjustment.java
 *
 * Created on January 9, 2002, 11:20 AM
 */

package javax.sdp;

import java.util.Hashtable;

/**
 * A TimeZoneAdjustment represents the z= fields contained within a TimeDescription.
 * A TimeZoneAdjustment
 * specifies timezone changes which take place during or in between sessions
 * announced in a session description.
 * <p/>
 * From RFC 2327:
 * <p/>
 * To schedule a repeated session which spans a change from daylight- saving
 * time to standard time or
 * vice-versa, it is necessary to specify offsets from the base repeat times.
 * This is required because different
 * time zones change time at different times of day, different countries change
 * to or from daylight time on
 * different dates, and some countries do not have daylight saving time at all.
 * <p/>
 * Thus in order to schedule a session that is at the same time winter and
 * summer, it must be possible to
 * specify unambiguously by whose time zone a session is scheduled.
 * To simplify this task for receivers, we
 * allow the sender to specify the NTP time that a time zone adjustment
 * happens and the offset from the time
 * when the session was first scheduled. The "z" field allows the sender to
 * specify a list of these adjustment
 * times and offsets from the base time.
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface TimeZoneAdjustment extends Field {

    /**
     * Returns a Hashtable of adjustment times, where:
     * key = Date. This is the equivalent of the decimal NTP time value.
     * value = Int Adjustment. This is a relative time value in seconds.
     *
     * @param create to set
     * @return create - when true, an empty Hashtable is created, if it is null.
     * @throws SdpParseException
     */
    public Hashtable getZoneAdjustments(boolean create)
            throws SdpParseException;

    /**
     * Sets the Hashtable of adjustment times, where:
     * key = Date. This is the equivalent of the decimal NTP time value.
     * value = Int Adjustment. This is a relative time value in seconds.
     *
     * @param map Hashtable to set
     * @throws SdpException if the parameter is null
     */
    public void setZoneAdjustments(Hashtable map)
            throws SdpException;

    /**
     * Sets whether the field will be output as a typed time or a integer value.
     * <p/>
     * Typed time is formatted as an integer followed by a unit character.
     * The unit indicates an appropriate multiplier for
     * the integer.
     * <p/>
     * The following unit types are allowed.
     * d - days (86400 seconds)
     * h - hours (3600 seconds)
     * m - minutes (60 seconds)
     * s - seconds ( 1 seconds)
     *
     * @param typedTime typedTime - if set true, the start and stop times will be
     *                  output in an optimal typed time format; if false, the
     *                  times will be output as integers.
     */
    public void setTypedTime(boolean typedTime);

    /**
     * Returns whether the field will be output as a typed time or a integer value.
     * <p/>
     * Typed time is formatted as an integer followed by a unit character.
     * The unit indicates an appropriate multiplier for
     * the integer.
     * <p/>
     * The following unit types are allowed.
     * d - days (86400 seconds)
     * h - hours (3600 seconds)
     * m - minutes (60 seconds)
     * s - seconds ( 1 seconds)
     *
     * @return true, if the field will be output as a typed time; false, if as an integer value.
     */
    public boolean getTypedTime();

}

