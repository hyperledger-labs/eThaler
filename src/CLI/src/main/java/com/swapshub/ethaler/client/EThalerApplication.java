/**
Copyright 2020 Swapshub
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
*/

package com.swapshub.ethaler.client;

import ch.qos.logback.classic.Level;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.Security;
import java.util.ArrayList;
import java.util.Scanner;

/**
* This class is the main class for the CLI application.
*
*/
public class EThalerApplication {
    static final String CREATE_WALLET = "CreateWallet";
    static String LOG_LEVEL = "INFO";
    static String BESU_URL = "";
    static String ETHALER_CONTRACT_ADDR = "";
    static int CHAIN_ID = 2018;
    static long POLLING_INTERVAL = 2000;
    static int  POLLING_ATTEMPTS = 5;
    static String configDirectory = "config";
    static String walletDirectory = "wallets";
    static Scanner scanner = new Scanner(System.in);
    static boolean isOwner = false;
    static ArrayList tokenIdList = new ArrayList();
    static ArrayList tokenNameList = new ArrayList();
    static ArrayList tokenDecimalList = new ArrayList();
    static ArrayList tokenTTFList = new ArrayList();

    /**
     * Entry point for the command line interface application
     * @param args
     */
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        Level lvl = Level.toLevel(LOG_LEVEL);
        rootLogger.setLevel(lvl);

        //for wallet creation
        if (args.length > 0) {
            if (args[0].trim().length() > 0) //one argument is expected
            {
                configDirectory = args[0] + File.separator + configDirectory;
                walletDirectory = args[0] + File.separator + walletDirectory;
            }
            try { //second argument is optional
                if (args[1] != null && args[1].trim().length() > 0 && args[1].trim().equalsIgnoreCase(CREATE_WALLET)) {
                    WalletInitUtil wInitUtil = new WalletInitUtil();
                    wInitUtil.createWalletWithPvtKey();
                    wInitUtil.shutdownApp("Wallet created successfully. Ensure a backup of wallet json file is taken.");
                }
            } catch (Exception ex) {

            }
        }
        NetWorkUtil netUtil = new NetWorkUtil();
        WalletInitUtil wInitUtil = new WalletInitUtil();
        netUtil.connectToNetwork();

        //for running either as Central banker or dealer
        if (netUtil.getWeb3j() != null) {
            wInitUtil.getUserWalletOptions();
            ContractAccountUtil contractAccountUtil = new ContractAccountUtil();
            contractAccountUtil.startApplication(netUtil.getWeb3j());
        } else {
            netUtil.shutdownApp("Not connected to network. Exiting.");
        }
    }
}//end of class
