UI-suggestions
UI should be driven by the underlying functions

A note on types:
1. address (basic Ethereum type 20 byte address link= https://solidity.readthedocs.io/en/v0.6.9/types.html?highlight=address#address )
2. uint256 for tokenId and amount


I. Offer choice about wallets
    a. open existing wallet 
    b. create new wallet

a. open existing wallet (next screen)
    If you open the Central Bank wallet

A. If You are the Central Bank User Offer a screen with following tabs

 Tabs: 
 Query|Token Supply|Transfer|Admin|Exit

Query
    Get Balance for own account
     
    Get Balance for another account
        tokenId
    Token details
        tokenId
    CheckTokenExists
        tokenId

Token Supply
        Mint
            tokenId & amount
        Burn
            tokenId & amount
Transfer
    tokenId & to(address) & amount 
    
    %result = success "Amount transferred to " 
    %result = error if to is wrong, amount out of bounds
Admin
    RegisterDealer
        address & tokenId
    UnRegisterDealer
        address & tokenId

    Add Token Definition
        tokenId (unique)
		string name (non-unique)- todo make it unique
		decimals uint8 
		url = string 

    Pause 
        tokenId
    Resume
        #can only be called after pause
        tokenId

B. If You are a Dealer

Tabs 
Query|Transfer|Exit

Transfer
    tokenId & to(address) & amount 
    
Query
    Get Balance for own account
        
    Token details
        tokenId
    CheckTokenExists
        tokenId
