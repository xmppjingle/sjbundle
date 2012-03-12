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

package org.zoolu.sip.message;

import org.apache.log4j.Logger;
import org.xmpp.packet.JID;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.FromHeader;
import org.zoolu.sip.header.ToHeader;


public class Participants {

    final private static Logger log = Logger.getLogger(Participants.class);
    private final JID initiator;
    private final JID responder;

    public Participants(final JID initiator, final JID responder) {
        this.initiator = initiator;
        this.responder = responder;
    }

    public JID getInitiator() {
        return initiator;
    }

    public JID getResponder() {
        return responder;
    }

    public static Participants getParticipants(final Message msg) throws SipParsingException {

        try {

            final FromHeader fHeader = msg.getFromHeader();
            final ToHeader tHeader = msg.getToHeader();

            if (fHeader == null || tHeader == null) {
                throw new SipParsingException("Invalid From/To Headers");
            }

            final NameAddress fAddr = fHeader.getNameAddress();
            final NameAddress tAddr = tHeader.getNameAddress();

            if (fAddr == null || tAddr == null) {
                throw new SipParsingException("Invalid From/To Addresses");
            }

            SipURL fURL = fAddr.getBareAddress();
            SipURL tURL = tAddr.getBareAddress();

            if (fURL == null || tURL == null) {
                throw new SipParsingException("Invalid From/To URLs");
            }

            final String f[] = fURL.toString().split(":");
            final String t[] = tURL.toString().split(":");

            if (f.length < 2 && t.length < 2) {
                return null;
            }

            final String from = f[1];
            final String to = t[1];

            String fResource = "";
            String tResource = "";

            if (fHeader.getTag() != null) {
                fResource = "/" + fHeader.getTag();
            }

            if (tHeader.getTag() != null) {
                tResource = "/" + tHeader.getTag();
            }

            final JID initiator = JIDFactory.getInstance().getJID(new StringBuilder().append(from).append(fResource).toString());
            final JID responder = JIDFactory.getInstance().getJID(new StringBuilder().append(to).append(tResource).toString());

            return new Participants(initiator, responder);

        } catch (Throwable e) {
            log.warn("Error getting Participants for Message");
            throw new SipParsingException(e.toString());
        }
    }

    public static JID getFromJidForResponse(final Message msg) {

        if (msg.getToHeader() == null) {
            return null;
        }

        final NameAddress na = msg.getToHeader().getNameAddress();

        if (na == null) {
            return null;
        }

        final String c = na.getAddress().getUserName();
        final String server = na.getAddress().getHost();

        return JIDFactory.getInstance().getJID(c, server, msg.getToHeader().getTag());
    }
}
