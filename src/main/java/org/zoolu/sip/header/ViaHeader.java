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


import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.provider.SipParser;


/**
 * SIP Header Via.
 * The Via header field indicates the transport used for the transaction
 * and identifies the location where the response is to be sent.
 * <BR> When the UAC creates a request, it MUST insert a Via into that
 * request.  The protocol name and protocol version in the header field
 * is SIP and 2.0, respectively.
 * <BR> The Via header field value MUST
 * contain a branch parameter.  This parameter is used to identify the
 * transaction created by that request.  This parameter is used by both
 * the client and the server.
 * <BR> The branch parameter value MUST be unique across space and time for
 * all requests sent by the UA.  The exceptions to this rule are CANCEL
 * and ACK for non-2xx responses.  A CANCEL request
 * will have the same value of the branch parameter as the request it
 * cancels.  An ACK for a non-2xx
 * response will also have the same branch ID as the INVITE whose
 * response it acknowledges.
 */
public class ViaHeader extends ParametricHeader {
    protected static final String receivedParam = "received";
    protected static final String rportParam = "rport";
    protected static final String branchParam = "branch";
    protected static final String maddrParam = "maddr";
    protected static final String ttlParam = "ttl";

    //public ViaHeader()
    //{  super(SipHeaders.Via);
    //}

    public ViaHeader(String hvalue) {
        super(SipHeaders.Via, hvalue);
    }

    public ViaHeader(Header hd) {
        super(hd);
    }

    public ViaHeader(String host, int port) {
        super(SipHeaders.Via, "SIP/2.0/UDP " + host + ":" + port);
    }

    /*public ViaHeader(String host, int port, String branch)
    {  super(SipHeaders.Via,"SIP/2.0/UDP "+host+":"+port+";branch="+branch);
    }*/

    public ViaHeader(String proto, String host, int port) {
        super(SipHeaders.Via, "SIP/2.0/" + proto.toUpperCase() + " " + host + ":" + port);
    }

    /*public ViaHeader(String proto, String host, int port, String branch)
    {  super(SipHeaders.Via,"SIP/2.0/"+proto.toUpperCase()+" "+host+":"+port+";branch="+branch);
    }*/

    /**
     * Gets the transport protocol
     *
     * @return
     */
    public String getProtocol() {
        SipParser par = new SipParser(value);
        return par.goTo('/').skipChar().goTo('/').skipChar().skipWSP().getString();
    }

    /**
     * Gets "sent-by" parameter
     *
     * @return
     */
    public String getSentBy() {
        SipParser par = new SipParser(value);
        par.goTo('/').skipChar().goTo('/').skipString().skipWSP();
        if (!par.hasMore()) {
            return null;
        }
        String sentby = value.substring(par.getPos(), par.indexOfSeparator());
        return sentby;
    }

    /**
     * Gets host of ViaHeader
     *
     * @return
     */
    public String getHost() {
        String sentby = getSentBy();
        SipParser par = new SipParser(sentby);
        par.goTo(':');
        if (par.hasMore()) {
            return sentby.substring(0, par.getPos());
        } else {
            return sentby;
        }
    }

    /**
     * Returns boolean value indicating if ViaHeader has port
     *
     * @return
     */
    public boolean hasPort() {
        String sentby = getSentBy();
        return sentby.indexOf(':') > 0;
    }

    /**
     * Gets port of ViaHeader
     *
     * @return
     */
    public int getPort() {
        SipParser par = new SipParser(getSentBy());
        par.goTo(':');
        if (par.hasMore()) {
            return par.skipChar().getInt();
        }
        return -1;
    }

    /**
     * Makes a SipURL from ViaHeader
     *
     * @return
     */
    public SipURL getSipURL() {
        return new SipURL(getHost(), getPort());
    }

    /**
     * Checks if "branch" parameter is present
     *
     * @return
     */
    public boolean hasBranch() {
        return hasParameter(branchParam);
    }

    /**
     * Gets "branch" parameter
     *
     * @return
     */
    public String getBranch() {
        return getParameter(branchParam);
    }

    /**
     * Sets "branch" parameter
     *
     * @param value
     */
    public void setBranch(String value) {
        setParameter(branchParam, value);
    }

    /**
     * Checks if "received" parameter is present
     *
     * @return
     */
    public boolean hasReceived() {
        return hasParameter(receivedParam);
    }

    /**
     * Gets "received" parameter
     *
     * @return
     */
    public String getReceived() {
        return getParameter(receivedParam);
    }

    /**
     * Sets "received" parameter
     *
     * @param value
     */
    public void setReceived(String value) {
        setParameter(receivedParam, value);
    }

    /**
     * Checks if "rport" parameter is present
     *
     * @return
     */
    public boolean hasRport() {
        return hasParameter(rportParam);
    }

    /**
     * Gets "rport" parameter
     *
     * @return
     */
    public int getRport() {
        String value = getParameter(rportParam);
        if (value != null) {
            return Integer.parseInt(value);
        } else {
            return -1;
        }
    }

    /**
     * Sets "rport" parameter
     */
    public void setRport() {
        setParameter(rportParam, null);
    }

    /**
     * Sets "rport" parameter
     *
     * @param port
     */
    public void setRport(int port) {
        if (port < 0) {
            setParameter(rportParam, null);
        } else {
            setParameter(rportParam, Integer.toString(port));
        }
    }

    /**
     * Checks if "maddr" parameter is present
     *
     * @return
     */
    public boolean hasMaddr() {
        return hasParameter(maddrParam);
    }

    /**
     * Gets "maddr" parameter
     *
     * @return
     */
    public String getMaddr() {
        return getParameter(maddrParam);
    }

    /**
     * Sets "maddr" parameter
     *
     * @param value
     */
    public void setMaddr(String value) {
        setParameter(maddrParam, value);
    }

    /**
     * Checks if "ttl" parameter is present
     *
     * @return
     */
    public boolean hasTtl() {
        return hasParameter(ttlParam);
    }

    /**
     * Gets "ttl" parameter
     *
     * @return
     */
    public int getTtl() {
        String value = getParameter(ttlParam);
        if (value != null) {
            return Integer.parseInt(value);
        } else {
            return -1;
        }
    }

    /**
     * Sets "ttl" parameter
     *
     * @param ttl
     */
    public void setTtl(int ttl) {
        setParameter(ttlParam, Integer.toString(ttl));
    }

}
