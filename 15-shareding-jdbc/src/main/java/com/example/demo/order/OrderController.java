package com.example.demo.order;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: zh4ngyj
 * @date: 2025/8/15 17:44
 * @des:
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Long create(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        return repo.create(userId, amount);
    }

    @GetMapping
    public List<Order> list(@RequestParam(defaultValue = "20") int limit) {
        return repo.listRecent(limit);
    }
}
