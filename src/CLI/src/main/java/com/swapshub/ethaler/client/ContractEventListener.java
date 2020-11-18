/**
Copyright 2020 Swapshub
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
*/

package com.swapshub.ethaler.client;

import com.swapshub.ethaler.w3generated.EThaler;
import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.Hashtable;

/**
* This class listnes for the event messages broadcast by the network
*/

class ContractEventListener  {
    EThaler contract;
    static final int REGISTER_EVENT = 0;
    static final int UNREGISTER_EVENT = 1;
    static final int TRANSFER_EVENT = 2;
    static final int TOKENDEFINED_EVENT = 3;

    Hashtable<Integer, Event> eventHash = new Hashtable<Integer, Event>();

    /**
     * constructor -- initializes the events to be monitored.
     * @param contract_
     */
    ContractEventListener(EThaler contract_) {
        contract = contract_;
        populateEventHash();
    }

    /**
     * populates hashtable container with key and event
     */
    private void populateEventHash() {
        eventHash.put(REGISTER_EVENT, EThaler.REGISTEREDDEALER_EVENT);
        eventHash.put(UNREGISTER_EVENT, EThaler.UNREGISTEREDDEALER_EVENT);
        eventHash.put(TRANSFER_EVENT, EThaler.TRANSFERSINGLE_EVENT);
        eventHash.put(TOKENDEFINED_EVENT, EThaler.TOKENDEFINED_EVENT);
    }

    /**
     * Susbribes to events
     * @param web3j
     */
    public void startListeningEthEvents(Web3j web3j) {
        final EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
                contract.getContractAddress());

        web3j.ethLogFlowable(ethFilter).subscribe(event -> {
            try {
                processEthEvent(event);
            } catch (Exception ex) {
                System.out.println("Error in ETH eventes listening : " + ex.getMessage());
            }
        }, onError ->
        {
            System.out.println("Error in subscribing t0 ETH events : " + onError.getMessage());
        });
    }

    /**
     * process the event log
     * @param log
     */
    private void processEthEvent(Log log) {
        int size = eventHash.size();
        if (size == 0) {
            System.out.println("No contract event registered for listening");
            return;
        }
        for (int i = 0; i < size; i++) {
            Event event = eventHash.get(i);
            EventValues eventValues = EThaler.staticExtractEventParameters(event, log);
            if (eventValues != null) {
                printEventData(i, eventValues);
            }
        }
    }

    private void printEventData(int i, EventValues eventValues) {
        switch (i) {
            case REGISTER_EVENT: {
                printRegisterEvent(eventValues);
                GenUtil.printUserOptions();
                break;
            }
            case UNREGISTER_EVENT: {
                printUnregisterEvent(eventValues);
                GenUtil.printUserOptions();
                break;
            }
            case TRANSFER_EVENT: {
                printTransferEvent(eventValues);
                GenUtil.printUserOptions();
                break;
            }
            case TOKENDEFINED_EVENT: {
                printTokenDefinedEvent(eventValues);
                GenUtil.printUserOptions();
                break;
            }
            default:
                System.out.println("Unknown event ");
        }
    }

    /**
     * prints the msg handed over
     * @param msg
     */
    void printEventMsg(String msg) {
        System.out.println();
        System.out.println(GenUtil.SEPARATOR);
        System.out.println(msg);
    }

    /**
     * prints Register dealer event related values
     * @param eventValues
     */
    private void printRegisterEvent(EventValues eventValues) {
        String accountId = (String) eventValues.getIndexedValues().get(0).getValue();
        BigInteger tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        printEventMsg("Registering event for account [" + accountId + "] , token id [" + tokenId.intValue() + "] completed.");
    }

    /**
     * prints Unregister a dealer event related values
     * @param eventValues
     */
    private void printUnregisterEvent(EventValues eventValues) {
        String accountId = (String) eventValues.getIndexedValues().get(0).getValue();
        BigInteger tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        printEventMsg("Unregistering event for account [" + accountId + "] , token id [" + tokenId.intValue() + "] completed.");
    }

    /**
     * prints transfer event related values
     * @param eventValues
     */
    private void printTransferEvent(EventValues eventValues) {
        String operator = (String) eventValues.getIndexedValues().get(0).getValue();
        String from = (String) eventValues.getIndexedValues().get(1).getValue();
        String to = (String) eventValues.getIndexedValues().get(2).getValue();
        BigInteger id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        BigInteger value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        printEventMsg("Transfer Event from account [" + from + "] to account [" + to + "] for token id [" + id
                + "] for amount [" + value + "] completed.");
    }

    /**
     * prints a new token created event related values
     * @param eventValues
     */
    private void printTokenDefinedEvent(EventValues eventValues) {
        BigInteger tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        String tokenName = (String) eventValues.getNonIndexedValues().get(1).getValue();
        String ttfURL = (String) eventValues.getNonIndexedValues().get(2).getValue();
        printEventMsg("Token Defined event for token id [" + tokenId.intValue() + "] token name [" + tokenName
                + "] TTF Url [" + ttfURL + "] completed.");
    }

}//end of class

