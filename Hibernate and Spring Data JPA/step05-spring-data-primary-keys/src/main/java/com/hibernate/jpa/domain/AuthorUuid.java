package com.hibernate.jpa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import java.sql.Types;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUuid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // @Type(type="org.hibernate.type.UUIDCharType") > not working with hibernate 6+
    @JdbcTypeCode(value = Types.VARCHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false )
    private UUID id;
    private String firstName;
    private String lastName;

    public AuthorUuid(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorUuid author = (AuthorUuid) o;

        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
