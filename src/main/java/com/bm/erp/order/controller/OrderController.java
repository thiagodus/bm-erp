package com.bm.erp.order.controller;

import com.bm.erp.order.dto.OrderRequest;
import com.bm.erp.order.dto.OrderResponse;
import com.bm.erp.order.dto.OrderUpdateRequest;
import com.bm.erp.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest orderRequest){
        return orderService.save(orderRequest);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable UUID id){
        return orderService.findById(id);
    }

    @GetMapping
    public List<OrderResponse> findAll(){
        return orderService.findAll();
    }

    @PutMapping("/{id}")
    public OrderResponse update(@PathVariable UUID id,
                                @Valid @RequestBody OrderUpdateRequest request){
        return orderService.update(id, request);
    }
}
