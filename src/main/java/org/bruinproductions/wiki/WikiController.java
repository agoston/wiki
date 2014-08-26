package org.bruinproductions.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WikiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikiController.class);

    @RequestMapping(value = "/singleton", method = GET)
    public WikiDoc getDocument() {
        throw new DocumentNotFoundException();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Unknown document")
    class DocumentNotFoundException extends RuntimeException {
    }
}
