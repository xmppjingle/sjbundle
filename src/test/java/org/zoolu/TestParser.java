package org.zoolu;

import junit.framework.TestCase;
import org.zoolu.sip.message.Message;

/**
 * Created by IntelliJ IDEA.
 * User: thiago
 * Date: 5/26/12
 * Time: 10:47 AM
 */
public class TestParser extends TestCase {

    public void testSIPParsing() {
        final String sipString = "SIP/2.0 200 OK\n" +
                "Via: SIP/2.0/UDP 194.183.72.28:5060;branch=z9hG4bK7942901908306987;received=178.33.112.237;rport=5062\n" +
                "From: \"+31651827042@yuilop.tv\" <sip:+31651827042@yuilop.tv>;tag=A(1.6)(6295399E195B795057FD01CF6D65301CB2E41499)\n" +
                "To: \"0031611537782\" <sip:0031611537782@sip.yuilop.tv>;tag=as3efe9f43\n" +
                "Call-ID: 7942901908306987\n" +
                "CSeq: 1 INVITE\n" +
                "User-Agent: Asterisk PBX 1.6.0.26-FONCORE-r78\n" +
                "Allow: INVITE, ACK, CANCEL, OPTIONS, BYE, REFER, SUBSCRIBE, NOTIFY, INFO\n" +
                "Supported: replaces, timer\n" +
                "Contact: <sip:0031611537782@194.183.72.28>\n" +
                "Content-Type: application/sdp\n" +
                "Content-Length: 124\n" +
                "\n" +
                "v=0\n" +
                "o=root 2139395421 2139395422 IN IP4 194.183.72.28\n" +
                "s=Asterisk PBX 1.6.0.26-FONCORE-r78\n" +
                "c=IN IP4 194.183.72.28\n" +
                "t=0 0\n" +
                "m=audio 0 RTP/AVP 18 112 3 0";

        final Message m = new Message(sipString);

        final String b = "v=0\n" +
                "o=root 2139395421 2139395422 IN IP4 194.183.72.28\n" +
                "s=Asterisk PBX 1.6.0.26-FONCORE-r78\n" +
                "c=IN IP4 194.183.72.28\n" +
                "t=0 0\n" +
                "m=audio 0 RTP/AVP 18 112 3 0";

        System.out.println(m.toString() + "\n Len:" + b.length());
    }
}
