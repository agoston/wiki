package org.bruinproductions.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WikiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikiController.class);

    @Autowired
    WikiDB wikiDB;

    @RequestMapping(value = "/wiki/{documentId}", method = GET)
    public WikiDoc getDocument(@PathVariable String documentId) {
        return wikiDB.pull(documentId);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Unknown document")
    class DocumentNotFoundException extends RuntimeException {
    }
}
