package com.barbershop.problem;

import com.barbershop.animation.character.Character;
import com.barbershop.animation.character.Orientation;
import com.barbershop.animation.character.Position;
import com.barbershop.animation.character.nurse.NurseGenerator;
import com.barbershop.animation.character.pokemon.PokemonRandomizer;
import com.barbershop.animation.scenario.PokemonCenter;
import com.barbershop.animation.scenario.Seat;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.barbershop.animation.scenario.StandingRoom.SEAT_WIDTH;
import static com.barbershop.animation.scenario.StandingRoom.SPACE_BETWEEN_SEATS;

public class PokemonCenterProblem implements Runnable {

    public static final int NURSES_MARGIN = 20;
    public static final int SPACE_BETWEEN_NURSES = 10;
    public static final int MINIMUM_SLEEP_BETWEEN_CLIENTS_ARRIVE = 500;
    public static final Position STANDING_ROOM_POSITION = new Position(50, 200);
    public static final int MAX_NUMBER_OF_SEATS = 6;

    protected int customers = 0;

    protected Semaphore pokemonMutex;

    protected Semaphore nurseMutex;
    protected Semaphore chargeMutex;
    protected Semaphore standingRoom;
    protected Semaphore sofa;
    protected Semaphore chair;
    protected Semaphore barber;
    protected Semaphore customer;
    protected Semaphore cash;
    protected Semaphore receipt;

    private final int numberOfNurses;
    private final int maxSeats;
    protected final PokemonCenter pokemonCenter;

    protected final ConcurrentLinkedQueue<Nurse> nurses;

    private Pane pane;

    public PokemonCenterProblem(Pane pane, int maxNurses, int maxSeats) {
        this.pane = pane;

        int numberOfPlacesToWaitStanding = maxSeats;
        int numberOfSeats = MAX_NUMBER_OF_SEATS;
        this.maxSeats = maxSeats;
        if (maxSeats < MAX_NUMBER_OF_SEATS) {
            numberOfSeats = maxSeats;
        }
        
        this.numberOfNurses = maxNurses;

        this.customers = 0;
        this.pokemonMutex = new Semaphore(1);
        this.nurseMutex = new Semaphore(1);
        this.chargeMutex = new Semaphore(1);
        this.standingRoom = new Semaphore(numberOfPlacesToWaitStanding, true);
        this.sofa = new Semaphore(numberOfSeats, true);
        this.chair = new Semaphore(numberOfNurses);
        this.barber = new Semaphore(0);
        this.customer = new Semaphore(0);
        this.cash = new Semaphore(0);
        this.receipt = new Semaphore(0);

        this.nurses = new ConcurrentLinkedQueue<>();

        this.pokemonCenter = new PokemonCenter(newCanvas(), numberOfSeats, numberOfPlacesToWaitStanding,
                STANDING_ROOM_POSITION);
        this.pokemonCenter.draw();

    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfNurses; i++) {
            int id = i + 1;
            Position nursePosition = new Position(i * NurseGenerator.JOY_WIDTH + i * SPACE_BETWEEN_NURSES + NURSES_MARGIN,
                    this.pokemonCenter.getNurseInitialPosition(NurseGenerator.JOY_HEIGHT));
            this.pokemonCenter.addNurseChair(new Position(nursePosition.getX() + 10,
                    nursePosition.getY() + NurseGenerator.JOY_HEIGHT));

            Nurse nurse = newNurse(id, nursePosition, newCanvas());
            this.nurses.add(nurse);
            Thread thread = new Thread(nurse);
            thread.setDaemon(true);
            thread.start();
        }

        int i = 1;
        while(true) {
            Pokemon pokemon = newPokemon(i, this.pokemonCenter.getPokemonInitialPosition(), newCanvas());

            Thread thread = new Thread(pokemon);
            thread.setDaemon(true);
            thread.start();
            i++;

            sleep(new Random().nextInt(1200) + MINIMUM_SLEEP_BETWEEN_CLIENTS_ARRIVE);
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Canvas newCanvas() {
        Canvas pokemonLayer = new Canvas(pane.getWidth(), pane.getHeight());
        Platform.runLater(() -> pane.getChildren().add(pokemonLayer));
        return pokemonLayer;
    }

    private void removeCanvas(Canvas canvas) {
        Platform.runLater(() -> pane.getChildren().removeAll(canvas));
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

        private final Canvas map;
        private final Character character;
        private final Position initialPosition;
        private Nurse nurse;

        public Pokemon(int id, Canvas canvas, Character character) {
            this.id = id;
            this.map = canvas;
            this.character = character;
            this.initialPosition = character.getPosition();
        }

        public void run() {
            try {
                pokemonMutex.acquire();
                if (customers == maxSeats) {
                    pokemonMutex.release();
                    System.out.println(id + " foi embora");
                    leave();
                    return;
                }
                customers++;
                pokemonMutex.release();

                standingRoom.acquire();
                System.out.println(id + " entrou na sala de espera");
                Optional<Seat> freePlaceToWaitStanding = pokemonCenter.getStandingRoom().getFreePlaceToWaitStanding();
                waitOn(freePlaceToWaitStanding);

                sofa.acquire();
                freePlaceToWaitStanding.get().setBusy(false);

                System.out.println(id + " sentou no sofa");
                Optional<Seat> seat = pokemonCenter.getStandingRoom().getFreeSeat();
                waitOn(seat);

                chair.acquire();
                standingRoom.release();
                seat.get().setBusy(false);
                System.out.println(id + " sentou na cadeira da enfermeira");

                this.nurse = getNurse().get();
                walkToNurseChair();

                sofa.release();

                customer.release();
                barber.acquire();
                System.out.println(id + " cortando o cabelo");
                Thread.sleep(100);

                System.out.println(id + " pagando o corte");
                chargeMutex.acquire();
                cash.release();
                pay();
                receipt.acquire();

                pokemonMutex.acquire();
                customers--;
                pokemonMutex.release();
                System.out.println(id + " saiu do centro pokemon");
                leaveAfterPay();
                removeCanvas(this.map);

            } catch (Exception e) {

            }
        }

        private void leave() {
            Position newPosition = new Position(initialPosition.getX(), initialPosition.getY() + 10);
            character.walkTo(map, newPosition);
            character.disappear(map);
        }

        private void waitOn(Optional<Seat> freeSeat) {
            if(!freeSeat.isPresent()) {
                throw new IllegalStateException("Seat should be free!!");
            }

            Position seatPosition = freeSeat.get().getPosition();
            character.walkTo(map, new Position(seatPosition.getX() - SEAT_WIDTH - (SPACE_BETWEEN_SEATS / 2),
                    character.getPosition().getY()));
            character.walkTo(map, new Position(character.getPosition().getX(), seatPosition.getY()));
            character.walkTo(map, new Position(seatPosition.getX() - 8, seatPosition.getY() - 25));
        }

        private void walkToNurseChair() {
            Position chairPosition = this.nurse.getChairPosition();
            this.character.walkTo(map, new Position(this.character.getPosition().getX() - SPACE_BETWEEN_SEATS + 15,
                    this.character.getPosition().getY()));
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    STANDING_ROOM_POSITION.getY() - PokemonRandomizer.POKEMON_HEIGHT - 15));
            this.character.walkTo(map, new Position(chairPosition.getX(), chairPosition.getY() - 20));
        }

        private Optional<Nurse> getNurse() {
            Optional<Nurse> availableNurse = Optional.of(nurses.remove());
            if(!availableNurse.isPresent()) {
                System.err.println(id + " there's something wrong!");
                return null;
            }
            return availableNurse;
        }

        private void pay() {
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    this.character.getPosition().getY() + PokemonRandomizer.POKEMON_HEIGHT - 10));
            this.character.walkTo(map, pokemonCenter.getTablePosition());
        }

        private void leaveAfterPay() {
            this.character.walkTo(map, new Position(this.character.getPosition().getX(),
                    map.getHeight() - PokemonRandomizer.POKEMON_HEIGHT));
            this.character.walkTo(map, initialPosition);
            leave();
        }
    }

    public class Nurse extends Thread {

        private final int id;
        private final Position initialPosition;
        private final Canvas map;
        private Boolean serving;

        private final Character character;

        public Nurse(int id, Canvas canvas, Character character) {
            this.id = id;
            this.map = canvas;
            this.character = character;
            this.serving = false;

            this.initialPosition = this.character.getPosition();

            this.character.stay(map, Orientation.DOWN);
        }

        public Position getChairPosition() {
            return new Position(initialPosition.getX(), initialPosition.getY() + NurseGenerator.JOY_HEIGHT);
        }

        public void run() {
            while (true) {
                try {
                    customer.acquire();
                    barber.release();
                    System.out.println("Enfermeira " + id + " cortando cabelo");
                    cutCustomersHair();
                    cash.acquire();
                    System.out.println("Enfermeira " + id + "  aceitando pagamento");
                    walkToChargeTable();
                    receipt.release();
                    chargeMutex.release();
                    returnToChair();

                    nurses.add(this);
                    chair.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cutCustomersHair() throws InterruptedException {
            int seconds = new Random(3).nextInt() + 1;
            while(seconds > 0) {
                this.character.stay(map, Orientation.DOWN);
                TimeUnit.SECONDS.sleep(seconds);
            }
        }

        private void walkToChargeTable() {
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
