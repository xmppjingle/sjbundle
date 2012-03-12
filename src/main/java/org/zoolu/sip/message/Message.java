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

import org.zoolu.sip.header.*;

import java.net.DatagramPacket;
import java.net.SocketAddress;


/**
 * Class Message extends class sip.message.BaseMessage adding some SIP extensions.
 * <p/>
 * Class Message supports all methods and header definened in RFC3261, plus:
 * <ul>
 * <li> method MESSAGE (RFC3428) </>
 * <li> method REFER (RFC3515) </>
 * <li> header Refer-To </>
 * <li> header Referred-By </>
 * <li> header Event </>
 * </ul>
 */
public class Message extends BaseMessageOtp {

    private SocketAddress sendTo;
    private SipChannel arrivedAt;
    private Participants participants;

    public SocketAddress getSendTo() {
        return sendTo;
    }

    public void setSendTo(SocketAddress sendTo) {
        this.sendTo = sendTo;
    }

    /**
     * Costructs a new empty Message
     */
    public Message() {
        super();
    }

    /**
     * Costructs a new Message
     *
     * @param str
     */
    public Message(String str) {
        super(str);
        try {
            if (str != null && str.length() > 10) {
                participants = Participants.getParticipants(this);
            }
        } catch (SipParsingException e) {
            log.warn("Error Getting Participants.", e);
        }
    }

    /**
     * Costructs a new Message
     *
     * @param buff
     * @param offset
     * @param len
     */
    public Message(byte[] buff, int offset, int len) {
        super(buff, offset, len);

        try {
            participants = Participants.getParticipants(this);
        } catch (SipParsingException e) {
            log.warn("Error Getting Participants.", e);
        }

    }

    /**
     * Costructs a new Message
     *
     * @param packet
     */
    public Message(DatagramPacket packet) {
        super(packet);
        setSendTo(packet.getSocketAddress());
        try {
            participants = Participants.getParticipants(this);
        } catch (SipParsingException e) {
            log.warn("Error Getting Participants.", e);
        }


    }

    /**
     * Costructs a new Message
     *
     * @param msg
     */
    public Message(Message msg) {
        super(msg);
        try {
            participants = Participants.getParticipants(this);
        } catch (SipParsingException e) {
            log.error("Error Getting Participants.", e);
        }


    }

    /**
     * Creates and returns a clone of the Message
     */
    public Object clone() {
        return new Message(this);
    }


    //****************************** Extensions *******************************/

    /**
     * Returns boolean value to indicate if Message is a MESSAGE request (RFC3428)
     *
     * @return
     * @throws NullPointerException
     */
    public boolean isMessage() throws NullPointerException {
        return isRequest(SipMethods.MESSAGE);
    }

    /**
     * Returns boolean value to indicate if Message is a REFER request (RFC3515)
     *
     * @return
     * @throws NullPointerException
     */
    public boolean isRefer() throws NullPointerException {
        return isRequest(SipMethods.REFER);
    }

    /**
     * Returns boolean value to indicate if Message is a NOTIFY request (RFC3265)
     *
     * @return
     * @throws NullPointerException
     */
    public boolean isNotify() throws NullPointerException {
        return isRequest(SipMethods.NOTIFY);
    }

    /**
     * Returns boolean value to indicate if Message is a SUBSCRIBE request (RFC3265)
     *
     * @return
     * @throws NullPointerException
     */
    public boolean isSubscribe() throws NullPointerException {
        return isRequest(SipMethods.SUBSCRIBE);
    }

    /**
     * Returns boolean value to indicate if Message is a PUBLISH request (RFC3903)
     *
     * @return
     * @throws NullPointerException
     */
    public boolean isPublish() throws NullPointerException {
        return isRequest(SipMethods.PUBLISH);
    }


    /**
     * Whether the message has the Refer-To header
     *
     * @return
     */
    public boolean hasReferToHeader() {
        return hasHeader(SipHeaders.Refer_To);
    }

    /**
     * Gets ReferToHeader
     *
     * @return
     */
    public ReferToHeader getReferToHeader() {
        Header h = getHeader(SipHeaders.Refer_To);
        if (h == null) {
            return null;
        }
        return new ReferToHeader(h);
    }

    /**
     * Sets ReferToHeader
     *
     * @param h
     */
    public void setReferToHeader(ReferToHeader h) {
        setHeader(h);
    }

    /**
     * Removes ReferToHeader from Message (if it exists)
     */
    public void removeReferToHeader() {
        removeHeader(SipHeaders.Refer_To);
    }


    /**
     * Whether the message has the Referred-By header
     *
     * @return
     */
    public boolean hasReferredByHeader() {
        return hasHeader(SipHeaders.Refer_To);
    }

    /**
     * Gets ReferredByHeader
     *
     * @return
     */
    public ReferredByHeader getReferredByHeader() {
        Header h = getHeader(SipHeaders.Referred_By);
        if (h == null) {
            return null;
        }
        return new ReferredByHeader(h);
    }

    /**
     * Sets ReferredByHeader
     *
     * @param h
     */
    public void setReferredByHeader(ReferredByHeader h) {
        setHeader(h);
    }

    /**
     * Removes ReferredByHeader from Message (if it exists)
     */
    public void removeReferredByHeader() {
        removeHeader(SipHeaders.Referred_By);
    }


    /**
     * Whether the message has the EventHeader
     *
     * @return
     */
    public boolean hasEventHeader() {
        return hasHeader(SipHeaders.Event);
    }

    /**
     * Gets EventHeader
     *
     * @return
     */
    public EventHeader getEventHeader() {
        Header h = getHeader(SipHeaders.Event);
        if (h == null) {
            return null;
        }
        return new EventHeader(h);
    }

    /**
     * Sets EventHeader
     *
     * @param h
     */
    public void setEventHeader(EventHeader h) {
        setHeader(h);
    }

    /**
     * Removes EventHeader from Message (if it exists)
     */
    public void removeEventHeader() {
        removeHeader(SipHeaders.Event);
    }


    /**
     * Whether the message has the AllowEventsHeader
     *
     * @return
     */
    public boolean hasAllowEventsHeader() {
        return hasHeader(SipHeaders.Allow_Events);
    }

    /**
     * Gets AllowEventsHeader
     *
     * @return
     */
    public AllowEventsHeader getAllowEventsHeader() {
        Header h = getHeader(SipHeaders.Allow_Events);
        if (h == null) {
            return null;
        }
        return new AllowEventsHeader(h);
    }

    /**
     * Sets AllowEventsHeader
     *
     * @param h
     */
    public void setAllowEventsHeader(AllowEventsHeader h) {
        setHeader(h);
    }

    /**
     * Removes AllowEventsHeader from Message (if it exists)
     */
    public void removeAllowEventsHeader() {
        removeHeader(SipHeaders.Allow_Events);
    }

    /**
     * Whether the message has the Subscription-State header
     *
     * @return
     */
    public boolean hasSubscriptionStateHeader() {
        return hasHeader(SipHeaders.Subscription_State);
    }

    /**
     * Gets SubscriptionStateHeader
     *
     * @return
     */
    public SubscriptionStateHeader getSubscriptionStateHeader() {
        Header h = getHeader(SipHeaders.Subscription_State);
        if (h == null) {
            return null;
        }
        return new SubscriptionStateHeader(h);
    }

    /**
     * Sets SubscriptionStateHeader
     *
     * @param h
     */
    public void setSubscriptionStateHeader(SubscriptionStateHeader h) {
        setHeader(h);
    }

    /**
     * Removes SubscriptionStateHeader from Message (if it exists)
     */
    public void removeSubscriptionStateHeader() {
        removeHeader(SipHeaders.Subscription_State);
    }

    public SipChannel getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(SipChannel arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public Participants getParticipants() throws SipParsingException {
        if (participants == null) {
            participants = Participants.getParticipants(this);
        }
        return participants;
    }
}
