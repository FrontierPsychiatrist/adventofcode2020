package adventofcode2020;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 {

    static sealed class Operation {
        public final int value;

        Operation(int value) {
            this.value = value;
        }
    }

    static final class NoOp extends Operation {
        NoOp(int value) {
          super(value);
        }
    }

    static final class Acc extends Operation {
        Acc(int value) {
            super(value);
        }
    }

    static final class Jmp extends Operation {
        Jmp(int value) {
            super(value);
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
        private int currentFix = -1;
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

        public void changeNext() {
          if (currentFix != -1) {
            var changed = program.get(currentFix);
            if (changed.operation instanceof Jmp) {
              System.out.println("Reverting back to NoOp at " + currentFix);
              program.set(currentFix, new Instruction(new NoOp(changed.operation.value)));
            } else {
              System.out.println("Reverting back to Jmp at " + currentFix);
              program.set(currentFix, new Instruction(new Jmp(changed.operation.value)));
            }
          }

          this.accumulator = 0;
          for (int i = 0; i < program.size(); i++) {
            program.get(i).visited = 0;
          }

          currentFix++;
          while (program.get(currentFix).operation instanceof Acc) {
            currentFix++;
          }
          var toSwap = program.get(currentFix);
          if (toSwap.operation instanceof Jmp) {
            System.out.println("Changing jump at " + currentFix);
            program.set(currentFix, new Instruction(new NoOp(toSwap.operation.value)));
          } else {
            System.out.println("Changing noop at " + currentFix);
            program.set(currentFix, new Instruction(new Jmp(toSwap.operation.value)));
          }
        }

        public boolean terminates() {
          this.cursor = 0;
          while (this.cursor < program.size() - 1) {
            Instruction nextInstruction = program.get(cursor);
            if (nextInstruction.visited == 1) {
              System.out.println("Program looped at " + cursor);
              return false;
            }
            executeInstruction(nextInstruction);
          }
          return true;
        }

        private void executeInstruction(Instruction instruction) {
          instruction.visited++;
          if (instruction.operation instanceof NoOp) {
            cursor++;
          } else if (instruction.operation instanceof Acc acc) {
            accumulator += acc.value;
            cursor++;
          } else if (instruction.operation instanceof Jmp jmp) {
            this.cursor += jmp.value;
          }
        }

        public int runUntilRepeat() {
            Instruction nextInstruction = program.get(cursor);
            while (nextInstruction.visited < 1) {
              executeInstruction(nextInstruction);
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
                case "nop" -> new NoOp(argument);
                case "acc" -> new Acc(argument);
                case "jmp" -> new Jmp(argument);
                default -> throw new IllegalArgumentException("Unknown operation: " + operation);
            };

        }
    }

    public static void main(String[] args) {
      var emulator = new Emulator();
      // var result = emulator.runUntilRepeat();
      while (!emulator.terminates()) {
        emulator.changeNext();
      }
      System.out.println("result = " + emulator.accumulator);
    }
}

