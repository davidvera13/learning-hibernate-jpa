package com.hibernate.jpa.beer.objs.beers;

import lombok.Builder;

import java.util.Date;

@Builder
public class BeerCreationRequest {
    public String beerName;
    public String beerStyle;
    public String upc;
    public int quantityOnHand;
    public double price;
}
