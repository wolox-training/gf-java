package com.wolox.training.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("professor")
public class Professor extends User{

    @NotNull
    private String subject;
}
