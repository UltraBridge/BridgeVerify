package org.bridge.common;

import org.web3j.codegen.SolidityFunctionWrapperGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContractCodeGenerate {

    public static void main(String[] args) {
        List<String> options = new ArrayList<>();
        options.add("--javaTypes");
        options.add("-a");
        options.add(new File("common/src/main/resources").getAbsolutePath() + File.separator + "Bridge.abi");
        options.add("-p");
        options.add("org.bridge.common.contracts");
        options.add("-o");
        options.add(new File("common/src/main/java").getAbsolutePath());

        SolidityFunctionWrapperGenerator.main(options.toArray(new String[options.size()]));
    }
}