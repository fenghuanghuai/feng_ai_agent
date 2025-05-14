package com.qcdfz.fengaiagent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {PgVectorStoreAutoConfiguration.class}
)
@MapperScan("com.qcdfz.fengaiagent.mapper")
public class FengAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FengAiAgentApplication.class, args);
    }

}
