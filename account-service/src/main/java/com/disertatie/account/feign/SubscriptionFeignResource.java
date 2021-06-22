package com.disertatie.account.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="subscription-service", url = "http://localhost:8765")
public interface SubscriptionFeignResource {

}
