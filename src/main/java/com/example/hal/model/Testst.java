package com.example.hal.model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public abstract class Testst {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
