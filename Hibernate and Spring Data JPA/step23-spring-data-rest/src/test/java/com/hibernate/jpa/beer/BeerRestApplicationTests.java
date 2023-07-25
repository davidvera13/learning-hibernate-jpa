package com.hibernate.jpa.beer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hibernate.jpa.beer.domain.Beer;
import com.hibernate.jpa.beer.domain.BeerStyleEnum;
import com.hibernate.jpa.beer.objs.beers.BeerCreationRequest;
import com.hibernate.jpa.beer.objs.beers.CoolBeer;
import com.hibernate.jpa.beer.objs.beers.Root;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static com.hibernate.jpa.beer.JsonUtil.getMapper;
import static com.hibernate.jpa.beer.bootstrap.BeerLoader.BEER_4_UPC;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BeerRestApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() {
    }

    // we use HATEOAS and we didn't write any controller method for entry point due to spring data rest dependency
    @Test
    void getAllBeers() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("http://localhost:8080/api/v1/beers")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getSingleBeerAndReadHeaderForEtag() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get("http://localhost:8080/api/v1/beers")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println("Get Header names");
        mvcResult.getResponse().getHeaderNames().forEach(System.out::println);
        System.out.println(mvcResult.getResponse().getContentAsString());
        Map<String, Object> map = getMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<HashMap<String,Object>>() {});
        System.out.println("*****************************************");
        map.forEach((k,v) -> System.out.println("key: " + k + ", value: " + v));
        System.out.println("*****************************************");
        System.out.println(map.get("_embedded").toString());

        Root root = getMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                Root.class);
        System.out.println("*****************************************");
        System.out.println(root._embedded.coolBeers.get(0)._links.self.href);

        // http://localhost:8080/api/v1/beers/18c7a9ba-8f84-464c-98f0-b81ffb589992
        mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(root._embedded.coolBeers.get(0)._links.self.href)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        mvcResult.getResponse().getHeaderNames().forEach(System.out::println);
        // The ETag (or entity tag) HTTP response header is an identifier for a specific version of a resource.
        // It lets caches be more efficient and save bandwidth, as a web server does not need to resend a full response
        // if the content was not changed. Additionally, etags help to prevent simultaneous updates of a resource from
        // overwriting each other ("mid-air collisions").
        // If the resource at a given URL changes, a new Etag value must be generated. A comparison of them can determine
        // whether two representations of a resource are the same.

        System.out.println("ETAG >> " + mvcResult.getResponse().getHeader("ETag"));
    }

    @Test
    void apiProfile() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/api/v1/profile/beers")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void createBeer() throws Exception {
        BeerCreationRequest request = BeerCreationRequest.builder()
                .beerName("Rochefort")
                .beerStyle(String.valueOf(BeerStyleEnum.STOUT))
                .upc(BEER_4_UPC)
                .quantityOnHand(11)
                .price(12.35)
                .build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/api/v1/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    void updateBeer() throws Exception {
        // create
        BeerCreationRequest request = BeerCreationRequest.builder()
                .beerName("Rochefort")
                .beerStyle(String.valueOf(BeerStyleEnum.STOUT))
                .upc(BEER_4_UPC)
                .quantityOnHand(11)
                .price(12.35)
                .build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/api/v1/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        CoolBeer beer = getMapper().readValue(mvcResult.getResponse().getContentAsString(), CoolBeer.class);
        String url = beer._links.self.href;

        request = BeerCreationRequest.builder()
                .beerName("Rochefort 8")
                .beerStyle(String.valueOf(BeerStyleEnum.STOUT))
                .upc(BEER_4_UPC)
                .quantityOnHand(125)
                .price(11.00)
                .build();
        System.out.println("*******************************");
        mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        // ETAG updated
        System.out.println("ETAG >> " + mvcResult.getResponse().getHeader("ETag"));

    }

    @Test
    void deleteBeer() throws Exception {
        // create
        BeerCreationRequest request = BeerCreationRequest.builder()
                .beerName("Rochefort")
                .beerStyle(String.valueOf(BeerStyleEnum.STOUT))
                .upc(BEER_4_UPC)
                .quantityOnHand(11)
                .price(12.35)
                .build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/api/v1/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        CoolBeer beer = getMapper().readValue(mvcResult.getResponse().getContentAsString(), CoolBeer.class);
        String url = beer._links.self.href;

        System.out.println("*******************************");
        // delete
        mvcResult = mvc.perform(MockMvcRequestBuilders
                        .delete(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

        System.out.println("*******************************");
        // check
        mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println("after delete " + mvcResult.getResponse().getContentAsString());
        System.out.println("after delete " + mvcResult.getResponse().getStatus());
    }

    @Test
    void usingRepositoryMethods() throws Exception {
        // Beer findByUpc(String upc);
        MvcResult mvcResult;

        mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/api/v1/beers/search/findByUpc?upc=" + BEER_4_UPC)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

        System.out.println("***********************");
        // Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);
        mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/api/v1/beers/search/findAllByBeerStyle?beerStyle=" + BeerStyleEnum.ALE)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println("***********************");


    }

}
