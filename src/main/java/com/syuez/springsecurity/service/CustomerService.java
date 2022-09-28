package com.syuez.springsecurity.service;

import com.syuez.springsecurity.entity.Authority;
import com.syuez.springsecurity.entity.Customer;
import com.syuez.springsecurity.repository.AuthorityRepository;
import com.syuez.springsecurity.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
/*
* CustomerService 是项目中存在的 Customer 业务处理类，该类结合 Redis 缓存定义了通过 username 获取用户信息和用户权限信息的方法。
* */

/**
 * 对用户数据结合 Redis 缓存进行业务处理
 */
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 业务控制：使用唯一用户名查询用户信息
     * @param username 用户名
     */
    public Customer getCustomer(String username) {
        Customer customer = null;
        Object object = redisTemplate.opsForValue().get("customer_" + username);
        if(object == null) {
            Optional<Customer> optional = Optional.ofNullable(customerRepository.findByUsername(username));
            if(optional.isPresent()) {
                customer = optional.get();
                redisTemplate.opsForValue().set("customer_" + username,customer);
            }
        } else {
            customer = (Customer) object;
        }
        return customer;
    }

    /**
     * 业务控制：使用唯一用户名查询用户权限
     * @param username 用户名
     */
    public List<Authority> getCustomerAuthority(String username) {
        List<Authority> authorities = null;
        Object object = redisTemplate.opsForValue().get("authorities_" + username);
        if(object == null) {
            Optional<List<Authority>> optional = Optional.ofNullable(authorityRepository.findAuthoritiesByUsername(username));
            if(optional.isPresent()) {
                authorities = optional.get();
                if(!authorities.isEmpty()) {
                    redisTemplate.opsForValue().set("authorities_" + username, authorities);
                }
            }
        } else {
            authorities = (List<Authority>) object;
        }

        return authorities;
    }

}
