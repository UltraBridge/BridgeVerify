// SPDX-License-Identifier: MIT
pragma solidity ^0.7.0;

import "./BridgeLogic.sol";

contract BridgeAdmin is Container {
    /// @dev keccak256(abi.encodePacked('owner'))
    bytes32 internal constant OWNERHASH = 0x02016836a56b71f0d02689e69e326f4f4c1b9057164ef592671cf0d37c8040c0;
    /// @dev keccak256(abi.encodePacked('operator'))
    bytes32 internal constant OPERATORHASH = 0x46a52cf33029de9f84853745a87af28464c80bf0346df1b32e205fc73319f622;
    /// @dev keccak256(abi.encodePacked('pauser'))
    bytes32 internal constant PAUSERHASH = 0x0cc58340b26c619cd4edc70f833d3f4d9d26f3ae7d5ef2965f81fe5495049a4f;
    /// @dev keccak256(abi.encodePacked('store'))
    bytes32 internal constant STOREHASH = 0xe41d88711b08bdcd7556c5d2d24e0da6fa1f614cf2055f4d7e10206017cd1680;
    /// @dev keccak256(abi.encodePacked('logic'))
    bytes32 internal constant LOGICHASH = 0x397bc5b97f629151e68146caedba62f10b47e426b38db589771a288c0861f182;
    uint256 internal constant MAXUSERNUM = 255;
    bytes32[] private classHashArray;

    uint256 internal ownerRequireNum;
    uint256 internal operatorRequireNum;

    event AdminChanged(string TaskType, string _class, address oldAddress, address newAddress);
    event AdminRequiredNumChanged(string TaskType, string _class, uint256 previousNum, uint256 requiredNum);
    event AdminTaskDropped(bytes32 taskHash);

    modifier validRequirement(uint256 ownerCount, uint256 _required) {
        require(ownerCount <= MaxItemAdressNum && _required <= ownerCount && _required > 0 && ownerCount > 0);
        _;
    }

    modifier onlyOwner() {
        require(itemAddressExists(OWNERHASH, msg.sender), "only use owner to call");
        _;
    }

    function initAdmin(address[] memory _owners, uint256 _ownerRequired) internal validRequirement(_owners.length, _ownerRequired) {
        for (uint256 i = 0; i < _owners.length; i++) {
            addItemAddress(OWNERHASH, _owners[i]);
        }
        addItemAddress(PAUSERHASH, _owners[0]); // we need an init pauser
        addItemAddress(LOGICHASH, address(0x0));
        addItemAddress(STOREHASH, address(0x1));

        classHashArray.push(OWNERHASH);
        classHashArray.push(OPERATORHASH);
        classHashArray.push(PAUSERHASH);
        classHashArray.push(STOREHASH);
        classHashArray.push(LOGICHASH);
        ownerRequireNum = _ownerRequired;
        operatorRequireNum = 2;
    }

    function classHashExist(bytes32 aHash) private view returns (bool) {
        for (uint256 i = 0; i < classHashArray.length; i++) if (classHashArray[i] == aHash) return true;
        return false;
    }

    function getAdminAddresses(string memory _class) public view returns (address[] memory) {
        bytes32 classHash = getClassHash(_class);
        return getItemAddresses(classHash);
    }

    function getOwnerRequireNum() public view returns (uint256) {
        return ownerRequireNum;
    }

    function getOperatorRequireNum() public view returns (uint256) {
        return operatorRequireNum;
    }

    function resetRequiredNum(string memory _class, uint256 requiredNum) public onlyOwner returns (bool) {
        bytes32 classHash = getClassHash(_class);
        require((classHash == OPERATORHASH) || (classHash == OWNERHASH), "wrong class");

        bytes32 taskHash = keccak256(abi.encodePacked("resetRequiredNum", _class, requiredNum));
        addItemAddress(taskHash, msg.sender);

        if (getItemAddressCount(taskHash) >= ownerRequireNum) {
            removeItem(taskHash);
            uint256 previousNum = 0;
            if (classHash == OWNERHASH) {
                previousNum = ownerRequireNum;
                ownerRequireNum = requiredNum;
            } else if (classHash == OPERATORHASH) {
                previousNum = operatorRequireNum;
                operatorRequireNum = requiredNum;
            } else {
                revert("wrong class");
            }
            emit AdminRequiredNumChanged("resetRequiredNum", _class, previousNum, requiredNum);
        }
        return true;
    }

    function modifyAddress(
        string memory _class,
        address oldAddress,
        address newAddress
    ) internal onlyOwner returns (bool) {
        bytes32 classHash = getClassHash(_class);
        bytes32 taskHash = keccak256(abi.encodePacked("modifyAddress", _class, oldAddress, newAddress));
        addItemAddress(taskHash, msg.sender);
        if (getItemAddressCount(taskHash) >= ownerRequireNum) {
            replaceItemAddress(classHash, oldAddress, newAddress);
            emit AdminChanged("modifyAddress", _class, oldAddress, newAddress);
            removeItem(taskHash);
            return true;
        }
        return false;
    }

    function getClassHash(string memory _class) private view returns (bytes32) {
        bytes32 classHash = keccak256(abi.encodePacked(_class));
        require(classHashExist(classHash), "invalid class");
        return classHash;
    }

    function dropAddress(string memory _class, address oneAddress) public onlyOwner returns (bool) {
        bytes32 classHash = getClassHash(_class);
        require(classHash != STOREHASH && classHash != LOGICHASH, "wrong class");
        require(itemAddressExists(classHash, oneAddress), "no such address exist");

        if (classHash == OWNERHASH) require(getItemAddressCount(classHash) > ownerRequireNum, "insuffience addresses");

        bytes32 taskHash = keccak256(abi.encodePacked("dropAddress", _class, oneAddress));
        addItemAddress(taskHash, msg.sender);
        if (getItemAddressCount(taskHash) >= ownerRequireNum) {
            removeOneItemAddress(classHash, oneAddress);
            emit AdminChanged("dropAddress", _class, oneAddress, oneAddress);
            removeItem(taskHash);
            return true;
        }
        return false;
    }

    function addAddress(string memory _class, address oneAddress) public onlyOwner returns (bool) {
        bytes32 classHash = getClassHash(_class);
        require(classHash != STOREHASH && classHash != LOGICHASH, "wrong class");

        bytes32 taskHash = keccak256(abi.encodePacked("addAddress", _class, oneAddress));
        addItemAddress(taskHash, msg.sender);
        if (getItemAddressCount(taskHash) >= ownerRequireNum) {
            addItemAddress(classHash, oneAddress);
            emit AdminChanged("addAddress", _class, oneAddress, oneAddress);
            removeItem(taskHash);
            return true;
        }
        return false;
    }

    function dropTask(bytes32 taskHash) public onlyOwner returns (bool) {
        removeItem(taskHash);
        emit AdminTaskDropped(taskHash);
        return true;
    }
}
