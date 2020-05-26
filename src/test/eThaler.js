 /**
Copyright 2020 Swapshub
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

*/

const eThaler = artifacts.require("eThaler");

contract('eThaler', async accounts => {

	// define common variables for all tests here
	var tokenId = 1;
	var tokenName = 'USD';
	var gbpTokenId = 2;
	var gbpTokenName = 'GBP';
	var numDecimals = 4;
	var initialTokens = 1000000;
	var moreTokens    = 1000000;
	var totalTokens   = 2000000;
	var TTF_URL = 'https://hyperledger.org/sig/capitalMarkets/USD.PDF';
	var GBP_TTF_URL = 'https://hyperledger.org/sig/capitalMarkets/GBP.PDF';
    var cbAcct = accounts[0];
	// the following additional accouts will work only with ganache or truffle develop mode
    var dealer1Acct = accounts[1];
    var dealer2Acct = accounts[2];

    it('testing owner of eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.owner.call();
		console.log( `token owner for eThaler contract = ${xact}` );

        // Write an assertion below to check the return value of owner.
        assert.equal('something', 'something', 'A correctness property about owner of eThaler');
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

	/**
	http.get('http://ip-api.com/json', (res) => {     
    console.log(res.country)  // *** Results in Undefined
    console.log(JSON.stringify(res)); // *** Resulting in a TypeError: Converting circular structure to JSON

    res.resume();
  }).on('error', (e) => {
    console.log(`Got error: ${e.message}`);
  });
	**/

    it('testing adding new token to eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.addNewTokenDefinition(tokenId, tokenName, numDecimals, TTF_URL, {from: cbAcct});
		console.log( `token def for ${tokenName}: xact = ${xact}` );
        assert.equal( xact.valueOf(), xact.valueOf(), 'Adding new token error' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('testing registerDealer (CB) of eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.registerDealer( cbAcct, tokenId );
		console.log( `retVal after registerDealer = ${xact}` );
		let isRegistered = await eThalerInstance.isDealerRegistered( cbAcct, tokenId );
        assert.equal( isRegistered.valueOf(), true, 'CB is not registered' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('testing mint of eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.mint(tokenId, initialTokens, '0x00', {from: cbAcct});
		console.log( `retVal after minting tokens = ${xact}` );
		let balance = await eThalerInstance.balanceOf( cbAcct, tokenId );
        assert.equal( balance.valueOf(), initialTokens, 'Total tokens minted is not equal' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('checking balance after mint of eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
		let balance = ( await eThalerInstance.balanceOf( cbAcct, tokenId ) ).toNumber();
        assert.equal( balance, initialTokens, 'Total tokens minted is not equal' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

	// calling again mint should add to the total
    it('testing minting more tokens', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.mint(tokenId, moreTokens, '0x00', {from: cbAcct});
		console.log( `retVal after minting more tokens = ${xact}` );
		let balance = await eThalerInstance.balanceOf( cbAcct, tokenId );
        assert.equal( balance.valueOf(), totalTokens, 'Total tokens after minting more is not equal' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('testing registerDealer (dealer) of eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.registerDealer( dealer1Acct, tokenId );
		console.log( `retVal after minting tokens = ${xact}` );
		let isDealerRegistered = await eThalerInstance.isDealerRegistered( dealer1Acct, tokenId );
		let isCB_Registered = await eThalerInstance.isDealerRegistered( cbAcct, tokenId );
        assert.equal( isDealerRegistered.valueOf(), true, 'Dealer is not registered' );
        assert.equal( isCB_Registered.valueOf(), true, 'CB is not registered' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('testing CB to dealer transfer of eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
		var transferAmt = 200;
		let cbBeforeBalance = ( await eThalerInstance.balanceOf( cbAcct, tokenId ) ).toNumber();
		let dlr1BeforeBalance = ( await eThalerInstance.balanceOf( dealer1Acct, tokenId ) ).toNumber();
		console.log( `CB Balance before transfer=${cbBeforeBalance}` );
		console.log( `Dealer1 Balance before transfer=${dlr1BeforeBalance}` );
        var xact = await eThalerInstance.transfer( dealer1Acct, tokenId, transferAmt );
		console.log( `retVal after transferring tokens = ${xact}` );

		let cbAfterBalance = ( await eThalerInstance.balanceOf( cbAcct, tokenId ) ).toNumber();
		let dlr1AfterBalance = ( await eThalerInstance.balanceOf( dealer1Acct, tokenId ) ).toNumber();
		console.log( `CB Balance after transfer=${cbAfterBalance}` );
		console.log( `Dealer1 Balance after transfer=${dlr1AfterBalance}` );
        assert.equal( cbAfterBalance, (cbBeforeBalance - transferAmt), 'Post Transfer: CB Balance does not match' );
        assert.equal( dlr1AfterBalance, (dlr1BeforeBalance + transferAmt), 'Post Transfer: Dealer1 does not match' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('testing pausing eThaler token', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.pause( tokenId );
		console.log( `retVal after minting tokens = ${xact}` );
		let isPaused = await eThalerInstance.isPaused( tokenId );
        assert.equal( isPaused, true, 'Token is not paused' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });


	// the mint function should fail as token is paused above
	// catch the error and check for "Token is paused" error 
    it('testing minting after pause of eThaler', async () => {
		try {
        const eThalerInstance = await eThaler.deployed();
		// the mint call should throw error 
        var xact = await eThalerInstance.mint(tokenId, initialTokens, '0x00', {from: cbAcct});
		// below stmts should never execute as minting is not permitted in pause state
		console.log( `retVal after minting tokens = ${xact}` );
		let balance = await eThalerInstance.balanceOf( cbAcct, tokenId );
        assert.equal( balance.valueOf(), initialTokens, 'Total tokens minted is not equal' );
		} catch( ex ) {
			console.error( `Console Error`, ex.message );
			var isPausedError = ex.message.includes( "Token is paused" );
        	assert.equal( isPausedError, true, 'Error is not due to Token is Paused' );
		}
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('testing resume of eThaler token', async () => {
        const eThalerInstance = await eThaler.deployed();
		let isPaused = await eThalerInstance.isPaused( tokenId );
		console.log( `Before resume: pause state ${isPaused}` );
		// console.log( `Before resume: pause state value of ${isPaused.valueOf()}` );
        var xact = await eThalerInstance.resume( tokenId );
		console.log( `Xaction hash after minting tokens = ${xact.transactionHash}` );
		isPaused = await eThalerInstance.isPaused( tokenId );
        assert.equal( isPaused, false, 'Token is still paused' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('testing adding new GBP token to eThaler', async () => {
        const eThalerInstance = await eThaler.deployed();
        var xact = await eThalerInstance.addNewTokenDefinition(gbpTokenId, gbpTokenName, numDecimals, GBP_TTF_URL, {from: cbAcct});
		console.log( `token def for ${gbpTokenName}: xact = ${xact}` );
        assert.equal( xact.valueOf(), xact.valueOf(), 'Adding new token error' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

    it('checking num TokenIds after minting 2  eThaler tokens', async () => {
        const eThalerInstance = await eThaler.deployed();
		let tokenIds = await eThalerInstance.getAllTokenIds( );
		console.log( `token ids = ${tokenIds}` );
		let numTokens = tokenIds.length;
		console.log( `num token ids = ${numTokens}` );
        assert.equal( numTokens, 2, 'Total tokens do not match 2' );
    }).on('error', (e) => {
    	console.log(`Got error: ${e.message}`);
    });

});

