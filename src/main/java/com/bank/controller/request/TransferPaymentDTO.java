package com.bank.controller.request;

import lombok.Data;

@Data
public class TransferPaymentDTO {

    private Double avaiableBalance;
    private Double amountPaid;
    private Boolean isEnabled;
}
