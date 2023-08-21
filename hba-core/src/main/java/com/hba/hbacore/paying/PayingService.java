package com.hba.hbacore.paying;

public interface PayingService {

  PaymentResponse executePayment(String paymentId, String payerId);

  PaymentDetail getPaymentDetail(String orderId);
}