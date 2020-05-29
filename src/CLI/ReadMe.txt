ReadMe for Running eThalerClient application

Prerequisities



1. Ensure your java version is 1.8 (for the uninitiated it is Java 8) is installed .

Gradle 5.1.3 the build tool for 
Getting the software and Preparing 

1. Get "eThaler" project in github .

2. Navigate into the foldler "eThaler/src/CLI".

3. Execute the "PrepareeThalerClient.bat" (Windows ) or "PrepareeThalerClient.sh" (Linux)
    (This builds, copies and creates required script files for running the CLI application)


Configuring / Resource needed to run the application.

1. "config" directory  contains "config.properties" file. 
        Two properties in this file need to be modified as per the target env
	i. BESU_URL=<URL for network>
       ii. ETHALER_CONTRACT_ADDR=<contract address>

    b. "wallets" directory.
         Application expects the wallets of "Central Banker" and "Dealer Clients" present in this directory to connect to network.

To create Wallets

1. To create wallet for Central Banker , run "StarteThalerClient.sh createwallet" from "eThaler/src/CLI" folder
     This will prompt to provide 
      a. wallet name 
      b. wallet password
      c. private key used to deploy contract to network

2. To create wallet for dealers , run "StarteThalerClient.sh" from "eThaler/src/CLI" folder
     This would give 3 options.
     Choose option "2 Create brand new wallet"
     Give wallet name and password.
     Note the wallet account "address" displayed in the console for future use.

Running :

1.  Execute the "StarteThalerClient.bat" (Windows) or "StarteThalerClient.sh" (Linux) file from "eThaler/src/CLI" folder
     This starts the eThalerClient java CLI application.

2. Choose the option "1. Open existing wallet"
     Provide the wallet name and password.
   
  
