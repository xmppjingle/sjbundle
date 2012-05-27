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
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.*;
import org.zoolu.sip.provider.MethodIdentifier;
import org.zoolu.sip.provider.SipParser;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;


/**
 * Class BaseMessage implements a generic SIP Message.
 */
public abstract class BaseMessage {

    static final Logger log = Logger.getLogger(BaseMessage.class);
    /**
     * UDP
     */
    public static final String PROTO_UDP = "udp";
    /**
     * TCP
     */
    public static final String PROTO_TCP = "tcp";
    /**
     * TLS
     */
    public static final String PROTO_TLS = "tls";
    /**
     * SCTP
     */
    public static final String PROTO_SCTP = "sctp";

    /**
     * Maximum receiving packet size
     */
    protected static int MAXPKT_SIZE = 8000;

    /**
     * The remote ip address
     */
    protected String remoteAddr;

    /**
     * The remote port
     */
    protected int remotePort;

    /**
     * Transport protocol
     */
    protected String transportProto;

    /** Packet length */
    //protected int packet_length;

    /**
     * The message string
     */
    private String message;


    private final String SIP_RESPONSEPRE = "SIP/";

    /**
     * Inits empty Message
     */
    private void init() {  //message="";
        remoteAddr = null;
        remotePort = 0;
        transportProto = null;
    }

    /**
     * Costructs a new empty Message
     */
    public BaseMessage() {
        init();
        message = "";
    }

    /**
     * Costructs a new Message
     *
     * @param data
     * @param offset
     * @param len
     */
    public BaseMessage(byte[] data, int offset, int len) {
        init();
        message = new String(data, offset, len).trim();
    }

    /**
     * Costructs a new Message
     *
     * @param packet
     */
    public BaseMessage(DatagramPacket packet) {
        init();
        message = new String(packet.getData(), packet.getOffset(), packet.getLength()).trim();
    }

    /**
     * Costructs a new Message
     *
     * @param str
     */
    public BaseMessage(String str) {
        init();
        message = str.trim();
    }

    /**
     * Costructs a new Message
     *
     * @param msg
     */
    public BaseMessage(BaseMessage msg) {  //message=new String(msg.message);
        message = msg.message;
        remoteAddr = msg.remoteAddr;
        remotePort = msg.remotePort;
        transportProto = msg.transportProto;
        //packet_length=msg.packet_length;
    }

    /**
     * Creates and returns a clone of the Message
     */
    abstract public Object clone();
    //{  return new Message(message);
    //}

    /**
     * Sets the entire message
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message.trim();
    }

    /**
     * Gets string representation of Message
     */
    public String toString() {
        return message;
    }

    /**
     * Gets remote ip address
     *
     * @return
     */
    public String getRemoteAddress() {
        return remoteAddr;
    }

    /**
     * Gets remote port
     *
     * @return
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * Gets transport protocol
     *
     * @return
     */
    public String getTransportProtocol() {
        return transportProto;
    }

    /**
     * Gets message length
     *
     * @return
     */
    public int getLength() {
        return message.length();
    }

    /**
     * Sets remote ip address
     *
     * @param addr
     */
    public void setRemoteAddress(String addr) {
        remoteAddr = addr;
    }

    /**
     * Sets remote port
     *
     * @param port
     */
    public void setRemotePort(int port) {
        remotePort = port;
    }

    /**
     * Sets transport protocol
     *
     * @param proto
     */
    public void setTransport(String proto) {
        transportProto = proto;
    }

    //**************************** Requests ****************************/

    /**
     * Whether Message is a Request
     *
     * @return
     * @throws NullPointerException
     */
    public boolean isRequest() throws NullPointerException {  // Req-Line = Method ' ' SIP-URL ' ' "SIP/2.0" CRLF
        if (message == null || isResponse()) {
            return false;
        }
        String firstline = (new SipParser(message)).getLine();
        String version = (new SipParser(firstline)).skipString().skipString().getString();
        if (version == null || version.length() < 4) {
            return false;
        }
        version = version.substring(0, 4);
        String target = "SIP/";
        //if (version.compareToIgnoreCase(target)==0) return true;
        return version.equalsIgnoreCase(target);
    }

    /**
     * Gets the MethodIdentifier
     *
     * @return
     */
    public MethodIdentifier getMethodId() {
        String method = getCSeqHeader().getMethod();
        return new MethodIdentifier(method);
    }


    /**
     * Whether Message is a <i>method</i> request
     *
     * @param method
     * @return
     */
    public boolean isRequest(String method) {  //if (message==null) return false;
        return message.startsWith(method);
    }

    /**
     * Whether Message is a Method that creates a dialog
     *
     * @return
     */
    public boolean createsDialog() {
        if (!isRequest()) {
            return false;
        }
        //else
        String method = getRequestLine().getMethod();
        for (int i = 0; i < SipMethods.dialog_methods.length; i++)
            if (method.equalsIgnoreCase(SipMethods.dialog_methods[i])) {
                return true;
            }
        //else
        return false;
    }

    /**
     * Whether Message is an Invite
     *
     * @return
     */
    public boolean isInvite() {
        return isRequest(SipMethods.INVITE);
    }

    /**
     * Whether Message is an Invite
     *
     * @return
     */
    public boolean isRinging() {
        return this.getStatusLine() != null && (this.getStatusLine().getCode() >= 180 && this.getStatusLine().getCode() < 190);
    }

    /**
     * Whether Message is an Trying
     *
     * @return
     */
    public boolean isTrying() {
        return this.getStatusLine() != null && this.getStatusLine().getCode() == 100;
    }

    /**
     * Whether Message is a Register
     *
     * @return
     */
    public boolean isRegister() {
        return isRequest(SipMethods.REGISTER);
    }

    /**
     * Whether Message is a Cancel
     *
     * @return
     */
    public boolean isCancel() {
        return isRequest(SipMethods.CANCEL);
    }

    /**
     * Whether Message is a Bye
     *
     * @return
     */
    public boolean isBye() {
        return isRequest(SipMethods.BYE);
    }

    /**
     * Whether Message is an Ack
     *
     * @return
     */
    public boolean isAck() {
        return isRequest(SipMethods.ACK);
    }

    /**
     * Whether Message is an Info
     *
     * @return
     */
    public boolean isInfo() {
        return isRequest(SipMethods.INFO);
    }

    /**
     * Whether Message is an Option
     *
     * @return
     */
    public boolean isOption() {
        return isRequest(SipMethods.OPTIONS);
    }

    /**
     * Whether Message has Request-line
     *
     * @return
     */
    protected boolean hasRequestLine() {
        return isRequest();
    }

    /**
     * Gets RequestLine in Message (Returns null if called for no request message)
     *
     * @return
     */
    public RequestLine getRequestLine() {
        if (!isRequest()) {  //printWarning("getRequestLine(): called for no request message\n",1);
            return null;
        }
        SipParser par = new SipParser(message);
        String method = par.getString();
        par.skipWSP();
        par = new SipParser(par.subParser(par.indexOfEOH() - par.getPos()));
        return new RequestLine(method, par.getSipURL());
    }

    /**
     * Sets RequestLine of the Message
     *
     * @param rl
     */
    public void setRequestLine(RequestLine rl) {
        if (hasRequestLine()) {
            removeRequestLine();
        }
        String value = rl.toString();
        message = value + message;
    }

    /**
     * Removes RequestLine of the Message
     */
    public void removeRequestLine() {
        if (!isRequest()) {
            return;
        }
        removeFirstLine();
    }

    //**************************** Responses ****************************/

    /**
     * Whether Message is a Response
     *
     * @return
     * @throws NullPointerException
     */
    public boolean isResponse() throws NullPointerException {  // Status-Line = "SIP/2.0" ' ' Status-Code ' 'Reason-Phrase" CRLF
        //if (message==null) return false;
        if (message == null || message.length() < 4) {
            return false;
        }
        String version = message.substring(0, 4);
        //if (version.compareToIgnoreCase(target)==0) return true;
        return version.equalsIgnoreCase(SIP_RESPONSEPRE);

    }

    /**
     * Whether Message has Status-line
     *
     * @return
     */
    protected boolean hasStatusLine() {
        return isResponse();
    }

    /**
     * Gets StautsLine in Message (Returns null if called for no response message)
     *
     * @return
     */
    public StatusLine getStatusLine() {
        if (!isResponse()) {  //printWarning("getStatusLine(): called for no response message\n",1);
            return null;
        }
        SipParser par = new SipParser(message);
        par.skipString().skipWSP(); // "SIP/2.0 "
        int code = par.getInt();
        int begin = par.getPos();
        int end = par.indexOfEOH();
        String reason = message.substring(begin, end).trim();
        return new StatusLine(code, reason);
    }

    /**
     * Sets StatusLine of the Message
     *
     * @param sl
     */
    public void setStatusLine(StatusLine sl) {
        if (hasStatusLine()) {
            removeStatusLine();
        }
        message = sl.toString() + message;
    }

    /**
     * Removes StatusLine of the Message
     */
    public void removeStatusLine() {
        if (!isResponse()) {
            return;
        }
        removeFirstLine();
    }

    //**************************** Generic Headers ****************************/

    /**
     * Returns the session method
     *
     * @return
     */
    public String getTransactionMethod() {
        return getCSeqHeader().getMethod();
    }

    /**
     * Gets the first line of the Message
     *
     * @return
     */
    public String getFirstLine() {
        if (isRequest()) {
            return getRequestLine().toString();
        }
        if (isResponse()) {
            return getStatusLine().toString();
        }
        return null;
    }

    /** Sets Request/Status Line of the Message */
    /*private void setFirstLine(String value)
   {  message=value+" \r\n"+message;
   } */

    /**
     * Removes Request\Status Line of the Message
     */
    protected void removeFirstLine() {
        message = message.substring((new SipParser(message)).indexOfNextHeader());
    }

    /**
     * Whether Message has any headers of specified name
     *
     * @param name
     * @return
     */
    public boolean hasHeader(String name) {
        Header hd = getHeader(name);
        return hd != null;
    }

    /**
     * Gets the first Header of specified name (Returns null if no Header is found)
     *
     * @param hname
     * @return
     */
    public Header getHeader(String hname) {
        SipParser par = new SipParser(message);
        return par.getHeader(hname);
    }

    /**
     * Gets a List of all Headers of specified name (Returns empty List if no Header is found)
     *
     * @param hname
     * @return
     */
    public List getHeaders(String hname) {
        List v = new ArrayList();
        SipParser par = new SipParser(message);
        Header h;
        while ((h = par.getHeader(hname)) != null) {
            v.add(h);
        }
        return v;
    }

    /**
     * Adds Header at the top/bottom.
     * The bottom is considered before the Content-Length and Content-Type headers
     *
     * @param header
     * @param top
     */
    public void addHeader(Header header, boolean top) {
        addHeaders(header.toString(), top);
    }

    /**
     * Adds a List of Headers at the top/bottom
     *
     * @param headers
     * @param top
     */
    public void addHeaders(List<Header> headers, boolean top) {
        final StringBuilder str = new StringBuilder();
        for (Object header : headers) str.append(header.toString());
        addHeaders(str.toString(), top);
    }

    /**
     * Adds MultipleHeader(s) <i>mheader</i> at the top/bottom
     *
     * @param mheader
     * @param top
     */
    public void addHeaders(MultipleHeader mheader, boolean top) {
        addHeaders(mheader.toString(), top);
    }

    /**
     * Adds a one or more Headers at the top/bottom.
     * The bottom is considered before the Content-Length and Content-Type headers
     *
     * @param str
     * @param top
     */
    protected void addHeaders(String str, boolean top) {
        int i, aux;
        if (top) {
            if (this.hasRequestLine() || this.hasStatusLine()) {
                SipParser par = new SipParser(message);
                par.goToNextHeader();
                i = par.getPos();
            } else {
                i = 0;
            }
        } else {
            SipParser par = new SipParser(message);
            // index the end of headers
            i = par.goToEndOfLastHeader().goToNextLine().getPos();
            par = new SipParser(message);
            // if Content_Length is present, jump before
            aux = par.indexOfHeader(SipHeaders.Content_Length);
            if (aux < i) {
                i = aux;
            }
            // if Content_Type is present, jump before
            aux = par.indexOfHeader(SipHeaders.Content_Type);
            if (aux < i) {
                i = aux;
            }
        }
        String head = message.substring(0, i);
        String tail = message.substring(i);
        String new_message = head.concat(str);
        new_message = new_message.concat(tail);
        message = new_message;
    }

    /**
     * Adds Headers on position <i>index</i> within the Message
     *
     * @param str
     * @param index
     */
    protected void addHeaders(String str, int index) {
        if (index > message.length()) {
            index = message.length();
        }
        message = message.substring(0, index) + str + message.substring(index);
    }

    /**
     * Adds Header before the first header <i>refer_header</i>
     * . <p>If there is no header of such type, it is added at top
     *
     * @param new_header
     * @param refer_header
     */
    public void addHeaderBefore(Header new_header, String refer_header) {
        addHeadersBefore(new_header.toString(), refer_header);
    }

    /**
     * Adds MultipleHeader(s) before the first header <i>refer_header</i>
     * . <p>If there is no header of such type, they are added at top
     *
     * @param mheader
     * @param refer_header
     */
    public void addHeadersBefore(MultipleHeader mheader, String refer_header) {
        addHeadersBefore(mheader.toString(), refer_header);
    }

    /**
     * Adds Headers before the first header <i>refer_header</i>
     * . <p>If there is no header of such type, they are added at top
     *
     * @param str
     * @param refer_header
     */
    protected void addHeadersBefore(String str, String refer_header) {
        if (!hasHeader(refer_header)) {
            addHeaders(str, true);
        } else {
            SipParser par = new SipParser(message);
            par.goTo(refer_header);
            int here = par.getPos();
            message = message.substring(0, here) + str + message.substring(here);
        }
    }

    /**
     * Adds Header after the first header <i>refer_header</i>
     * . <p>If there is no header of such type, it is added at bottom
     *
     * @param new_header
     * @param refer_header
     */
    public void addHeaderAfter(Header new_header, String refer_header) {
        addHeadersAfter(new_header.toString(), refer_header);
    }

    /**
     * Adds MultipleHeader(s) after the first header <i>refer_header</i>
     * . <p>If there is no header of such type, they are added at bottom
     *
     * @param mheader
     * @param refer_header
     */
    public void addHeadersAfter(MultipleHeader mheader, String refer_header) {
        addHeadersAfter(mheader.toString(), refer_header);
    }

    /**
     * Adds Headers after the first header <i>refer_header</i>
     * . <p>If there is no header of such type, they are added at bottom
     *
     * @param str
     * @param refer_header
     */
    protected void addHeadersAfter(String str, String refer_header) {
        if (!hasHeader(refer_header)) {
            addHeaders(str, false);
        } else {
            SipParser par = new SipParser(message);
            par.goTo(refer_header);
            int here = par.indexOfNextHeader();
            message = message.substring(0, here) + str + message.substring(here);
        }
    }

    /**
     * Removes first Header of specified name
     *
     * @param hname
     */
    public void removeHeader(String hname) {
        removeHeader(hname, true);
    }

    /**
     * Removes first (or last) Header of specified name
     *
     * @param hname
     * @param first
     */
    public void removeHeader(String hname, boolean first) {
        String[] target = {'\n' + hname, '\r' + hname};
        SipParser par = new SipParser(message);
        par.goTo(target);
        if (!par.hasMore()) {
            return;
        }
        if (!first) {
            while (true) {
                int next = par.indexOf(target);
                if (next < 0) {
                    break;
                }
                par.setPos(next);
            }
        }
        par.skipChar();
        String head = message.substring(0, par.getPos());
        par.goToNextHeader();
        String tail = message.substring(par.getPos());
        message = head.concat(tail);
    }

    /** Sets the new Header (removing any previous headers of the same name) */
    /*public void setHeader(Header hd)
    {  if (hasHeader(hd.getName())) removeAllHeaders(hd.getName());
       addHeader(hd,false);
    }*/

    /**
     * Sets the Header <i>hd</i> removing any previous headers of the same type
     *
     * @param hd
     */
    public void setHeader(Header hd) {
        if (hd != null) {
            String hname = hd.getName();
            if (hasHeader(hname)) {
                int index = (new SipParser(message)).indexOfHeader(hname);
                removeAllHeaders(hname);
                addHeaders(hd.toString(), index);
            } else {
                addHeader(hd, false);
            }
        } else {
            log.warn("Invalid Header Received.");
        }
    }

    /**
     * Removes all Headers of specified name
     *
     * @param hname
     */
    public void removeAllHeaders(String hname) {
        String[] target = {'\n' + hname, '\r' + hname};
        SipParser par = new SipParser(message);
        par.goTo(target);
        while (par.hasMore()) {
            par.skipChar();
            String head = message.substring(0, par.getPos());
            String tail = message.substring(par.indexOfNextHeader());
            message = head.concat(tail);
            par = new SipParser(message, par.getPos() - 1);
            par.goTo(target);
        }
    }

    /** Sets MultipleHeader <i>mheader</i> */
    /*public void setHeaders(MultipleHeader mheader)
    {  if (hasHeader(mheader.getName())) removeAllHeaders(mheader.getName());
       addHeaders(mheader,false);
    }*/

    /**
     * Sets MultipleHeader <i>mheader</i>
     *
     * @param mheader
     */
    public void setHeaders(MultipleHeader mheader) {
        String hname = mheader.getName();
        if (hasHeader(hname)) {
            int index = (new SipParser(message)).indexOfHeader(hname);
            removeAllHeaders(hname);
            addHeaders(mheader.toString(), index);
        } else {
            addHeaders(mheader, false);
        }
    }

    //**************************** Specific Headers ****************************/

    /**
     * Whether Message has MaxForwardsHeader
     *
     * @return
     */
    public boolean hasMaxForwardsHeader() {
        return hasHeader(SipHeaders.Max_Forwards);
    }

    /**
     * Gets MaxForwardsHeader of Message
     *
     * @return
     */
    public MaxForwardsHeader getMaxForwardsHeader() {
        Header h = getHeader(SipHeaders.Max_Forwards);
        if (h == null) {
            return null;
        } else {
            return new MaxForwardsHeader(h);
        }
    }

    /**
     * Sets MaxForwardsHeader of Message
     *
     * @param mfh
     */
    public void setMaxForwardsHeader(MaxForwardsHeader mfh) {
        setHeader(mfh);
    }

    /**
     * Removes MaxForwardsHeader from Message
     */
    public void removeMaxForwardsHeader() {
        removeHeader(SipHeaders.Max_Forwards);
    }


    /**
     * Whether Message has FromHeader
     *
     * @return
     */
    public boolean hasFromHeader() {
        return hasHeader(SipHeaders.From) || hasHeader(SipHeaders.From_short);
    }

    /**
     * Gets FromHeader of Message
     *
     * @return
     */
    public FromHeader getFromHeader() {
        Header h = getHeader(SipHeaders.From);
        if (h == null) {
            h = getHeader(SipHeaders.From_short);
        }

        if (h == null) {
            return null;
        } else {
            return new FromHeader(h);
        }
    }

    /**
     * Sets FromHeader of Message
     *
     * @param fh
     */
    public void setFromHeader(FromHeader fh) {
        setHeader(fh);
    }

    /**
     * Removes FromHeader from Message
     */
    public void removeFromHeader() {
        removeHeader(SipHeaders.From);
        removeHeader(SipHeaders.From_short);
    }

    /**
     * Whether Message has ToHeader
     *
     * @return
     */
    public boolean hasToHeader() {
        return hasHeader(SipHeaders.To) || hasHeader(SipHeaders.To_short);
    }

    /**
     * Gets ToHeader of Message
     *
     * @return
     */
    public ToHeader getToHeader() {
        Header h = getHeader(SipHeaders.To);
        if (h == null) {
            h = getHeader(SipHeaders.To_short);
        }
        if (h == null) {
            return null;
        } else {
            return new ToHeader(h);
        }
    }

    /**
     * Sets ToHeader of Message
     *
     * @param th
     */
    public void setToHeader(ToHeader th) {
        setHeader(th);
    }

    /**
     * Removes ToHeader from Message
     */
    public void removeToHeader() {
        removeHeader(SipHeaders.To);
        removeHeader(SipHeaders.To_short);
    }


    /**
     * Whether Message has ContactHeader
     *
     * @return
     */
    public boolean hasContactHeader() {
        return hasHeader(SipHeaders.Contact) || hasHeader(SipHeaders.Contact_short);
    }

    /**
     * <b>Deprecated</b>. Gets ContactHeader of Message. Use getContacts instead.
     *
     * @return
     */
    public ContactHeader getContactHeader() {  //Header h=getHeader(SipHeaders.Contact);
        //if (h==null) return null; else return new ContactHeader(h);
        MultipleHeader mh = getContacts();
        if (mh == null) {
            return null;
        }
        return new ContactHeader(mh.getTop());
    }

    /**
     * Adds ContactHeader
     *
     * @param ch
     * @param top
     */
    public void addContactHeader(ContactHeader ch, boolean top) {
        addHeader(ch, top);
    }

    /**
     * Sets ContactHeader
     *
     * @param ch
     */
    public void setContactHeader(ContactHeader ch) {
        if (hasContactHeader()) {
            removeContacts();
        }
        addHeader(ch, false);
    }

    /**
     * Gets a MultipleHeader of Contacts
     *
     * @return
     */
    public MultipleHeader getContacts() {
        List v = getHeaders(SipHeaders.Contact);
        if (v.size() == 0) {
            v = getHeaders(SipHeaders.Contact_short);
        }
        if (v.size() > 0) {
            return new MultipleHeader(v);
        } else {
            return null;
        }
    }

    /**
     * Adds Contacts
     *
     * @param contacts
     * @param top
     */
    public void addContacts(MultipleHeader contacts, boolean top) {
        addHeaders(contacts, top);
    }

    /**
     * Sets Contacts
     *
     * @param contacts
     */
    public void setContacts(MultipleHeader contacts) {
        if (hasContactHeader()) {
            removeContacts();
        }
        addContacts(contacts, false);
    }

    /**
     * Removes ContactHeaders from Message
     */
    public void removeContacts() {
        removeAllHeaders(SipHeaders.Contact);
        removeAllHeaders(SipHeaders.Contact_short);
    }


    /**
     * Whether Message has ViaHeaders
     *
     * @return
     */
    public boolean hasViaHeader() {
        return hasHeader(SipHeaders.Via) || hasHeader(SipHeaders.Via_short);
    }

    /**
     * Adds ViaHeader at the top
     *
     * @param vh
     */
    public void addViaHeader(ViaHeader vh) {
        addHeader(vh, true);
    }

    /**
     * Gets the first ViaHeader
     *
     * @return
     */
    public ViaHeader getViaHeader() {  //Header h=getHeader(SipHeaders.Via);
        //if (h==null) return null; else return new ViaHeader(h);
        MultipleHeader mh = getVias();
        if (mh == null) {
            return null;
        }
        return new ViaHeader(mh.getTop());
    }

    /**
     * Removes the top ViaHeader
     */
    public void removeViaHeader() {  //removeHeader(SipHeaders.Via);
        MultipleHeader mh = getVias();
        mh.removeTop();
        setVias(mh);
    }

    /**
     * Gets all Vias
     *
     * @return
     */
    public MultipleHeader getVias() {
        List v = getHeaders(SipHeaders.Via);

        if (v.size() == 0) {
            v = getHeaders(SipHeaders.Via_short);
        }

        if (v.size() > 0) {
            return new MultipleHeader(v);
        } else {
            return null;
        }
    }

    /**
     * Adds Vias
     *
     * @param vias
     * @param top
     */
    public void addVias(MultipleHeader vias, boolean top) {
        addHeaders(vias, top);
    }

    /**
     * Sets Vias
     *
     * @param vias
     */
    public void setVias(MultipleHeader vias) {
        if (hasViaHeader()) {
            removeVias();
        }
        addContacts(vias, true);
    }

    /**
     * Removes ViaHeaders from Message (if any exists)
     */
    public void removeVias() {
        removeAllHeaders(SipHeaders.Via);
        removeAllHeaders(SipHeaders.Via_short);
    }


    /**
     * Whether Message has RouteHeader
     *
     * @return
     */
    public boolean hasRouteHeader() {
        return hasHeader(SipHeaders.Route);
    }

    /**
     * Adds RouteHeader at the top
     *
     * @param h
     */
    public void addRouteHeader(RouteHeader h) {
        addHeaderAfter(h, SipHeaders.Via);
    }

    /**
     * Adds multiple Route headers at the top
     *
     * @param routes
     */
    public void addRoutes(MultipleHeader routes) {
        addHeadersAfter(routes, SipHeaders.Via);
    }

    /**
     * Gets the top RouteHeader
     *
     * @return
     */
    public RouteHeader getRouteHeader() {  //Header h=getHeader(SipHeaders.Route);
        //if (h==null) return null; else return new RouteHeader(h);
        MultipleHeader mh = getRoutes();
        if (mh == null) {
            return null;
        }
        return new RouteHeader(mh.getTop());
    }

    /**
     * Gets the whole route
     *
     * @return
     */
    public MultipleHeader getRoutes() {
        List v = getHeaders(SipHeaders.Route);
        if (v.size() > 0) {
            return new MultipleHeader(v);
        } else {
            return null;
        }
    }

    /**
     * Removes the top RouteHeader
     */
    public void removeRouteHeader() {  //removeHeader(SipHeaders.Route);
        MultipleHeader mh = getRoutes();
        mh.removeTop();
        setRoutes(mh);
    }

    /**
     * Removes all RouteHeaders from Message (if any exists)
     */
    public void removeRoutes() {
        removeAllHeaders(SipHeaders.Route);
    }

    /**
     * Sets the whole route
     *
     * @param routes
     */
    public void setRoutes(MultipleHeader routes) {
        if (hasRouteHeader()) {
            removeRoutes();
        }
        addRoutes(routes);
    }


    /**
     * Whether Message has RecordRouteHeader
     *
     * @return
     */
    public boolean hasRecordRouteHeader() {
        return hasHeader(SipHeaders.Record_Route);
    }

    /**
     * Adds RecordRouteHeader at the top
     *
     * @param rr
     */
    public void addRecordRouteHeader(RecordRouteHeader rr) {  //addHeaderAfter(rr,SipHeaders.Via);
        addHeaderAfter(rr, SipHeaders.CSeq);
    }

    /**
     * Adds multiple RecordRoute headers at the top
     *
     * @param routes
     */
    public void addRecordRoutes(MultipleHeader routes) {  //addHeadersAfter(routes,SipHeaders.Via);
        addHeadersAfter(routes, SipHeaders.CSeq);
    }

    /**
     * Gets the top RecordRouteHeader
     *
     * @return
     */
    public RecordRouteHeader getRecordRouteHeader() {  //Header h=getHeader(SipHeaders.Record_Route);
        //if (h==null) return null; else return new RecordRouteHeader(h);
        MultipleHeader mh = getRecordRoutes();
        if (mh == null) {
            return null;
        }
        return new RecordRouteHeader(mh.getTop());
    }

    /**
     * Gets the whole RecordRoute headers
     *
     * @return
     */
    public MultipleHeader getRecordRoutes() {
        List v = getHeaders(SipHeaders.Record_Route);
        if (v.size() > 0) {
            return new MultipleHeader(v);
        } else {
            return null;
        }
    }

    /**
     * Removes the top RecordRouteHeader
     */
    public void removeRecordRouteHeader() {  //removeHeader(SipHeaders.Record_Route);
        MultipleHeader mh = getRecordRoutes();
        mh.removeTop();
        setRecordRoutes(mh);
    }

    /**
     * Removes all RecordRouteHeader from Message (if any exists)
     */
    public void removeRecordRoutes() {
        removeAllHeaders(SipHeaders.Record_Route);
    }

    /**
     * Sets the whole RecordRoute headers
     *
     * @param routes
     */
    public void setRecordRoutes(MultipleHeader routes) {
        if (hasRecordRouteHeader()) {
            removeRecordRoutes();
        }
        addRecordRoutes(routes);
    }


    /**
     * Whether Message has CSeqHeader
     *
     * @return
     */
    public boolean hasCSeqHeader() {
        return hasHeader(SipHeaders.CSeq);
    }

    /**
     * Gets CSeqHeader of Message
     *
     * @return
     */
    public CSeqHeader getCSeqHeader() {
        Header h = getHeader(SipHeaders.CSeq);
        if (h == null) {
            return null;
        } else {
            return new CSeqHeader(h);
        }
    }

    /**
     * Sets CSeqHeader of Message
     *
     * @param csh
     */
    public void setCSeqHeader(CSeqHeader csh) {
        if (csh == null) {
            log.warn("Invalid CSeq Header Received.");
            return;
        }
        setHeader(csh);
    }

    /**
     * Removes CSeqHeader from Message
     */
    public void removeCSeqHeader() {
        removeHeader(SipHeaders.CSeq);
    }


    /**
     * Whether has CallIdHeader
     *
     * @return
     */
    public boolean hasCallIdHeader() {
        return hasHeader(SipHeaders.CallID) || hasHeader(SipHeaders.CallID_short);
    }

    /**
     * Sets CallIdHeader of Message
     *
     * @param cih
     */
    public void setCallIdHeader(CallIdHeader cih) {
        if (cih == null) {
            log.warn("Invalid Call-ID Header Received.");
            return;
        }
        setHeader(cih);
    }

    /**
     * Gets CallIdHeader of Message
     *
     * @return
     */
    public CallIdHeader getCallIdHeader() {
        Header h = getHeader(SipHeaders.CallID);
        if (h == null) {
            h = getHeader(SipHeaders.CallID_short);
        }
        if (h == null) {
            return null;
        } else {
            return new CallIdHeader(h);
        }
    }

    /**
     * Removes CallIdHeader from Message
     */
    public void removeCallIdHeader() {
        removeHeader(SipHeaders.CallID);
        removeHeader(SipHeaders.CallID_short);
    }


    /**
     * Whether Message has SubjectHeader
     *
     * @return
     */
    public boolean hasSubjectHeader() {
        return hasHeader(SipHeaders.Subject);
    }

    /**
     * Sets SubjectHeader of Message
     *
     * @param sh
     */
    public void setSubjectHeader(SubjectHeader sh) {
        setHeader(sh);
    }

    /**
     * Gets SubjectHeader of Message
     *
     * @return
     */
    public SubjectHeader getSubjectHeader() {
        Header h = getHeader(SipHeaders.Subject);
        if (h == null) {
            return null;
        } else {
            return new SubjectHeader(h);
        }
    }

    /**
     * Removes SubjectHeader from Message
     */
    public void removeSubjectHeader() {
        removeHeader(SipHeaders.Subject);
    }


    /**
     * Whether Message has DateHeader
     *
     * @return
     */
    public boolean hasDateHeader() {
        return hasHeader(SipHeaders.Date);
    }

    /**
     * Gets DateHeader of Message
     *
     * @return
     */
    public DateHeader getDateHeader() {
        Header h = getHeader(SipHeaders.Date);
        if (h == null) {
            return null;
        } else {
            return new DateHeader(h);
        }
    }

    /**
     * Sets DateHeader of Message
     *
     * @param dh
     */
    public void setDateHeader(DateHeader dh) {
        setHeader(dh);
    }

    /**
     * Removes DateHeader from Message (if it exists)
     */
    public void removeDateHeader() {
        removeHeader(SipHeaders.Date);
    }


    /**
     * Whether has UserAgentHeader
     *
     * @return
     */
    public boolean hasUserAgentHeader() {
        return hasHeader(SipHeaders.User_Agent);
    }

    /**
     * Sets UserAgentHeader
     *
     * @param h
     */
    public void setUserAgentHeader(UserAgentHeader h) {
        setHeader(h);
    }

    /**
     * Gets UserAgentHeader
     *
     * @return
     */
    public UserAgentHeader getUserAgentHeader() {
        Header h = getHeader(SipHeaders.User_Agent);
        if (h == null) {
            return null;
        } else {
            return new UserAgentHeader(h);
        }
    }

    /**
     * Removes UserAgentHeader
     */
    public void removeUserAgentHeader() {
        removeHeader(SipHeaders.User_Agent);
    }


    /**
     * Whether has ServerHeader
     *
     * @return
     */
    public boolean hasServerHeader() {
        return hasHeader(SipHeaders.Server);
    }

    /**
     * Sets ServerHeader
     *
     * @param h
     */
    public void setServerHeader(ServerHeader h) {
        setHeader(h);
    }

    /**
     * Gets ServerHeader
     *
     * @return
     */
    public ServerHeader getServerHeader() {
        Header h = getHeader(SipHeaders.Server);
        if (h == null) {
            return null;
        } else {
            return new ServerHeader(h);
        }
    }

    /**
     * Removes ServerHeader
     */
    public void removeServerHeader() {
        removeHeader(SipHeaders.Server);
    }


    /**
     * Whether has AcceptHeader
     *
     * @return
     */
    public boolean hasAcceptHeader() {
        return hasHeader(SipHeaders.Accept);
    }

    /**
     * Sets AcceptHeader
     *
     * @param h
     */
    public void setAcceptHeader(AcceptHeader h) {
        setHeader(h);
    }

    /**
     * Gets AcceptHeader
     *
     * @return
     */
    public AcceptHeader getAcceptHeader() {
        Header h = getHeader(SipHeaders.Accept);
        if (h == null) {
            return null;
        } else {
            return new AcceptHeader(h);
        }
    }

    /**
     * Removes AcceptHeader
     */
    public void removeAcceptHeader() {
        removeHeader(SipHeaders.Accept);
    }


    /**
     * Whether has AlertInfoHeader
     *
     * @return
     */
    public boolean hasAlertInfoHeader() {
        return hasHeader(SipHeaders.AlertInfo);
    }

    /**
     * Sets AlertInfoHeader
     *
     * @param h
     */
    public void setAlertInfoHeader(AlertInfoHeader h) {
        setHeader(h);
    }

    /**
     * Gets AlertInfoHeader
     *
     * @return
     */
    public AlertInfoHeader getAlertInfoHeader() {
        Header h = getHeader(SipHeaders.AlertInfo);
        if (h == null) {
            return null;
        } else {
            return new AlertInfoHeader(h);
        }
    }

    /**
     * Removes AlertInfoHeader
     */
    public void removeAlertInfoHeader() {
        removeHeader(SipHeaders.AlertInfo);
    }


    /**
     * Whether has AllowHeader
     *
     * @return
     */
    public boolean hasAllowHeader() {
        return hasHeader(SipHeaders.Allow);
    }

    /**
     * Sets AllowHeader
     *
     * @param h
     */
    public void setAllowHeader(AllowHeader h) {
        setHeader(h);
    }

    /**
     * Gets AllowHeader
     *
     * @return
     */
    public AllowHeader getAllowHeader() {
        Header h = getHeader(SipHeaders.Allow);
        if (h == null) {
            return null;
        } else {
            return new AllowHeader(h);
        }
    }

    /**
     * Removes AllowHeader
     */
    public void removeAllowHeader() {
        removeHeader(SipHeaders.Allow);
    }


    /**
     * Whether Message has ExpiresHeader
     *
     * @return
     */
    public boolean hasExpiresHeader() {
        return hasHeader(SipHeaders.Expires);
    }

    /**
     * Gets ExpiresHeader of Message
     *
     * @return
     */
    public ExpiresHeader getExpiresHeader() {
        Header h = getHeader(SipHeaders.Expires);
        if (h == null) {
            return null;
        } else {
            return new ExpiresHeader(h);
        }
    }

    /**
     * Sets ExpiresHeader of Message
     *
     * @param eh
     */
    public void setExpiresHeader(ExpiresHeader eh) {
        setHeader(eh);
    }

    /**
     * Removes ExpiresHeader from Message (if it exists)
     */
    public void removeExpiresHeader() {
        removeHeader(SipHeaders.Expires);
    }


    /**
     * Whether Message has ContentTypeHeader
     *
     * @return
     */
    public boolean hasContentTypeHeader() {
        return hasHeader(SipHeaders.Content_Type) || hasHeader(SipHeaders.Content_Type_short);
    }

    /**
     * Gets ContentTypeHeader of Message
     *
     * @return
     */
    public ContentTypeHeader getContentTypeHeader() {
        Header h = getHeader(SipHeaders.Content_Type);

        if (h == null) {
            h = getHeader(SipHeaders.Content_Type_short);
        }

        if (h == null) {
            return null;
        } else {
            return new ContentTypeHeader(h);
        }
    }

    /**
     * Sets ContentTypeHeader of Message
     *
     * @param cth
     */
    protected void setContentTypeHeader(ContentTypeHeader cth) {
        setHeader(cth);
    }

    /**
     * Removes ContentTypeHeader from Message (if it exists)
     */
    protected void removeContentTypeHeader() {
        removeHeader(SipHeaders.Content_Type);
        removeHeader(SipHeaders.Content_Type_short);
    }


    /**
     * Whether Message has ContentLengthHeader
     *
     * @return
     */
    public boolean hasContentLengthHeader() {
        return hasHeader(SipHeaders.Content_Length) || hasHeader(SipHeaders.Content_Length_short);
    }

    /**
     * Gets ContentLengthHeader of Message
     *
     * @return
     */
    public ContentLengthHeader getContentLengthHeader() {
        Header h = getHeader(SipHeaders.Content_Length);
        if (h == null) {
            h = getHeader(SipHeaders.Content_Length_short);
        }
        if (h == null) {
            return null;
        } else {
            return new ContentLengthHeader(h);
        }
    }

    /**
     * Sets ContentLengthHeader of Message
     *
     * @param clh
     */
    protected void setContentLengthHeader(ContentLengthHeader clh) {
        setHeader(clh);
    }

    /**
     * Removes ContentLengthHeader from Message (if it exists)
     */
    protected void removeContentLengthHeader() {
        removeHeader(SipHeaders.Content_Length);
        removeHeader(SipHeaders.Content_Length_short);
    }


    /**
     * Whether Message has Body
     *
     * @return
     */
    public boolean hasBody() {
        if (hasContentLengthHeader()) {
            return getContentLengthHeader().getContentLength() > 0;
        } else {
            return hasContentTypeHeader();
        }
    }

    /**
     * Gets body(content) type
     *
     * @return
     */
    public String getBodyType() {
        return getContentTypeHeader().getContentType();
    }

    /**
     * Sets the message body
     *
     * @param content_type
     * @param body
     */
    public void setBody(String content_type, String body) {
        removeBody();
        if (body != null && body.length() > 0) {
            setContentTypeHeader(new ContentTypeHeader(content_type));
            setContentLengthHeader(new ContentLengthHeader(body.length()));
            message = message + "\r\n" + body;
        } else {
            setContentLengthHeader(new ContentLengthHeader(0));
            message = message + "\r\n";
        }
    }

    /**
     * Sets sdp body
     *
     * @param body
     */
    public void setBody(String body) {
        setBody("application/sdp", body);
    }

    /**
     * Gets message body. The end of body is evaluated
     * from the Content-Length header if present (SIP-RFC compliant),
     * or from the end of message if no Content-Length header is present (non-SIP-RFC compliant)
     *
     * @return
     */
    public String getBody() {  //if (!hasBody()) return "";
        if (!hasBody()) {
            return null;
        }
        int begin = (new SipParser(message)).goToBody().getPos();
        int len;
        // the following 'if' is for robustness with non SIP-compliant UAs;
        // copliant UAs must insert Content-Length header when body is present..
        if (this.hasContentLengthHeader()) {
            len = getContentLengthHeader().getContentLength();
        } else {  //printWarning("No Content-Length header found for the Body",3);
            len = message.length() - begin;
        }
        int end = begin + len;
        if (end > message.length()) {  //printWarning("Found a Message Body shorter than Content-Length",3);
            end = message.length();
        }
        return message.substring(begin, end);
    }

    /**
     * Removes the message body (if it exists) and the final empty line
     */
    public void removeBody() {
        int pos = (new SipParser(message)).goToEndOfLastHeader().goToNextLine().getPos();
        message = message.substring(0, pos);
        removeContentLengthHeader();
        removeContentTypeHeader();
    }

    //**************************** Authentication ****************************/


    /**
     * Whether has AuthenticationInfoHeader
     *
     * @return
     */
    public boolean hasAuthenticationInfoHeader() {
        return hasHeader(SipHeaders.AuthenticationInfo);
    }

    /**
     * Sets AuthenticationInfoHeader
     *
     * @param h
     */
    public void setAuthenticationInfoHeader(AuthenticationInfoHeader h) {
        setHeader(h);
    }

    /**
     * Gets AuthenticationInfoHeader
     *
     * @return
     */
    public AuthenticationInfoHeader getAuthenticationInfoHeader() {
        Header h = getHeader(SipHeaders.AuthenticationInfo);
        if (h == null) {
            return null;
        } else {
            return new AuthenticationInfoHeader(h);
        }
    }

    /**
     * Removes AuthenticationInfoHeader
     */
    public void removeAuthenticationInfoHeader() {
        removeHeader(SipHeaders.AuthenticationInfo);
    }


    /**
     * Whether has AuthorizationHeader
     *
     * @return
     */
    public boolean hasAuthorizationHeader() {
        return hasHeader(SipHeaders.Authorization);
    }

    /**
     * Sets AuthorizationHeader
     *
     * @param h
     */
    public void setAuthorizationHeader(AuthorizationHeader h) {
        setHeader(h);
    }

    /**
     * Gets AuthorizationHeader
     *
     * @return
     */
    public AuthorizationHeader getAuthorizationHeader() {
        Header h = getHeader(SipHeaders.Authorization);
        if (h == null) {
            return null;
        } else {
            return new AuthorizationHeader(h);
        }
    }

    /**
     * Removes AuthorizationHeader
     */
    public void removeAuthorizationHeader() {
        removeHeader(SipHeaders.Authorization);
    }


    /**
     * Whether has WwwAuthenticateHeader
     *
     * @return
     */
    public boolean hasWwwAuthenticateHeader() {
        return hasHeader(SipHeaders.WWW_Authenticate);
    }

    /**
     * Sets WwwAuthenticateHeader
     *
     * @param h
     */
    public void setWwwAuthenticateHeader(WwwAuthenticateHeader h) {
        setHeader(h);
    }

    /**
     * Gets WwwAuthenticateHeader
     *
     * @return
     */
    public WwwAuthenticateHeader getWwwAuthenticateHeader() {
        Header h = getHeader(SipHeaders.WWW_Authenticate);
        if (h == null) {
            return null;
        } else {
            return new WwwAuthenticateHeader(h);
        }
    }

    /**
     * Removes WwwAuthenticateHeader
     */
    public void removeWwwAuthenticateHeader() {
        removeHeader(SipHeaders.WWW_Authenticate);
    }


    /**
     * Whether has ProxyAuthenticateHeader
     *
     * @return
     */
    public boolean hasProxyAuthenticateHeader() {
        return hasHeader(SipHeaders.Proxy_Authenticate);
    }

    /**
     * Sets ProxyAuthenticateHeader
     *
     * @param h
     */
    public void setProxyAuthenticateHeader(ProxyAuthenticateHeader h) {
        setHeader(h);
    }

    /**
     * Gets ProxyAuthenticateHeader
     *
     * @return
     */
    public ProxyAuthenticateHeader getProxyAuthenticateHeader() {
        Header h = getHeader(SipHeaders.Proxy_Authenticate);
        if (h == null) {
            return null;
        } else {
            return new ProxyAuthenticateHeader(h);
        }
    }

    /**
     * Removes ProxyAuthenticateHeader
     */
    public void removeProxyAuthenticateHeader() {
        removeHeader(SipHeaders.Proxy_Authenticate);
    }


    /**
     * Whether has ProxyAuthorizationHeader
     *
     * @return
     */
    public boolean hasProxyAuthorizationHeader() {
        return hasHeader(SipHeaders.Proxy_Authorization);
    }

    /**
     * Sets ProxyAuthorizationHeader
     *
     * @param h
     */
    public void setProxyAuthorizationHeader(ProxyAuthorizationHeader h) {
        setHeader(h);
    }

    /**
     * Gets ProxyAuthorizationHeader
     *
     * @return
     */
    public ProxyAuthorizationHeader getProxyAuthorizationHeader() {
        Header h = getHeader(SipHeaders.Proxy_Authorization);
        if (h == null) {
            return null;
        } else {
            return new ProxyAuthorizationHeader(h);
        }
    }

    /**
     * Removes ProxyAuthorizationHeader
     */
    public void removeProxyAuthorizationHeader() {
        removeHeader(SipHeaders.Proxy_Authorization);
    }

    //**************************** RFC 2543 Legacy ****************************/


    /**
     * Checks whether the next Route is formed according to RFC2543 Strict Route
     * and adapts the message.
     */
    public void rfc2543RouteAdapt() {
        if (hasRouteHeader()) {
            MultipleHeader mrh = getRoutes();
            if (!(new RouteHeader(mrh.getTop())).getNameAddress().getAddress().hasLr()) {  // re-format the message according to the RFC2543 Strict Route rule
                SipURL next_hop = (new RouteHeader(mrh.getTop())).getNameAddress().getAddress();
                SipURL recipient = getRequestLine().getAddress();
                mrh.removeTop();
                mrh.addBottom(new RouteHeader(new NameAddress(recipient)));
                setRoutes(mrh);
                setRequestLine(new RequestLine(getRequestLine().getMethod(), next_hop));
            }
        }
    }


    /**
     * Changes form RFC2543 Strict Route to RFC3261 Lose Route.
     * <p> The Request-URI is replaced with the last
     * value from the Route header, and that value is removed from the
     * Route header.
     */
    public void rfc2543toRfc3261RouteUpdate() {  // the message is formed according with RFC2543 strict route
        // the next hop is the request-uri
        // the recipient of the message is the last Route value
        RequestLine request_line = getRequestLine();
        SipURL next_hop = request_line.getAddress();
        MultipleHeader mrh = getRoutes();
        SipURL target = (new RouteHeader(mrh.getBottom())).getNameAddress().getAddress();
        mrh.removeBottom();
        next_hop.addLr();
        mrh.addTop(new RouteHeader(new NameAddress(next_hop)));
        removeRoutes();
        addRoutes(mrh);
        setRequestLine(new RequestLine(request_line.getMethod(), target));
    }

}
