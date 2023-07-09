package com.hibernate.jpa.domain;

import com.hibernate.jpa.domain.composite.AuthorCompositePk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter @Setter
@NoArgsConstructor
@IdClass(AuthorCompositePk.class)
public class AuthorComposite {
    @Id
    private String firstName;
    @Id
    private String lastName;
    private String country;

    public AuthorComposite(String firstName, String lastName, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorComposite that = (AuthorComposite) o;

        if (!firstName.equals(that.firstName)) return false;
        return lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
