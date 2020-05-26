
// Note: Interfaces and abstract contracts cannot be deployed and hence not linked
const SafeMath = artifacts.require("SafeMath");
const ERC1155 = artifacts.require("ERC1155");
const eThaler = artifacts.require("eThaler");

module.exports = function(deployer) {
  deployer.deploy(SafeMath);
  deployer.link(SafeMath, ERC1155);
  deployer.deploy(ERC1155);
  deployer.link(ERC1155, eThaler);
  deployer.deploy(eThaler);
};

