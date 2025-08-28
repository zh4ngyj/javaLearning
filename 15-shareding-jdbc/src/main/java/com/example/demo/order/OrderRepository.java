package com.example.demo.order;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * @author: zh4ngyj
 * @date: 2025/8/15 17:42
 * @des:
 */
@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 插入时不指定 order_id，ShardingSphere 会用 SNOWFLAKE 生成分布式主键
    public Long create(Long userId, BigDecimal amount) {
        final String sql = "INSERT INTO t_order(user_id, amount, status, create_time) VALUES (?, ?, ?, NOW())";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setBigDecimal(2, amount);
            ps.setString(3, "CREATED");
            return ps;
        }, kh);
        Number key = kh.getKey();
        return key == null ? null : key.longValue();
    }

    public List<Order> listRecent(int limit) {
        String sql = "SELECT order_id, user_id, amount, status, create_time " +
                "FROM t_order ORDER BY create_time DESC LIMIT ?";
        return jdbcTemplate.query(sql, rs -> {
            new Object(); // placeholder
        }, ps -> ps.setInt(1, limit), (rs, rowNum) -> {
            Order o = new Order();
            o.setOrderId(rs.getLong("order_id"));
            o.setUserId(rs.getLong("user_id"));
            o.setAmount(rs.getBigDecimal("amount"));
            o.setStatus(rs.getString("status"));
            o.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            return o;
        });
    }
}
