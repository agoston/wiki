package org.bruinproductions.wiki;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/** DESIGN NOTES
 *
 * At the moment, WikiDB is called from a RestController, that is backed by a CPU*2 event-driven threads.
 * Since we have a massive load, ways to improve it would be:
 * - separate the query/update load (but this would add extra problems with consistency between the two)
 * - offload merging to a separate backend server, relinquest async event processing thread to continue processing meanwhile
 * - cache even more aggressively, to the point of keeping a cache of DynamicBuffers in memory and simply streaming bytes directly from there
 *
 * - think of a way of how to handle the massive update load on a single article so that end users don't feel duped by 'others have changed the page, merge conflict' messages
 *    - but also don't merge too heavily as it can result in looks that was not intended
 *    - maybe prioritise users based on experience and overrule lower priority updates when overloaded?
 *    - ..., this is quite a complex problem, with quite some human aspects as well
 */

@Component
public class WikiDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikiDB.class);

    private static final double SQRT_5 = Math.sqrt(5);
    private static final double PHI = (1 + SQRT_5) / 2.0;
    private static final double PHI_NEG = (1 - SQRT_5) / 2.0;

    private final Path tempFile;
    private final LoadingCache<String, WikiDoc> documentCache;

    private final Set<String> merging = new HashSet<>();

    public WikiDB() throws IOException {
        tempFile = Files.createTempFile("wiki", ".html");

        documentCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, WikiDoc>() {
                    @Override
                    public WikiDoc load(String key) throws Exception {
                        return loadDocument(key);
                    }
                });
    }

    public WikiDoc<String> pull(String documentId) throws ExecutionException {
        // emulating process & crunch
        recursiveFibonacci(34);

        return documentCache.get(documentId);
    }

    public WikiDoc push(String documentId, WikiDoc wikiDoc) throws ExecutionException, IOException {
        WikiDoc current = documentCache.get(documentId);
        synchronized (current) {
            if (current.getRevision() != wikiDoc.getRevision()) {
                // TODO: [AH] implement merge strategy to try merge non-conflicting changes
                return current;
            }
            wikiDoc.incrementRevision();
            storeDocument(documentId, wikiDoc);
            documentCache.put(documentId, wikiDoc);
            return wikiDoc;
        }
    }

    WikiDoc loadDocument(String documentId) throws IOException {
        // TODO: [AH] add proper versioning
        // TODO: [AH] add file store
        return new WikiDoc(new String(Files.readAllBytes(tempFile)), 1);
    }

    void storeDocument(String documentId, WikiDoc document) throws IOException {
        // TODO: [AH] serialize on-the-fly or keep DynamicBuffer in WikiDoc to avoid unnecessary memory allocations/copies
        Files.write(tempFile, document.getDocument().toString().getBytes());
    }

    private static long recursiveFibonacci(long n) {
        if (n <= 1) return n;
        return recursiveFibonacci(n - 1) + recursiveFibonacci(n - 2);
    }

    private static long fibonacci(int n) {
        return (long) Math.floor((Math.pow(PHI, n) - Math.pow(PHI_NEG, n)) / SQRT_5);
    }
}
