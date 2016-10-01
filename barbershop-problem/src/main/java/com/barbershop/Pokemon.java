package com.barbershop;

/**
 * Created by caiocesar on 01/10/16.
 */
class Pokemon implements Runnable {

    private int id;
    private PokemonCenter pokemonCenter;

    public Pokemon (int id, PokemonCenter barbershop) {
        this.id = id;
        this.pokemonCenter = barbershop;
    }

    public void run() {
        pokemonCenter.addPokemon(id);
    }
}
