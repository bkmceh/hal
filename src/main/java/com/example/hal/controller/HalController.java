package com.example.hal.controller;

import com.example.hal.model.dto.CustomerDTO;
import com.example.hal.model.dto.OrderDTO;
import com.example.hal.model.dto.response.CustomerResponse;
import com.example.hal.model.dto.response.OrderResponse;
import com.example.hal.service.HalService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/hal")
@RequiredArgsConstructor
public class HalController {

    private final HalService halService;

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<CustomerResponse>>> getAllCustomers() {
        return ResponseEntity.ok(CollectionModel.of(halService.getCustomers().stream()
                        .map(element -> EntityModel.of(element,
                                linkTo(methodOn(HalController.class).getCustomerById(element.getId())).withSelfRel(),
                                linkTo(methodOn(HalController.class).getAllCustomers()).withRel("customers"),
                                linkTo(methodOn(HalController.class).getCustomerOrders(element.getId())).withRel("orders")))
                        .toList(),
                linkTo(methodOn(HalController.class).getAllCustomers()).withSelfRel()));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable final Long id) {
        var methodInvocation = methodOn(HalController.class).addNewOrder(null);
        var selfMethod = methodOn(HalController.class).getCustomerById(id);

        var link = Affordances.of(linkTo(methodInvocation).withRel("new customer"))
                .afford(HttpMethod.POST)
                .withInput(OrderDTO.class)
                .withName("create new order")
                .withOutput(OrderResponse.class)
                .toLink();

        var link2 = Affordances.of(linkTo(methodInvocation).withRel("test"))
                .afford(HttpMethod.POST)
                .withInput(OrderDTO.class)
                .withName("create new order")
                .withOutput(OrderResponse.class)
                .toLink();

        return ResponseEntity.ok(halService.getCustomer(id)
                .add(
                        linkTo(selfMethod).withSelfRel(),
                        linkTo(methodOn(HalController.class).getAllCustomers()).withRel("customers"),
                        linkTo(methodOn(HalController.class).getCustomerOrders(id)).withRel("orders"),
                        link,
                        link2,
                        linkTo(methodInvocation).withRel("methods")
                ));
    }

    @PostMapping(value = "/customers/new")
    public ResponseEntity<CustomerResponse> addNewCustomer(@RequestBody final CustomerDTO customerDTO) {
        final CustomerResponse response = halService.addCustomer(customerDTO);
        return ResponseEntity.ok(response
                .add(
                        linkTo(methodOn(HalController.class).getCustomerById(response.getId())).withSelfRel(),
                        linkTo(methodOn(HalController.class).getAllCustomers()).withRel("customers"),
                        linkTo(methodOn(HalController.class).getCustomerOrders(response.getId())).withRel("orders")));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = halService.getOrders();
        responses.forEach(element -> element.add(linkTo(methodOn(HalController.class).getOrderById(element.getId())).withSelfRel()));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/orders/customers")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(@RequestParam("customerId") final Long customerId) {
        List<OrderResponse> responses = halService.getOrders(customerId);
        responses.forEach(element -> element.add(linkTo(methodOn(HalController.class).getOrderById(element.getId())).withSelfRel()));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable final Long id) {
        return ResponseEntity.ok(halService.getOrder(id).add(linkTo(methodOn(HalController.class).getOrderById(id)).withSelfRel()));
    }

    @PostMapping("/orders/new")
    public ResponseEntity<OrderResponse> addNewOrder(@RequestBody final OrderDTO orderDTO) {
        return ResponseEntity.ok(halService.addOrder(orderDTO)
                .add(
                        linkTo(methodOn(HalController.class).addNewOrder(orderDTO)).withSelfRel(),
                        linkTo(methodOn(HalController.class).getAllOrders()).withRel("orders")
                ));
    }
}
