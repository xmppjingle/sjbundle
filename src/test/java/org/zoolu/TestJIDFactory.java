package org.zoolu;

import junit.framework.TestCase;
import org.zoolu.sip.message.JIDFactory;

/**
 * Created with IntelliJ IDEA.
 * User: bhlangonijr
 * Date: 4/4/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestJIDFactory extends TestCase {

    public void testCache() {

        long init = System.currentTimeMillis();
        for (int i=0;i<=40000;i++) {

            JIDFactory.getInstance().getJID("user"+i+"@xmpp.org/test");

            if (Math.random()>0.7) {
                JIDFactory.getInstance().getJID("user1@xmpp.org/test"); //bubble down user1 in LRU list
            }

        }

        assertTrue(JIDFactory.getInstance().isCached("user1@xmpp.org/test"));

        System.out.println("Time " +(System.currentTimeMillis()-init));

    }


    public void testJID() {

        String jid = JIDFactory.getInstance().getJID("xmpp.org");

        String s = null;
        String z = "test";

        boolean b =  z.equals(s);

        System.out.println("B="+b);


    }

}
