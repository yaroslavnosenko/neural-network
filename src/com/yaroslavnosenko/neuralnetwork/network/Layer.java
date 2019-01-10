package com.yaroslavnosenko.neuralnetwork.network;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    List<Neuron> neurons;

    Layer() {
        this.neurons = new ArrayList<>();
    }

    @Override
    public String toString() {
        String message = "-- Layer\n";
        for (Neuron neuron : neurons) {
            message += neuron.toString();
            message += "\n";
        }
        return message;
    }

}
