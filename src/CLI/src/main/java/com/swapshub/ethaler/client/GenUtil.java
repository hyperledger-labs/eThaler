/**
Copyright 2020 Swapshub
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
*/

package com.swapshub.ethaler.client;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Properties;

/**
 * This class is base class . Has various common functionalities
 */
public class GenUtil {
    public static final String SEPARATOR = "-------------------------------------------------------";
    protected static Web3j web3j = null;
    protected static Properties properties = null;

    /**
     * Constructor : reads from properties file
     */
    public GenUtil() {
        if (properties == null) {
            initProperties();
        }
    }

    /**
     * @param tokenId
     * @param amt
     * @return converted value for the display based on decimal value of the token
     */
    protected BigInteger getConvertedValForDisplay(BigInteger tokenId, BigInteger amt) {
        BigInteger tokenDecimal = getDecimalForTokenId(tokenId);
        return amt.divide(tokenDecimal);
    }

    /**
     * @param tokenId
     * @param amt
     * @return converted  value for storing in the network based on the decimal value of the token
     */
    protected BigInteger getConvertedValForNetwork(BigInteger tokenId, BigInteger amt) {
        BigInteger tokenDecimal = getDecimalForTokenId(tokenId);
        return amt.multiply(tokenDecimal);
    }

    /**
     * @param tokenId
     * @return the decimal value of the given token id
     */
    private BigInteger getDecimalForTokenId(BigInteger tokenId) {
        int size = EThalerApplication.tokenIdList.size();
        for (int i = 0; i < size; i++) {
            if (tokenId.intValue() == ((BigInteger) EThalerApplication.tokenIdList.get(i)).intValue()) {
                BigInteger numDecimal = (BigInteger) EThalerApplication.tokenDecimalList.get(i);
                return getConverterValue(numDecimal.intValue());
            }
        }
        //return default value of 1
        return new BigInteger("1");
    }

    /**
     * @param decimals
     * @return converted value
     */
    private BigInteger getConverterValue(int decimals) {
        StringBuilder val = new StringBuilder("1");
        for (int i = 0; i < decimals; i++) {
            val.append("0");
        }
        return new BigInteger(val.toString());
    }

    /**
     * @return web3j object
     */
    public Web3j getWeb3j() {
        return web3j;
    }

    /**
     * @return collects the user typed in value in the command prompt
     */
    protected String collectUserInput() {
        StringBuilder inputStr = new StringBuilder();
        boolean cont = true;
        while (cont) {
            if (EThalerApplication.scanner.hasNextLine())
                inputStr.append(EThalerApplication.scanner.nextLine());
            if (inputStr.toString().trim().length() > 0) {
                cont = false;
            }
        }
        if (inputStr.toString().trim().equalsIgnoreCase("exit")) {
            shutdownApp("Exiting");
        }
        return inputStr.toString().trim();
    }

    /**
     * @param msg to close tha applciation after printing the msg.
     */
    protected void shutdownApp(String msg) {
        printLog(msg);
        System.exit(0);
    }

    /**
     * @param receipt
     * convenience method
     */
    protected void printTransactionHash(TransactionReceipt receipt) {
        printLog("Transaction Hash : [" + receipt.getTransactionHash() + "]");
    }

    /**
     * @param msg
     * convenience method -- to change to log file or print in the console
     */
    public static void printLog(String msg) {
        System.out.println();
        System.out.println(msg);
    }

    /**
     * print the menu options as per the (organization / bank) type
     */
    public static void printUserOptions() {
        if (EThalerApplication.isOwner) {
            printOwnerMenu();
        } else {
            printDealerMenu();
        }
    }

    /**
     * print on the console only -- Central Banker menu options
     */
    private static void printOwnerMenu() {
        //printLog not used
        System.out.print("\010 \010 \010");
        System.out.println("\010");
        System.out.println(SEPARATOR);
        System.out.println("You are the Central Bank User" + "\r\n" + "Please choose one of the following options");
        System.out.println(" 1.   Mint");
        System.out.println(" 2.   Get Balance for own account");
        System.out.println(" 3.   Get Balance for another account");
        System.out.println(" 4    Add New Token Definition");
        System.out.println(" 5.   Register a dealer");
        System.out.println(" 6.   Transfer");
        System.out.println(" 7.   Unregister a dealer");
        System.out.println(" 8.   Pause a token");
        System.out.println(" 9.   Resume a token");
        System.out.println(" 10.  Burn");
        System.out.println(" 11.  Token details");
        System.out.println(" 0.   Exit Application");
        System.out.print(" Please enter your option (1 / 2 / 3 / 4 / 5 / 6 / 7 / 8 / 9 / 10 / 11 / 0) : ");
    }
    /**
     * print on the console only -- Dealer Banker menu options
     */
    private static void printDealerMenu() {
        //printLog not used
        System.out.print("\010 \010 \010");
        System.out.println("\010");
        System.out.println(SEPARATOR);
        System.out.println("You are Commercial Bank User" + "\r\n" + "Please choose one of the following options");
        System.out.println(" 1.   Get Balance for own account");
        System.out.println(" 2.   Transfer to another account ");
        System.out.println(" 3.   Token Details ");
        System.out.println(" 0.   Exit Application ");
        System.out.print(" Please enter your option (1 / 2 / 3 / 0) : ");
    }

    /**
     * initiates values from property file
     */
    private void initProperties() {
        try {
            File configDir = new File(EThalerApplication.configDirectory);
            if (!configDir.exists()) {
                configDir.mkdir();
            }
            FileReader reader = new FileReader(EThalerApplication.configDirectory + File.separator + "config.properties");
            properties = new Properties();
            properties.load(reader);
            EThalerApplication.BESU_URL = getProperty("BESU_URL", "http://127.0.0.1:9545");
            EThalerApplication.ETHALER_CONTRACT_ADDR = getProperty("ETHALER_CONTRACT_ADDR", "0x1a21603d62d0718e5210634BAD9e7Fe711634215");
            EThalerApplication.LOG_LEVEL = getProperty("LOG_LEVEL", "INFO").toUpperCase();
        } catch (Exception ex) {
            printLog("Error in loading properties : " + ex.getMessage());
        }
    }

    /**
     *
     * @param key
     * @param defaultVal
     * @return value for the given key
     */
    private String getProperty(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }
} //end of class
