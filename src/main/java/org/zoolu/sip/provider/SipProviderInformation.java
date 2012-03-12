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

import org.zoolu.tools.Random;

public class SipProviderInformation implements SipProviderInfoInterface {

    public SipProviderInformation(String via_addr, int hostPort) {
        this.via_addr = via_addr;
        this.hostPort = hostPort;
    }

    // ***************** Readable/configurable attributes *****************

    /**
     * Via address/name.
     * Use 'auto-configuration' for auto detection, or let it undefined.
     */
    String via_addr = null;

    /**
     * Local SIP port
     */
    int hostPort = 0;

    /**
     * Network interface (IP address) used by SIP.
     * Use 'ALL-INTERFACES' for binding SIP to all interfaces (or let it undefined).
     */
    String hostIfaddr = null;

    /**
     * Default transport
     */
    String default_transport = "udp";

    /**
     * Whether adding 'rport' parameter on outgoing requests.
     */
    boolean rport = true;

    /**
     * Whether forcing 'rport' parameter on incoming requests ('force-rport' mode).
     */
    boolean force_rport = false;

    // ************************** Public methods *************************

    /**
     * Gets via address.
     */
    public String getViaAddress() {
        return via_addr;
    }

    /**
     * Sets via address.
     *
     * @param addr address
     */
    public void setViaAddress(String addr) {
        via_addr = addr;
    }

    /**
     * Gets host port.
     */
    public int getPort() {
        return hostPort;
    }

    /**
     * Gets the default transport protocol.
     */
    public String getDefaultTransport() {
        return default_transport;
    }

    /**
     * Gets the default transport protocol.
     */
    public void setDefaultTransport(String proto) {
        default_transport = proto;
    }

    /**
     * Sets rport support.
     */
    public void setRport(boolean flag) {
        rport = flag;
    }

    /**
     * Whether using rport.
     */
    public boolean isRportSet() {
        return rport;
    }

    /**
     * Sets 'force-rport' mode.
     */
    public void setForceRport(boolean flag) {
        force_rport = flag;
    }

    /**
     * Whether using 'force-rport' mode.
     */
    public boolean isForceRportSet() {
        return force_rport;
    }

    /**
     * Picks a new call-id.
     * The call-id is a globally unique
     * identifier over space and time. It is implemented in the
     * form "localid@host". Call-id must be considered case-sensitive and is
     * compared byte-by-byte.
     */
    public String pickCallId() {
        return Random.nextNumString(12) + "_" + getViaAddress();
    }

    public int pickInitialCSeq() {
        return 1;
    }

    /**
     * Picks a new tag.
     * A tag  MUST be globally unique and cryptographically random
     * with at least 32 bits of randomness.  A property of this selection
     * requirement is that a UA will place a different tag into the From
     * header of an INVITE than it would place into the To header of the
     * response to the same INVITE.  This is needed in order for a UA to
     * invite itself to a session.
     */
    public String pickTag() {
        return "z9hG4bK" + Random.nextNumString(8);
    }

    public String getIP() {
        return via_addr;
    }
}
