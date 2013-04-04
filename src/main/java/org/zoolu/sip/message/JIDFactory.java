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


import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.xmpp.packet.JID;
import org.zoolu.tools.ConcurrentTimelineHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class JIDFactory {

    private final ConcurrentLinkedHashMap<String, JID> jidCache = new ConcurrentLinkedHashMap.Builder<String, JID>()
            .maximumWeightedCapacity(4000)
            .build();

    private static JIDFactory jidFactory;
    private final List<JIDFormatter> jidPrepFilters = new ArrayList<JIDFormatter>();

    private JIDFactory() {
    }

    /*only for testing purposes
     */
    public boolean isCached(String str) {
        return jidCache.containsKey(str);
    }

    public static JIDFactory getInstance() {
        if (jidFactory == null) {
            jidFactory = new JIDFactory();
        }
        return jidFactory;
    }

    public JID getJID(String str) throws IllegalArgumentException {
        if (jidCache.containsKey(str)) {
            return jidCache.get(str);
        }

        for (final JIDFormatter filter : jidPrepFilters) {
            str = filter.prepForJID(str);
        }

        final JID jidEntry = new JID(str);
        jidCache.put(str, jidEntry);
        return jidEntry;
    }

    public void addJIDPrepFilter(final JIDFormatter filter) {
        jidPrepFilters.add(filter);
    }

    public JID getJID(final String node, final String domain, final String resource) throws IllegalArgumentException {

        final StringBuilder str = new StringBuilder();
        if (node != null) {
            str.append(node);
        }
        if (domain != null) {
            if (node != null) {
                str.append("@");
            }
            str.append(domain);
        }
        if (resource != null) {
            if (domain != null || node != null) {
                str.append("/");
            }
            str.append(resource);
        }

        return getJID(str.toString());

    }

    public void reset() {
        jidCache.clear();
    }
}
