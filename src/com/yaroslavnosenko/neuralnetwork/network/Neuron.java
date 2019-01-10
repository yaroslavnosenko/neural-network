package com.yaroslavnosenko.neuralnetwork.network;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private List<Double> weights;
    private int index;
    private Double out;
    private Double sum;
    private Double gradient;

    void setOut(Double out) {
        this.out = out;
    }

    Double getOut() {
        return out;
    }

    Neuron(int prevLayerCount, int index) {
        this.index = index;
        this.weights = new ArrayList<>();
        if (prevLayerCount == 0) return;
        for (int i = 0; i < prevLayerCount; i++) {
            weights.add(Math.random());
        }
        //Bias
        weights.add(Math.random());
    }

    void feedForward(Layer prev) {
        this.sum = 0.0;
        int lastWeightIndex = weights.size() - 1;
        for (int i = 0; i < lastWeightIndex; i++) {
            this.sum += weights.get(i) * prev.neurons.get(i).out;
        }
        this.sum += weights.get(lastWeightIndex);
        this.out = activationFunction(this.sum);
    }

    void calcOutGradient(Double target) {
        double delta = target - this.out;
        this.gradient = activationFunctionDerivative(this.out) * delta;
    }

    void calcHiddenGradient(Layer next) {
        double sum = 0.0;
        for (int neuronIndex = 0; neuronIndex < next.neurons.size(); neuronIndex++) {
            Neuron neuron = next.neurons.get(neuronIndex);
            sum += neuron.weights.get(this.index) * neuron.gradient;
        }
        this.gradient = sum * activationFunctionDerivative(out);
    }

    void updateWeights(Layer prev) {
        for (int neuronIndex = 0; neuronIndex < prev.neurons.size(); neuronIndex++) {
            Neuron prevNeuron = prev.neurons.get(neuronIndex);
            Double delta_weight = Network.getAlpha() * this.gradient * prevNeuron.out;
            Double weight = weights.get(neuronIndex);
            weight += delta_weight;
            weights.set(neuronIndex, weight);
        }
        Double bias = weights.get(weights.size() - 1);
        bias += Network.getAlpha() * gradient * 1;
        weights.set(weights.size() - 1, bias);
    }

    private static double activationFunction(double x) {
        return (1 / (1 + Math.pow(Math.E, (-1 * x))));
    }

    private static double activationFunctionDerivative(double sigmoid) {
        return sigmoid * (1 - sigmoid);
    }

    @Override
    public String toString() {
        String message = "--- Neuron | ";
        if (!weights.isEmpty()) {
            message += "w: ";
        }
        for (Double weight : weights) {
            message += weight + "; ";
        }
        message += "o: " + out + "; ";
        message += "s: " + this.sum + "; ";
        message += "g: " + this.gradient + "; ";
        return message;
    }

}
