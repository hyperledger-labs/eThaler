# eThaler

Hyperledger eThaler private repo

Objective:
Issue a Central Bank Digital Currency (CBDC) using an OpenZeppelin based ERC1155 contract
on a Enterprise Ethereum private network based on Hyperledger Besu
and implement the interfaces as per Token Taxonomy Framework (TTF).

This project enables a Central Bank (CB) to mint and operate a Central Bank Digital Currency. CB also authorizes a number of Commercial Banks (DB)
so these DBs can transfer these currencies amongst themselves but cannot
mint themselves.
The name eThaler is given based on Thaler currency - a precursor to most
western currencies in operation today (refer https://en.wikipedia.org/wiki/Thaler).

The ERC1155 token is chosen here so a CB can use the same contract to
issue other digital tokens representing its activities (for example, the
current issuance of various monetary facilities to address COVID-19 crisis).
Similarly a commercial bank can use this contract to mint and trade with
its own clients and other banks.

The project has 2 components : ERC1155 Smart Contract and a sample
Web3J based Java Client CLI to test the smart contract from a Central Bank and a Commercial Bank perspective.

Smart Contract:
The contract directory contains the eThaler.sol contract based on OpenZeppelin
ERC1155 sample contract implementation Note: OpenZeppelin has not yet
officially released the token for use.

You can independently deploy the contract alone using truffle and ganache and
test the contract features using truffle console. You can also deploy
your own private Hyperledger Besu network by following https://besu.hyperledger.org/en/stable/Tutorials/Private-Network/Create-IBFT-Network/ and then
deploy the contract and test.

Web3J based Java Client CLI:
Please refer to README in the eThalerClient directory to install and test
the java client against your own Besu network or connect with the following
prebuilt ERC1155 contract in the test Besu network.

Brief notes for Java CLI to connect with the test Besu network:

Requirements:
Java 8
web3j

Steps:

1. Create a key-store with password to store the private key for dealer account. Remember the password and use it to run the TesteThaler java CLI program.
   Run java TesteThaler eThaler.swapshub.com:8545

# Using with your own private Hyperledger Besu network

1. Create a bring up a Besu network: https://besu.hyperledger.org/en/stable/Tutorials/Private-Network/Create-IBFT-Network/

2. Create a new network name "besu" in truffle-config.js with the private key and network ID from your newly created network.

3. Run the following:

```bash
truffle migrate --network besu
```

4. After migrating, take the value of the first "address" present in eThaler/src/build/contracts/eThaler.json and set it as the ETHALER_CONTRACT_ADDR in src/CLI/config/config.properties

5. See src/CLI/ReadMe.txt for using the Cli with the network.
