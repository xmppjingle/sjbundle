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


import org.zoolu.tools.Parser;

import java.util.ArrayList;
import java.util.List;


/**
 * Generic SIP Header containing a list of tokens (Strings).
 */
public abstract class ListHeader extends Header {
    public ListHeader(String hname, String hvalue) {
        super(hname, hvalue);
    }

    public ListHeader(Header hd) {
        super(hd);
    }

    /**
     * Gets list of tokens (as List of Strings).
     *
     * @return
     */
    public List getElements() {
        List elements = new ArrayList();
        Parser par = new Parser(value);
        char[] delim = {','};
        while (par.hasMore()) {
            String elem = par.getWord(delim).trim();
            if (elem != null && elem.length() > 0) {
                elements.add(elem);
            }
            par.skipChar();
        }
        return elements;
    }

    /**
     * Sets the list of tokens.
     *
     * @param elements
     */
    public void setElements(List elements) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append((String) elements.get(i));
        }
        value = sb.toString();
    }

    /**
     * Adds a new token to the elements list.
     *
     * @param elem
     */
    public void add(String elem) {
        if (value == null || value.length() == 0) {
            value = elem;
        } else {
            value += ", " + elem;
        }
    }
}
