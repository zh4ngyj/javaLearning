package com.example.demo.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: zh4ngyj
 * @date: 2025/8/15 17:41
 * @des:
 */
public class Order {

    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createTime;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
