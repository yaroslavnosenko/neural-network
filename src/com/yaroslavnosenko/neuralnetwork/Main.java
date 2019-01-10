package com.yaroslavnosenko.neuralnetwork;

import com.yaroslavnosenko.neuralnetwork.draw.Draw;
import com.yaroslavnosenko.neuralnetwork.network.Network;

import java.util.*;

public class Main {


    public static void main(String[] args) {
        Data data = null;
        Network network = null;
        Draw draw = new Draw();
        List<Integer> trainOutDraw = new ArrayList<>();
        List<Integer> testOutDraw = new ArrayList<>();
        System.out.println();
        System.out.println("- Welcome -");
        System.out.println();
        System.out.println("---- Choose dataset:");
        System.out.println("- Banana[0]");
        System.out.println("- Spiral[1]");
        System.out.println("- Data[2]");
        System.out.println();
        System.out.print("- Choose: ");
        Scanner scanner = new Scanner(System.in);
        int choose = scanner.nextInt();

        if (choose == 0) {
            data = new Data("banana.csv");
            data.read(0.8, new Data.DataAdapter() {
                @Override
                public Double[] adaptInput(Double[] input) {
                    return input;
                }

                @Override
                public Double[] adaptOutput(Double[] output) {
                    if (output[0] == 1.0) output[0] = 0.0;
                    if (output[0] == 2.0) output[0] = 0.5;
                    if (output[0] == 3.0) output[0] = 1.0;
                    return output;
                }
            });

            for (Double[] target : data.getTrainOut()) {
                int result = 0;
                if (target[0] == 0.5) result = 1;
                if (target[0] == 1.0) result = 2;
                trainOutDraw.add(result);
            }

            network = new Network(0.2, 2, 7, 1);
        }

        if (choose == 1) {
            data = new Data("spiral.csv");
            data.read(0.7, new Data.DataAdapter() {
                @Override
                public Double[] adaptInput(Double[] input) {
                    input[0] /= 100;
                    input[1] /= 100;
                    return input;
                }

                @Override
                public Double[] adaptOutput(Double[] output) {
                    return output;
                }
            });

            for (Double[] target : data.getTrainOut()) {
                trainOutDraw.add((int) Math.round(target[0]));
            }

            network = new Network(0.1, 2, 12, 10, 1);
        }

        if (choose == 2) {
            data = new Data("data_2.csv");
            data.read(0.9, new Data.DataAdapter() {
                @Override
                public Double[] adaptInput(Double[] input) {
                    return input;
                }

                @Override
                public Double[] adaptOutput(Double[] output) {
                    return output;
                }
            });

            network = new Network(0.2, 9, 15, 1);
        }

        if (choose != 2) {
            draw.draw("Train set", data.getTrainIn(), trainOutDraw);
        }

        for (int j = 0; j < 3000; j++) {
            double error = 0.0;
            for (int i = 0; i < data.getTrainIn().size(); i++) {
                network.train(data.getTrainIn().get(i), data.getTrainOut().get(i));
                error += network.getLastError() * network.getLastError();
            }
            error /= data.getTrainIn().size();
            error = Math.sqrt(error);
            System.out.println("- e: " + error);
            if (error < 0.02) break;
        }

        int testError = 0;
        int[] tries = new int[3];
        int[] errors = new int[3];
        for (int i = 0; i < data.getTestIn().size(); i++) {
            Double[] out = network.run(data.getTestIn().get(i));
            Double target = data.getTestOut().get(i)[0];
            Double result = out[0];

            if (choose == 0) {
                if (result <= 0.25) {
                    testOutDraw.add(0);
                    tries[0]++;
                    if (target != 0.0) {
                        testError++;
                        errors[0]++;
                    }
                }
                if (result > 0.25 && result < 0.75) {
                    testOutDraw.add(1);
                    tries[1]++;
                    if (target != 0.5) {
                        testError++;
                        errors[1]++;
                    }
                }
                if (result >= 0.75) {
                    testOutDraw.add(2);
                    tries[2]++;
                    if (target != 1) {
                        testError++;
                        errors[2]++;
                    }
                }
            }
            if (choose == 1 || choose == 2) {
                testOutDraw.add((int) Math.round(result));
                if (target == 0.0) tries[0]++;
                else tries[1]++;
                if (target == 0.0 && result > 0.5) {
                    testError++;
                    errors[0]++;
                }
                if (target == 1.0 && result < 0.5) {
                    testError++;
                    errors[1]++;
                }
            }

        }

        System.out.println("\n\n- TEST");
        System.out.println();
        System.out.println("Classified as 0 | Correct: " + (tries[0] - errors[0]) + " | Incorrect: " + errors[0] + " | Total: " + tries[0]);
        System.out.println("Classified as 1 | Correct: " + (tries[1] - errors[1]) + " | Incorrect: " + errors[1] + " | Total: " + tries[1]);
        if (choose == 0)
            System.out.println("Classified as 2 | Correct: " + (tries[2] - errors[2]) + " | Incorrect: " + errors[2] + " | Total: " + tries[2]);
        System.out.println("Total | Correct: " + (data.getTestIn().size() - testError) + " | Incorrect: " + testError + " | Total: " + data.getTestIn().size());
        if (choose != 2) {
            draw.draw("Test set", data.getTestIn(), testOutDraw);
        }
    }

}
