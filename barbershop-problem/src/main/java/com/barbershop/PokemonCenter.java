package com.barbershop;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by caiocesar on 01/10/16.
 */
class PokemonCenter {

    private Semaphore empty;
    private Semaphore barber;
    private int chairs;

    public PokemonCenter(int chairs) {
        barber = new Semaphore(1);
        empty = new Semaphore(chairs);
        this.chairs = chairs;
    }

    public void addPokemon(int id) {
        if(empty.availablePermits() > 0){
            try{
                empty.acquire();
            } catch(InterruptedException e) {

            }
            try {
                barber.acquire();
            } catch (InterruptedException e) {

            }
            empty.release();
            cutHair(id);
            barber.release();
        }
        else{
            System.out.println("Customer " + id + " left");
        }

    }

    private void cutHair(int id) {
        System.out.println ("Customer " + id + " is being serviced.");
        try {
            TimeUnit.SECONDS.sleep(Long.valueOf(new Random().nextInt(5)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println ("Customer " + id + " is done being serviced.");
    }
}