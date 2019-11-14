package com.prompto.api;

import com.googlecode.objectify.ObjectifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index(){
        return "Greetings from Spring Boot!";
    }

    @PutMapping("/put")
    public String put() {
        ObjectifyService.ofy().save().entity(new Item("001", "name", "desc"));

        return "put";
    }

    @GetMapping("/get")
    public String get() {
        Item e = ObjectifyService.ofy().cache(true).load().type(Item.class).id("001").now();

        return String.format("%s: %s - %s", e.getId(), e.getName(), e.getDescription());
    }
}
