package com.hba.hbacore.paying.paypal;

import com.hba.hbacore.paying.PayingService;
import com.hba.hbacore.paying.PaymentDetail;
import com.hba.hbacore.paying.PaymentResponse;
import com.hba.hbacore.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PaypalService implements PayingService {

  private static final String DEFAULT_CURRENCY_CODE = "USD";
  private static final String SANDBOX_URL = "https://api-m.sandbox.paypal.com";

  private static final Logger log = LoggerFactory.getLogger(PaypalService.class);

  private final RestTemplate restTemplate;
  private final ApplicationContext apiContext = new ApplicationContext();
  private final PaypalClientInfo clientInfo;

  private String accessToken;

  public PaypalService(RestTemplate restTemplate, PaypalClientInfo clientInfo) {
    this.restTemplate = restTemplate;
    this.clientInfo = clientInfo;
    apiContext
        .setCancelUrl(this.clientInfo.getCancelUrl())
        .setReturnUrl(this.clientInfo.getReturnUrl());
    authenticate();
  }

  @Override
  public PaymentResponse executePayment(String paymentId, String payerId) {
    return null;
  }

  @Override
  public PaymentDetail getPaymentDetail(String orderId) {
    return null;
  }

  private void authenticate() {
    HttpHeaders headers = new HttpHeaders();
    RequestUtil.addBasicAuthHeader(headers, clientInfo.getClientId(), clientInfo.getClientSecret());
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<Map<String, String>> request =
        new HttpEntity<>(Map.of("grant_type", "client_credentials"), headers);

    try {
      AccessTokenResponse response =
          restTemplate.postForObject(
              SANDBOX_URL + "/v1/oauth2/token", request, AccessTokenResponse.class);
      if (response != null && response.getAccessToken() != null) {
        this.accessToken = response.getAccessToken();
        log.debug("Paypal access token: {}", response);
      }
    } catch (RestClientException e) {
      throw new PaypalRestException("unexpected error", e);
    }
  }
}