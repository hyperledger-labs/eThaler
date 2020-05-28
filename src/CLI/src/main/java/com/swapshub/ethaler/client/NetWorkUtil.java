package com.swapshub.ethaler.client;

import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
            web3j = makeConnectionToNetwork(EThalerApplication.BESU_URL);
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

}//end of class
