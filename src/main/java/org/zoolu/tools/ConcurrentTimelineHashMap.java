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

package org.zoolu.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentTimelineHashMap<T, S> extends ConcurrentHashMap<T, S> {

    final private ConcurrentSkipListMap<Long, ConcurrentHashMap<T, T>> timeoutPending = new ConcurrentSkipListMap<Long, ConcurrentHashMap<T, T>>();

    public S put(final T t, final S s) {

        final long timestamp = System.currentTimeMillis();
        ConcurrentHashMap<T, T> frame = this.timeoutPending.get(timestamp);

        if (frame == null) {
            synchronized (timeoutPending) {
                frame = this.timeoutPending.get(timestamp);
                if (frame == null) {
                    frame = new ConcurrentHashMap<T, T>();
                    this.timeoutPending.put(timestamp, frame);
                }
            }
        }
        frame.put(t, t);
        return super.put(t, s);

    }

    public List<S> cleanUpExpired(final long mileseconds) {
        final long lastValid = System.currentTimeMillis() - (mileseconds);
        return cleanUpOlderThan(lastValid);
    }

    public List<S> cleanUpOlderThan(final long timeInMileseconds) {
        final List<S> removed = new ArrayList<S>();
        for (final Map.Entry<Long, ConcurrentHashMap<T, T>> entry : timeoutPending.headMap(timeInMileseconds).entrySet()) {
            if (entry != null) {
                for (final T t : entry.getValue().values()) {
                    if (t != null) {
                        final S s = this.remove(t);
                        removed.add(s);
                    }
                }
                timeoutPending.remove(entry.getKey());
            }
        }
        return removed;
    }

}
