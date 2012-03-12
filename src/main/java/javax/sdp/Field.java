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
 * Field.java
 *
 * Created on December 18, 2001, 10:42 AM
 */

package javax.sdp;

import java.io.Serializable;

/**
 * A Field represents a single line of information within a SDP
 * session description.
 *
 * @author deruelle
 * @version 1.0
 */
public interface Field extends Serializable, Cloneable {

    /**
     * Returns the type character for the field.
     *
     * @return the type character for the field.
     */
    public char getTypeChar();


    /**
     * Returns a clone of this field.
     *
     * @return a clone of this field.
     */
    public Object clone();

}

