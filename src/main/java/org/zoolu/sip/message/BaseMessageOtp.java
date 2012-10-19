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
import org.zoolu.sip.provider.SipParser;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;


/**
 * Class BaseMessageOtp implements a generic SIP Message.
 * It extends class BaseMessage adding one-time-parsing functionality
 * (it parses the entire Message just when it is costructed).
 * <p/> At the contrary, class BaseMessage works in a just-in-time manner
 * (it parses the message each time a particular header field is requested).
 */
public abstract class BaseMessageOtp extends BaseMessage {

    private RequestLine request_line;
    private StatusLine status_line;

    private List<Header> headers;
    private String body;


    /**
     * Inits empty Message
     */
    private void init() {
        request_line = null;
        status_line = null;
        headers = null;
        body = null;
    }

    /**
     * Costructs a new empty Message
     */
    public BaseMessageOtp() {
        init();
        headers = new ArrayList<Header>();
    }

    /**
     * Costructs a new Message
     *
     * @param data
     * @param offset
     * @param len
     */
    public BaseMessageOtp(byte[] data, int offset, int len) {
        init();
        parseIt(new String(data, offset, len));
    }

    /**
     * Costructs a new Message
     *
     * @param packet
     */
    public BaseMessageOtp(DatagramPacket packet) {
        init();
        parseIt(new String(packet.getData(), packet.getOffset(), packet.getLength()));
    }

    /**
     * Costructs a new Message
     *
     * @param str
     */
    public BaseMessageOtp(String str) {
        init();
        parseIt(str);
    }

    /**
     * Costructs a new Message
     *
     * @param msg
     */
    public BaseMessageOtp(BaseMessageOtp msg) {
        init();
        remoteAddr = msg.remoteAddr;
        remotePort = msg.remotePort;
        transportProto = msg.transportProto;
        //packet_length=msg.packet_length;
        request_line = msg.request_line;
        status_line = msg.status_line;
        headers = new ArrayList();
        for (int i = 0; i < msg.headers.size(); i++) headers.add(msg.headers.get(i));
        body = msg.body;
    }

    /**
     * Sets the entire message
     */
    public void setMessage(String str) {
        parseIt(str);
    }

    /**
     * Parses the Message from a String.
     *
     * @param str
     */
    private void parseIt(final String str) {
        SipParser par = new SipParser(str);
        final String version = par.getRemainingString().length() > 4 ? par.getRemainingString().substring(0, 4) : "q";
        if (version.equalsIgnoreCase("SIP/")) {
            status_line = par.getStatusLine();
        } else {
            request_line = par.getRequestLine();
        }

        headers = new ArrayList();
        Header h = par.getHeader();
        while (h != null) {
            headers.add(h);
            h = par.getHeader();
        }
        ContentLengthHeader clh = getContentLengthHeader();
        if (clh != null) {
            int len = clh.getContentLength() + 100; // TODO Remove Patch for Buggy SIP Servers
            body = par.getString(len);
        } else if (getContentTypeHeader() != null) {
            body = par.getRemainingString();
            if (body.length() == 0) {
                body = null;
            }
        }
    }

    /**
     * Gets string representation of Message
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (request_line != null) {
            str.append(request_line.toString());
        } else if (status_line != null) {
            str.append(status_line.toString());
        }
        for (Object header : headers) str.append(header.toString());
        str.append("\r\n");
        if (body != null) {
            str.append(body);
        }
        str.append("\r\n");
        return str.toString();
    }

    /**
     * Gets message length
     */
    public int getLength() {
        return toString().length();
    }


    //**************************** Requests ****************************/

    /**
     * Whether Message is a Request
     */
    public boolean isRequest() {
        return request_line != null;
    }

    /**
     * Whether Message is a <i>method</i> request
     */
    public boolean isRequest(String method) {
        return request_line != null && request_line.getMethod().equalsIgnoreCase(method);
    }


    /**
     * Whether Message has Request-line
     */
    protected boolean hasRequestLine() {
        return request_line != null;
    }

    /**
     * Gets RequestLine in Message (Returns null if called for no request message)
     */
    public RequestLine getRequestLine() {
        return request_line;
    }

    /**
     * Sets RequestLine of the Message
     */
    public void setRequestLine(RequestLine rl) {
        request_line = rl;
    }

    /**
     * Removes RequestLine of the Message
     */
    public void removeRequestLine() {
        request_line = null;
    }


    //**************************** Responses ****************************/

    /**
     * Whether Message is a Response
     */
    public boolean isResponse() throws NullPointerException {
        return status_line != null;
    }

    /**
     * Whether Message has Status-line
     */
    protected boolean hasStatusLine() {
        return status_line != null;
    }

    /**
     * Gets StautsLine in Message (Returns null if called for no response message)
     */
    public StatusLine getStatusLine() {
        return status_line;
    }

    /**
     * Sets StatusLine of the Message
     */
    public void setStatusLine(StatusLine sl) {
        status_line = sl;
    }

    /**
     * Removes StatusLine of the Message
     */
    public void removeStatusLine() {
        status_line = null;
    }


    //**************************** Generic Headers ****************************/

    /**
     * Removes Request\Status Line of the Message
     */
    protected void removeFirstLine() {
        removeRequestLine();
        removeStatusLine();
    }

    /**
     * Gets the position of header <i>hname</i>.
     *
     * @param hname
     * @return
     */
    protected int indexOfHeader(String hname) {
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.get(i);
            if (hname.equalsIgnoreCase(h.getName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the first Header of specified name (Returns null if no Header is found)
     */
    public Header getHeader(String hname) {
        int i = indexOfHeader(hname);
        if (i < 0) {
            return null;
        } else {
            return (Header) headers.get(i);
        }
    }

    /**
     * Gets a List of all Headers of specified name (Returns empty List if no Header is found)
     */
    public List getHeaders(String hname) {
        List v = new ArrayList();
        for (Object header : headers) {
            Header h = (Header) header;
            if (hname.equalsIgnoreCase(h.getName())) {
                v.add(h);
            }
        }
        return v;
    }

    /**
     * Adds Header at the top/bottom.
     * The bottom is considered before the Content-Length and Content-Type headers
     */
    public void addHeader(Header header, boolean top) {
        if (top) {
            headers.add(0, header);
        } else {
            headers.add(header);
        }
    }

    /**
     * Adds a List of Headers at the top/bottom
     */
    public void addHeaders(List<Header> headers, boolean top) {
        for (int i = 0; i < headers.size(); i++)
            if (top) {
                this.headers.add(i, headers.get(i));
            } else {
                this.headers.add(headers.get(i));
            }
    }

    /**
     * Adds MultipleHeader(s) <i>mheader</i> at the top/bottom
     */
    public void addHeaders(MultipleHeader mheader, boolean top) {
        if (mheader.isCommaSeparated()) {
            addHeader(mheader.toHeader(), top);
        } else {
            addHeaders(mheader.getHeaders(), top);
        }
    }

    /**
     * Adds Header before the first header <i>refer_hname</i>
     * . <p>If there is no header of such type, it is added at top
     */
    public void addHeaderBefore(Header new_header, String refer_hname) {
        int i = indexOfHeader(refer_hname);
        if (i < 0) {
            i = 0;
        }
        headers.add(i, new_header);
    }

    /**
     * Adds MultipleHeader(s) before the first header <i>refer_hname</i>
     * . <p>If there is no header of such type, they are added at top
     */
    public void addHeadersBefore(MultipleHeader mheader, String refer_hname) {
        if (mheader.isCommaSeparated()) {
            addHeaderBefore(mheader.toHeader(), refer_hname);
        } else {
            int index = indexOfHeader(refer_hname);
            if (index < 0) {
                index = 0;
            }
            List<Header> hs = mheader.getHeaders();
            for (int k = 0; k < hs.size(); k++) headers.add(index + k, hs.get(k));
        }
    }

    /**
     * Adds Header after the first header <i>refer_hname</i>
     * . <p>If there is no header of such type, it is added at bottom
     */
    public void addHeaderAfter(Header new_header, String refer_hname) {
        int i = indexOfHeader(refer_hname);
        if (i >= 0) {
            i++;
        } else {
            i = headers.size();
        }
        headers.add(i, new_header);
    }

    /**
     * Adds MultipleHeader(s) after the first header <i>refer_hname</i>
     * . <p>If there is no header of such type, they are added at bottom
     */
    public void addHeadersAfter(MultipleHeader mheader, String refer_hname) {
        if (mheader.isCommaSeparated()) {
            addHeaderAfter(mheader.toHeader(), refer_hname);
        } else {
            int index = indexOfHeader(refer_hname);
            if (index >= 0) {
                index++;
            } else {
                index = headers.size();
            }
            List<Header> hs = mheader.getHeaders();
            for (int k = 0; k < hs.size(); k++) headers.add(index + k, hs.get(k));
        }
    }

    /**
     * Removes first Header of specified name
     */
    public void removeHeader(String hname) {
        removeHeader(hname, true);
    }

    /**
     * Removes first (or last) Header of specified name.
     */
    public void removeHeader(String hname, boolean first) {
        int index = -1;
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.get(i);
            if (hname.equalsIgnoreCase(h.getName())) {
                index = i;
                if (first) {
                    i = headers.size();
                }
            }
        }
        if (index >= 0) {
            headers.remove(index);
        }
    }

    /**
     * Removes all Headers of specified name
     */
    public void removeAllHeaders(String hname) {
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.get(i);
            if (hname.equalsIgnoreCase(h.getName())) {
                headers.remove(i);
                i--;
            }
        }
    }

    /**
     * Sets the Header <i>hd</i> removing any previous headers of the same type.
     */
    public void setHeader(Header hd) {
        boolean first = true;
        String hname = hd.getName();
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.get(i);
            if (hname.equalsIgnoreCase(h.getName())) {
                if (first) {  // replace it
                    headers.remove(i);
                    headers.add(i, hd);
                    first = false;
                } else {  // remove it
                    headers.remove(i);
                    i--;
                }
            }
        }
        if (first) {
            headers.add(hd);
        }
    }

    /**
     * Sets MultipleHeader <i>mheader</i>
     */
    public void setHeaders(MultipleHeader mheader) {
        if (mheader.isCommaSeparated()) {
            setHeader(mheader.toHeader());
        } else {
            boolean first = true;
            String hname = mheader.getName();
            for (int i = 0; i < headers.size(); i++) {
                Header h = (Header) headers.get(i);
                if (hname.equalsIgnoreCase(h.getName())) {
                    if (first) {  // replace it
                        List<Header> hs = mheader.getHeaders();
                        for (int k = 0; k < hs.size(); k++) headers.add(i + k, hs.get(k));
                        first = false;
                        i += hs.size() - 1;
                    } else {  // remove it
                        headers.remove(i);
                        i--;
                    }
                }
            }
        }
    }


    //**************************** Specific Headers ****************************/

    /**
     * Whether Message has Body
     */
    public boolean hasBody() {
        return this.body != null;
    }

    /**
     * Gets body(content) type
     */
    public String getBodyType() {
        return getContentTypeHeader().getContentType();
    }

    /**
     * Sets the message body
     */
    public void setBody(String content_type, String body) {
        removeBody();
        if (body != null && body.length() > 0) {
            body = body.trim();
            setContentTypeHeader(new ContentTypeHeader(content_type));
            setContentLengthHeader(new ContentLengthHeader(body.length()));
            this.body = body;
        } else {
            setContentLengthHeader(new ContentLengthHeader(0));
            this.body = null;
        }
    }

    /**
     * Gets message body. The end of body is evaluated
     * from the Content-Length header if present (SIP-RFC compliant),
     * or from the end of message if no Content-Length header is present (non-SIP-RFC compliant)
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Removes the message body (if it exists) and the final empty line
     */
    public void removeBody() {
        removeContentLengthHeader();
        removeContentTypeHeader();
        this.body = null;
    }

}
