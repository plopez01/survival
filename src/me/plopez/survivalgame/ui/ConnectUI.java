package me.plopez.survivalgame.ui;

import processing.core.PVector;

import java.util.function.BiFunction;
import java.util.function.Function;

import static me.plopez.survivalgame.Globals.focusedElement;
import static me.plopez.survivalgame.Globals.mainCanvas;
import static processing.core.PConstants.CENTER;

public class ConnectUI extends Canvas {
    InputBox serverIpBox = new InputBox(new PVector(0f, 0.1f), new PVector(0.4f, 0.04f), CENTER);
    InputBox playerNameBox = new InputBox(new PVector(0f, 0), new PVector(0.4f, 0.04f), CENTER);
    public ConnectUI(BiFunction<String, String, Void> whenDone){
        serverIpBox.setHorizontalAlignment(HorizontalAlignment.Center);
        serverIpBox.setVerticalAlignment(VerticalAlignment.Center);
        serverIpBox.setValue("127.0.0.1");
        addElement(serverIpBox);

        playerNameBox.setHorizontalAlignment(HorizontalAlignment.Center);
        playerNameBox.setVerticalAlignment(VerticalAlignment.Center);
        playerNameBox.setValue("Player");

        playerNameBox.onSubmitHandler(() -> {
            mainCanvas = null;
            whenDone.apply(serverIpBox.getValue(), playerNameBox.getValue());
        });

        addElement(playerNameBox);


        focusedElement = playerNameBox;
    }

}
