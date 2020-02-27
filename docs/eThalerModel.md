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

# Behaviors

The formulae that came up during the workshop are similar to the one for eMoney, we had some other attributes like loggable etc.

Workshop guide: tF{d,t,g,h,c,SC} is Emoney
Unpacking we have: It is a fungible token

d= Divisible An ability for the token to be divided from a single whole token into fractions, which are represented as decimal places. Any value greater than 0 will indicate how many fractions are possible where the smallest fraction is also the smallest ownable unit of the token. in our case this will be 100.

t= Transferable Every token instance has an owner. The Transferable behavior provides the owner the ability to transfer the ownership to another party or account. **This behavior is often inferred by other behaviors that might exist like Redeem, Sell, etc. This behavior is Delegable. If the token definition is Delegable, TransferFrom will be available** _Initially this will not be delegable._

g= Delegable A token class that implements this behavior will support the delegation of certain behaviors to another party or account to invoke them on the behalf of the owner. When applied to a token, behaviors that are Delegable will enable delegated request invocations. This is useful to provide another party to automatically be able to perform the behaviors that can be delegated without seeking permission up to a certain allowance. _How is the allowance controlled? is this needed for delegable?_

h= Holdable Every token instance has an owner. The Transferable behavior provides the owner the ability to transfer the ownership to another party or account. A hold specifies a payer, a payee, a maximum amount, a notary and an expiration time. When the hold is created, the specified token balance from the payer is put on hold. A held balance cannot be transferred until the hold is either executed or released. The hold can only be executed (partially or the full amount) by the notary, which triggers the transfer of the tokens from the payer to the payee. If a hold is released, either by the notary at any time, or by anyone after the expiration, no transfer is carried out and the amount is available again for the payer. This behavior is Delegable. If the token definition is Delegable, HoldFrom will be available.  _Who is the notary? Is this behavior needed for eThaler?_

c= Compliant A regulated token needs to comply with several legal requirements, especially KYC and AML. If the necessary checks have to be made off-chain the token transfer becomes centralized. Further the transfer in this case takes longer to complete as it can not be done in one transaction, but requires a second confirmation step. A compliant token fulfills all legal requirements on-chain without interaction from an off-chain entity. _Not feasible for fast payments, in wholesale eThaler all roles can be assumed to be compliant. In which case do we need this behavior? What is needed to implement compliance only over a certain amount ($1000 according to FATF guidelines)? This may be needed only for retail eThaler._

SC is a behavior group, a short form for expressing *SC = {m, b, r}*

m = Mintable A token class that implements this behavior will support the minting or issuing of new token instances in the class. These new tokens can be minted and belong to the owneror minted to another account. This behavior may be invalidated by a restrictive behavior like Singleton, where only a single instance of the token can exist. Mintable is technically delegable, but it's delegation should be controlled by a behavior like Roles. 

b = Burnable token class that implements this behavior will support the burning or decommissioning of token instances of the class. This does not delete a token, but rather places it in a permanent non-use state.  Burning is a one way operation and cannot be reversed. This behavior is Delegable. If the token definition is Delegable, BurnFrom will be available.

r = Roles A token can have behaviors that the class will restrict invocations to a select set of parties or accounts that are members of a role or group.  This is a generic behavior that can apply to a token many times to represent many role definitions within the template. This behavior will allow you to define what role(s) to create and what behavior(s) to apply the role to in the TemplateDefinition.



