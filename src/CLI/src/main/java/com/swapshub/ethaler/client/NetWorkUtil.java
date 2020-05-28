/**
Copyright 2020 Swapshub
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
*/

package com.swapshub.ethaler.client;

import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
/**
* This class contains functionalities for connecting to network.
*/

public class NetWorkUtil extends GenUtil {
    public NetWorkUtil() {
        super();
    }

    /**
     * Connects to Besu network
     */
    void connectToNetwork() {
        try {
            printLog(" BESU_URL got from config file " + EThalerApplication.BESU_URL);
            printLog(" Please ensure correct contract address in config.properites file");
            //check the format of url is valid.
            if (pingURL(EThalerApplication.BESU_URL, 50)) {
                //connect to network
                web3j = makeConnectionToNetwork(EThalerApplication.BESU_URL);
            } else {
                printLog("BESU_URL is not valid. Could not establish connection");
            }
        } catch (Exception ex) {
            printLog("Error in connecting to network : " + ex.getMessage());
        }
    }

    /**
     * connects to given url
     *
     * @param networkURL
     * @return
     * @throws Exception
     */
    private Web3j makeConnectionToNetwork(String networkURL) throws Exception {
        try {
            return Web3j.build(new HttpService(networkURL, createOkHttpClient(), false));
        } catch (Exception ex) {
            printLog("Error in creating web3j object " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * helps in setting various connection related parameters
     *
     * @return
     */
    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        Long tos = 3000L;
        builder.connectTimeout(tos, TimeUnit.SECONDS);
        builder.readTimeout(tos, TimeUnit.SECONDS);  // Sets the socket timeout too
        builder.writeTimeout(tos, TimeUnit.SECONDS);
    }

    /**
     * helps in doing a sanity check whether the Besu Url actually exists.
     * @param url
     * @param timeout
     * @return
     */
    private boolean pingURL(String url, int timeout) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 1000);
        } catch (IOException ex) {
            printLog("Error in PingURL : " + ex.getMessage());
            return false;
        }
    }

}//end of class
