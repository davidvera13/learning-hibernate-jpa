package com.hibernate.jpa.domain.singletable;

import jakarta.persistence.*;

/**
 * A single table will be created with columns from each child classes +
 * a vehicule_type column
 * ID  	VEHICLE_TYPE  	PAYLOAD  	TRIM_LEVEL
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type")
public abstract class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
