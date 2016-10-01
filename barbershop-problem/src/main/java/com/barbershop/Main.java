package com.barbershop;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by caiocesar on 01/10/16.
 */
public class Main {
    public static final int NUM_OF_CHAIRS = 4;
    public static final int NUM_OF_CUSTOMERS = 15;

    public static void main(String args[]) throws InterruptedException {
        PokemonCenter pokemonCenter = new PokemonCenter(NUM_OF_CHAIRS);

        for (int i = 0; i < NUM_OF_CUSTOMERS; i++) {
            (new Thread(new Pokemon(i, pokemonCenter))).start();
            TimeUnit.SECONDS.sleep(Long.valueOf(new Random().nextInt(3)));
        }
    }
}
