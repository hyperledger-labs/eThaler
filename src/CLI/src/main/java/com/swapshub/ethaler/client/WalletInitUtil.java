/**
Copyright 2020 Swapshub
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
*/

package com.swapshub.ethaler.client;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

import java.io.File;

/**
 * This class helps in opening  wallet file and getting
 * account address and privatekey for use in connecting to a contract
 */
public class WalletInitUtil extends GenUtil {
    private static final int LOAD_WALLET = 1;
    private static final int BRAND_NEW_WALLET = 2;
    private static final int EXIT_APPLICATION = 0;

    public static String acctAddress = "";
    public static String privateKey = "";

    public WalletInitUtil() {
        super();
    }

    String getAccountAddress() {
        return acctAddress;
    }

    /**
     * prints the wallet options menu items
     */
    void getUserWalletOptions() {
        boolean cont = true;
        while (cont) {
            printWalletMenu();
            String userOption = collectUserInput();
            cont = processWalletOption(userOption);
        }
    }

    private boolean processWalletOption(String userOption) {
        try {
            int opt = Integer.parseInt(userOption);
            switch (opt) {
                case LOAD_WALLET:
                    return loadExistingWallet();
                case BRAND_NEW_WALLET:
                    return createBrandNewWallet();
                case EXIT_APPLICATION:
                    shutdownApp("Exiting");
                    break;
                default:
                    System.out.println("Unkown choice . Ignored.");
            }
            return true;
        } catch (Exception ex) {
            printLog("Error in Wallet option : " + ex.getMessage());
            return true;
        }
    }

    /**
     * loads existing wallet after collecting the wallet filename and password for the wallet
     * @return
     */
    boolean loadExistingWallet() {
        String password = "";
        String walletFileName = "";
        System.out.print("Enter wallet file name : ");
        walletFileName = collectUserInput();
        boolean exists = checkForWalletFileExistance(walletFileName);
        try {
            if (exists) {
                password = new String(PasswordField.getPassword(System.in, "Enter wallet password : "));
                loadWallet(walletFileName, password);
                return false;
            } else {
                printLog("Entered wallet " + walletFileName + " does not exist");
                return true;
            }
        } catch (Exception ex) {
            return true;
        }
    }

    /**
     * creates a new wallet after collecting the wallet fileanme and password
     * @return
     */
    boolean createBrandNewWallet() {
        try {
            String password = "";
            String walletFileName = "";
            System.out.print("Enter wallet file name : ");
            walletFileName = collectUserInput();
            boolean exists = checkForWalletFileExistance(walletFileName);
            if (!exists) {
                password = new String(PasswordField.getPassword(System.in, "Enter wallet password : "));
                createWallet(walletFileName, password);
                shutdownApp("Wallet created successfully. Note account public address to give to central banker.");
                return false;
            } else {
                printLog("Wallet with file name " + walletFileName + " already exists");
                return true;
            }
        } catch (Exception ex) {
            printLog("Error in creating new wallet : " + ex.getMessage());
            return true;
        }
    }

    /**
     * creates a wallet with inputs walletfilename , password and private key.
     * @return
     */
    boolean createWalletWithPvtKey() {
        try {
            String password = "";
            String walletFileName = "";
            String pvtKey = "";
            System.out.print("Enter wallet file name :");
            walletFileName = collectUserInput();
            boolean exists = checkForWalletFileExistance(walletFileName);
            if (!exists) {
                password = new String(PasswordField.getPassword(System.in, "Enter wallet password : "));
                System.out.print("Enter wallet private key:");
                pvtKey = collectUserInput();
                createWalletWithPvtKey(walletFileName, password, pvtKey);
                return false;
            } else {
                printLog("Wallet with file name " + walletFileName + " already exists");
                return true;
            }
        } catch (Exception ex) {
            System.out.println("Error in creating wallet : " + ex.getMessage());
            return true;
        }
    }

    /**
     * adds the .json extension
     * @param walletFileName
     * @return
     */
    private String addFileExtension(String walletFileName) {
        if (walletFileName.trim().length() <= 5) {
            walletFileName = walletFileName + ".json";
            return walletFileName;
        }
        if (walletFileName.substring(walletFileName.length() - 5).equalsIgnoreCase(".json")) {
            //do nothing
        } else {
            walletFileName = walletFileName + ".json";
        }
        return walletFileName;
    }

    /**
     * checks for the wallet file already exists.
     * @param walletFileName
     * @return
     */
    private boolean checkForWalletFileExistance(String walletFileName) {
        try {
            int fileCount = getFileCount();
            walletFileName = addFileExtension(walletFileName);
            if (fileCount == 0) {
                return false;
            } else {
                File walletFile = new File(EThalerApplication.walletDirectory + "\\" + walletFileName);
                return walletFile.exists();
            }
        } catch (Exception ex_) {
            printLog("Error in checking wallet file existance : " + ex_.getMessage());
            return false;
        }
    }

    /**
     * gets the number of files in the wallet directory
     * @return
     */
    private int getFileCount() {
        File dir = new File(EThalerApplication.walletDirectory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.list().length;
    }

    /**
     * helps renames system generated wallet filename to user provided wallet filename
     * @param createdFileName
     * @param passwordName_
     */
    private void renameWalletFile(String createdFileName, String passwordName_) {
        File wFile = new File(EThalerApplication.walletDirectory + "\\" + createdFileName);
        File pFile = new File(EThalerApplication.walletDirectory + "\\" + passwordName_);
        wFile.renameTo(pFile);
    }

    /**
     * creates wallet file form the given walletfilename , password and privatekey
     * @param walletFileName
     * @param password_
     * @param pvtKey
     */
    private void createWalletWithPvtKey(String walletFileName, String password_, String pvtKey) {
        try {
            Credentials cred = Credentials.create(pvtKey);
            ECKeyPair keyPair = cred.getEcKeyPair();
            String walletName = WalletUtils.generateWalletFile(password_, keyPair, new File(EThalerApplication.walletDirectory), false);
            String walFileName = addFileExtension(walletFileName);
            renameWalletFile(walletName, walFileName);
            Credentials credentials = WalletUtils.loadCredentials(password_, EThalerApplication.walletDirectory + "/" + walFileName);
            acctAddress = credentials.getAddress();
            printLog("New Account address: " + acctAddress + "Note this account address to give to other dealers");
            privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        } catch (Exception ex_) {
            printLog("Error in creating wallet : " + ex_.getMessage());
        }
    }

    /**
     * creates a wallet from the given wallet filename and password
     * @param walletFileName
     * @param password_
     */
    private void createWallet(String walletFileName, String password_) {
        try {
            String walletName = WalletUtils.generateNewWalletFile(password_, new File(EThalerApplication.walletDirectory));
            String walFileName = addFileExtension(walletFileName);
            renameWalletFile(walletName, walFileName);
            Credentials credentials = WalletUtils.loadCredentials(password_, EThalerApplication.walletDirectory + "/" + walFileName);
            acctAddress = credentials.getAddress();
            printLog("New Account address: " + acctAddress + "\r\n");
            privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        } catch (Exception ex_) {
            printLog("Error in creating wallet : " + ex_.getMessage());
        }
    }

    /**
     * loads the existing wallet from the given wallet filename and password
     * @param walletFileName
     * @param password_
     */
    private void loadWallet(String walletFileName, String password_) {
        try {
            String walletName = addFileExtension(walletFileName);
            // Load the JSON encryted wallet
            Credentials credentials = WalletUtils.loadCredentials(password_, EThalerApplication.walletDirectory + "/" + walletName);
            // Get the account address
            acctAddress = credentials.getAddress();
            // Get the unencrypted private key into hexadecimal
            privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        } catch (Exception ex_) {
            printLog("Error could not load credentials : " + ex_.getMessage());
            ex_.printStackTrace();
            //throw new RuntimeException(ex_.getMessage());
        }
    }

    private void printWalletMenu() {
        System.out.println("");
        System.out.println("-------------------------------------------------------");
        System.out.println("Enter your option for wallet ");
        System.out.println("");
        System.out.println(" 1.   Open existing wallet");
        System.out.println(" 2.   Create brand new wallet");
        System.out.println(" 0.   Exit Application");
        System.out.print(" Please enter your option (1 / 2 / 0) : ");
    }
}//end of class
