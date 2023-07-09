package com.hibernate.jpa.domain;

import com.hibernate.jpa.domain.composite.AuthorCompositeEmbeddedPk;
import com.hibernate.jpa.domain.composite.AuthorCompositePk;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthorCompositeEmbedded {
    @EmbeddedId
    private AuthorCompositeEmbeddedPk id;

    private String country;

    public AuthorCompositeEmbedded(AuthorCompositeEmbeddedPk id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorCompositeEmbedded that = (AuthorCompositeEmbedded) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
