/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

import java.lang.*;
import java.io.*;
import java.net.*;

import com.sun.ejte.ccl.reporter.*;

public class WebTest2 {
    
    static SimpleReporterAdapter stat=
        new SimpleReporterAdapter("appserv-tests");
    private static URLConnection conn = null;
    private static URL url;
    private static ObjectOutputStream objectWriter = null;
    private static ObjectInputStream objectReader = null;  
    private static String host;
    private static String contextRoot;
    private static String port;

    public static void main(String args[]) {
        stat.addDescription("Load balance mod_jk");
        host = args[0];
        contextRoot = args[1];
        port = args[2];
        run(port);
    }

    public static void run(String port) {
        try{
            System.out.println("Running test");
            url = new URL("http://" + host  + ":" + port  + "/" + contextRoot + "/hello");
            String originalLoc = url.toString();
            System.out.println("\n Invoking url: " + url.toString());
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
               HttpURLConnection urlConnection = (HttpURLConnection)conn;
               urlConnection.setDoOutput(true);
               int responseCode=  urlConnection.getResponseCode();
               System.out.println("Response code: " + responseCode + " Expected code: 200"); 
               if (urlConnection.getResponseCode() != 200){
                    stat.addStatus("load-balancer-mod-jk", stat.FAIL);
               } else {
                    stat.addStatus("load-balancer-mod-jk", stat.PASS);
               }
            }
            stat.printSummary("web/load-balancer-mod-jk");
        }catch(Exception ex){
            ex.printStackTrace();
            stat.addStatus("load-balancer-mod-jk", stat.FAIL);
        }
    }
}
