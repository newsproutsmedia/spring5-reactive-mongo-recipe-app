package guru.springframework.controllers;

import guru.springframework.config.WebConfig;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

public class RouterFunctionTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        WebConfig webConfig = new WebConfig();

        RouterFunction<?> routerFunction = webConfig.routes(recipeService);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    public void testGetRecipes() throws Exception {

        // when getRecipes() is called, return a Flux of objects of type Recipe
        // as a Flux expects multiple objects, must create at least two new Recipe objects
        when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        // call the uri of the JSON microservice, "/api/recipes"
        webTestClient.get().uri("/api/recipes")
                // accept a MediaType of JSON
                .accept(MediaType.APPLICATION_JSON)
                // execute the command
                .exchange()
                // expect a status of "ok"
                .expectStatus().isOk()
                // expect that the returned list will be comprised of objects with type recipe
                .expectBodyList(Recipe.class);
    }

}
