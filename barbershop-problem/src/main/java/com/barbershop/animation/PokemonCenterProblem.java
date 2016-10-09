package com.barbershop.animation;

import com.barbershop.animation.character.Position;
import com.barbershop.animation.character.nurse.NurseGenerator;
import com.barbershop.animation.character.pokemon.PokemonRandomizer;
import com.barbershop.animation.scenario.Scenario;
import com.barbershop.animation.scenario.StandingRoom;
import javafx.scene.canvas.Canvas;

public class PokemonCenterProblem implements Runnable {

    private final Canvas scenarioLayer;
    private final Canvas animationLayer;

    public PokemonCenterProblem(Canvas scenarioLayer, Canvas animationLayer) {
        this.scenarioLayer = scenarioLayer;
        this.animationLayer = animationLayer;
    }

    @Override
    public void run() {
        Scenario scenario = new Scenario(scenarioLayer);
        scenario.draw();

        //max places 18
        StandingRoom standingRoom = new StandingRoom(scenarioLayer, 18, new Position(30, 150));
        standingRoom.draw();

        Position initialPosition = scenario.getPokemonInitialPosition();
        PokemonRandomizer characterRandomizer = new PokemonRandomizer(initialPosition);
        Thread pokemonThread = new Thread(new ThreadPokemonTest(animationLayer, characterRandomizer.newPokemon()));

        NurseGenerator nurseGenerator = new NurseGenerator(new Position(0, scenario.getWallHeight() - NurseGenerator.JOY_HEIGHT));
        Thread joyThread = new Thread(new ThreadJoyTest(animationLayer, nurseGenerator.newJoy()));

        joyThread.start();
        pokemonThread.start();
    }
}
