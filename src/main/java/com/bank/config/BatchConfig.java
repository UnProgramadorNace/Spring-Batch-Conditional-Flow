package com.bank.config;

import com.bank.batch.CancelTransactionTasklet;
import com.bank.batch.ProcessPaymentTasket;
import com.bank.batch.SendNotificationTasklet;
import com.bank.batch.ValidateAccountTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;


    @Bean
    public ValidateAccountTasklet validateAccountTasklet(){
        return new ValidateAccountTasklet();
    }

    @Bean
    public ProcessPaymentTasket processPaymentTasket(){
        return new ProcessPaymentTasket();
    }

    @Bean
    public CancelTransactionTasklet cancelTransactionTasklet(){
        return new CancelTransactionTasklet();
    }

    @Bean
    public SendNotificationTasklet sendNotificationTasklet(){
        return new SendNotificationTasklet();
    }

    @Bean
    @JobScope
    public Step validateAccount(){
        return stepBuilderFactory.get("validateAccount")
                .tasklet(validateAccountTasklet())
                .build();
    }

    @Bean
    public Step processPayment(){
        return stepBuilderFactory.get("processPayment")
                .tasklet(processPaymentTasket())
                .build();
    }

    @Bean
    public Step cancelTransaction(){
        return stepBuilderFactory.get("cancelTransaction")
                .tasklet(cancelTransactionTasklet())
                .build();
    }

    @Bean
    public Step sendNotification(){
        return stepBuilderFactory.get("sendNotification")
                .tasklet(sendNotificationTasklet())
                .build();
    }

    @Bean
    public Job transactionPaymentsJob(){
        return jobBuilderFactory.get("transactionPaymentsJob")
                .start(validateAccount())
                    .on("VALID").to(processPayment())
                    .next(sendNotification())

                .from(validateAccount())
                    .on("INVALID").to(cancelTransaction())
                    .next(sendNotification())

                .end()
                .build();
    }
}
