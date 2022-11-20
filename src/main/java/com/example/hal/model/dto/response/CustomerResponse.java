package com.example.hal.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse extends RepresentationModel<CustomerResponse> {

    private Long id;

    @JsonProperty("name")
    private String customerName;

    @JsonProperty("email")
    private String customerEmail;

    private List<Long> ordersId;

}
