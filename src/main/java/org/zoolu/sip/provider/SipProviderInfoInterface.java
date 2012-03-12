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

package org.zoolu.sip.provider;

/**
 * Provides information about a SipProvider
 */
public interface SipProviderInfoInterface {
// ************************** Public methods *************************

    /**
     * Gets via address.
     *
     * @return
     */
    public String getViaAddress();

    /**
     * Gets host port.
     *
     * @return
     */
    public int getPort();

    /**
     * Gets host ip.
     *
     * @return
     */
    public String getIP();

    /**
     * Gets the default transport protocol.
     *
     * @return
     */
    public String getDefaultTransport();

    /**
     * Gets the default transport protocol.
     *
     * @param proto
     */
    public void setDefaultTransport(String proto);

    /**
     * Sets rport support.
     *
     * @param flag
     */
    public void setRport(boolean flag);

    /**
     * Whether using rport.
     *
     * @return
     */
    public boolean isRportSet();

    /**
     * Sets 'force-rport' mode.
     *
     * @param flag
     */
    public void setForceRport(boolean flag);

    /**
     * Whether using 'force-rport' mode.
     *
     * @return
     */
    public boolean isForceRportSet();

    /**
     * Picks a new call-id.
     * The call-id is a globally unique
     * identifier over space and time. It is implemented in the
     * form "localid@host". Call-id must be considered case-sensitive and is
     * compared byte-by-byte.
     *
     * @return
     */
    public String pickCallId();

    /**
     * Picks a initial CSeq Value.
     *
     * @return CSeq Value.
     */
    public int pickInitialCSeq();

    /**
     * Picks a new tag.
     * A tag  MUST be globally unique and cryptographically random
     * with at least 32 bits of randomness.  A property of this selection
     * requirement is that a UA will place a different tag into the From
     * header of an INVITE than it would place into the To header of the
     * response to the same INVITE.  This is needed in order for a UA to
     * invite itself to a session.
     *
     * @return
     */
    public String pickTag();

}
