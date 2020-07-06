UI-suggestions
UI should be driven by the underlying functions

A note on types:
1. address (basic Ethereum type 20 byte address link= https://solidity.readthedocs.io/en/v0.6.9/types.html?highlight=address#address )
2. uint256 for tokenId and amount

Login screen opens with 
I. Offer choice about wallets
    a. open existing wallet
       Wallet name ("140woner") 
       Wallet Password ("a password string")
    b. create new wallet -
    

a. open existing wallet (next screen)
    If you open the Central Bank wallet

A. If You are the Central Bank User Offer a screen with following tabs

 Tabs: 
 Query|Token Supply|Transfer|Admin|Exit

Query Each one is driven off a list of Token Ids (which can be looked up)

    Get Balance for own account
        Token Id (1)
     
    Get Balance for another account
        Token Id (1)
        Address (0x9535fB57245ea162027449b39be10Ce32aa59e76)
        
    Token details
        Is a list of tokenIds, names, decimals, URL (a table can be used-see Add Token definition)


    CheckTokenExists
        Token Id (1)

Token Supply
        Mint
            Token Id (1)
            Amount (1000000) //amount can accept (1,000,000) but must send it as an amount to underlying web3
        Burn
            Token Id (1)
            Amount (1000000)
Transfer
            Token Id (1)
            To (0x9535fB57245ea162027449b39be10Ce32aa59e76)
            Amount (1000000)
    
    %result = success "Amount transferred to " 
    %result = error if to is wrong, amount out of bounds
Admin
    RegisterDealer
            Token Id (1)
            Dealer (0x9535fB57245ea162027449b39be10Ce32aa59e76)
    UnRegisterDealer
            Token Id (1)
            Dealer (0x9535fB57245ea162027449b39be10Ce32aa59e76)

    Add Token Definition
            Token Id (1) //should not clash with any existing TID
		    string name ("eGBP")- todo make it unique
		    decimals (2) 
		    url = (http://example.com/example.pdf) 

    Pause 
            Token Id (1)
    Resume
        #can only be called after pause
            Token Id (1)

B. If You are a Dealer

Tabs 
Query|Transfer|Exit

Transfer
            Token Id (1)
            To (0x9535fB57245ea162027449b39be10Ce32aa59e76)
            Amount (1000000)
   
Query
    Get Balance for own account
                Token Id (1)
        
    Token details
            Token Id (1)
    CheckTokenExists
            Token Id (1)
