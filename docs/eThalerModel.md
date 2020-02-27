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

Physical cash has serial numbers, so they are unique fractional fungible, except for coins. Which implies indivisibility for the whole, but change can be made with other unique fractional fungible tokens.

# Behaviors

The formulae that came up during the workshop are:
Workshop guide:
VB's formula:
MP's formula:

Let us get to the bottom of these behaviors

