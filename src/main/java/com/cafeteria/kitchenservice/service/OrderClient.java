package com.cafeteria.kitchenservice.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ORDER-SERVICE", path = "/orders")
public interface OrderClient {

    @PutMapping("/{id}/internal-status")
    void updateOrderStatus(@PathVariable("id") Long orderId, @RequestBody Map<String, String> body);
}
