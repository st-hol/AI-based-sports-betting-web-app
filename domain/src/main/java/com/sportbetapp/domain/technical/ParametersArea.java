package com.sportbetapp.domain.technical;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
public class ParametersArea {

    @Id
    private String name;

    @Column(length = 2000, columnDefinition = "varchar")
    private String value = StringUtils.EMPTY;

    @Column(length = 100, columnDefinition = "varchar")
    private String description;

}
