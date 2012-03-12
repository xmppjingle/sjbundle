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
 * SIP WWW-Authenticate header
 */
public class WwwAuthenticateHeader extends AuthenticationHeader {
    /**
     * Creates a new WwwAuthenticateHeader
     *
     * @param hvalue
     */
    public WwwAuthenticateHeader(String hvalue) {
        super(SipHeaders.WWW_Authenticate, hvalue);
    }

    /**
     * Creates a new WwwAuthenticateHeader
     *
     * @param hd
     */
    public WwwAuthenticateHeader(Header hd) {
        super(hd);
    }

    /**
     * Creates a new WwwAuthenticateHeader
     * specifing the <i>auth_scheme</i> and the List of authentication parameters.
     * <p> <i>authParam</i> is a List of String of the form <i>parm_name</i> "=" <i>parm_value</i>
     *
     * @param auth_scheme
     * @param authParams
     */
    public WwwAuthenticateHeader(String auth_scheme, List authParams) {
        super(SipHeaders.WWW_Authenticate, auth_scheme, authParams);
    }
}
