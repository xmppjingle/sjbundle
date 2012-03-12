/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 *
 *  This file is part of MjSip (http://www.mjsip.org)
 *
 *  MjSip is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  MjSip is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with MjSip; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Author(s):
 *  Luca Veltri (luca.veltri@unipr.it)
 *
 *  Modified:
 *  Benhur Langoni (bhlangonijr@gmail.com)
 *  Thiago Camargo (barata7@gmail.com)
 */

package org.zoolu.sip.header;


import org.zoolu.sip.provider.SipParser;

import java.util.ArrayList;
import java.util.List;


/**
 * MultipleHeader can be used to handle SIP headers that support comma-separated (multiple-header) rapresentation,
 * as explaned in section 7.3.1 of RFC 3261.
 */
public class MultipleHeader {
    /**
     * The header type
     */
    protected String name;
    /**
     * List of header values (as Strings)
     */
    protected List<String> values;
    /**
     * whether to be rapresented with a comma-separated(compact) header line or multiple header lines
     */
    protected boolean compact;

    protected MultipleHeader() {
        name = null;
        values = new ArrayList<String>();
        compact = true;
    }

    /**
     * Costructs a MultipleHeader named <i>hname</i>
     *
     * @param hname
     */
    public MultipleHeader(String hname) {
        name = hname;
        values = new ArrayList<String>();
        compact = true;
    }

    /**
     * Costructs a MultipleHeader named <i>hname</i> from a List of header values (as Strings).
     *
     * @param hname
     * @param hvalues
     */
    public MultipleHeader(String hname, List<String> hvalues) {
        name = hname;
        values = hvalues;
        compact = true;
    }

    /**
     * Costructs a MultipleHeader from a List of Headers. Each Header can be a single header or a multiple-comma-separated header.
     *
     * @param headers
     */
    public MultipleHeader(List headers) {
        name = ((Header) headers.get(0)).getName();
        values = new ArrayList<String>(headers.size());
        for (Object header : headers) {
            addBottom((Header) header);
        }
        compact = false;
    }

    /**
     * Costructs a MultipleHeader from a comma-separated header
     *
     * @param hd
     */
    public MultipleHeader(Header hd) {
        name = hd.getName();
        values = new ArrayList<String>();
        SipParser par = new SipParser(hd.getValue());
        int comma = par.indexOfCommaHeaderSeparator();
        while (comma >= 0) {
            values.add(par.getString(comma - par.getPos()).trim());
            par.skipChar(); //skip comma
            comma = par.indexOfCommaHeaderSeparator();
        }
        values.add(par.getRemainingString().trim());
        compact = true;
    }

    /**
     * Costructs a MultipleHeader from a MultipleHeader
     *
     * @param mhd
     */
    public MultipleHeader(MultipleHeader mhd) {
        name = mhd.getName();
        values = mhd.getValues();
        compact = mhd.isCommaSeparated();
    }

    /**
     * Checks if Header <i>hd</i> contains comma-separated multi-header
     *
     * @param hd
     * @return
     */
    public static boolean isCommaSeparated(Header hd) {
        SipParser par = new SipParser(hd.getValue());
        return par.indexOfCommaHeaderSeparator() >= 0;
    }

    /**
     * Sets the MultipleHeader rappresentation as comma-separated or multiple headers
     *
     * @param comma_separated
     */
    public void setCommaSeparated(boolean comma_separated) {
        compact = comma_separated;
    }

    /**
     * Whether the MultipleHeader rappresentation is comma-separated or multiple headers
     *
     * @return
     */
    public boolean isCommaSeparated() {
        return compact;
    }

    /**
     * Gets the size of th MultipleHeader
     *
     * @return
     */
    public int size() {
        return values.size();
    }

    /**
     * Whether it is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * Creates and returns a copy of Header
     */
    public Object clone() {
        return new MultipleHeader(getName(), getValues());
    }

    /**
     * Indicates whether some other Object is "equal to" this Header
     */
    public boolean equals(Object obj) {
        MultipleHeader hd = (MultipleHeader) obj;
        return hd.getName().equals(this.getName()) && hd.getValues().equals(this.getValues());
    }

    /**
     * Gets name of Header
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a List of header values
     *
     * @return
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * Sets header values
     *
     * @param v
     */
    public void setValues(List<String> v) {
        values = v;
    }

    /**
     * Gets a List of headers
     *
     * @return
     */
    public List<Header> getHeaders() {
        List<Header> v = new ArrayList<Header>(values.size());
        for (Object value : values) {
            Header h = new Header(name, (String) value);
            v.add(h);
        }
        return v;
    }

    /**
     * Sets header values
     *
     * @param hdv
     */
    public void setHeaders(List hdv) {
        values = new ArrayList<String>(hdv.size());
        for (Object aHdv : hdv) {
            values.add(((Header) aHdv).getValue());
        }
    }

    /**
     * Gets the i-value
     *
     * @param i
     * @return
     */
    public String getValue(int i) {
        return (String) values.get(i);
    }

    /** Adds top */
    //public void addTop(String value)
    //{  values.add(value,0);
    //}

    /**
     * Adds top
     *
     * @param hd
     */
    public void addTop(Header hd) {
        values.add(0, hd.getValue());
    }

    /**
     * Gets top Header
     *
     * @return
     */
    public Header getTop() {
        return new Header(name, (String) values.get(0));
    }

    /**
     * Removes top Header
     */
    public void removeTop() {
        values.remove(0);
    }

    /** Adds bottom */
    //public void addBottom(String value)
    //{  values.add(value);
    //}

    /**
     * Adds bottom
     *
     * @param hd
     */
    public void addBottom(Header hd) {
        if (!MultipleHeader.isCommaSeparated(hd)) {
            values.add(hd.getValue());
        } else {
            addBottom(new MultipleHeader(hd));
        }
    }

    /**
     * Adds other MultipleHeader at bottom
     *
     * @param mhd
     */
    public void addBottom(MultipleHeader mhd) {
        for (int i = 0; i < mhd.size(); i++)
            values.add(mhd.getValue(i));
    }

    /**
     * Gets bottom Header
     *
     * @return
     */
    public Header getBottom() {
        return new Header(name, (String) values.get(values.size() - 1));
    }

    /**
     * Removes bottom Header
     */
    public void removeBottom() {
        values.remove(values.size() - 1);
    }

    /**
     * Gets an Header containing the comma-separated(compact) representation.
     *
     * @return
     */
    public Header toHeader() {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < values.size() - 1; i++) str.append(values.get(i)).append(", ");
        if (values.size() > 0) {
            str.append(values.get(values.size() - 1));
        }
        return new Header(name, str.toString());
    }

    /**
     * Gets comma-separated(compact) or multi-headers(extended) representation.<BR>
     * Note that an empty header is rapresentated as:<BR>
     * - empty String (i.e. ""), for multi-headers(extended) rapresentation,
     * - empty-value Header (i.e. "HeaderName: \r\n"), for comma-separated(compact) rapresentation.
     */
    public String toString() {
        if (compact) {
            final StringBuilder str = new StringBuilder(name + ": ");
            for (int i = 0; i < values.size() - 1; i++) str.append(values.get(i)).append(", ");
            if (values.size() > 0) {
                str.append(values.get(values.size() - 1));
            }
            return str.append("\r\n").toString();
        } else {
            final StringBuilder str = new StringBuilder();
            for (Object value : values) str.append(name).append(": ").append(value).append("\r\n");
            return str.toString();
        }
    }


}

