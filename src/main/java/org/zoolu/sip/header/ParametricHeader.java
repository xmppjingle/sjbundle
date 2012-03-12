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
import org.zoolu.tools.Parser;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract ParametricHeader is the base class for all SIP Headers that include parameters
 */
public abstract class ParametricHeader extends Header {
    //public ParametricHeader(String hname)
    //{  super(hname);
    //}

    /**
     * Costructs the abstract ParametricHeader.
     *
     * @param hname
     * @param hvalue
     */
    protected ParametricHeader(String hname, String hvalue) {
        super(hname, hvalue);
    }

    /**
     * Costructs the abstract ParametricHeader.
     *
     * @param hd
     */
    protected ParametricHeader(Header hd) {
        super(hd);
    }

    /** Gets the first word.
     * @returns the first word or null if no value is present beafore parameters. */
    /*protected String getFirstWord()
    {  int index=indexOfFirstSemi();
       if (index<0) return value;
       else return value.substring(0,index).trim();
    }*/

    /**
     * Returns the index of the first semicolon before the first parameter.
     *
     * @return
     * @returns the index of the semicolon before the first parameter, or -1 if no parameter is present.
     */
    protected int indexOfFirstSemi() {  //int index=(new Parser(value)).goToSkippingQuoted(';').skipChar().skipWSP().getPos();
        int index = (new Parser(value)).goToSkippingQuoted(';').getPos();
        return (index >= value.length()) ? -1 : index;
    }

    /**
     * Gets the value of specified parameter.
     *
     * @param name
     * @return
     * @returns the parameter value or null if parameter does not exist or doesn't have a value (i.e. in case of flag parameter).
     */
    public String getParameter(String name) {
        int index = indexOfFirstSemi();
        if (index < 0) {
            return null;
        }
        return (new SipParser((new Parser(getValue(), index)).skipChar().skipWSP())).getParameter(name);
    }

    /**
     * Gets a String List of parameter names.
     *
     * @return
     * @returns a List of String
     */
    public List getParameterNames() {
        int index = indexOfFirstSemi();
        if (index < 0) {
            return new ArrayList();
        }
        return (new SipParser((new Parser(getValue(), index)).skipChar().skipWSP())).getParameters();
    }


    /**
     * Whether there is the specified parameter
     *
     * @param name
     * @return
     */
    public boolean hasParameter(String name) {
        int index = indexOfFirstSemi();
        if (index < 0) {
            return false;
        }
        return (new SipParser((new Parser(getValue(), index)).skipChar().skipWSP())).hasParameter(name);
    }


    /**
     * Whether there are any parameters
     *
     * @return
     */
    public boolean hasParameters() {
        return indexOfFirstSemi() >= 0;
    }


    /**
     * Removes all parameters (if any)
     */
    public void removeParameters() {
        if (!hasParameters()) {
            return;
        }
        String header = getValue();
        //System.out.println(header);
        int i = header.indexOf(';');
        header = header.substring(0, i);
        //System.out.println(header);
        setValue(header);
    }


    /**
     * Removes specified parameter (if present)
     *
     * @param name
     */
    public void removeParameter(String name) {
        int index = indexOfFirstSemi();
        if (index < 0) {
            return;
        }
        String header = getValue();
        Parser par = new Parser(header, index);
        while (par.hasMore()) {
            int beginParam = par.getPos();
            par.skipChar();
            if (par.getWord(SipParser.param_separators).equals(name)) {
                String top = header.substring(0, beginParam);
                par.goToSkippingQuoted(';');
                String bottom = "";
                if (par.hasMore()) {
                    bottom = header.substring(par.getPos());
                }
                header = top.concat(bottom);
                setValue(header);
                return;
                //par=new Parser(header,par.getPos());
            }
            par.goTo(';');
        }
    }


    /**
     * Sets the value of a specified parameter.
     * Zero-length String is returned in case of flag parameter (without value).
     *
     * @param name
     * @param value
     */
    public void setParameter(String name, String value) {
        if (getValue() == null) {
            setValue("");
        }
        if (hasParameter(name)) {
            removeParameter(name);
        }
        String header = getValue();
        header = header.concat(";" + name);
        if (value != null) {
            header = header.concat("=" + value);
        }
        setValue(header);
    }
}
