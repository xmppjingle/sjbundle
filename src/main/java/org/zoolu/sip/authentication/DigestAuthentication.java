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

package org.zoolu.sip.authentication;

import org.zoolu.sip.header.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The HTTP Digest Authentication as defined in RFC2617.
 * It can be used to i) calculate an authentication response
 * from an authentication request, or ii) validate an authentication response.
 * <br/> in the former case the DigestAuthentication is created based on
 * a WwwAuthenticationHeader (or ProxyAuthenticationHeader),
 * while in the latter case it is created based on an AuthorizationHeader
 * (or ProxyAuthorizationHeader).
 */
public class DigestAuthentication {
    protected String method;
    protected String username;
    protected String passwd;

    protected String realm;
    protected String nonce; // e.g. base 64 encoding of time-stamp H(time-stamp ":" ETag ":" private-key)
    //protected String[] domain;
    protected String opaque;
    //protected boolean stale; // "true" | "false"
    protected String algorithm; // "MD5" | "MD5-sess" | token

    protected String qop; // "auth" | "auth-int" | token

    protected String domain;
    protected String stale;

    protected String uri;
    protected String cnonce;
    protected String nc;
    protected String response;

    protected String body;
    protected boolean useSecret;

    final static char pseudo[] = {'0', '1', '2',
            '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    protected DigestAuthentication() {
    }


    public DigestAuthentication(String method, AuthorizationHeader ah, String body, String passwd, String nc) {
        this(method, ah, body, passwd, nc, false);
    }

    public DigestAuthentication(String method, String uri, WwwAuthenticateHeader ah, String qop,
                                String body, String username, String passwd, String nc) {
        this(method, uri, ah, qop, body, username, passwd, nc, false);
    }

    public DigestAuthentication(String method, String uri, AuthenticationInfoHeader ah, String qop,
                                String body, String username, String passwd, String nc, String realm) {
        this(method, uri, ah, qop, body, username, passwd, nc, realm, false);
    }

    public DigestAuthentication(String method, AuthorizationHeader ah, String body, String passwd, String nc, boolean useSecret) {
        init(method, ah, body, passwd, nc, useSecret);
    }

    public DigestAuthentication(String method, String uri, WwwAuthenticateHeader ah, String qop,
                                String body, String username, String passwd, String nc, boolean useSecret) {
        init(method, ah, body, passwd, nc, useSecret);
        this.uri = uri;
        this.qop = qop;
        this.username = username;
        this.nc = nc;
    }

    public DigestAuthentication(String method, String uri, AuthenticationInfoHeader ah, String qop,
                                String body, String username, String passwd, String nc, String realm, boolean useSecret) {
        init(method, ah, body, passwd, nc, useSecret);
        this.uri = uri;
        this.qop = qop;
        this.username = username;
        this.nc = nc;
        this.realm = realm;
    }

    private void init(String method, AuthenticationHeader ah, String body, String passwd, String nc, boolean useSecret) {
        this.method = method;
        this.username = ah.getUsernameParam();
        this.passwd = passwd;
        this.realm = ah.getRealmParam();
        this.opaque = ah.getOpaqueParam();
        this.nonce = ah.getNonceParam();
        this.useSecret = useSecret;
        if (this.nonce == null) {
            this.nonce = ah.getNextnonceParam();
        }
        this.algorithm = ah.getAlgorithParam();
        this.qop = ah.getQopParam();
        this.uri = ah.getUriParam();
        this.cnonce = ah.getCnonceParam();
        this.nc = nc;

        if (this.cnonce == null && this.qop != null && this.qop.equals("auth")) {
            this.cnonce = String.valueOf(System.currentTimeMillis()).substring(0, 8);
        }

        if (this.nc == null) {
            this.nc = ah.getNcParam();

            if (this.nc == null && this.cnonce != null) {
                this.nc = "00000001";
            }
        }

        this.response = ah.getResponseParam();
        this.body = body;

        if (ah.getParameter("domain") != null) {
            this.domain = ah.getParameter("domain");
        }

        if (ah.getParameter("stale") != null) {
            this.stale = ah.getParameter("stale");
        }
    }


    /**
     * Gets a String representation of the object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("method=").append(method).append("\n");
        sb.append("username=").append(username).append("\n");
        sb.append("passwd=").append(passwd).append("\n");
        sb.append("realm=").append(realm).append("\n");
        sb.append("nonce=").append(nonce).append("\n");
        sb.append("opaque=").append(opaque).append("\n");
        sb.append("algorithm=").append(algorithm).append("\n");

        if (domain != null) {
            sb.append("domain=").append(domain).append("\n");
        }
        if (stale != null) {
            sb.append("stale=").append(algorithm).append("\n");
        }

        sb.append("qop=").append(qop).append("\n");
        sb.append("uri=").append(uri).append("\n");
        sb.append("cnonce=").append(cnonce).append("\n");
        sb.append("nc=").append(nc).append("\n");
        sb.append("response=").append(response).append("\n");
        sb.append("body=").append(body).append("\n");
        return sb.toString();
    }


    /**
     * Whether the digest-response in the 'response' parameter in correct.
     *
     * @return
     */
    public boolean checkResponse() {
        if (response == null) {
            return false;
        } else {
            return response.equals(getResponse());
        }
    }


    /**
     * Gets a new AuthorizationHeader based on current authentication attributes.
     *
     * @return
     */
    public AuthorizationHeader getAuthorizationHeader() {
        AuthorizationHeader ah = new AuthorizationHeader("Digest");
        ah.addUsernameParam(username);
        ah.addRealmParam(realm);
        ah.addNonceParam(nonce);
        ah.addUriParam(uri);
        if (algorithm != null) {
            ah.addAlgorithParam(algorithm);
        }
        if (opaque != null) {
            ah.addOpaqueParam(opaque);
        }
        if (qop != null) {
            ah.addQopParam(qop);
        }
        if (nc != null) {
            ah.addNcParam(nc);
        }
        if (cnonce != null) {
            ah.addCnonceParam(cnonce);
        }
        if (domain != null) {
            ah.addParameter("domain", domain);
        }
        if (stale != null) {
            ah.addParameter("stale", stale);
        }
        String response = getResponse();
        ah.addResponseParam(response);
        return ah;
    }


    /**
     * Gets a new ProxyAuthorizationHeader based on current authentication attributes.
     *
     * @return
     */
    public ProxyAuthorizationHeader getProxyAuthorizationHeader() {
        return new ProxyAuthorizationHeader(getAuthorizationHeader().getValue());
    }


    /**
     * Calculates the digest-response.
     * <p> If the "qop" value is "auth" or "auth-int":
     * <br>   KD ( H(A1), unq(nonce) ":" nc ":" unq(cnonce) ":" unq(qop) ":" H(A2) )
     * <p/>
     * <p> If the "qop" directive is not present:
     * <br>   KD ( H(A1), unq(nonce) ":" H(A2) )
     *
     * @return
     */
    public String getResponse() {
        String secret = this.useSecret ? passwd : toHex(getMD5(A1()));
        StringBuilder sb = new StringBuilder();
        if (nonce != null) {
            sb.append(nonce);
        }
        sb.append(":");
        if (qop != null) {
            if (nc != null) {
                sb.append(nc);
            }
            sb.append(":");
            if (cnonce != null) {
                sb.append(cnonce);
            }
            sb.append(":");
            sb.append(qop);
            sb.append(":");
        }
        sb.append(toHex(getMD5(A2())));
        String data = sb.toString();
        return toHex(KD(secret, data));
    }


    /**
     * Calculates KD() value.
     * <p> KD(secret, data) = H(concat(secret, ":", data))
     *
     * @param secret
     * @param data
     * @return
     */
    private byte[] KD(String secret, String data) {
        StringBuilder sb = new StringBuilder();
        sb.append(secret).append(":").append(data);
        return getMD5(sb.toString());
    }


    /**
     * Calculates A1 value.
     * <p> If the "algorithm" directive's value is "MD5" or is unspecified:
     * <br>   A1 = unq(username) ":" unq(realm) ":" passwd
     * <p/>
     * <p> If the "algorithm" directive's value is "MD5-sess":
     * <br>   A1 = H( unq(username) ":" unq(realm) ":" passwd ) ":" unq(nonce) ":" unq(cnonce)
     *
     * @return
     */
    private byte[] A1() {
        StringBuilder sb = new StringBuilder();
        if (username != null) {
            sb.append(username);
        }
        sb.append(":");
        if (realm != null) {
            sb.append(realm);
        }
        sb.append(":");
        if (passwd != null) {
            sb.append(passwd);
        }

        if (algorithm == null || !algorithm.equalsIgnoreCase("MD5-sess")) {
            return sb.toString().getBytes();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(":");
            if (nonce != null) {
                sb2.append(nonce);
            }
            sb2.append(":");
            if (cnonce != null) {
                sb2.append(cnonce);
            }
            if (useSecret) {
                return cat(hexStringToByteArray(passwd) , sb2.toString().getBytes());
            } else {
                return cat(getMD5(sb.toString()), sb2.toString().getBytes());
            }
        }
    }


    /**
     * Calculates A2 value.
     * <p> If the "qop" directive's value is "auth" or is unspecified:
     * <br>   A2 = Method ":" digest-uri
     * <p/>
     * <p> If the "qop" value is "auth-int":
     * <br>   A2 = Method ":" digest-uri ":" H(entity-body)
     *
     * @return
     */
    private String A2() {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append(":");
        if (uri != null) {
            sb.append(uri);
        }

        if (qop != null && qop.equalsIgnoreCase("auth-int")) {
            sb.append(":");
            if (body == null) {
                sb.append(toHex(getMD5("")));
            } else {
                sb.append(toHex(getMD5(body)));
            }
        }
        return sb.toString();
    }


    /**
     * Concatenates two arrays of bytes.
     *
     * @param a
     * @param b
     * @return
     */
    private static byte[] cat(byte[] a, byte[] b) {
        int len = a.length + b.length;
        byte[] c = new byte[len];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    /**
     * Calculates the MD5 of a String.
     *
     * @param str
     * @return
     */
    static byte[] getMD5(String str) {
        try {
            return MessageDigest.getInstance("MD5").digest(str.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calculates the MD5 of an array of bytes.
     *
     * @param bb
     * @return
     */
    static byte[] getMD5(byte[] bb) {
        try {
            return MessageDigest.getInstance("MD5").digest(bb);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calculates the HEX of an array of bytes.
     *
     * @param bb
     * @return
     */
    static String toHex(byte[] bb) {
        if (bb == null || bb.length <= 0) {
            return null;
        }

        final char[] out = new char[bb.length * 2];

        int i = 0;
        for (final byte b : bb) {
            out[i++] = pseudo[((b & 0xF0) >>> 4) & 0x0F];
            out[i++] = pseudo[b & 0x0F];
        }

        return new String(out);
    }

    static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
 