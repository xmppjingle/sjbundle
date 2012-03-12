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

package org.zoolu.sip.header;


import org.zoolu.sip.provider.SipParser;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract header for various authentication schemes
 * <p> It is inherited by WwwAuthenticateHeader, AuthorizationHeader, etc.
 */
public abstract class AuthenticationHeader extends Header {

    /**
     * Lienar white space separator inserted bethween parameters.
     */
    //public static String LWS_SEPARATOR="\r\n   ";
    public static String LWS_SEPARATOR = " ";

    /**
     * Array of parameters that are quoted.
     */
    public static String[] QUOTEDPARAMETERS = {"auts", "cnonce", "nextnonce", "nonce", "opaque", "realm", "response", "rspauth", "uri", "username", "domain", "qop"};


    /**
     * Whether is a quoted parameter (i.e. belongs to <i>QUOTEDPARAMETERS</i>).
     *
     * @param param_name
     * @return
     */
    private static boolean isQuotedParameter(String param_name) {
        for (String QUOTED_PARAMETER : QUOTEDPARAMETERS)
            if (param_name.equalsIgnoreCase(QUOTED_PARAMETER)) {
                return true;
            }
        return false;
    }


    /**
     * Creates a new AuthenticationHeader.
     *
     * @param hname
     * @param hvalue
     */
    public AuthenticationHeader(String hname, String hvalue) {
        super(hname, hvalue);
    }

    /**
     * Creates a new AuthenticationHeader.
     *
     * @param hd
     */
    public AuthenticationHeader(Header hd) {
        super(hd);
    }

    /**
     * Creates a new AuthenticationHeader.
     * specifing the <i>auth_scheme</i> and the List of authentication parameters.
     * <p> <i>authParams</i> is a List of String of the form <i>parm_name</i> "=" <i>parm_value</i>
     *
     * @param hname
     * @param auth_scheme
     * @param authParams
     */
    public AuthenticationHeader(String hname, String auth_scheme, List authParams) {
        super(hname, auth_scheme);
        if (authParams.size() > 0) {
            value += " " + authParams.get(0);
        }
        for (int i = 1; i < authParams.size(); i++) value += "," + LWS_SEPARATOR + authParams.get(i);
    }


    /**
     * Adds a parameter.
     * If <i>param_name</i> belongs to <i>QUOTEDPARAMETERS</i>, <i>param_value</i> is quoted (if already not).
     *
     * @param param_name
     * @param param_value
     */
    public void addParameter(String param_name, String param_value) {
        if (param_value.indexOf('"') < 0 && isQuotedParameter(param_name)) {
            addQuotedParameter(param_name, param_value);
        } else {
            addUnquotedParameter(param_name, param_value);
        }
    }


    /**
     * Adds a parameter without inserting quotes.
     *
     * @param param_name
     * @param param_value
     */
    public void addUnquotedParameter(String param_name, String param_value) {
        if (value.indexOf('=') < 0) {
            value += " ";
        } else {
            value += "," + LWS_SEPARATOR;
        }
        value += param_name + "=" + param_value;
    }


    /**
     * Adds a parameter with quotes.
     *
     * @param param_name
     * @param param_value
     */
    public void addQuotedParameter(String param_name, String param_value) {
        if (value.indexOf('=') < 0) {
            value += " ";
        } else {
            value += "," + LWS_SEPARATOR;
        }
        if (param_value.indexOf('"') >= 0) {
            value += param_name + "=" + param_value;
        } else {
            value += param_name + "=\"" + param_value + "\"";
        }
    }


    /**
     * Whether has parameter <i>param_name</i>
     *
     * @param param_name
     * @return
     */
    public boolean hasParameter(String param_name) {
        char[] name_separators = {'=', ' ', '\t', '\r', '\n'};
        SipParser par = new SipParser(value);
        par.skipString(); // skip the auth_scheme
        par.skipWSPCRLF();
        while (par.hasMore()) {
            String name = par.getWord(name_separators);
            if (name.equals(param_name)) {
                return true;
            }
            par.goToCommaHeaderSeparator().skipChar().skipWSPCRLF();
        }
        return false;
    }


    /**
     * Returns the parameter <i>param_name</i>, without quotes.
     *
     * @param param_name
     * @return
     */
    public String getParameter(String param_name) {
        char[] name_separators = {'=', ' ', '\t'};
        SipParser par = new SipParser(value);
        par.skipString(); // skip the auth_scheme
        par.skipWSPCRLF();
        while (par.hasMore()) {
            String name = par.getWord(name_separators);
            if (name.equals(param_name)) {
                par.goTo('=').skipChar().skipWSP();
                int comma = par.indexOfCommaHeaderSeparator();
                if (comma >= 0) {
                    par = new SipParser(par.getString(comma - par.getPos()));
                }
                return par.getStringUnquoted();
            } else {
                par.goToCommaHeaderSeparator().skipChar().skipWSPCRLF();
            }
        }
        return null;
    }


    /**
     * Gets a String List of parameter names.
     *
     * @return
     * @returns a List of String.
     */
    public List getParameters() {
        char[] name_separators = {'=', ' ', '\t'};
        SipParser par = new SipParser(value);
        par.skipString(); // skip the auth_scheme
        par.skipWSPCRLF();
        List names = new ArrayList();
        while (par.hasMore()) {
            String name = par.getWord(name_separators);
            names.add(name);
            par.goToCommaHeaderSeparator().skipChar().skipWSPCRLF();
        }
        return names;
    }

    /**
     * Gets the athentication scheme (i.e. the first token).
     *
     * @return
     */
    public String getAuthScheme() {
        SipParser par = new SipParser(value);
        return par.getString();
    }

    // ***************** quoted parameters *****************

    /**
     * Whether has realm
     *
     * @return
     */
    public boolean hasRealmParam() {
        return hasParameter("realm");
    }

    /**
     * Returns the realm (unquoted)
     *
     * @return
     */
    public String getRealmParam() {
        return getParameter("realm");
    }

    /**
     * Adds the realm
     *
     * @param unquoted_realm
     */
    public void addRealmParam(String unquoted_realm) {
        addQuotedParameter("realm", unquoted_realm);
    }


    /**
     * Whether has nonce
     *
     * @return
     */
    public boolean hasNonceParam() {
        return hasParameter("nonce");
    }

    /**
     * Returns the nonce (unquoted)
     *
     * @return
     */
    public String getNonceParam() {
        return getParameter("nonce");
    }

    /**
     * Adds the nonce
     *
     * @param unquoted_nonce
     */
    public void addNonceParam(String unquoted_nonce) {
        addQuotedParameter("nonce", unquoted_nonce);
    }


    /**
     * Whether has opaque
     *
     * @return
     */
    public boolean hasOpaqueParam() {
        return hasParameter("opaque");
    }

    /**
     * Returns the opaque (unquoted)
     *
     * @return
     */
    public String getOpaqueParam() {
        return getParameter("opaque");
    }

    /**
     * Adds the opaque
     *
     * @param unquoted_opaque
     */
    public void addOpaqueParam(String unquoted_opaque) {
        addQuotedParameter("opaque", unquoted_opaque);
    }


    /**
     * Whether has username
     *
     * @return
     */
    public boolean hasUsernameParam() {
        return hasParameter("username");
    }

    /**
     * Returns the username (unquoted)
     *
     * @return
     */
    public String getUsernameParam() {
        return getParameter("username");
    }

    /**
     * Adds the username
     *
     * @param unquoted_username
     */
    public void addUsernameParam(String unquoted_username) {
        addQuotedParameter("username", unquoted_username);
    }


    /**
     * Whether has uri
     *
     * @return
     */
    public boolean hasUriParam() {
        return hasParameter("uri");
    }

    /**
     * Returns the uri (unquoted)
     *
     * @return
     */
    public String getUriParam() {
        return getParameter("uri");
    }

    /**
     * Adds the uri
     *
     * @param unquoted_uri
     */
    public void addUriParam(String unquoted_uri) {
        addQuotedParameter("uri", unquoted_uri);
    }


    /**
     * Whether has response
     *
     * @return
     */
    public boolean hasResponseParam() {
        return hasParameter("response");
    }

    /**
     * Returns the response (unquoted)
     *
     * @return
     */
    public String getResponseParam() {
        return getParameter("response");
    }

    /**
     * Adds the response
     *
     * @param unquoted_response
     */
    public void addResponseParam(String unquoted_response) {
        addQuotedParameter("response", unquoted_response);
    }


    /**
     * Whether has cnonce
     *
     * @return
     */
    public boolean hasCnonceParam() {
        return hasParameter("cnonce");
    }

    /**
     * Returns the cnonce (unquoted)
     *
     * @return
     */
    public String getCnonceParam() {
        return getParameter("cnonce");
    }

    /**
     * Adds the cnonce
     *
     * @param unquoted_cnonce
     */
    public void addCnonceParam(String unquoted_cnonce) {
        addQuotedParameter("cnonce", unquoted_cnonce);
    }


    /**
     * Whether has rspauth
     *
     * @return
     */
    public boolean hasRspauthParam() {
        return hasParameter("rspauth");
    }

    /**
     * Returns the rspauth (unquoted)
     *
     * @return
     */
    public String getRspauthParam() {
        return getParameter("rspauth");
    }

    /**
     * Adds the rspauth
     *
     * @param unquoted_rspauth
     */
    public void addRspauthParam(String unquoted_rspauth) {
        addQuotedParameter("rspauth", unquoted_rspauth);
    }


    /**
     * Whether has auts
     *
     * @return
     */
    public boolean hasAutsParam() {
        return hasParameter("auts");
    }

    /**
     * Returns the auts
     *
     * @return
     */
    public String getAutsParam() {
        return getParameter("auts");
    }

    /**
     * Adds the auts
     *
     * @param unquoted_auts
     */
    public void addAutsParam(String unquoted_auts) {
        addQuotedParameter("auts", unquoted_auts);
    }


    /**
     * Whether has nextnonce
     *
     * @return
     */
    public boolean hasNextnonceParam() {
        return hasParameter("nextnonce");
    }

    /**
     * Returns the nextnonce
     *
     * @return
     */
    public String getNextnonceParam() {
        return getParameter("nextnonce");
    }

    /**
     * Adds the nextnonce
     *
     * @param unquoted_nextnonce
     */
    public void addNextnonceParam(String unquoted_nextnonce) {
        addQuotedParameter("nextnonce", unquoted_nextnonce);
    }


    /**
     * Whether has qop-options
     *
     * @return
     */
    public boolean hasQopOptionsParam() {
        return hasParameter("qop");
    }

    /** Gets the qop-options */
    /*public String[] getQopOptionsParam()
    {  List aux=new List();
       Parser par=new Parser(getParameter("qop"));
       char[] separators={','};
       String qop=null;
       while ((qop=par.getWord(separators))!=null) aux.add(qop);
       if (aux.size()==0) return null;
       String[] qop_options=new String[aux.size()];
       for (int i=0; i<qop_options.length; i++) qop_options[i]=(String)aux.get(i);
       return qop_options;
    }*/

    /**
     * Gets the qop-options
     *
     * @return
     */
    public String getQopOptionsParam() {
        return getParameter("qop");
    }

    /** Adds the qop-options */
    /*public void addQopOptionsParam(String[] qop_options)
    {  StringBuffer sb=new StringBuffer();
       for (int i=0; i<qop_options.length; i++)
       {  if (i>0) sb.append(",");
          sb.append(qop_options[i]);
       }
       addQuotedParameter("qop",sb.toString());
    }*/

    /**
     * Adds the qop-options
     *
     * @param unquoted_qop_options
     */
    public void addQopOptionsParam(String unquoted_qop_options) {
        addQuotedParameter("qop", unquoted_qop_options);
    }

    // **************** unquoted parameters ****************

    /**
     * Whether has qop
     *
     * @return
     */
    public boolean hasQopParam() {
        return hasParameter("qop");
    }

    /**
     * Returns the qop
     *
     * @return
     */
    public String getQopParam() {
        return getParameter("qop");
    }

    /**
     * Adds the qop
     *
     * @param qop
     */
    public void addQopParam(String qop) {
        addUnquotedParameter("qop", qop);
    }


    /**
     * Whether has nc
     *
     * @return
     */
    public boolean hasNcParam() {
        return hasParameter("nc");
    }

    /**
     * Returns the nc
     *
     * @return
     */
    public String getNcParam() {
        return getParameter("nc");
    }

    /**
     * Adds the nc
     *
     * @param nc
     */
    public void addNcParam(String nc) {
        addUnquotedParameter("nc", nc);
    }


    /**
     * Whether has algorithm
     *
     * @return
     */
    public boolean hasAlgorithmParam() {
        return hasParameter("algorithm");
    }

    /**
     * Returns the algorithm
     *
     * @return
     */
    public String getAlgorithParam() {
        return getParameter("algorithm");
    }

    /**
     * Adds the algorithm
     *
     * @param algorithm
     */
    public void addAlgorithParam(String algorithm) {
        addUnquotedParameter("algorithm", algorithm);
    }


}
