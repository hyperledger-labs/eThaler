ERC 20 & ERC 1155
The TTF behavior model had to use a specific platform for eThaler implementation. eThaler uses
Consensys Quorum which is an Ethereum variant, specifically targeted for enterprises. ERC 20
and ERC 1155 are Ethereum specific standards. ERC 20 has wide adoption. Most of the tokens
on the Ethereum mainnet are implemented using ERC 20. Defi tokens are also implemented in
ERC 20.
ERC 1155 is an evolving multi-token standard. eThaler implementation used ERC 1155 with
some extensions. There are several reasons for choosing ERC 1155. In ERC 1155, a single solidity
smart contract can implement multiple tokens. A single eThaler smart contract implementation
addresses multiple wCBDC implementations, each with slightly different characteristics. For
example, in response to COVID-19, CBs announced several special purpose financings schemes
with specific restrictions in a short period of time. The ERC 1155 contract could be used to
implement such schemes and also can track the effectiveness of such programs.
