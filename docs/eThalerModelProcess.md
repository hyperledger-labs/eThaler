# Model eThaler using TTF

This document puts down the process used in modeling the eThaler. Different choices in TTF are presented with the choice and the reason for the choice expressed in this doc.

eThaler is modeled as an electronic currency issuable by the Fed. Mintable by the mint who can only issue it to the Fed. The Fed then transfers the token to the one of the institutions that have an account in the Fed. In exchange for eThaler, a corresponding amount in their deposit is transferred to the Fed account. So far we have the following roles: the mint, the Fed, institutions that have an account at the Fed.

Initially eThaler is modeled as a wholesale token. Which means that it can only be transferred between institutions that have an account with the Fed. When this transfer happens, the Fed does not need to get involved.

When the token is redeemed with the Fed, the Fed deposits eThaler into its account and the corresponding reserve is increased. Afterwards, the Fed can either use the eThaler in its account to transfer to another institution at their request or burn eThaler by transferring it to the mint or to a null account.

The KYC/AML is assumed to have been done since the institutions are all the ones who have an account with the Fed.

# Token Base

The choices we have are a. Fractional Fungible or b.Unique Fractional Fungible
For this use case my choice is Fractional Fungible.

From the base descriptions in TTF we have:

Fractional Fungible: Fractional Fungible tokens have interchangeable value with each other, where any owned sum of them from a class has the same value as another owned sum from the same class. Similar to physical cash money, a crypto currency is an example of a fungible token that is divisible.

Unique Fractional Fungible: Unique, fractional fungible tokens have interchangeable value with each other, where any owned sum of them from a class has the same value as another owned sum from the same class. Similar to physical cash money, a crypto-currencyis an example of a fungible token that is divisible. Because this token is unique, it will have its own identity and can have unique properties like a serial number. Implementations should support a GetBalance or List for owners to see their balances or tokens they own. 

_Physical cash has serial numbers, so they are unique fractional fungible, except for coins. Which implies indivisibility for the whole, but change can be made with other unique fractional fungible tokens. How can this be reconciled with eThaler? The workshop guide tells us further that UFF are tokens that have UTXO implementation, which is where analogy with physical cash disappears_

# Behaviors & Behavior Groups

The formulae that came up during the workshop are similar to the one for eMoney, we had some other attributes like loggable etc.

Workshop guide: tF{d,t,g,h,c,SC} is Emoney
Unpacking we have: It is a fungible token

d= Divisible An ability for the token to be divided from a single whole token into fractions, which are represented as decimal places. Any value greater than 0 will indicate how many fractions are possible where the smallest fraction is also the smallest ownable unit of the token. in our case this will be 100.

t= Transferable Every token instance has an owner. The Transferable behavior provides the owner the ability to transfer the ownership to another party or account. **This behavior is often inferred by other behaviors that might exist like Redeem, Sell, etc. This behavior is Delegable. If the token definition is Delegable, TransferFrom will be available** _Initially this will not be delegable._

g= Delegable A token class that implements this behavior will support the delegation of certain behaviors to another party or account to invoke them on the behalf of the owner. When applied to a token, behaviors that are Delegable will enable delegated request invocations. This is useful to provide another party to automatically be able to perform the behaviors that can be delegated without seeking permission up to a certain allowance. _How is the allowance controlled? is delegable needed for eThaler?_

h= Holdable Every token instance has an owner. The Transferable behavior provides the owner the ability to transfer the ownership to another party or account. A hold specifies a payer, a payee, a maximum amount, a notary and an expiration time. When the hold is created, the specified token balance from the payer is put on hold. A held balance cannot be transferred until the hold is either executed or released. The hold can only be executed (partially or the full amount) by the notary, which triggers the transfer of the tokens from the payer to the payee. If a hold is released, either by the notary at any time, or by anyone after the expiration, no transfer is carried out and the amount is available again for the payer. This behavior is Delegable. If the token definition is Delegable, HoldFrom will be available.  _Who is the notary? Is this behavior needed for eThaler? Seems to be no. However this might be needed for a Layer2 solution_

c= Compliant A regulated token needs to comply with several legal requirements, especially KYC and AML. If the necessary checks have to be made off-chain the token transfer becomes centralized. Further the transfer in this case takes longer to complete as it can not be done in one transaction, but requires a second confirmation step. A compliant token fulfills all legal requirements **on-chain** without interaction from an off-chain entity. _How will this be done on-chain? Not feasible for fast payments, in wholesale eThaler all roles can be assumed to be compliant. In which case do we need this behavior? What is needed to implement compliance only over a certain amount ($1000 according to FATF guidelines)? This may be needed only for retail eThaler._

SC stands for Supply Control, it is a **behavior group**, a short form for expressing *SC = {m, b, r}* *definitely needed for eThaler*

m = Mintable A token class that implements this behavior will support the minting or issuing of new token instances in the class. These new tokens can be minted and belong to the owneror minted to another account. This behavior may be invalidated by a restrictive behavior like Singleton, where only a single instance of the token can exist. Mintable is technically delegable, but it's delegation should be controlled by a behavior like Roles. 

b = Burnable token class that implements this behavior will support the burning or decommissioning of token instances of the class. This does not delete a token, but rather places it in a permanent non-use state.  Burning is a one way operation and cannot be reversed. This behavior is Delegable. If the token definition is Delegable, BurnFrom will be available.

r = Roles A token can have behaviors that the class will restrict invocations to a select set of parties or accounts that are members of a role or group.  This is a generic behavior that can apply to a token many times to represent many role definitions within the template. This behavior will allow you to define what role(s) to create and what behavior(s) to apply the role to in the TemplateDefinition. 

### Additional behaviors considered during workshop

a = Attestable A token class that implements this behavior will support abasic attestation request returning a true or false and if true it will return a cryptographic proof the requester may store for future validations. Attestable will accept a simple ownership query to validate that an account is the owner of the token or a attestation proof and validate it. *What is the relationship to Compliant? Does compliant only track the transfer and not the holder? in the first cut eThaler will not have compliance built into it since the role of the receiver of a wholesale token implies compliance*

e = Encumberable A token class that implements this behavior will have restrictions preventing certain behaviors like transferable,burnable, etc. from working while it is encumbered. The encumbering party should make a request to encumber, the owner should be notified about the request, and accept the request, which will finalize the encumbrance and send the EncumberResponse message to the requestor. *This behavior seems desirable, however there is no logic for releasing the encumbrance, Inital eThaler will not implement this behavior*

l = loggable A token class that implements this behavior will record log entries from its owner with a generic payload. These entries can be recorded stand alone and be given an unique identifier, EntryId, upon recording or these entries can be recorded in a series or group that will create a SeriesId and a EntryId, where all the entries will have a unique EntryId but have the same SeriesId. Log entries can be queried by their EntryId or you can request an entire series with the SeriesId. The last recorded entry can also be requested without an Id and you can also request entries from a starting point to a finish point. For example, you could request entries 100 through 125, which will return the entries starting at position 100 through 125 or the last entry recorded up to 125. To add entry query by any other property of the token, that property must be specifically defined and cannot be a property in the base token property list. *although seen as a nice to have, this need not be done for initial eThaler*

p = Pausable is an influencing behavior that can be applied to other behaviors in the Token. Pausable will have an applies to A token class that implements this behavior will halt trades and free all transfers, handy if there is a bug found in the token implementation. *Seems desirable in an initial eThaler*

q = Redeemable This behavior only applies to unique tokens.Redeemed tokens can no longer be spent.Redeeming a token removes an asset from the business network and guarantees that it can nolonger be transferred or changed.You redeem a quantity represented in a token or tokens you own.If the redemption amount is less than the quantity represented in your token submitted,the remaining quantity after redemption is deposited into a new token and returned to you as the owner. *Does not seem relevant, how is it going to be used for eThaler?*

u = Unique Transferable The unique transferable behavior provides the owner the ability to transfer the ownership to another party or account of one or more unique tokens owned.This behavior does not transfer the tokens themselves. Rather,new tokens are created by the transfer transaction.Because this behavior works with unique tokens, the invocation request can take multiple tokens as inputs to be transferred. The quantity of the assets being transferred to the recipients of the transaction needs to be the same quantity as the input tokens.If you do not want to transfer the entire quantity of the asset represented by the token,you can transfer a portion of the asset and the transaction will automatically make you the owner of the remaining balance. Using the example above, if you only spent 50 dollars of the 100 dollar token,the transfer transaction will automatically create a newtoken worth 50 dollars with you as the owner.All input tokens of the transaction need to be of the same type and the tokens being transferred need to belong to the transaction initiator and are unspent.
*Seems like a fairly straightforward description of UTXO a la bit coin with some minor differences- still does not jell with SKU and other features*

# Property Sets
*This boils down to whether eThaler is a unique token (like dollar bills) or just a fungible token, many other behaviors are influenced by this. If Unique we need to use a number like a SKU (or a serial number), then how do we make change? This relates to the property u referenced above*

# Token Classification 

*Token Unit- definitely needs to be fractional. Value type is Intrinsic. Representation type can be either common or unique tokens (this is our main conundrum for eThaler). Template Type can be single, but if we follow a hybrid rule then we can have common token* 


# eThalerWS functional specification

## Why eThaler
There are many CBDC projects under way. eThaler addresses many of the same functions. The main differentiators are:
- We are starting from a standard (TTF) and progressing to an implementation in Besu, also following a token standard.
- Completely open source
- Following the Fedcash protocol closely, however limiting the PoC to just the depositary institutions with a Fed account
- These institutions can instantaneously settle in eThaler with each other without counterparty risk or settling through the medium of the Fed.
- There will be reduced risk under the following categories.

## Functional Requirements

Although mimicking the physical cash system in its minting and distribution, eThalerWS is targeted at the interbank payment system. It is expected to interact with the future [payment system planned by the Fed](https://www.frbservices.org/financial-services/fednow/index.html "FedNow"). Details of this interaction cannot be known at this time. Such a recirculating wholesale CBDC can also ease the pressure on the demands on any payment system developed by the Fed. The settlement can be instantaneous and happen between the settling peers.

The FED mints eThalerWS (physical plants like the Bureau of Printing and Engraving or the US Mint are not needed) and stores eThalerWS in the Fed's own accounts. They can also burn eThalerWS, controlling the wholesale CBDC available for settlement.

Upon request from an institution with sufficient reserves, the Fed transfers the eThalerWS to the Wallet of the requesting institution (Bank P1). A corresponding amount is moved from Bank P1's reserve account into the Fed's account. If Bank P1 needs to pay another bank (Bank P2) for a transaction executed elsewhere, on any transaction system; Bank P2 is paid from the eThalerWS wallet of Bank P1. Bank P2 can subsequently pay another bank with this eThalerWS. The inter bank payments become a peer-to-peer system with no round trip to the Fed for each payment. Once any bank who now has the eThalerWS can optionally send the money back to the Fed and get its reserve account credited. 

In its first avatar, eThaler is a wholesale token to be exchanged between banks; next it needs to be expanded to retail, first via institutions to its account holders.  

## Roles
Since the implementation is on Besu, we use Role Based Access Control through openly available Open Zeppelin code.

For phase1 the following roles are available:

The Federal Reserve Bank (although there are 12 branches, represented by a single role in eThalerWS, in real life there many users)

38 % of Americas 8000 banks, bank holding companies have accounts at the Fed. All nationally chartered banks have accounts. In addition foreign banks also have accounts.

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
Fractional Token with an Intrinsic Value Type, representation type is common with a single template type. 


## Roles
- the FED
- the Mint (optional)
- the Institutions with a Fed Window Account

## eThalerWS Formula
We will call this **eThalerWS** for eThaler Wholesale.
After discussions on Feb 28, 2020 we settled on the following simplified token formula: **tF{d,t,c,p,SC}**
where SC={m,b,r}
This is still draft, comments and suggestions are welcome. We need input from the community. The aim was to produce 

To restate:

d= Divisible An ability for any eThalerWS to be divided into fractions, which are represented as decimal places. Any value greater than 0 will indicate how many fractions are possible where the smallest fraction is also the smallest ownable unit of the token. in our case this will be 100, since it mirrors dollars.

t= Transferable Every eThalerWS instance has an owner. The Transferable behavior provides the owner the ability to transfer the ownership to another party or account. Role restricted to the peers set up _Not delegable._

c= Compliant eThalerWS needs to comply with legal requirements, transfers between banks needs the regulator to have transparency into the reason. Hence a proof in the form of a signed transaction ID should be deposited from both the sender and receiver and they have to tally. A compliant token fulfills all legal requirements **on-chain** without interaction from an off-chain entity.

m = Mintable eThalerWS implements this behavior will support the minting or issuing of new token instances in the class. These new tokens can be minted and belong to the FED, the only role that can mint tokens, these tokens have to minted to the FED account.  

b = eThalerWS will be Burnable. This behavior will support the burning or decommissioning of token instances of the class. This does not delete a token, but rather places it in a permanent non-use state.  Burning is a one way operation and cannot be reversed. This can only be done from the FED account by the FED role.

r = Roles A token can have behaviors that the class will restrict invocations to a select set of parties or accounts that are members of a role or group.  This is a generic behavior that can apply to a token many times to represent many role definitions within the template. This behavior will allow you to define what role(s) to create and what behavior(s) to apply the role to in the TemplateDefinition. Many of the role vs. behavior interactions are given above.  




