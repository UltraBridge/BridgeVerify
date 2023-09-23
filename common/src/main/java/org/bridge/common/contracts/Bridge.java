package org.bridge.common.contracts;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.chain.common.base.exception.BusinessException;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.filters.FilterException;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
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
public class Bridge extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ADDADDRESS = "addAddress";

    public static final String FUNC_ADMINADDRESSEXISTS = "adminAddressExists";

    public static final String FUNC_DEPOSITNATIVE = "depositNative";

    public static final String FUNC_DEPOSITSELECTOR = "depositSelector";

    public static final String FUNC_DEPOSITTOKEN = "depositToken";

    public static final String FUNC_DROPADDRESS = "dropAddress";

    public static final String FUNC_DROPTASK = "dropTask";

    public static final String FUNC_GETADMINADDRESSES = "getAdminAddresses";

    public static final String FUNC_GETLOGICADDRESS = "getLogicAddress";

    public static final String FUNC_GETOPERATORREQUIRENUM = "getOperatorRequireNum";

    public static final String FUNC_GETOWNERREQUIRENUM = "getOwnerRequireNum";

    public static final String FUNC_GETSTOREADDRESS = "getStoreAddress";

    public static final String FUNC_GETTOKENPAIR = "getTokenPair";

    public static final String FUNC_GETTOKENPAIRCOUNT = "getTokenPairCount";

    public static final String FUNC_MODIFYADMINADDRESS = "modifyAdminAddress";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_REMOVEDONEORCANCELEDTASK = "removeDoneOrCanceledTask";

    public static final String FUNC_REMOVETASK = "removeTask";

    public static final String FUNC_REMOVETOKENPAIR = "removeTokenPair";

    public static final String FUNC_RESETREQUIREDNUM = "resetRequiredNum";

    public static final String FUNC_SETDEPOSITSELECTOR = "setDepositSelector";

    public static final String FUNC_SETTOKENPAIR = "setTokenPair";

    public static final String FUNC_SETWITHDRAWFEERATE = "setWithdrawFeeRate";

    public static final String FUNC_SETWITHDRAWSELECTOR = "setWithdrawSelector";

    public static final String FUNC_TOKENPAIR = "tokenPair";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERTOKEN = "transferToken";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_WITHDRAWFEERATE = "withdrawFeeRate";

    public static final String FUNC_WITHDRAWNATIVE = "withdrawNative";

    public static final String FUNC_WITHDRAWSELECTOR = "withdrawSelector";

    public static final String FUNC_WITHDRAWTOKEN = "withdrawToken";

    public static final Event ADMINCHANGED_EVENT = new Event("AdminChanged",
            Arrays.asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }));

    public static final Event ADMINREQUIREDNUMCHANGED_EVENT = new Event("AdminRequiredNumChanged",
            Arrays.asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));

    public static final Event ADMINTASKDROPPED_EVENT = new Event("AdminTaskDropped",
            Arrays.asList(new TypeReference<Bytes32>() {
            }));

    public static final Event DEPOSIT_EVENT = new Event("Deposit",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Uint256>() {
            }));

    public static final Event PAUSED_EVENT = new Event("Paused",
            Arrays.asList(new TypeReference<Address>() {
            }));

    public static final Event REMOVETASK_EVENT = new Event("RemoveTask",
            Arrays.asList(new TypeReference<Bytes32>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));

    public static final Event SETWITHDRAWFEERATE_EVENT = new Event("SetWithdrawFeeRate",
            Arrays.asList(new TypeReference<Uint256>(true) {
            }));

    public static final Event TOKENPAIR_EVENT = new Event("TokenPair",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Bool>() {
            }));

    public static final Event UNPAUSED_EVENT = new Event("Unpaused",
            Arrays.asList(new TypeReference<Address>() {
            }));

    public static final Event WITHDRAW_EVENT = new Event("Withdraw",
            Arrays.asList(new TypeReference<Bytes32>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Utf8String>() {
            }));

    @Deprecated
    protected Bridge(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Bridge(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Bridge(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Bridge(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AdminChangedEventResponse> getAdminChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<AdminChangedEventResponse> responses = new ArrayList<AdminChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.TaskType = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._class = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.oldAddress = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AdminChangedEventResponse>() {
            @Override
            public AdminChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADMINCHANGED_EVENT, log);
                AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
                typedResponse.log = log;
                typedResponse.TaskType = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._class = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.oldAddress = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADMINCHANGED_EVENT));
        return adminChangedEventFlowable(filter);
    }

    public List<AdminRequiredNumChangedEventResponse> getAdminRequiredNumChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADMINREQUIREDNUMCHANGED_EVENT, transactionReceipt);
        ArrayList<AdminRequiredNumChangedEventResponse> responses = new ArrayList<AdminRequiredNumChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AdminRequiredNumChangedEventResponse typedResponse = new AdminRequiredNumChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.TaskType = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._class = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.previousNum = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.requiredNum = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AdminRequiredNumChangedEventResponse> adminRequiredNumChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AdminRequiredNumChangedEventResponse>() {
            @Override
            public AdminRequiredNumChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADMINREQUIREDNUMCHANGED_EVENT, log);
                AdminRequiredNumChangedEventResponse typedResponse = new AdminRequiredNumChangedEventResponse();
                typedResponse.log = log;
                typedResponse.TaskType = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._class = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.previousNum = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.requiredNum = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AdminRequiredNumChangedEventResponse> adminRequiredNumChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADMINREQUIREDNUMCHANGED_EVENT));
        return adminRequiredNumChangedEventFlowable(filter);
    }

    public List<AdminTaskDroppedEventResponse> getAdminTaskDroppedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADMINTASKDROPPED_EVENT, transactionReceipt);
        ArrayList<AdminTaskDroppedEventResponse> responses = new ArrayList<AdminTaskDroppedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AdminTaskDroppedEventResponse typedResponse = new AdminTaskDroppedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.taskHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AdminTaskDroppedEventResponse> adminTaskDroppedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AdminTaskDroppedEventResponse>() {
            @Override
            public AdminTaskDroppedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADMINTASKDROPPED_EVENT, log);
                AdminTaskDroppedEventResponse typedResponse = new AdminTaskDroppedEventResponse();
                typedResponse.log = log;
                typedResponse.taskHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AdminTaskDroppedEventResponse> adminTaskDroppedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADMINTASKDROPPED_EVENT));
        return adminTaskDroppedEventFlowable(filter);
    }

    public List<DepositEventResponse> getDepositEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DEPOSIT_EVENT, transactionReceipt);
        ArrayList<DepositEventResponse> responses = new ArrayList<DepositEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DepositEventResponse typedResponse = new DepositEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.token = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.chain = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.nativeValue = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<DepositEventResponse> getDepositEvents(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock) throws Exception {
        EthFilter filter = new EthFilter(fromBlock, toBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(Bridge.DEPOSIT_EVENT));
        EthLog ethLog = web3j.ethGetLogs(filter).send();
        if (ethLog.getError() != null) {
            throw new BusinessException("get log error, " + ethLog.getError().getMessage());
        }
        List<EthLog.LogResult> logs = ethLog.getLogs();
        if (CollectionUtils.isEmpty(logs)) {
            return new ArrayList<>();
        }
        ArrayList<DepositEventResponse> responses = new ArrayList<>(logs.size());
        for (EthLog.LogResult logResult : logs) {
            if (logResult instanceof EthLog.LogObject) {
                Log log = ((EthLog.LogObject) logResult).get();
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DEPOSIT_EVENT, log);
                DepositEventResponse typedResponse = new DepositEventResponse();
                typedResponse.log = eventValues.getLog();
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.token = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.chain = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.nativeValue = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                responses.add(typedResponse);
            } else {
                throw new FilterException("Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
        return responses;
    }

    public Flowable<DepositEventResponse> depositEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DepositEventResponse>() {
            @Override
            public DepositEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DEPOSIT_EVENT, log);
                DepositEventResponse typedResponse = new DepositEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.token = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.chain = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.nativeValue = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DepositEventResponse> depositEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DEPOSIT_EVENT));
        return depositEventFlowable(filter);
    }

    public List<PausedEventResponse> getPausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSED_EVENT, transactionReceipt);
        ArrayList<PausedEventResponse> responses = new ArrayList<PausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PausedEventResponse typedResponse = new PausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PausedEventResponse>() {
            @Override
            public PausedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PAUSED_EVENT, log);
                PausedEventResponse typedResponse = new PausedEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PausedEventResponse> pausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSED_EVENT));
        return pausedEventFlowable(filter);
    }

    public List<RemoveTaskEventResponse> getRemoveTaskEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REMOVETASK_EVENT, transactionReceipt);
        ArrayList<RemoveTaskEventResponse> responses = new ArrayList<RemoveTaskEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RemoveTaskEventResponse typedResponse = new RemoveTaskEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.taskHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.taskType = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.supporterNum = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RemoveTaskEventResponse> removeTaskEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RemoveTaskEventResponse>() {
            @Override
            public RemoveTaskEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REMOVETASK_EVENT, log);
                RemoveTaskEventResponse typedResponse = new RemoveTaskEventResponse();
                typedResponse.log = log;
                typedResponse.taskHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.taskType = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.supporterNum = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RemoveTaskEventResponse> removeTaskEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REMOVETASK_EVENT));
        return removeTaskEventFlowable(filter);
    }

    public List<SetWithdrawFeeRateEventResponse> getSetWithdrawFeeRateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETWITHDRAWFEERATE_EVENT, transactionReceipt);
        ArrayList<SetWithdrawFeeRateEventResponse> responses = new ArrayList<SetWithdrawFeeRateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetWithdrawFeeRateEventResponse typedResponse = new SetWithdrawFeeRateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.withdrawFeeRate = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<SetWithdrawFeeRateEventResponse> getSetWithdrawFeeRateEvents(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock) throws Exception {
        EthFilter filter = new EthFilter(fromBlock, toBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(Bridge.SETWITHDRAWFEERATE_EVENT));
        EthLog ethLog = web3j.ethGetLogs(filter).send();
        if (ethLog.getError() != null) {
            throw new BusinessException("get log error, " + ethLog.getError().getMessage());
        }
        List<EthLog.LogResult> logs = ethLog.getLogs();
        if (CollectionUtils.isEmpty(logs)) {
            return new ArrayList<>();
        }
        ArrayList<SetWithdrawFeeRateEventResponse> responses = new ArrayList<>(logs.size());
        for (EthLog.LogResult logResult : logs) {
            if (logResult instanceof EthLog.LogObject) {
                Log log = ((EthLog.LogObject) logResult).get();
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETWITHDRAWFEERATE_EVENT, log);
                SetWithdrawFeeRateEventResponse typedResponse = new SetWithdrawFeeRateEventResponse();
                typedResponse.log = eventValues.getLog();
                typedResponse.withdrawFeeRate = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                responses.add(typedResponse);
            } else {
                throw new FilterException("Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
        return responses;
    }

    public Flowable<SetWithdrawFeeRateEventResponse> setWithdrawFeeRateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SetWithdrawFeeRateEventResponse>() {
            @Override
            public SetWithdrawFeeRateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETWITHDRAWFEERATE_EVENT, log);
                SetWithdrawFeeRateEventResponse typedResponse = new SetWithdrawFeeRateEventResponse();
                typedResponse.log = log;
                typedResponse.withdrawFeeRate = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SetWithdrawFeeRateEventResponse> setWithdrawFeeRateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETWITHDRAWFEERATE_EVENT));
        return setWithdrawFeeRateEventFlowable(filter);
    }

    public List<TokenPairEventResponse> getTokenPairEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENPAIR_EVENT, transactionReceipt);
        ArrayList<TokenPairEventResponse> responses = new ArrayList<TokenPairEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TokenPairEventResponse typedResponse = new TokenPairEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ownToken = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.ownChainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.ownTokenName = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ownWithdrawMinFee = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.ownWithdrawMaxFee = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.toChainId = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.toToken = (String) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.toTokenName = (String) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.enabled = (Boolean) eventValues.getNonIndexedValues().get(7).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<TokenPairEventResponse> getTokenPairEvents(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock) throws Exception {
        EthFilter filter = new EthFilter(fromBlock, toBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(Bridge.TOKENPAIR_EVENT));
        EthLog ethLog = web3j.ethGetLogs(filter).send();
        if (ethLog.getError() != null) {
            throw new BusinessException("get log error, " + ethLog.getError().getMessage());
        }
        List<EthLog.LogResult> logs = ethLog.getLogs();
        if (CollectionUtils.isEmpty(logs)) {
            return new ArrayList<>();
        }
        ArrayList<TokenPairEventResponse> responses = new ArrayList<>(logs.size());
        for (EthLog.LogResult logResult : logs) {
            if (logResult instanceof EthLog.LogObject) {
                Log log = ((EthLog.LogObject) logResult).get();
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TOKENPAIR_EVENT, log);
                TokenPairEventResponse typedResponse = new TokenPairEventResponse();
                typedResponse.log = eventValues.getLog();
                typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ownToken = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.ownChainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ownTokenName = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.ownWithdrawMinFee = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.ownWithdrawMaxFee = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.toChainId = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.toToken = (String) eventValues.getNonIndexedValues().get(5).getValue();
                typedResponse.toTokenName = (String) eventValues.getNonIndexedValues().get(6).getValue();
                typedResponse.enabled = (Boolean) eventValues.getNonIndexedValues().get(7).getValue();
                responses.add(typedResponse);
            } else {
                throw new FilterException("Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
        return responses;
    }

    public Flowable<TokenPairEventResponse> tokenPairEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TokenPairEventResponse>() {
            @Override
            public TokenPairEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TOKENPAIR_EVENT, log);
                TokenPairEventResponse typedResponse = new TokenPairEventResponse();
                typedResponse.log = log;
                typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ownToken = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.ownChainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ownTokenName = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.ownWithdrawMinFee = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.ownWithdrawMaxFee = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.toChainId = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.toToken = (String) eventValues.getNonIndexedValues().get(5).getValue();
                typedResponse.toTokenName = (String) eventValues.getNonIndexedValues().get(6).getValue();
                typedResponse.enabled = (Boolean) eventValues.getNonIndexedValues().get(7).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TokenPairEventResponse> tokenPairEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENPAIR_EVENT));
        return tokenPairEventFlowable(filter);
    }

    public List<UnpausedEventResponse> getUnpausedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UNPAUSED_EVENT, transactionReceipt);
        ArrayList<UnpausedEventResponse> responses = new ArrayList<UnpausedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UnpausedEventResponse typedResponse = new UnpausedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, UnpausedEventResponse>() {
            @Override
            public UnpausedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(UNPAUSED_EVENT, log);
                UnpausedEventResponse typedResponse = new UnpausedEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<UnpausedEventResponse> unpausedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSED_EVENT));
        return unpausedEventFlowable(filter);
    }

    public List<WithdrawEventResponse> getWithdrawEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(WITHDRAW_EVENT, transactionReceipt);
        ArrayList<WithdrawEventResponse> responses = new ArrayList<WithdrawEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            WithdrawEventResponse typedResponse = new WithdrawEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.token = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.taskHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.proof = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<WithdrawEventResponse> getWithdrawEvents(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock) throws Exception {
        EthFilter filter = new EthFilter(fromBlock, toBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(Bridge.WITHDRAW_EVENT));
        EthLog ethLog = web3j.ethGetLogs(filter).send();
        if (ethLog.getError() != null) {
            throw new BusinessException("get log error, " + ethLog.getError().getMessage());
        }
        List<EthLog.LogResult> logs = ethLog.getLogs();
        if (CollectionUtils.isEmpty(logs)) {
            return new ArrayList<>();
        }
        ArrayList<WithdrawEventResponse> responses = new ArrayList<>(logs.size());
        for (EthLog.LogResult logResult : logs) {
            if (logResult instanceof EthLog.LogObject) {
                Log log = ((EthLog.LogObject) logResult).get();
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(WITHDRAW_EVENT, log);
                WithdrawEventResponse typedResponse = new WithdrawEventResponse();
                typedResponse.log = eventValues.getLog();
                typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.token = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.taskHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.proof = (String) eventValues.getNonIndexedValues().get(4).getValue();
                responses.add(typedResponse);
            } else {
                throw new FilterException("Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
        return responses;
    }

    public Flowable<WithdrawEventResponse> withdrawEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, WithdrawEventResponse>() {
            @Override
            public WithdrawEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(WITHDRAW_EVENT, log);
                WithdrawEventResponse typedResponse = new WithdrawEventResponse();
                typedResponse.log = log;
                typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.token = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.taskHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.proof = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<WithdrawEventResponse> withdrawEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(WITHDRAW_EVENT));
        return withdrawEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addAddress(String _class, String oneAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDADDRESS,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(_class),
                        new org.web3j.abi.datatypes.Address(160, oneAddress)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> adminAddressExists(String _class, String _oneAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ADMINADDRESSEXISTS,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(_class),
                        new org.web3j.abi.datatypes.Address(160, _oneAddress)),
                Arrays.asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> depositNative(String _targetAddress, String chain) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DEPOSITNATIVE,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(_targetAddress),
                        new org.web3j.abi.datatypes.Utf8String(chain)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<String, Boolean>> depositSelector(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEPOSITSELECTOR,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, param0)),
                Arrays.asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Bool>() {
                }));
        return new RemoteFunctionCall<Tuple2<String, Boolean>>(function,
                new Callable<Tuple2<String, Boolean>>() {
                    @Override
                    public Tuple2<String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, Boolean>(
                                (String) results.get(0).getValue(),
                                (Boolean) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> depositToken(String _token, BigInteger value, String _targetAddress, String chain) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DEPOSITTOKEN,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, _token),
                        new org.web3j.abi.datatypes.generated.Uint256(value),
                        new org.web3j.abi.datatypes.Utf8String(_targetAddress),
                        new org.web3j.abi.datatypes.Utf8String(chain)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> dropAddress(String _class, String oneAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DROPADDRESS,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(_class),
                        new org.web3j.abi.datatypes.Address(160, oneAddress)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> dropTask(byte[] taskHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DROPTASK,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(taskHash)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getAdminAddresses(String _class) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETADMINADDRESSES,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(_class)),
                Arrays.asList(new TypeReference<DynamicArray<Address>>() {
                }));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> getLogicAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETLOGICADDRESS,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getOperatorRequireNum() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETOPERATORREQUIRENUM,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getOwnerRequireNum() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETOWNERREQUIRENUM,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getStoreAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTOREADDRESS,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Pair> getTokenPair(BigInteger index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETTOKENPAIR,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(index)),
                Arrays.asList(new TypeReference<Pair>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Pair.class);
    }

    public RemoteFunctionCall<BigInteger> getTokenPairCount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETTOKENPAIRCOUNT,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> modifyAdminAddress(String _class, String oldAddress, String newAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MODIFYADMINADDRESS,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(_class),
                        new org.web3j.abi.datatypes.Address(160, oldAddress),
                        new org.web3j.abi.datatypes.Address(160, newAddress)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> pause() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAUSE,
                Arrays.asList(),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> paused() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PAUSED,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeDoneOrCanceledTask(List<byte[]> taskHashList) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEDONEORCANCELEDTASK,
                Arrays.asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.datatypes.generated.Bytes32.class,
                        org.web3j.abi.Utils.typeMap(taskHashList, org.web3j.abi.datatypes.generated.Bytes32.class))),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeTask(List<byte[]> taskHashList) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVETASK,
                Arrays.asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.datatypes.generated.Bytes32.class,
                        org.web3j.abi.Utils.typeMap(taskHashList, org.web3j.abi.datatypes.generated.Bytes32.class))),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeTokenPair(String ownToken) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVETOKENPAIR,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, ownToken)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> resetRequiredNum(String _class, BigInteger requiredNum) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RESETREQUIREDNUM,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(_class),
                        new org.web3j.abi.datatypes.generated.Uint256(requiredNum)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setDepositSelector(String token, String method, Boolean _isValueFirst) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETDEPOSITSELECTOR,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, token),
                        new org.web3j.abi.datatypes.Utf8String(method),
                        new org.web3j.abi.datatypes.Bool(_isValueFirst)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setTokenPair(BigInteger ownChainId, String ownToken, String ownTokenName, BigInteger ownWithdrawMinFee, BigInteger ownWithdrawMaxFee, BigInteger toChainId, String toToken, String toTokenName, Boolean enabled) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETTOKENPAIR,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(ownChainId),
                        new org.web3j.abi.datatypes.Address(160, ownToken),
                        new org.web3j.abi.datatypes.Utf8String(ownTokenName),
                        new org.web3j.abi.datatypes.generated.Uint256(ownWithdrawMinFee),
                        new org.web3j.abi.datatypes.generated.Uint256(ownWithdrawMaxFee),
                        new org.web3j.abi.datatypes.generated.Uint256(toChainId),
                        new org.web3j.abi.datatypes.Address(160, toToken),
                        new org.web3j.abi.datatypes.Utf8String(toTokenName),
                        new org.web3j.abi.datatypes.Bool(enabled)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setWithdrawFeeRate(BigInteger _withdrawFeeRate) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETWITHDRAWFEERATE,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(_withdrawFeeRate)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setWithdrawSelector(String token, String method, Boolean _isValueFirst) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETWITHDRAWSELECTOR,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, token),
                        new org.web3j.abi.datatypes.Utf8String(method),
                        new org.web3j.abi.datatypes.Bool(_isValueFirst)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple10<String, BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, String, Boolean>> tokenPair(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOKENPAIR,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, param0)),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Bool>() {
                }));
        return new RemoteFunctionCall<Tuple10<String, BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, String, Boolean>>(function,
                new Callable<Tuple10<String, BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, String, Boolean>>() {
                    @Override
                    public Tuple10<String, BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple10<String, BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, String, Boolean>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (String) results.get(2).getValue(),
                                (String) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue(),
                                (BigInteger) results.get(5).getValue(),
                                (BigInteger) results.get(6).getValue(),
                                (String) results.get(7).getValue(),
                                (String) results.get(8).getValue(),
                                (Boolean) results.get(9).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFER,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferToken(String token, String to, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERTOKEN,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, token),
                        new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> unpause() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UNPAUSE,
                Arrays.asList(),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> withdrawFeeRate() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_WITHDRAWFEERATE,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawNative(String to, BigInteger value, String proof, byte[] taskHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWNATIVE,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value),
                        new org.web3j.abi.datatypes.Utf8String(proof),
                        new org.web3j.abi.datatypes.generated.Bytes32(taskHash)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<String, Boolean>> withdrawSelector(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_WITHDRAWSELECTOR,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, param0)),
                Arrays.asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Bool>() {
                }));
        return new RemoteFunctionCall<Tuple2<String, Boolean>>(function,
                new Callable<Tuple2<String, Boolean>>() {
                    @Override
                    public Tuple2<String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, Boolean>(
                                (String) results.get(0).getValue(),
                                (Boolean) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawToken(String _token, String to, BigInteger value, String proof, byte[] taskHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWTOKEN,
                Arrays.asList(new org.web3j.abi.datatypes.Address(160, _token),
                        new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value),
                        new org.web3j.abi.datatypes.Utf8String(proof),
                        new org.web3j.abi.datatypes.generated.Bytes32(taskHash)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Bridge load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Bridge(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Bridge load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Bridge(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Bridge load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Bridge(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Bridge load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Bridge(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class Pair extends DynamicStruct {
        public String operator;

        public BigInteger ownChainId;

        public String ownTokenName;

        public String ownToken;

        public BigInteger ownWithdrawMinFee;

        public BigInteger ownWithdrawMaxFee;

        public BigInteger toChainId;

        public String toToken;

        public String toTokenName;

        public Boolean enabled;

        public Pair(String operator, BigInteger ownChainId, String ownTokenName, String ownToken, BigInteger ownWithdrawMinFee, BigInteger ownWithdrawMaxFee, BigInteger toChainId, String toToken, String toTokenName, Boolean enabled) {
            super(new org.web3j.abi.datatypes.Address(operator), new org.web3j.abi.datatypes.generated.Uint256(ownChainId), new org.web3j.abi.datatypes.Utf8String(ownTokenName), new org.web3j.abi.datatypes.Address(ownToken), new org.web3j.abi.datatypes.generated.Uint256(ownWithdrawMinFee), new org.web3j.abi.datatypes.generated.Uint256(ownWithdrawMaxFee), new org.web3j.abi.datatypes.generated.Uint256(toChainId), new org.web3j.abi.datatypes.Address(toToken), new org.web3j.abi.datatypes.Utf8String(toTokenName), new org.web3j.abi.datatypes.Bool(enabled));
            this.operator = operator;
            this.ownChainId = ownChainId;
            this.ownTokenName = ownTokenName;
            this.ownToken = ownToken;
            this.ownWithdrawMinFee = ownWithdrawMinFee;
            this.ownWithdrawMaxFee = ownWithdrawMaxFee;
            this.toChainId = toChainId;
            this.toToken = toToken;
            this.toTokenName = toTokenName;
            this.enabled = enabled;
        }

        public Pair(Address operator, Uint256 ownChainId, Utf8String ownTokenName, Address ownToken, Uint256 ownWithdrawMinFee, Uint256 ownWithdrawMaxFee, Uint256 toChainId, Address toToken, Utf8String toTokenName, Bool enabled) {
            super(operator, ownChainId, ownTokenName, ownToken, ownWithdrawMinFee, ownWithdrawMaxFee, toChainId, toToken, toTokenName, enabled);
            this.operator = operator.getValue();
            this.ownChainId = ownChainId.getValue();
            this.ownTokenName = ownTokenName.getValue();
            this.ownToken = ownToken.getValue();
            this.ownWithdrawMinFee = ownWithdrawMinFee.getValue();
            this.ownWithdrawMaxFee = ownWithdrawMaxFee.getValue();
            this.toChainId = toChainId.getValue();
            this.toToken = toToken.getValue();
            this.toTokenName = toTokenName.getValue();
            this.enabled = enabled.getValue();
        }
    }

    public static class AdminChangedEventResponse extends BaseEventResponse {
        public String TaskType;

        public String _class;

        public String oldAddress;

        public String newAddress;
    }

    public static class AdminRequiredNumChangedEventResponse extends BaseEventResponse {
        public String TaskType;

        public String _class;

        public BigInteger previousNum;

        public BigInteger requiredNum;
    }

    public static class AdminTaskDroppedEventResponse extends BaseEventResponse {
        public byte[] taskHash;
    }

    public static class DepositEventResponse extends BaseEventResponse {
        public String from;

        public String token;

        public String to;

        public BigInteger value;

        public String chain;

        public BigInteger nativeValue;
    }

    public static class PausedEventResponse extends BaseEventResponse {
        public String account;
    }

    public static class RemoveTaskEventResponse extends BaseEventResponse {
        public byte[] taskHash;

        public BigInteger taskType;

        public BigInteger status;

        public BigInteger supporterNum;
    }

    public static class SetWithdrawFeeRateEventResponse extends BaseEventResponse {
        public BigInteger withdrawFeeRate;
    }

    public static class TokenPairEventResponse extends BaseEventResponse {
        public String operator;

        public String ownToken;

        public BigInteger ownChainId;

        public String ownTokenName;

        public BigInteger ownWithdrawMinFee;

        public BigInteger ownWithdrawMaxFee;

        public BigInteger toChainId;

        public String toToken;

        public String toTokenName;

        public Boolean enabled;
    }

    public static class UnpausedEventResponse extends BaseEventResponse {
        public String account;
    }

    public static class WithdrawEventResponse extends BaseEventResponse {
        public String to;

        public String token;

        public byte[] taskHash;

        public BigInteger status;

        public BigInteger value;

        public BigInteger fee;

        public String proof;
    }
}
