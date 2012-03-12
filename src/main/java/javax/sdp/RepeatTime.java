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
 * RepeatTime.java
 *
 * Created on December 20, 2001, 3:09 PM
 */

package javax.sdp;

/**
 * A RepeatTime represents a r= field contained within a TimeDescription.
 * <p/>
 * A RepeatTime specifies the repeat times for a SessionDescription.
 * <p/>
 * This consists of a "repeat interval", an "active duration", and a list of offsets relative to the t=
 * start-time (see Time.getStart()).
 * <p/>
 * Quoting from RFC 2327:
 * <p/>
 * For example, if a session is active at 10am on Monday and 11am on Tuesday for
 * one hour each week for three months, then the <start time> in the corresponding
 * "t=" field would be the NTP representation of 10am on the first Monday, the
 * <repeat interval> would be 1 week, the <active duration> would be 1 hour, and
 * the offsets would be zero and 25 hours. The corresponding "t=" field stop time
 * would be the NTP representation of the end of the last session three months later.
 * By default all fields are in seconds, so the "r=" and "t=" fields might be:
 * <p/>
 * t=3034423619 3042462419
 * r=604800 3600 0 90000
 * <p/>
 * <p/>
 * Please refer to IETF RFC 2327 for a description of SDP.
 *
 * @author deruelle
 * @version 1.0
 */
public interface RepeatTime extends Field {

    /**
     * Returns the "repeat interval" in seconds.
     *
     * @return the "repeat interval" in seconds.
     * @throws SdpParseException
     */
    public int getRepeatInterval()
            throws SdpParseException;

    /**
     * Set the "repeat interval" in seconds.
     *
     * @param repeatInterval the "repeat interval" in seconds.
     * @throws SdpException if repeatInterval is <0
     */
    public void setRepeatInterval(int repeatInterval)
            throws SdpException;

    /**
     * Returns the "active duration" in seconds.
     *
     * @return the "active duration" in seconds.
     * @throws SdpParseException
     */
    public int getActiveDuration()
            throws SdpParseException;

    /**
     * Sets the "active duration" in seconds.
     *
     * @param activeDuration the "active duration" in seconds.
     * @throws SdpException if the active duration is <0
     */
    public void setActiveDuration(int activeDuration)
            throws SdpException;

    /**
     * Returns the list of offsets. These are relative to the start-time given
     * in the Time object (t=
     * field) with which this RepeatTime is associated.
     *
     * @return the list of offsets
     * @throws SdpParseException
     */
    public int[] getOffsetArray()
            throws SdpParseException;

    /**
     * Set the list of offsets. These are relative to the start-time given in the
     * Time object (t=
     * field) with which this RepeatTime is associated.
     *
     * @param offsets array of repeat time offsets
     * @throws SdpException
     */
    public void setOffsetArray(int[] offsets)
            throws SdpException;

    /**
     * Returns whether the field will be output as a typed time or a integer value.
     * <p/>
     * Typed time is formatted as an integer followed by a unit character. The unit indicates an
     * appropriate multiplier for the integer.
     * <p/>
     * The following unit types are allowed.
     * d - days (86400 seconds)
     * h - hours (3600 seconds)
     * m - minutes (60 seconds)
     * s - seconds ( 1 seconds)
     *
     * @return true, if the field will be output as a typed time; false, if as an integer value.
     * @throws SdpParseException
     */
    public boolean getTypedTime()
            throws SdpParseException;

    /**
     * Sets whether the field will be output as a typed time or a integer value.
     * <p/>
     * Typed time is formatted as an integer followed by a unit character. The unit indicates an
     * appropriate multiplier for the integer.
     * <p/>
     * The following unit types are allowed.
     * d - days (86400 seconds)
     * h - hours (3600 seconds)
     * m - minutes (60 seconds)
     * s - seconds ( 1 seconds)
     *
     * @param typedTime typedTime - if set true, the start and stop times will be output in an optimal typed
     *                  time format; if false, the times will be output as integers.
     */
    public void setTypedTime(boolean typedTime);
}

