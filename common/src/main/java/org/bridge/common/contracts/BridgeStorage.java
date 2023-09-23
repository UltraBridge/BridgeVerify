package org.bridge.common.contracts;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.0.
 */
@SuppressWarnings("rawtypes")
public class BridgeStorage extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ADDSUPPORTER = "addSupporter";

    public static final String FUNC_GETTASKINFO = "getTaskInfo";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_REMOVEALLSUPPORTER = "removeAllSupporter";

    public static final String FUNC_REMOVETASK = "removeTask";

    public static final String FUNC_SETTASKINFO = "setTaskInfo";

    public static final String FUNC_SUPPORTEREXISTS = "supporterExists";

    @Deprecated
    protected BridgeStorage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BridgeStorage(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BridgeStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BridgeStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addSupporter(byte[] taskHash, String oneAddress) {
        final Function function = new Function(
                FUNC_ADDSUPPORTER,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(taskHash),
                        new org.web3j.abi.datatypes.Address(160, oneAddress)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>> getTaskInfo(byte[] taskHash) {
        final Function function = new Function(FUNC_GETTASKINFO,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(taskHash)),
                Arrays.asList(new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeAllSupporter(byte[] taskHash) {
        final Function function = new Function(
                FUNC_REMOVEALLSUPPORTER,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(taskHash)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeTask(byte[] taskHash) {
        final Function function = new Function(
                FUNC_REMOVETASK,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(taskHash)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setTaskInfo(byte[] taskHash, BigInteger taskType, BigInteger status) {
        final Function function = new Function(
                FUNC_SETTASKINFO,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(taskHash),
                        new org.web3j.abi.datatypes.generated.Uint256(taskType),
                        new org.web3j.abi.datatypes.generated.Uint256(status)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supporterExists(byte[] taskHash, String user) {
        final Function function = new Function(FUNC_SUPPORTEREXISTS,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(taskHash),
                        new org.web3j.abi.datatypes.Address(160, user)),
                Arrays.asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static BridgeStorage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BridgeStorage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BridgeStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BridgeStorage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BridgeStorage load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BridgeStorage(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BridgeStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BridgeStorage(contractAddress, web3j, transactionManager, contractGasProvider);
    }
}
