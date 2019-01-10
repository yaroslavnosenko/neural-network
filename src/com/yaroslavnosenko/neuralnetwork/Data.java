package com.yaroslavnosenko.neuralnetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Data {

    private String fileName;
    private double testsCount;
    private List<Double[]> testIn;
    private List<Double[]> testOut;

    private List<Double[]> trainIn;
    private List<Double[]> trainOut;

    public List<Double[]> getTestIn() {
        return testIn;
    }

    public List<Double[]> getTestOut() {
        return testOut;
    }

    public List<Double[]> getTrainIn() {
        return trainIn;
    }

    public List<Double[]> getTrainOut() {
        return trainOut;
    }

    public Data(String fileName) {
        testIn = new ArrayList<>();
        testOut = new ArrayList<>();
        trainIn = new ArrayList<>();
        trainOut = new ArrayList<>();
        this.fileName = fileName;
    }

    public void read(double tests, DataAdapter adapter) {
        this.testsCount = tests;
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                String[] dataString = line.split(cvsSplitBy);
                Double[] input = new Double[dataString.length - 1];
                Double[] out = new Double[1];
                for (int i = 0; i < dataString.length - 1; i++) {
                    input[i] = Double.valueOf(dataString[i]);
                }
                out[0] = Double.valueOf(dataString[dataString.length - 1]);
                input = adapter.adaptInput(input);
                out = adapter.adaptOutput(out);
                testIn.add(input);
                testOut.add(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        separateData();
    }

    private void separateData() {
        int size = testIn.size();
        for (int i = testIn.size() - 1; i > size * this.testsCount; i--) {
            Random random = new Random();
            int index = random.nextInt(i);
            trainIn.add(testIn.get(index));
            testIn.remove(index);
            trainOut.add(testOut.get(index));
            testOut.remove(index);
        }
    }

    public interface DataAdapter {
        public Double[] adaptInput(Double[] input);

        public Double[] adaptOutput(Double[] output);
    }

}
