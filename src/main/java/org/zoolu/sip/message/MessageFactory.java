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


import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.*;
import org.zoolu.sip.provider.SipProviderInfoInterface;


/**
 * Class sipx.message.MessageFactory extends class sip.message.BaseMessageFactory.
 * <p/>
 * MessageFactory is used to create SIP messages (requests and
 * responses).
 * <br /> A valid SIP request sent by a UAC MUST, at least, contain
 * the following header fields: To, From, CSeq, Call-ID, Max-Forwards,
 * and Via; all of these header fields are mandatory in all SIP
 * requests.  These sip header fields are the fundamental building
 * blocks of a SIP message, as they jointly provide for most of the
 * critical message routing services including the addressing of
 * messages, the routing of responses, limiting message propagation,
 * ordering of messages, and the unique identification of transactions.
 * These header fields are in addition to the mandatory request line,
 * which contains the method, Request-URI, and SIP version.
 */
public class MessageFactory extends org.zoolu.sip.message.BaseMessageFactory {

    /**
     * Creates a new MESSAGE request (RFC3428)
     *
     * @param sipProvider
     * @param recipient
     * @param from
     * @param subject
     * @param type
     * @param body
     * @return
     */
    public static Message createMessageRequest(final SipProviderInfoInterface sipProvider, final NameAddress recipient, final NameAddress from, final String subject, final String type, final String body) {
        final SipURL request_uri = recipient.getAddress();
        final String callid = sipProvider.pickCallId();
        final int cseq = sipProvider.pickInitialCSeq();
        final String localtag = sipProvider.pickTag();
        //String branch=SipStack.pickBranch();
        final Message req = createRequest(sipProvider, SipMethods.MESSAGE, request_uri, recipient, from, null, callid, cseq, localtag, null, null, null);
        if (subject != null) {
            req.setSubjectHeader(new SubjectHeader(subject));
        }
        req.setBody(type, body);
        return req;
    }

    /**
     * Creates a new REFER request (RFC3515)
     *
     * @param sipProvider
     * @param recipient
     * @param from
     * @param contact
     * @param refer_to
     * @return
     */
    public static Message createReferRequest(final SipProviderInfoInterface sipProvider, final NameAddress recipient, final NameAddress from, final NameAddress contact, final NameAddress refer_to/*, NameAddress referred_by*/) {
        final SipURL request_uri = recipient.getAddress();
        final String callid = sipProvider.pickCallId();
        final int cseq = sipProvider.pickInitialCSeq();
        final String localtag = sipProvider.pickTag();
        //String branch=SipStack.pickBranch();
        final Message req = createRequest(sipProvider, SipMethods.REFER, request_uri, recipient, from, contact, callid, cseq, localtag, null, null, null);
        req.setReferToHeader(new ReferToHeader(refer_to));
        //if (referred_by!=null) req.setReferredByHeader(new ReferredByHeader(referred_by));
        req.setReferredByHeader(new ReferredByHeader(from));
        return req;
    }


    /**
     * Creates a new SUBSCRIBE request (RFC3265) out of any pre-existing dialogs.
     *
     * @param sipProvider
     * @param recipient
     * @param to
     * @param from
     * @param contact
     * @param event
     * @param id
     * @param contentType
     * @param body
     * @return
     */
    public static Message createSubscribeRequest(final SipProviderInfoInterface sipProvider, final SipURL recipient, final NameAddress to, final NameAddress from, final NameAddress contact, final String event, final String id, final String contentType, final String body) {
        final Message req = createRequest(sipProvider, SipMethods.SUBSCRIBE, recipient, to, from, contact, null);
        req.setEventHeader(new EventHeader(event, id));
        req.setBody(contentType, body);
        req.setCallIdHeader(new CallIdHeader(id));
        return req;
    }

    /**
     * Creates a new PUBLISH request (RFC3265) out of any pre-existing dialogs.
     *
     * @param sipProvider
     * @param recipient
     * @param to
     * @param from
     * @param fromTag
     * @param contact
     * @param event
     * @param id
     * @param contentType
     * @param body
     * @param expires
     * @param cseq
     * @param sim
     * @return
     */
    public static Message createPublishRequest(final SipProviderInfoInterface sipProvider, final SipURL recipient, final NameAddress to, final NameAddress from, final String fromTag, final NameAddress contact, final String event, final String id, final String contentType, final String body, final int expires, final long cseq, final String sim) {
        final Message req = createRequest(sipProvider, SipMethods.PUBLISH, recipient, to, from, contact, id, cseq, fromTag, null, null, body);
        if (contact.toString().contains("sip:*")) {
            req.setContactHeader(new ContactHeader());
        }
        req.setEventHeader(new EventHeader(event, null));
        req.setBody(contentType, body);
        req.setExpiresHeader(new ExpiresHeader(expires));
        if (sim != null) {
            req.addHeader(new Header("SIP-If-Match", sim), false);
        }
        return req;
    }

}  