// SPDX-License-Identifier: MIT
pragma solidity ^0.7.0;

import "@openzeppelin/contracts/access/Ownable.sol";

contract TokenRegister is Ownable {
    mapping(address => mapping(string => address)) public regist;

    function setRegist(address fromAddress, string memory chain, address toAddress) public onlyOwner {
        regist[fromAddress][chain] = toAddress;
    }
}
