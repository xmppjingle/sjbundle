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
import org.xmpp.jnodes.nio.DatagramListener;
import org.xmpp.jnodes.nio.ListenerDatagramChannel;
import org.xmpp.jnodes.nio.SelDatagramChannel;
import org.zoolu.tools.Random;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class SipChannel {
    private static final Logger log = Logger.getLogger(SipChannel.class);
    private final SocketAddress remoteAddress;
    private final String id;
    private final DatagramListener listener;
    private boolean active = false;
    private final String localIP;
    private int localPort;
    private long lastSentTimestamp;
    private long lastReceivedTimestamp;
    private SocketAddress localAddress;
    private ListenerDatagramChannel datagramChannel;

    public SipChannel(String id, SocketAddress remoteAddress, String localIP, DatagramListener listener) throws IOException {
        this(id, remoteAddress, localIP, -1, listener);
    }

    public SipChannel(String id, SocketAddress remoteAddress, String localIP, int localPort, final DatagramListener listener) throws IOException {
        this.id = id;
        this.remoteAddress = remoteAddress;
        this.localIP = localIP;
        this.localPort = localPort;
        this.lastSentTimestamp = this.lastReceivedTimestamp = System.currentTimeMillis();
        this.listener = listener;
        activate();
    }

    protected void activate() throws IOException {
        if (!active) {
            if (datagramChannel == null) {
                if (localPort <= 0) {
                    datagramChannel = getFreeChannel(listener);
                } else {
                    localAddress = new InetSocketAddress(InetAddress.getByName(localIP), localPort);
                    datagramChannel = SelDatagramChannel.open(listener, localAddress);
                    log.debug("Opening DatagramChannel at " + localIP + ":" + localPort);
                }
            }
            active = true;
        }
    }

    public String getId() {
        return id;
    }

    public void send(ByteBuffer byteBuffer, SocketAddress address) {
        lastSentTimestamp = System.currentTimeMillis();
        log.debug("Sending UDP Packet...");
        try {
            datagramChannel.send(byteBuffer, address != null ? address : remoteAddress);
        } catch (IOException e) {
            log.error("Could Not send Packet", e);
        }
    }

    public void closeDatagramChannel() {
        try {
            datagramChannel.close();
        } catch (IOException e) {
            log.error("Error closing DatagramChannel", e);
        }
    }

    public void shutdown() {
        closeDatagramChannel();
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    private ListenerDatagramChannel getFreeChannel(final DatagramListener listener) throws IOException {
        int port;
        ListenerDatagramChannel channel = null;
        for (int tries = 50; channel == null; tries--) {
            port = Random.nextInt(20000) + 4000;
            try {
                channel = SelDatagramChannel.open(listener, new InetSocketAddress(InetAddress.getByName(localIP), port));
                log.debug("Opening DatagramChannel at " + localIP + ":" + port);
                localPort = port;
            } catch (SocketException e) {
                if (tries == 0) {
                    log.error("Severe could not get FreeSocket", e);
                    throw e;
                }
            }
        }
        return channel;
    }

    public long getLastReceivedTimestamp() {
        return lastReceivedTimestamp;
    }

    public long getLastSentTimestamp() {
        return lastSentTimestamp;
    }

    /**
     * @param socketAddress the socketAddress to set
     */
    public void setSocketAddress(SocketAddress socketAddress) {
        this.localAddress = socketAddress;
    }

    /**
     * @return the socketAddress
     */
    public SocketAddress getSocketAddress() {
        return localAddress;
    }

    /**
     * @param datagramChannel the datagramChannel to set
     */
    public void setDatagramChannel(ListenerDatagramChannel datagramChannel) {
        this.datagramChannel = datagramChannel;
    }

    /**
     * @return the datagramChannel
     */
    public ListenerDatagramChannel getDatagramChannel() {
        return datagramChannel;
    }

    /**
     * @return the localPort
     */
    public int getLocalPort() {
        return localPort;
    }
}
