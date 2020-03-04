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


