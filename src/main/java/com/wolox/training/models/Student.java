package com.wolox.training.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("student")
public class Student extends User{

    @NotNull
    private String year;

}
