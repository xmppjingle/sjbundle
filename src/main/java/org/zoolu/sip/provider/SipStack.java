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
 * SipStack includes all static attributes used by the sip stack.
 * <p/>
 * Static attributes includes the logging configuration,
 * default SIP port, deafult supported transport protocols, timeouts, etc.
 */
public class SipStack {
    // ********************** private attributes **********************

    /**
     * Whether SipStack configuration has been already loaded
     */
    private static boolean isInit = false;

    /** The default SipProvider */
    //private static SipProvider provider=null;

    // *********************** software release ***********************

    /**
     * Release
     */
    public static String release = "Jingle Nodes Single";

    // ************* default sip provider configurations **************

    /**
     * Default SIP port.
     * Note that this is not the port used by the running stack, but simply the standard default SIP port.
     * <br> Normally it sould be set to 5060 as defined by RFC 3261. Using a different value may cause
     * some problems when interacting with other unaware SIP UAs.
     */
    public static int defaultPort = 5060;

    public static final String PROTO_UDP = "udp";
    public static final String PROTO_TCP = "tcp";

    /**
     * Default supported transport protocols.
     */
    public static String[] default_transportProtocols = {PROTO_UDP, PROTO_TCP};
    /**
     * Default max number of contemporary open transport connections.
     */
    public static int default_nmax_connections = 32;
    /**
     * Whether adding 'rport' parameter on via header fields of outgoing requests.
     */
    public static boolean use_rport = true;
    /**
     * Whether adding (forcing) 'rport' parameter on via header fields of incoming requests.
     */
    public static boolean force_rport = false;

    // ******************** general configurations ********************

    /**
     * default max-forwards value (RFC3261 recommends value 70)
     */
    public static int max_forwards = 70;
    /**
     * starting retransmission timeout (milliseconds); called T1 in RFC2361; they suggest T1=500ms
     */
    public static long retransmission_timeout = 500;
    /**
     * maximum retransmission timeout (milliseconds); called T2 in RFC2361; they suggest T2=4sec
     */
    public static long max_retransmission_timeout = 4000;
    /**
     * transaction timeout (milliseconds); RFC2361 suggests 64*T1=32000ms
     */
    public static long transaction_timeout = 32000;
    /**
     * clearing timeout (milliseconds); T4 in RFC2361; they suggest T4=5sec
     */
    public static long clearing_timeout = 5000;

    /**
     * Whether using only one thread for all timer instances.
     */
    public static boolean single_timer = false;

    /**
     * Whether 1xx responses create an "early dialog" for methods that create dialog.
     */
    public static boolean early_dialog = false;

    /**
     * Default 'expires' value in seconds. RFC2361 suggests 3600s as default value.
     */
    public static int default_expires = 3600;

    /**
     * UA info included in request messages in the 'User-Agent' header field.
     * Use "NONE" if the 'User-Agent' header filed must not be added.
     */
    public static String uaInfo = release;
    /**
     * Server info included in response messages in the 'Server' header field
     * Use "NONE" if the 'Server' header filed must not be added.
     */
    public static String serverInfo = release;


    // ************************** costructor **************************

    /**
     * Costructs a non-static SipStack
     */
    private SipStack() {
    }

    /**
     * Inits SipStack
     */
    public static void init() {
        isInit = true;
    }

    /**
     * Whether SipStack has been already initialized
     *
     * @return
     */
    public static boolean isInit() {
        return isInit;
    }

    // ************************ private methods ***********************

}
