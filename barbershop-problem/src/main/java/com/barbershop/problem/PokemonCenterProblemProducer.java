package com.barbershop.problem;

import com.barbershop.animation.character.Character;
import com.barbershop.animation.character.Orientation;
import com.barbershop.animation.character.Position;
import com.barbershop.animation.character.nurse.NurseGenerator;
import com.barbershop.animation.character.pokemon.PokemonRandomizer;
import com.barbershop.animation.scenario.PokemonCenter;
import com.barbershop.animation.scenario.Seat;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

import static com.barbershop.animation.scenario.StandingRoom.SEAT_WIDTH;
import static com.barbershop.animation.scenario.StandingRoom.SPACE_BETWEEN_SEATS;

public class PokemonCenterProblemProducer implements Runnable {

    public static final int NURSES_MARGIN = 20;
    public static final int SPACE_BETWEEN_NURSES = 10;
    public static final int MINIMUM_SLEEP_BETWEEN_CLIENTS_ARRIVE = 2000;
    public static final Position STANDING_ROOM_POSITION = new Position(50, 200);

    private final Canvas pokemonLayer;
    private final Canvas nursesLayer;

    protected int customers = 0;

    protected Semaphore pokemonMutex;
    protected Semaphore nurseMutex;
    protected Semaphore standingRoom;
    protected Semaphore sofa;
    protected Semaphore chair;
    protected Semaphore barber;
    protected Semaphore customer;
    protected Semaphore cash;
    protected Semaphore receipt;

    protected final List<Nurse> nurses;
    protected final PokemonCenter pokemonCenter;

    public PokemonCenterProblemProducer(Canvas scenarioLayer, Canvas pokemonLayer, Canvas nursesLayer) {
        this.pokemonLayer = pokemonLayer;
        this.nursesLayer = nursesLayer;

        this.customers = 0;
        this.pokemonMutex = new Semaphore(1);
        this.nurseMutex = new Semaphore(1);
        this.standingRoom = new Semaphore(18, true);
        this.sofa = new Semaphore(18, true);
        this.chair = new Semaphore(6);
        this.barber = new Semaphore(0);
        this.customer = new Semaphore(0);
        this.cash = new Semaphore(0);
        this.receipt = new Semaphore(0);

        //max places 18
        int numberOfSeats = 18;

        this.nurses = new ArrayList<>();

        this.pokemonCenter = new PokemonCenter(scenarioLayer, numberOfSeats, STANDING_ROOM_POSITION);
        this.pokemonCenter.draw();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            int id = i + 1;
            Position nursePosition = new Position(i * NurseGenerator.JOY_WIDTH + i * SPACE_BETWEEN_NURSES + NURSES_MARGIN,
                    this.pokemonCenter.getNurseInitialPosition(NurseGenerator.JOY_HEIGHT));
            this.pokemonCenter.addNurseChair(new Position(nursePosition.getX() + 10,
                    nursePosition.getY() + NurseGenerator.JOY_HEIGHT));

            Nurse nurse = newNurse(id, nursePosition, this.nursesLayer);
            this.nurses.add(nurse);
            Thread thread = new Thread(nurse);
            thread.start();
        }

        int i = 1;
        while(true) {
            Pokemon pokemon = newPokemon(i, this.pokemonCenter.getPokemonInitialPosition(), this.pokemonLayer);

            Thread thread = new Thread(pokemon);
            thread.start();
            i++;

            sleep(new Random().nextInt(1000) + MINIMUM_SLEEP_BETWEEN_CLIENTS_ARRIVE);
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Pokemon newPokemon(int id, Position initialPosition, Canvas animationLayer) {
        PokemonRandomizer characterRandomizer = new PokemonRandomizer(initialPosition);
        return new Pokemon(id, animationLayer, characterRandomizer.newPokemon());
    }

    public Nurse newNurse(int id, Position initialPosition, Canvas animationLayer) {
        NurseGenerator nurseGenerator = new NurseGenerator(initialPosition);
        return new Nurse(id, animationLayer, nurseGenerator.newJoy());
    }

    public class Pokemon implements Runnable {

        private int id;

        private final GraphicsContext map;
        private final double mapHeight;
        private final Character character;
        private final Position initialPosition;

        public Pokemon(int id, Canvas canvas, Character character) {
            this.id = id;
            this.mapHeight = canvas.getHeight();
            this.map = canvas.getGraphicsContext2D();
            this.character = character;
            this.initialPosition = character.getPosition();
        }

        public void run() {
            try {
                pokemonMutex.acquire();
                if (customers == 16) {
                    pokemonMutex.release();
                    System.out.println(id + " foi embora");
                    leave();
                    return;
                }
                customers++;
                pokemonMutex.release();

                standingRoom.acquire();
                System.out.println(id + " entrou na sala de espera");

                sofa.acquire();
                System.out.println(id + " sentou no sofa");
                seatStandingRoom();
                standingRoom.release();

                chair.acquire();
                System.out.println(id + " sentou na cadeira da enfermeira");
                nurseMutex.acquire();
                seatNurseChair();
                nurseMutex.release();
                sofa.release();

                customer.release();
                barber.acquire();
                System.out.println(id + " cortando o cabelo");

                System.out.println(id + " pagando o corte");
                cash.release();
                pay();
                receipt.acquire();

                chair.release();
                pokemonMutex.acquire();
                customers--;
                pokemonMutex.release();
                System.out.println(id + " saiu da barbearia");
                leaveAfterPay();
            } catch (Exception e) {

            }
        }

        private void leave() {
            Position newPosition = new Position(initialPosition.getX(), initialPosition.getY() + 10);
            character.walkTo(map, newPosition);
            character.disappear(map);
        }

        private void seatStandingRoom() {
            Optional<Seat> freeSeat = pokemonCenter.getStandingRoom().getFreeSeat();
            if(!freeSeat.isPresent()) {
                System.err.println("There's something wrong!!");
                return;
            }

            Position seatPosition = freeSeat.get().getPosition();
            character.walkTo(map, new Position(seatPosition.getX() - SEAT_WIDTH - (SPACE_BETWEEN_SEATS / 2),
                    character.getPosition().getY()));
            character.walkTo(map, new Position(character.getPosition().getX(), seatPosition.getY()));
            character.walkTo(map, new Position(seatPosition.getX() - 8, seatPosition.getY() - 25));
        }

        private void seatNurseChair() {
            Optional<Nurse> availableNurse = nurses.stream().filter(n -> !n.isServing()).findFirst();
            if(!availableNurse.isPresent()) {
                System.err.println("There's something wrong!");
                return;
            }

            Position nursePosition = availableNurse.get().character.getPosition();
            Position chairPosition =
                    new Position(nursePosition.getX(), nursePosition.getY() + NurseGenerator.JOY_HEIGHT);
            this.character.walkTo(map, new Position(this.character.getPosition().getX() - SPACE_BETWEEN_SEATS,
                    this.character.getPosition().getY()));
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    STANDING_ROOM_POSITION.getY() - PokemonRandomizer.POKEMON_HEIGHT));
            this.character.walkTo(map, new Position(chairPosition.getX(), chairPosition.getY() - 20));
        }

        private void pay() {
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    this.character.getPosition().getY() + PokemonRandomizer.POKEMON_HEIGHT - 10));
            this.character.walkTo(map, pokemonCenter.getTablePosition());
        }

        private void leaveAfterPay() {
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    mapHeight - PokemonRandomizer.POKEMON_HEIGHT));
            this.character.walkTo(map, initialPosition);
            leave();
        }
    }

    public class Nurse extends Thread {

        private final int id;
        private final GraphicsContext map;
        private final Position initialPosition;
        private Boolean serving;

        private final Character character;

        public Nurse(int id, Canvas canvas, Character character) {
            this.id = id;
            this.map = canvas.getGraphicsContext2D();
            this.character = character;
            this.serving = false;

            this.initialPosition = this.character.getPosition();

            this.character.stay(map, Orientation.DOWN);
        }

        public void run() {
            while (true) {
                try {
                    customer.acquire();
                    nurseMutex.acquire();
                    this.serving = true;
                    nurseMutex.release();
                    barber.release();
                    System.out.println("Barbeiro " + id + " cortando cabelo");
                    cutCustomersHair();
                    cash.acquire();
                    System.out.println("Barbeiro  " + id + "  aceitando pagamento");
                    chargePayment();
                    receipt.release();
                    returnToChair();
                    nurseMutex.acquire();
                    this.serving = false;
                    nurseMutex.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cutCustomersHair() throws InterruptedException {
            int seconds = new Random(3).nextInt() + 1;
            while(seconds > 0) {
                this.character.stay(map, Orientation.DOWN);
                Thread.sleep(1000);
            }
        }

        private void chargePayment() {
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    this.character.getPosition().getY() + NurseGenerator.JOY_HEIGHT));
            this.character.walkTo(map, new Position(pokemonCenter.getTablePosition().getX() - NurseGenerator.JOY_WIDTH,
                    this.character.getPosition().getY()));
            this.character.walkTo(map, new Position(this.character.getPosition().getX(), initialPosition.getY()));
            this.character.walkTo(map, new Position(pokemonCenter.getTablePosition().getX(),
                    this.character.getPosition().getY()));
            this.character.stay(map, Orientation.DOWN);
        }

        private void returnToChair() {
            this.character.walkTo(map, new Position(this.character.getPosition().getX() - NurseGenerator.JOY_WIDTH,
                    this.character.getPosition().getY()));
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    this.character.getPosition().getY() + NurseGenerator.JOY_HEIGHT));
            this.character.walkTo(map, initialPosition);
            this.character.stay(map, Orientation.DOWN);
        }

        public Boolean isServing() {
            return serving;
        }
    }


}
