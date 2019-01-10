package com.yaroslavnosenko.neuralnetwork.network;

import java.util.ArrayList;
import java.util.List;

public class Network {

    private static double alpha = 0.1;
    private List<Layer> layers;
    private Double error;

    static double getAlpha() {
        return alpha;
    }

    public double getLastError() {
        return error;
    }

    public Network(double alpha, int... structure) {
        Network.alpha = alpha;
        this.layers = new ArrayList<>();
        int prev = 0;
        for (int neuronCount : structure) {
            Layer layer = new Layer();
            this.layers.add(layer);

            for (int neuronIndex = 0; neuronIndex < neuronCount; neuronIndex++) {
                Neuron neuron = new Neuron(prev, neuronIndex);
                layer.neurons.add(neuron);
            }
            prev = neuronCount;
        }
    }

    private Double[] feedForward(Double[] inputs) {
        Layer first = layers.get(0);
        if (first.neurons.size() != inputs.length) {
            System.out.println("Structure error! " + "n: " + first.neurons.size() + "; i: " + inputs.length + ";");
            return null;
        }
        for (int neuronIndex = 0; neuronIndex < first.neurons.size(); neuronIndex++) {
            first.neurons.get(neuronIndex).setOut(inputs[neuronIndex]);
        }
        Layer prev = first;
        for (int layerIndex = 1; layerIndex < layers.size(); layerIndex++) {
            Layer curr = this.layers.get(layerIndex);
            for (Neuron neuron : curr.neurons) {
                neuron.feedForward(prev);
            }
            prev = this.layers.get(layerIndex);
        }
        Layer last = layers.get(layers.size() - 1);
        Double[] result = new Double[last.neurons.size()];
        for (int i = 0; i < last.neurons.size(); i++) {
            Neuron neuron = last.neurons.get(i);
            result[i] = neuron.getOut();
        }
        return result;
    }

    private void backPropagation(Double[] target) {
        error = 0.0;
        Layer outLayer = layers.get(layers.size() - 1);
        for (int neuronIndex = 0; neuronIndex < outLayer.neurons.size(); neuronIndex++) {
            Neuron neuron = outLayer.neurons.get(neuronIndex);
            neuron.calcOutGradient(target[neuronIndex]);
            double delta = target[neuronIndex] - neuron.getOut();
            error += delta * delta;
        }
        error /= outLayer.neurons.size();
        error = Math.sqrt(error);
        for (int layerIndex = layers.size() - 2; layerIndex > 0; layerIndex--) {
            Layer curr = layers.get(layerIndex);
            Layer next = layers.get(layerIndex + 1);
            for (int neuronIndex = 0; neuronIndex < curr.neurons.size(); neuronIndex++) {
                curr.neurons.get(neuronIndex).calcHiddenGradient(next);
            }
        }

        for (int layerNum = layers.size() - 1; layerNum > 0; --layerNum) {
            Layer layer = layers.get(layerNum);
            Layer prevLayer = layers.get(layerNum - 1);

            for (int n = 0; n < layer.neurons.size(); ++n) {
                layer.neurons.get(n).updateWeights(prevLayer);
            }
        }
    }

    public void train(Double[] inputs, Double[] targets) {
        feedForward(inputs);
        backPropagation(targets);
    }

    public Double[] run(Double[] inputs) {
        return  feedForward(inputs);
    }

    @Override
    public String toString() {
        String message = "\n- Network\n";
        for (Layer layer : layers) {
            message += layer.toString();
            message += "\n";
        }
        return message;
    }

}
