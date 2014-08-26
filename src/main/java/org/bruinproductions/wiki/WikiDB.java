package org.bruinproductions.wiki;

import org.springframework.stereotype.Component;

@Component
public class WikiDB {

    private static final double SQRT_5 = Math.sqrt(5);
    private static final double PHI = (1 + SQRT_5) / 2.0;
    private static final double PHI_NEG = (1 - SQRT_5) / 2.0;

    public WikiDoc<Long> pull(String documentUrl) {
        return new WikiDoc<>(fibonacci(34));
    }

    private static long fibonacci(int n) {
        return (long)Math.floor((Math.pow(PHI, n) - Math.pow(PHI_NEG, n)) / SQRT_5);
    }
}
