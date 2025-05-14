package com.qcdfz.fengaiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MultiQueryExpanderDomeTest {
    @Resource
    private MultiQueryExpanderDome multiQueryExpanderDome;

    @Test
    void expand() {
        List<Query> expand = multiQueryExpanderDome.expand();
        Assertions.assertNotNull(expand);
    }
}
