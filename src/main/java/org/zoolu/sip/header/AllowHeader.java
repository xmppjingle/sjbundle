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


import java.util.List;


/**
 * SIP Header Allow
 */
public class AllowHeader extends ListHeader {
    public AllowHeader(String hvalue) {
        super(SipHeaders.Allow, hvalue);
    }

    public AllowHeader(Header hd) {
        super(hd);
    }

    /**
     * Gets list of methods (as List of Strings).
     *
     * @return
     */
    public List getMethods() {
        return super.getElements();
    }

    /**
     * Sets the list of methods.
     *
     * @param methods
     */
    public void setMethod(List methods) {
        super.setElements(methods);
    }

    /**
     * Adds a new method to the methods list.
     *
     * @param method
     */
    public void addMethod(String method) {
        super.add(method);
    }
}
