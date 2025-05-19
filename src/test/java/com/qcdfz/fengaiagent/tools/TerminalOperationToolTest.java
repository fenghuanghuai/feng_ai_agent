package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TerminalOperationToolTest {

    @Test
    void executeTerminalCommand() {
        TerminalOperationTool tool = new TerminalOperationTool();
        String command= "dir";
        String result = tool.executeTerminalCommand(command);
        Assertions.assertNotNull(result);
    }
}
