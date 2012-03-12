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


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class NamingThreadFactory
        implements ThreadFactory {
    private final AtomicInteger number = new AtomicInteger(0);
    private final String prefix;
    private final String SEPARATOR = " - ";
    final ThreadGroup group;

    public NamingThreadFactory(String prefix) {
        if (prefix != null)
            this.prefix = prefix;
        else {
            this.prefix = "NoNameThread";
        }
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup());
    }

    public Thread newThread(Runnable runnable) {
        this.number.incrementAndGet();
        Thread t = new Thread(this.group, runnable, this.prefix + " - " + this.number.toString(), 0L);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != 5) {
            t.setPriority(5);
        }
        return t;
    }
}
