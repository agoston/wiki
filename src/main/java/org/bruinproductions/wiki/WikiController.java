package org.bruinproductions.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class WikiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikiController.class);

    @Autowired
    WikiDB wikiDB;

    @RequestMapping(value = "/wiki/{documentId}", method = GET)
    public WikiDoc getDocument(@PathVariable String documentId) {
        try {
            return wikiDB.pull(documentId);
        } catch (ExecutionException e) {
            LOGGER.info("Error pulling document " + documentId, e);
            // TODO: [AH] with document store, this should throw a 500 instead
            throw new DocumentNotFoundException();
        }
    }

    @RequestMapping(value = "/wiki/{documentId}", method = PUT)
    public WikiDoc putDocument(@PathVariable String documentId, @RequestBody WikiDoc wikiDoc) {
        try {
            return wikiDB.push(documentId, wikiDoc);
        } catch (ExecutionException e) {
            LOGGER.info("Error retrieving document " + documentId, e);
            // TODO: [AH] with document store, this should throw a 500 instead
            throw new DocumentNotFoundException();

        } catch (IOException e) {
            LOGGER.info("Error pushing document " + documentId, e);
            // TODO: [AH] with document store, this should throw a 500 instead
            throw new DocumentNotFoundException();
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Unknown document")
    class DocumentNotFoundException extends RuntimeException {
    }
}
