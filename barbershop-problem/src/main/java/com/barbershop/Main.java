package com.barbershop;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class Main {

    private static int customers = 0;

    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore standingRoom = new Semaphore(12, true);
    private static Semaphore sofa = new Semaphore(4, true);
    private static Semaphore chair = new Semaphore(3);
    private static Semaphore barber = new Semaphore(0);
    private static Semaphore customer = new Semaphore(0);
    private static Semaphore cash = new Semaphore(0);
    private static Semaphore receipt = new Semaphore(0);

    private static class Pokemon extends Thread {

        private int id;

        public Pokemon(int id) {
            this.id = id;
        }

        public void run() {
            try {
                mutex.acquire();
                if (customers == 16) {
                    mutex.release();
                    System.out.println(id + " foi embora");
                    return;
                }
                customers++;
                mutex.release();

                standingRoom.acquire();
                System.out.println(id + " entrou na sala de espera");

                sofa.acquire();
                System.out.println(id + " sentou no sofa");
                standingRoom.release();

                chair.acquire();
                System.out.println(id + " sentou na cadeira do barbeiro");
                sofa.release();

                customer.release();
                barber.acquire();
                System.out.println(id + " cortando o cabelo");

                System.out.println(id + " pagando o corte");
                cash.release();
                receipt.acquire();

                chair.release();
                mutex.acquire();
                customers--;
                mutex.release();
                System.out.println(id + " saiu da barbearia");
            } catch (Exception e) {

            }
        }
    }

    private static class Nurse extends Thread {

        private int id;

        public Nurse(int id) {
            this.id = id;
        }

        public void run() {
            while (true) {
                try {
                    customer.acquire();
                    barber.release();
                    System.out.println("Barbeiro " + id + " cortando cabelo");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                    cash.acquire();
                    System.out.println("Barbeiro  " + id + "  aceitando pagamento");
                    receipt.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] adsa) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            (new Nurse(i)).start();
        }

        for (int i = 0; i < 40; i++) {
            TimeUnit.SECONDS.sleep(new Random().nextInt(1));
            (new Pokemon(i)).start();
        }
    }

}
