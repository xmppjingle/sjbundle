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
package javax.sdp;

import javax.sdp.fields.RepeatField;
import javax.sdp.fields.TimeField;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Time Description
 *
 * @author Olivier Deruelle
 * @author M. Ranganathan  <br/>
 * @version 1.2
 */
public class TimeDescriptionImpl implements TimeDescription {

    private TimeField timeImpl;

    private List repeatList;

    /**
     * Creates new TimeDescriptionImpl
     */
    public TimeDescriptionImpl() {
        timeImpl = new TimeField();
        repeatList = new ArrayList();

    }

    /**
     * constructor
     *
     * @param timeField time field to create this descrition from
     */
    public TimeDescriptionImpl(TimeField timeField) {
        this.timeImpl = timeField;
        repeatList = new ArrayList();
    }

    /**
     * Returns the Time field.
     *
     * @return Time
     */
    public Time getTime() {
        return timeImpl;
    }

    /**
     * Sets the Time field.
     *
     * @param timeField Time to set
     * @throws SdpException if the time is null
     */
    public void setTime(Time timeField) throws SdpException {
        if (timeField == null) {
            throw new SdpException("The parameter is null");
        } else {
            if (timeField instanceof TimeField) {
                this.timeImpl = (TimeField) timeField;
            } else {
                throw new SdpException(
                        "The parameter is not an instance of TimeField");
            }
        }
    }

    /**
     * Returns the list of repeat times (r= fields) specified in the
     * SessionDescription.
     *
     * @param create boolean to set
     * @return List
     */
    public List getRepeatTimes(boolean create) {
        return this.repeatList;
    }

    /**
     * Returns the list of repeat times (r= fields) specified in the
     * SessionDescription.
     *
     * @param repeatTimes List to set
     * @throws SdpException if the parameter is null
     */
    public void setRepeatTimes(List repeatTimes) throws SdpException {
        this.repeatList = repeatTimes;
    }

    /**
     * Add a repeat field.
     *
     * @param repeatField --
     *                    repeat field to add.
     */
    public void addRepeatField(RepeatField repeatField) {
        if (repeatField == null) {
            throw new NullPointerException("null repeatField");
        }
        this.repeatList.add(repeatField);
    }

    public String toString() {
        final StringBuilder retval = new StringBuilder(timeImpl.encode());
        for (Object aRepeatList : this.repeatList) {
            RepeatField repeatField = (RepeatField) aRepeatList;
            retval.append(repeatField.encode());
        }
        return retval.toString();
    }

}
