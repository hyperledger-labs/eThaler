# eThaler (ETL) functional specification

## Why eThaler
There are many CBDC projects under way. eThaler addresses many of the same functions. The main differentiators are:
- We are starting from a standard (TTF) and progressing to an implementation in Besu, also following a token standard.
- Completely open source
- Following the Fedcash protocol closely, however limiting the PoC to just the depositary institutions with a Fed account
- These institutions can instantaneously settle in eThaler with each other without counterparty risk or settling through the medium of the Fed.
- There will be reduced risk under the following categories.

## Functional Requirements

Although mimicking the physical cash system in its minting and distribution, eThaler is targeted at the interbank payment system. It is expected to interact with the future [payment system planned by the Fed](https://www.frbservices.org/financial-services/fednow/index.html "FedNow"). 

Details of this interaction cannot be known at this time. Such a **recirculating** wholesale CBDC can also ease the pressure on the demands on any payment system developed by the Fed. The settlement can be instantaneous and happen between the settling peers.

In its first avatar, eThaler is a wholesale token to be exchanged between banks; next it needs to be expanded to retail, first via institutions to its account holders.  

The FED *mints* eThaler and stores eThaler in the Fed's own accounts, a wallet W0. They can also *burn* eThaler, controlling the wholesale CBDC available for settlement.

Upon request from an institution with sufficient reserves, the Fed transfers the eThaler to the wallet of the requesting institution (Bank P1), W1. A corresponding amount is moved from Bank P1's reserve account into the Fed's account. If Bank P1 needs to pay another bank (Bank P2) for a transaction executed elsewhere, on any transaction system; Bank P2 is paid eThaler into W2 from the eThaler W1. Bank P2 can subsequently pay another bank with this eThaler. The inter bank payments become a peer-to-peer system with no round trip to the Fed for each payment. Once any bank who now has the eThaler can optionally send the money back to the Fed (W0) and get its reserve account credited. 

## Roles

Since the implementation is on Besu, we use Role Based Access Control through Open Zeppelin code. Another idea is to use Hyperledger Indy.

For phase1 the following roles are available:

- The Federal Reserve Bank (although there are 12 branches, represented by a single role in eThaler)

38 % of Americas 8000 banks and bank holding companies have accounts at the Fed, all nationally chartered banks have accounts. In addition foreign banks also have accounts.

We represent these with 6 roles. Two for nationally charted banks two for state chartered banks and two more for FMUs.

FMUs (Financial Market Utilities)
    - The Clearing House Payments Company, L.L.C., on the basis of its role as operator of the Clearing House Interbank Payments System - (Board);
    - CLS Bank International - (Board);
    - Chicago Mercantile Exchange, Inc. - (Commodity Futures Trading Commission (CFTC));
    - The Depository Trust Company - (Securities and Exchange Commission (SEC));
    - Fixed Income Clearing Corporation - (SEC);
    - ICE Clear Credit L.L.C. - (CFTC);
    - National Securities Clearing Corporation - (SEC); and
    - The Options Clearing Corporation - (SEC).

## eThaler classification
*Fractional Token* with an *intrinsic* Value Type, representation type is *common* with a *single* template type. 


## Roles
- the FED Actions: Mint, Burn, Transfer (Both sender and receiver)
- the Institutions with a Fed Window Account Action: Transfer (Both sender and receiver)

## eThaler Formula

Token formula: **tF{d,t,c,p,SC}**
where SC={m,b,r}
This functional spec still draft, comments and suggestions are welcome. We need input from the community. 

*d*= Divisible An ability for any eThaler to be divided into fractions, which are represented as decimal places. Any value greater than 0 will indicate how many fractions are possible where the smallest fraction is also the smallest ownable unit of the token. in our case this will be 100, since it mirrors dollars.

*t*= Transferable, every eThaler instance has an owner. The transferable behavior provides the owner the ability to transfer the ownership to another party or account. Role restricted to the peers in the roles _Not delegable._

*c*= Compliant eThaler needs to comply with legal requirements, transfers between banks needs the regulator to have transparency into the reason. Hence a proof in the form of a signed transaction ID should be deposited from both the sender and receiver and they have to tally. A compliant token fulfills all legal requirements **on-chain** without interaction from an off-chain entity.

*m*= Mintable eThaler implements this behavior will support the minting or issuing of new token instances in the class. These new tokens can be minted and belong to the FED, the only role that can mint tokens, these tokens have to minted to the FED account.  

*b*= eThaler will be Burnable. This behavior will support the burning or decommissioning of token instances of the class. This does not delete a token, but rather places it in a permanent non-use state.  Burning is a one way operation and cannot be reversed. This can only be done from the FED account by the FED role.

*r* = Roles A token can have behaviors that the class will restrict invocations to a select set of parties or accounts that are members of a role or group.  This is a generic behavior that can apply to a token many times to represent many role definitions within the template. This behavior will allow you to define what role(s) to create and what behavior(s) to apply the role to in the TemplateDefinition. Many of the role vs. behavior interactions are given above.  
