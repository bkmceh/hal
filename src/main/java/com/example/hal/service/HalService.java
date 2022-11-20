package com.example.hal.service;

import com.example.hal.exceptions.CustomerNotFoundException;
import com.example.hal.exceptions.OrderNotFoundException;
import com.example.hal.model.customers.CustomerEntity;
import com.example.hal.model.dto.CustomerDTO;
import com.example.hal.model.dto.OrderDTO;
import com.example.hal.model.orders.OrderEntity;
import com.example.hal.model.dto.response.CustomerResponse;
import com.example.hal.model.dto.response.OrderResponse;
import com.example.hal.repository.CustomerRepo;
import com.example.hal.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HalService {

    private final CustomerRepo customerRepository;

    private final OrderRepo orderRepository;

    public List<CustomerResponse> getCustomers() {
        List<CustomerEntity> customerEntities = customerRepository.findAll();
        List<CustomerResponse> responses = new ArrayList<>(customerEntities.size());
        for (CustomerEntity customerEntity : customerEntities) {
            responses.add(createCustomerResponse(customerEntity));
        }
        return responses;
    }

    public CustomerResponse getCustomer(final Long id) {
        final Optional<CustomerEntity> foundCustomer = customerRepository.findById(id);
        if (foundCustomer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found");
        }
        return createCustomerResponse(foundCustomer.get());
    }

    public CustomerResponse addCustomer(final CustomerDTO customerDTO) {
        final CustomerEntity entity = new CustomerEntity();
        entity.setEmail(customerDTO.getEmail());
        entity.setName(customerDTO.getName());
        customerRepository.save(entity);
        return createCustomerResponse(entity);
    }

    public List<OrderResponse> getOrders() {
        final List<OrderEntity> orderEntities = orderRepository.findAll();
        List<OrderResponse> responses = new ArrayList<>(orderEntities.size());
        for (OrderEntity orderEntity : orderEntities) {
            final OrderResponse response = createOrderResponse(orderEntity);
            responses.add(response);
        }
        return responses;
    }

    public List<OrderResponse> getOrders(final Long userId) {
        final Optional<CustomerEntity> foundCustomer = customerRepository.findById(userId);
        if (foundCustomer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found");
        }
        final Optional<List<OrderEntity>> customerOrders = orderRepository.findAllByCustomerEntity(foundCustomer.get());
        if (customerOrders.isEmpty()) {
            throw new OrderNotFoundException("Orders empty");
        }
        final List<OrderResponse> responses = new ArrayList<>(customerOrders.get().size());
        for (OrderEntity entity : customerOrders.get()) {
            responses.add(createOrderResponse(entity));
        }
        return responses;
    }

    public OrderResponse getOrder(final Long id) {
        final Optional<OrderEntity> foundOrder = orderRepository.findById(id);
        if (foundOrder.isEmpty()) {
            throw new OrderNotFoundException("Order not found");
        }
        return createOrderResponse(foundOrder.get());
    }

    public OrderResponse addOrder(final OrderDTO orderDTO) {
        OrderEntity entity = new OrderEntity();
        entity.setName(orderDTO.getName());
        Optional<CustomerEntity> entityOptional = customerRepository.findById(orderDTO.getCustomerId());
        if (entityOptional.isPresent()) {
            entity.setCustomerEntity(entityOptional.get());
        }
        orderRepository.save(entity);
        return createOrderResponse(entity);
    }

    private CustomerResponse createCustomerResponse(final CustomerEntity entity) {
        final CustomerResponse response = new CustomerResponse();
        response.setId(entity.getId());
        response.setCustomerName(entity.getName());
        response.setCustomerEmail(entity.getEmail());
        response.setOrdersId(entity.getOrderEntities().stream().map(OrderEntity::getId).toList());
        return response;
    }

    private OrderResponse createOrderResponse(final OrderEntity entity) {
        final OrderResponse response = new OrderResponse();
        response.setId(entity.getId());
        response.setOrderName(entity.getName());
        response.setCustomerResponse(createCustomerResponse(entity.getCustomerEntity()));
        return response;
    }
}
