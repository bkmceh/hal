package com.example.hal.repository;

import com.example.hal.model.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends PagingAndSortingRepository<CustomerDTO, Long> {

    @RestResource(rel = "title-contains", path="title-contains")
    Page<CustomerDTO> findCustomerDTOByEmail(@Param("query") String query, Pageable page);

    @RestResource(rel = "author-contains", path="author-contains", exported = false)
    Page<CustomerDTO> findCustomerDTOByName(@Param("query") String query, Pageable page);
}