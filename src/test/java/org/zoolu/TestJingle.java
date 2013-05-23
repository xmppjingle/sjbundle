package org.zoolu;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import junit.framework.TestCase;
import org.xmpp.packet.JID;
import org.zoolu.sip.message.JIDFactory;
import org.zoolu.tools.ConcurrentTimelineHashMap;

/**
 * @author bhlangonijr
 *         Date: 5/10/13
 *         Time: 2:38 PM
 */
public class TestJingle extends TestCase {

    public void test() {

        final ConcurrentLinkedHashMap<String, String> map = new ConcurrentLinkedHashMap.Builder<String, String>()
                .maximumWeightedCapacity(10)
                .build();

        //JIDFactory.getInstance().getJID("dxx");
        map.put("test","test");


    }

    public void test2() {

        final ConcurrentLinkedHashMap<String, JID> jidCache = new ConcurrentLinkedHashMap.Builder<String, JID>()
                .maximumWeightedCapacity(400)
                .build();
        jidCache.put("t",new JID("tete"));


    }

    public void test3() {

        JIDFactory.getInstance().getJID("user1@xmpp.org/test");

    }

    public void test4() {

        ConcurrentTimelineHashMap<String, String> map = new ConcurrentTimelineHashMap<String, String>();
        map.put("test","test");

    }

}
