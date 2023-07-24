package com.hibernate.jpa.domain.tableperclass;

import jakarta.persistence.Entity;

@Entity
public class Dolphin extends Mammal{
    private boolean hasSpots;

    public boolean isHasSpots() {
        return hasSpots;
    }

    public void setHasSpots(boolean hasSpots) {
        this.hasSpots = hasSpots;
    }
}
