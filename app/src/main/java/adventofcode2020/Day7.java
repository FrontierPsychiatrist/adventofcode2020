package adventofcode2020;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record Bag(String colorModifier, String color) {

}

record Edge(Bag container, Bag contained, int number) {

}

record Graph(Set<Bag> nodes, Set<Edge> edges) {

}

public class Day7 {

    private static final Bag SHINY_GOLD = new Bag("shiny", "gold");

    private final Graph graph;

    public Day7() {
        var stream = this.getClass().getClassLoader().getResourceAsStream("day7_handy_haversacks");

        var bags = new HashSet<Bag>();
        var edges = new HashSet<Edge>();
        new BufferedReader(new InputStreamReader(stream))
                .lines()
                .forEach(line -> {
                    var tokenizer = new StringTokenizer(line, " ");

                    var bag = new Bag(tokenizer.nextToken(), tokenizer.nextToken());
                    bags.add(bag);
                    // Discard "bags"
                    tokenizer.nextToken();
                    // Discard "contain"
                    tokenizer.nextToken();

                    while (tokenizer.hasMoreTokens()) {
                        var firstNumber = tokenizer.nextToken();
                        if (firstNumber.equals("no")) break;

                        var count = Integer.parseInt(firstNumber);
                        var containedBag = new Bag(tokenizer.nextToken(), tokenizer.nextToken());
                        // Discard bag + punctuation
                        tokenizer.nextToken();

                        bags.add(containedBag);
                        edges.add(new Edge(bag, containedBag, count));
                    }
                });
        graph = new Graph(bags, edges);
    }

    public void run() {
        // -1 because getContainers adds the SHINY_GOLD itself...
        System.out.println(getContainers(SHINY_GOLD).collect(Collectors.toSet()).size() - 1);
    }

    public Stream<Bag> getContainers(Bag bag) {
        return graph.edges().stream().filter(edge -> edge.contained().equals(bag))
                .map(Edge::container)
                .flatMap(container -> {
                    if (isRoot(container)) {
                        return Stream.of(bag, container);
                    } else {
                        return Stream.concat(Stream.of(bag), getContainers(container));
                    }
                });
    }

    public boolean isRoot(Bag bag) {
        return graph.edges().stream().noneMatch(edge -> edge.contained().equals(bag));
    }

    public void runPartTwo() {
        System.out.println(getContained(SHINY_GOLD).reduce(0, Integer::sum));
    }

    public Stream<Integer> getContained(Bag bag) {
        return graph.edges().stream()
                .filter(edge -> edge.container().equals(bag))
                .flatMap(edge -> {
                    System.out.println(edge);
                    if (isLeaf(edge.contained())) {
                        return Stream.of(edge.number());
                    } else {
                        return Stream.concat(Stream.of(edge.number()),
                                getContained(edge.contained()).map(hurr -> hurr * edge.number()));
                    }
                });
    }

    public boolean isLeaf(Bag bag) {
        return graph.edges().stream().noneMatch(edge -> edge.container().equals(bag));
    }

    public static void main(String[] args) {
        new Day7().runPartTwo();
    }
}
