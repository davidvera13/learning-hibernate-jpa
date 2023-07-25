package com.hibernate.jpa.domain.creditcard;

import com.hibernate.jpa.domain.CreditCardConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = CreditCardConverter.class)
    private String cvv;

    @Convert(converter = CreditCardConverter.class)
    private String expirationDate;

    // transient properties can be used in java code but are not persisted with JPA
    // transient properties: from credit card pan
    @Transient
    private String creditCardNumber;
    // transient properties from cardHolder database
    @Transient
    private String firstName;
    @Transient
    private String lastName;
    @Transient
    private String zipCode;
}










