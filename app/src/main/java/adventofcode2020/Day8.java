package adventofcode2020;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 {

    static sealed class Operation {
    }

    static final class NoOp extends Operation {
    }

    static final class Acc extends Operation {
        public final int value;

        Acc(int value) {
            this.value = value;
        }
    }

    static final class Jmp extends Operation {
        public final int value;

        Jmp(int value) {
            this.value = value;
        }
    }

    static class Instruction {
        public final Operation operation;
        public int visited = 0;

        Instruction(Operation operation) {
            this.operation = operation;
        }
    }


    static class Emulator {
        private int accumulator;
        private int cursor;
        private final List<Instruction> program;

        Emulator() {
            var input = this.getClass().getClassLoader()
                    .getResourceAsStream("day8_handheld_halting");
            program = new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .map(Emulator::parseLine)
                    .map(Instruction::new)
                    .collect(Collectors.toList());
        }

        public int runUntilRepeat() {
            Instruction nextInstruction = program.get(cursor);
            while (nextInstruction.visited < 1) {
                nextInstruction.visited++;
                if (nextInstruction.operation instanceof NoOp) {
                    cursor++;
                } else if (nextInstruction.operation instanceof Acc acc) {
                    accumulator += acc.value;
                    cursor++;
                } else if (nextInstruction.operation instanceof Jmp jmp) {
                    this.cursor += jmp.value;
                }
                nextInstruction = program.get(cursor);
            }
            System.out.println("Stopped at instruction " + cursor);
            return this.accumulator;
        }

        private static Operation parseLine(String line) {
            var tokens = line.split(" ");
            var operation = tokens[0];
            var argument = Integer.parseInt(tokens[1]);
            return switch (operation) {
                case "nop" -> new NoOp();
                case "acc" -> new Acc(argument);
                case "jmp" -> new Jmp(argument);
                default -> throw new IllegalArgumentException("Unknown operation: " + operation);
            };

        }
    }

    public static void main(String[] args) {
        var result = new Emulator().runUntilRepeat();
        System.out.println("result = " + result);
    }
}