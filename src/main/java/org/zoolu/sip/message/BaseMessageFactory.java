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


import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.*;
import org.zoolu.sip.provider.SipProviderInfoInterface;
import org.zoolu.sip.provider.SipStack;


/**
 * BaseMessageFactory is used to create SIP messages, requests and
 * responses by means of
 * two static methods: createRequest(), createResponse().
 * <BR> A valid SIP request sent by a UAC MUST, at least, contain
 * the following header fields: To, From, CSeq, Call-ID, Max-Forwards,
 * and Via; all of these header fields are mandatory in all SIP
 * requests.  These sip header fields are the fundamental building
 * blocks of a SIP message, as they jointly provide for most of the
 * critical message routing services including the addressing of
 * messages, the routing of responses, limiting message propagation,
 * ordering of messages, and the unique identification of transactions.
 * These header fields are in addition to the mandatory request line,
 * which contains the method, Request-URI, and SIP version.
 */
public abstract class BaseMessageFactory {


    public static String BRANCH_PREFIX = "z9hG4bK";

    /**
     * Creates a SIP request message.
     *
     * @param method     method name
     * @param requestUri request-uri
     * @param to         ToHeader NameAddress
     * @param from       FromHeader NameAddress
     * @param contact    Contact NameAddress (if null, no ContactHeader is added)
     * @param proto
     * @param viaAddr
     * @param hostPort   Via port number
     * @param rport
     * @param callId     Call-ID value
     * @param cseq       CSeq value
     * @param localTag   tag in FromHeader
     * @param remoteTag  tag in ToHeader (if null, no tag is added)
     * @param branch     branch value (if null, a random value is picked)
     * @param body       body (if null, no body is added)
     * @return
     */
    public static Message createRequest(String method, SipURL requestUri, NameAddress to, NameAddress from, NameAddress contact, String proto, String viaAddr, int hostPort, boolean rport, String callId, long cseq, String localTag, String remoteTag, String branch, String body) {
        Message req = new Message();
        //mandatory headers first (To, From, Via, Max-Forwards, Call-ID, CSeq):
        req.setRequestLine(new RequestLine(method, requestUri));
        ViaHeader via = new ViaHeader(proto, viaAddr, hostPort);
        if (rport) {
            via.setRport();
        }
        if (branch == null) {
            branch = BRANCH_PREFIX + callId;
        }
        via.setBranch(branch);
        req.addViaHeader(via);
        req.setMaxForwardsHeader(new MaxForwardsHeader(70));
        if (remoteTag == null) {
            req.setToHeader(new ToHeader(to));
        } else {
            req.setToHeader(new ToHeader(to, remoteTag));
        }
        req.setFromHeader(new FromHeader(from, localTag));
        req.setCallIdHeader(new CallIdHeader(callId));
        req.setCSeqHeader(new CSeqHeader(cseq, method));
        //optional headers:
        if (contact != null) {
            MultipleHeader contacts = new MultipleHeader(SipHeaders.Contact);
            contacts.addBottom(new ContactHeader(contact));
            //System.out.println("DEBUG: Contact: "+contact.toString());
            req.setContacts(contacts);
        }
        req.setExpiresHeader(new ExpiresHeader(String.valueOf(SipStack.default_expires)));
        // add User-Agent header field
        if (SipStack.uaInfo != null) {
            req.setUserAgentHeader(new UserAgentHeader(SipStack.uaInfo));
        }
        //if (body!=null) req.setBody(body); else req.setBody("");
        req.setBody(body);
        //System.out.println("DEBUG: MessageFactory: request:\n"+req);
        return req;
    }


    /**
     * Creates a SIP request message.
     * Where <UL>
     * <LI> via address and port are taken from SipProvider
     * <LI> transport protocol is taken from request-uri (if transport parameter is present)
     * or the default transport for the SipProvider is used.
     * </UL>
     *
     * @param sipProvider the SipProvider used to fill the Via field
     * @param method
     * @param requestUri
     * @param to
     * @param from
     * @param contact
     * @param callId
     * @param cseq
     * @param localTag
     * @param remoteTag
     * @param branch
     * @param body
     * @return
     */
    public static Message createRequest(SipProviderInfoInterface sipProvider, String method, SipURL requestUri, NameAddress to, NameAddress from, NameAddress contact, String callId, long cseq, String localTag, String remoteTag, String branch, String body) {
        String viaAddr = sipProvider.getViaAddress();
        int hostPort = sipProvider.getPort();
        boolean rport = sipProvider.isRportSet();
        String proto;
        if (requestUri != null && requestUri.hasTransport()) {
            proto = requestUri.getTransport();
        } else {
            proto = sipProvider.getDefaultTransport();
        }

        return createRequest(method, requestUri, to, from, contact, proto, viaAddr, hostPort, rport, callId, cseq, localTag, remoteTag, branch, body);
    }


    /**
     * Creates a SIP request message.
     * Where <UL>
     * <LI> request-uri equals the To sip url
     * <LI> via address and port are taken from SipProvider
     * <LI> transport protocol is taken from request-uri (if transport parameter is present)
     * or the default transport for the SipProvider is used.
     * <LI> callId is picked random
     * <LI> cseq is picked random
     * <LI> localTag is picked random
     * <LI> branch is picked random
     * </UL>
     *
     * @param sipProvider
     * @param method
     * @param requestUri
     * @param to
     * @param from
     * @param contact
     * @param body
     * @return
     */
    public static Message createRequest(SipProviderInfoInterface sipProvider, String method, SipURL requestUri, NameAddress to, NameAddress from, NameAddress contact, String body) {  //SipURL requestUri=to.getAddress();
        String callId = sipProvider.pickCallId();
        int cseq = sipProvider.pickInitialCSeq();
        String localTag = sipProvider.pickTag();
        //String branch=SipStack.pickBranch();
        return createRequest(sipProvider, method, requestUri, to, from, contact, callId, cseq, localTag, null, null, body);
    }


    /**
     * Creates a SIP request message.
     * Where <UL>
     * <LI> request-uri equals the To sip url
     * <LI> via address and port are taken from SipProvider
     * <LI> transport protocol is taken from request-uri (if transport parameter is present)
     * or the default transport for the SipProvider is used.
     * <LI> contact is formed by the 'From' user-name and by the address and port taken from SipProvider
     * <LI> callId is picked random
     * <LI> cseq is picked random
     * <LI> localTag is picked random
     * <LI> branch is picked random
     * </UL>
     *
     * @param sipProvider
     * @param method
     * @param to
     * @param from
     * @param body
     * @return
     */
    public static Message createRequest(SipProviderInfoInterface sipProvider, String method, NameAddress to, NameAddress from, String body) {
        String contactUser = from.getAddress().getUserName();
        NameAddress contact = new NameAddress(new SipURL(contactUser, sipProvider.getViaAddress(), sipProvider.getPort()));
        return createRequest(sipProvider, method, to.getAddress(), to, from, contact, body);
    }

    /**
     * Creates a new INVITE request out of any pre-existing dialogs.
     *
     * @param sipProvider
     * @param requestUri
     * @param to
     * @param from
     * @param contact
     * @param body
     * @return
     * @see #createRequest(String, SipURL, NameAddress, NameAddress, NameAddress, String, String, int, boolean, String, long, String, String, String, String)
     */
    public static Message createInviteRequest(SipProviderInfoInterface sipProvider, SipURL requestUri, NameAddress to, NameAddress from, NameAddress contact, String body) {
        String callId = sipProvider.pickCallId();
        int cseq = sipProvider.pickInitialCSeq();
        String localTag = sipProvider.pickTag();
        //String branch=SipStack.pickBranch();
        if (contact == null) {
            contact = from;
        }
        return createRequest(sipProvider, SipMethods.INVITE, requestUri, to, from, contact, callId, cseq, localTag, null, null, body);
    }

    /**
     * Creates a new INVITE request out of any pre-existing dialogs.
     *
     * @param sipProvider
     * @param requestUri
     * @param to
     * @param from
     * @param contact
     * @param body
     * @param sid
     * @param fromTag
     * @return
     * @see #createRequest(String, SipURL, NameAddress, NameAddress, NameAddress, String, String, int, boolean, String, long, String, String, String, String)
     */
    public static Message createInviteRequest(SipProviderInfoInterface sipProvider, SipURL requestUri, NameAddress to, NameAddress from, NameAddress contact, String body, String sid, String fromTag) {
        String callId = sid;
        int cseq = sipProvider.pickInitialCSeq();
        //String localTag = SipProvider.pickTag();
        //String branch=SipStack.pickBranch();
        if (contact == null) {
            contact = from;
        }
        return createRequest(sipProvider, SipMethods.INVITE, requestUri, to, from, contact, callId, cseq, fromTag, null, "z9hG4bK" + sid, body);
    }

    /**
     * Creates an ACK request for a non-2xx response
     *
     * @param sipProvider
     * @param method
     * @param resp
     * @return
     */
    public static Message createNon2xxAckRequest(SipProviderInfoInterface sipProvider, Message method, Message resp) {
        SipURL requestUri = method.getRequestLine().getAddress();
        FromHeader from = method.getFromHeader();
        ToHeader to = resp.getToHeader();
        String viaAddr = sipProvider.getViaAddress();
        int hostPort = sipProvider.getPort();
        boolean rport = sipProvider.isRportSet();
        String proto;
        if (requestUri.hasTransport()) {
            proto = requestUri.getTransport();
        } else {
            proto = sipProvider.getDefaultTransport();
        }
        String branch = method.getViaHeader().getBranch();
        NameAddress contact = null;
        Message ack = createRequest(SipMethods.ACK, requestUri, to.getNameAddress(), from.getNameAddress(), contact, proto, viaAddr, hostPort, rport, method.getCallIdHeader().getCallId(), method.getCSeqHeader().getSequenceNumber(), from.getParameter("tag"), to.getParameter("tag"), branch, null);
        ack.removeExpiresHeader();
        if (method.hasRouteHeader()) {
            ack.setRoutes(method.getRoutes());
        }
        return ack;
    }

    /**
     * Creates an ACK request for a non-2xx response
     *
     * @param sipProvider
     * @param resp
     * @param requestUri
     * @return
     */
    public static Message createNon2xxAckRequest(final SipProviderInfoInterface sipProvider, Message resp, SipURL requestUri) {
        FromHeader from = resp.getFromHeader();
        ToHeader to = resp.getToHeader();
        String viaAddr = sipProvider.getViaAddress();
        int hostPort = sipProvider.getPort();
        boolean rport = sipProvider.isRportSet();
        String proto;
        if (requestUri.hasTransport()) {
            proto = requestUri.getTransport();
        } else {
            proto = sipProvider.getDefaultTransport();
        }
        String branch = resp.getViaHeader().getBranch();
        NameAddress contact = null;
        Message ack = createRequest(SipMethods.ACK, requestUri, to.getNameAddress(), from.getNameAddress(), contact, proto, viaAddr, hostPort, rport, resp.getCallIdHeader().getCallId(), resp.getCSeqHeader().getSequenceNumber(), from.getParameter("tag"), to.getParameter("tag"), branch, null);
        ack.removeExpiresHeader();
        final MultipleHeader mh = resp.getRecordRoutes();
        if (mh != null) {
            ack.setRecordRoutes(mh);
            ack.rfc2543RouteAdapt();
        }
        //if (method.hasRouteHeader()) ack.setRoutes(method.getRoutes());
        return ack;
    }

    /**
     * Creates an ACK request for a 2xx-response. Contact value is taken from SipStack
     *
     * @param sipProvider
     * @param resp
     * @param body
     * @return
     */
    public static Message create2xxAckRequest(SipProviderInfoInterface sipProvider, Message resp, String body) {
        ToHeader to = resp.getToHeader();
        FromHeader from = resp.getFromHeader();
        int code = resp.getStatusLine().getCode();
        SipURL requestUri;
        requestUri = resp.getContactHeader() != null ? resp.getContactHeader().getNameAddress().getAddress() : from.getNameAddress().getAddress();
        int hostPort = sipProvider.getPort();
        boolean rport = sipProvider.isRportSet();
        if (requestUri == null) {
            requestUri = to.getNameAddress().getAddress();
        }
        String viaAddr = sipProvider.getViaAddress();
        String branch = resp.getViaHeader() != null ? resp.getViaHeader().getBranch() : null;

        String contactUser = "";

        if (from.getNameAddress() != null) {
            SipURL url = from.getNameAddress().getAddress();
            if (url != null) {
                contactUser = url.getUserName();
                if (contactUser != null) {
                    contactUser = contactUser + "@";
                } else {
                    contactUser = "";
                }
            }
        }

        NameAddress contact = new NameAddress(new StringBuilder().append(contactUser).append(sipProvider.getIP()).append(":").append(sipProvider.getPort()).toString());
        Message ack = createRequest(SipMethods.ACK, requestUri, to.getNameAddress(), from.getNameAddress(), contact, resp.getViaHeader() != null ? resp.getViaHeader().getProtocol() : sipProvider.getDefaultTransport(), viaAddr, hostPort, rport, resp.getCallIdHeader() != null ? resp.getCallIdHeader().getCallId() : "12345678", resp.getCSeqHeader() != null ? resp.getCSeqHeader().getSequenceNumber() : 1, from.getParameter("tag"), to.getParameter("tag"), branch, body);

        if (code >= 200 && code < 300) {
            if (resp.getRecordRoutes() != null) {
                ack.setRoutes(new MultipleHeader(SipHeaders.Route, resp.getRecordRoutes().getValues()));
            }
        } else {
            if (resp.getRecordRoutes() != null) {
                ack.setRecordRoutes(resp.getRecordRoutes());
            }
        }
        return ack;
    }

    /** Creates an ACK request for a 2xx-response within a dialog */
    /*public static Message create2xxAckRequest(Dialog dialog, NameAddress contact, String body)
    {  return createRequest(SipMethods.ACK,dialog,contact,body);
    }*/

    /** Creates an ACK request for a 2xx-response within a dialog */
    /*public static Message create2xxAckRequest(Dialog dialog, String body)
    {  return createRequest(SipMethods.ACK,dialog,body);
    }*/

    /**
     * Creates a CANCEL request.
     *
     * @param method
     * @return
     */
    public static Message createCancelRequest(Message method) {
        ToHeader to = method.getToHeader();
        FromHeader from = method.getFromHeader();
        SipURL requestUri = method.getRequestLine().getAddress();
        NameAddress contact = method.getContactHeader().getNameAddress();
        ViaHeader via = method.getViaHeader();
        String hostAddr = via.getHost();
        int hostPort = via.getPort();
        boolean rport = via.hasRport();
        String proto = via.getProtocol();
        String branch = method.getViaHeader().getBranch();
        return createRequest(SipMethods.CANCEL, requestUri, to.getNameAddress(), from.getNameAddress(), contact, proto, hostAddr, hostPort, rport, method.getCallIdHeader().getCallId(), method.getCSeqHeader().getSequenceNumber(), from.getParameter("tag"), to.getParameter("tag"), branch, "");
    }

    /**
     * Creates a BYE request.
     *
     * @param sipProviderInfo
     * @param to
     * @param from
     * @param sid
     * @param fromTag
     * @param toTag
     * @param lastMessage
     * @param requestURI
     * @return
     */
    public static Message createByeRequest(SipProviderInfoInterface sipProviderInfo, NameAddress to, NameAddress from, String sid, String fromTag, String toTag, Message lastMessage, NameAddress requestURI) {

        if (lastMessage != null) {
            NameAddress target = requestURI;
            if (target == null) {
                target = lastMessage.getContactHeader() != null ? lastMessage.getContactHeader().getNameAddress() : null;
            }
            if (target == null) {
                target = to;
            }
            SipURL requestUri = target.getAddress();
            if (requestUri == null) {
                requestUri = lastMessage.getContactHeader().getNameAddress().getAddress();
            }
            SipProviderInfoInterface sipProvider = sipProviderInfo;
            String viaAddr = sipProvider.getViaAddress();
            int hostPort = sipProvider.getPort();
            boolean rport = sipProvider.isRportSet();
            String proto;
            if (target.getAddress().hasTransport()) {
                proto = target.getAddress().getTransport();
            } else {
                proto = sipProvider.getDefaultTransport();
            }
            String callId = lastMessage.getCallIdHeader().getCallId();
            long cseq = lastMessage.getCSeqHeader().getSequenceNumber() + 1;
            Message req = createRequest(SipMethods.BYE, requestUri, to, from, to, proto, viaAddr, hostPort, rport, callId, cseq, fromTag, toTag, lastMessage.getViaHeader().getBranch(), null);

            RecordRouteHeader recordRouterHeader = lastMessage.getRecordRouteHeader();
            if (recordRouterHeader != null) {
                //req.addRouteHeader(new RouteHeader(recordRouterHeader.getNameAddress()));
                req.addRoutes(new MultipleHeader(SipHeaders.Route, lastMessage.getRecordRoutes().getValues()));

            }
            req.rfc2543RouteAdapt();
            req.removeExpiresHeader();
            req.removeContacts();
            req.removeRecordRoutes();
            req.setCSeqHeader(new CSeqHeader(cseq, SipMethods.BYE));
            return req;
        }
        return null;
    }

    /**
     * Creates a new REGISTER request.
     * <p> If contact is null, set contact as star * (register all)
     *
     * @param sipProvider
     * @param to
     * @param from
     * @param contact
     * @param localTag
     * @return
     */
    public static Message createRegisterRequest(SipProviderInfoInterface sipProvider, NameAddress to, NameAddress from, NameAddress contact, String localTag) {
        SipURL toUrl = to.getAddress();
        SipURL registrar = new SipURL(toUrl.getHost(), toUrl.getPort());
        String viaAddr = sipProvider.getViaAddress();
        int hostPort = sipProvider.getPort();
        boolean rport = sipProvider.isRportSet();
        String proto;
        if (toUrl.hasTransport()) {
            proto = toUrl.getTransport();
        } else {
            proto = sipProvider.getDefaultTransport();
        }
        String callId = sipProvider.pickCallId();
        int cseq = sipProvider.pickInitialCSeq();
        if (localTag == null) {
            sipProvider.pickTag();
        }
        //String branch=SipStack.pickBranch();
        Message req = createRequest(SipMethods.REGISTER, registrar, to, from, contact, proto, viaAddr, hostPort, rport, callId, cseq, localTag, null, null, null);
        // if no contact, deregister all
        if (contact == null) {
            ContactHeader star = new ContactHeader(); // contact is *
            req.setContactHeader(star);
            req.setExpiresHeader(new ExpiresHeader(String.valueOf(SipStack.default_expires)));
        }
        return req;
    }

    /**
     * Creates a new UNREGISTER request.
     *
     * @param sipProvider
     * @param to
     * @param localTag
     * @return
     */
    public static Message createUnregisterRequest(SipProviderInfoInterface sipProvider, NameAddress to, String localTag) {
        SipURL toUrl = to.getAddress();
        SipURL registrar = new SipURL(toUrl.getHost(), toUrl.getPort());
        String viaAddr = sipProvider.getViaAddress();
        int hostPort = sipProvider.getPort();
        boolean rport = sipProvider.isRportSet();
        String proto;
        if (toUrl.hasTransport()) {
            proto = toUrl.getTransport();
        } else {
            proto = sipProvider.getDefaultTransport();
        }
        String callId = sipProvider.pickCallId();
        int cseq = sipProvider.pickInitialCSeq();
        if (localTag == null) {
            sipProvider.pickTag();
        }
        //String branch=SipStack.pickBranch();
        Message req = createRequest(SipMethods.REGISTER, registrar, to, to, null, proto, viaAddr, hostPort, rport, callId, cseq, localTag, null, null, null);
        ContactHeader star = new ContactHeader(); // contact is *
        req.setContactHeader(star);
        req.setExpiresHeader(new ExpiresHeader("0"));
        return req;
    }

    //################ Can be removed? ################
    /** Creates a new REGISTER request.
     * <p> If contact is null, set contact as star * (register all) */
    /*public static Message createRegisterRequest(SipProvider sipProvider, NameAddress to, NameAddress contact)
    {  return createRegisterRequest(sipProvider,to,to,contact);
    }*/


    /**
     * Creates a SIP response message.
     *
     * @param req         the request message
     * @param code        the response code
     * @param reason      the response reason
     * @param contact     the contact address
     * @param localTag    the local tag in the 'To' header
     * @param contentType
     * @param body        the message body
     * @return
     */
    public static Message createResponse(Message req, int code, String reason, String localTag, NameAddress contact, String contentType, String body) {
        Message resp = new Message();
        resp.setStatusLine(new StatusLine(code, reason));
        resp.setVias(req.getVias());
        if (code >= 180 && code < 300 && req.hasRecordRouteHeader()) {
            resp.setRecordRoutes(req.getRecordRoutes());
        }
        ToHeader toh = req.getToHeader();
        if (localTag != null) {
            toh.setParameter("tag", localTag);
        }
        resp.setToHeader(toh);
        resp.setFromHeader(req.getFromHeader());
        resp.setCallIdHeader(req.getCallIdHeader());
        resp.setCSeqHeader(req.getCSeqHeader());
        if (contact != null) {
            resp.setContactHeader(new ContactHeader(contact));
        }
        // add Server header field
        if (SipStack.serverInfo != null) {
            resp.setServerHeader(new ServerHeader(SipStack.serverInfo));
        }
        //if (body!=null) resp.setBody(body); else resp.setBody("");
        if (contentType == null) {
            resp.setBody(body);
        } else {
            resp.setBody(contentType, body);
        }
        //System.out.println("DEBUG: MessageFactory: response:\n"+resp.toString());
        return resp;
    }

    /**
     * Creates a SIP response message.
     *
     * @param code        the response code
     * @param reason      the response reason
     * @param fromTag
     * @param contact     the contact address
     * @param method
     * @param localTag    the local tag in the 'To' header
     * @param contentType
     * @param body        the message body
     * @param sipProvider
     * @param to
     * @param from
     * @param callId
     * @param mh
     * @return
     */
    public static Message createResponse(int code, String reason, String method, String localTag, String fromTag, NameAddress contact, String contentType, String body, SipProviderInfoInterface sipProvider, NameAddress to, NameAddress from, String callId, MultipleHeader mh) {
        Message resp = new Message();
        resp.setStatusLine(new StatusLine(code, reason));
        resp.setVias(mh);
        ToHeader toh = new ToHeader(to);
        if (localTag != null) {
            toh.setParameter("tag", localTag);
        }
        resp.setToHeader(toh);
        FromHeader fromH = new FromHeader(from);
        fromH.setParameter("tag", fromTag);
        resp.setFromHeader(fromH);
        resp.setCallIdHeader(new CallIdHeader(callId));
        resp.setCSeqHeader(new CSeqHeader(1, method));
        if (contact != null) {
            resp.setContactHeader(new ContactHeader(contact));
        }
        // add Server header field
        if (SipStack.serverInfo != null) {
            resp.setServerHeader(new ServerHeader(SipStack.serverInfo));
        }
        //if (body!=null) resp.setBody(body); else resp.setBody("");
        if (contentType == null) {
            resp.setBody(body);
        } else {
            resp.setBody(contentType, body);
        }
        //System.out.println("DEBUG: MessageFactory: response:\n"+resp.toString());
        return resp;
    }

    /**
     * Creates a SIP response message. For 2xx responses generates the local tag by means of the SipStack.pickTag(req) method.
     *
     * @param req
     * @param code
     * @param reason
     * @param contact
     * @return
     */
    public static Message createResponse(Message req, int code, String reason, NameAddress contact) {  //String reason=SipResponses.reasonOf(code);
        String localtag = null;
        if (req.createsDialog() && !req.getToHeader().hasTag()) {
            if ((code >= 200 && code < 300)) {
                localtag = req.getToHeader().getTag();
            }
        }
        return createResponse(req, code, reason, localtag, contact, null, null);
    }

}  