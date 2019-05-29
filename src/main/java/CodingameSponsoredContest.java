import java.util.*;
import java.util.stream.Collectors;

class CodingameSponsoredContest {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        final Init init = readInit(in);
        final List<GameTurn> gameTurns = new ArrayList<>();
        final Cartography cartography = new Cartography();
        int turn = 1;

        // game loop
        while (true) {
            final GameTurn gameTurn = readGameInput(init, turn++, in);
            cartography.addSurrounding(gameTurn);
            gameTurns.add(gameTurn);

            // every turn print A, B, C, D or E

            // this feels like movement on a map
            //  maybe try left wall algorithm
            //  maybe add mapping algorithm to draw map for me
            //  check numbers, completely ignored so far :)


            if (gameTurn.getC().equals("_")) {
                gameTurn.setOutput('D');
            } else if (gameTurn.getA().equals("_")) {
                gameTurn.setOutput('C');
            } else if (gameTurn.getB().equals("_")) {
                gameTurn.setOutput('A');
            } else if (gameTurn.getD().equals("_")) {
                gameTurn.setOutput('E');
            } else {
                gameTurn.setOutput('B'); // no clue what this does
            }

            //printHistory(gameTurns);
            cartography.print();
            System.out.println(gameTurn.getOutput());
        }
    }

    private static void printHistory(List<GameTurn> gameTurns) {
        StringBuilder result = new StringBuilder();
        for (GameTurn gameTurn : gameTurns) {
            result.append(gameTurn.toCompactString()).append('\n');
        }
        System.err.print(result);
    }

    private static GameTurn readGameInput(Init init, int turn, Scanner in) {
        final GameTurn gameTurn = new GameTurn(turn, in.nextLine(), in.nextLine(), in.nextLine(), in.nextLine());
        for (int i = 0; i < init.C; i++) {
            gameTurn.addTuple(readTuple(in));
        }
        in.nextLine();
        System.err.println("Input: " + gameTurn);
        return gameTurn;
    }

    private static Tuple readTuple(Scanner in) {
        return new Tuple(in.nextInt(), in.nextInt());
    }

    private static Init readInit(Scanner in) {
        final Init init = new Init(in.nextInt(), in.nextInt(), in.nextInt());
        if (in.hasNextLine()) {
            in.nextLine();
        }
        System.err.println("Init: " + init);
        return init;
    }

    private static class Init {
        private final int A;
        private final int B;
        private final int C;

        private Init(int a, int b, int c) {
            A = a;
            B = b;
            C = c;
        }

        @Override
        public String toString() {
            return String.format("Init{A=%d, B=%d, C=%d}", A, B, C);
        }

        public int getA() {
            return A;
        }

        public int getB() {
            return B;
        }

        public int getC() {
            return C;
        }
    }

    private static class GameTurn {
        private final int turn;
        private final String A;
        private final String B;
        private final String C;
        private final String D;
        private final List<Tuple> tuples;

        private char output;

        private GameTurn(int turn, String a, String b, String c, String d) {
            this.turn = turn;
            this.A = a;
            this.B = b;
            this.C = c;
            this.D = d;
            this.tuples = new ArrayList<>();
        }

        private void addTuple(Tuple tuple) {
            tuples.add(tuple);
        }

        @Override
        public String toString() {
            return String.format("GameTurn{turn=%d, A='%s', B='%s', C='%s', D='%s', tuples=%s, output=%s}", turn, A, B, C, D, tuples, output);
        }

        public String toCompactString() {
            return String.format("%02d %s%s%s%s -> %s (%s)", turn, A, B, C, D, output, getTuplesAsCompactString());
        }

        private String getTuplesAsCompactString() {
            return tuples.stream().map(Tuple::toString).collect(Collectors.joining(","));
        }

        public String getA() {
            return A;
        }

        public String getB() {
            return B;
        }

        public String getC() {
            return C;
        }

        public String getD() {
            return D;
        }

        public List<Tuple> getTuples() {
            return tuples;
        }

        public char getOutput() {
            return output;
        }

        public void setOutput(char output) {
            this.output = output;
        }

        public String getDirectionsAsString() {
            return String.format("%s%s%s%s", A, B, C, D);
        }
    }

    private static class Tuple {
        private final Integer X;
        private final Integer Y;

        private Tuple(Integer x, Integer y) {
            X = x;
            Y = y;
        }

        @Override
        public String toString() {
            return String.format("(%d/%d)", X, Y);
        }

        public boolean isNeighbor(Tuple other) {
            return (this.X.equals(other.X) && Math.abs(this.Y - other.Y) == 1) ||
                    (this.Y.equals(other.Y) && Math.abs(this.X - other.X) == 1);
        }

        public Tuple getNeighbor(Direction direction) {
            return add(direction.getDirectionalOffset());
        }

        public Tuple add(Tuple other) {
            return new Tuple(this.X + other.X, this.Y + other.Y);
        }
    }

    private static class Cartography {
        private BoundingBox boundingBox;
        private Tuple position;
        private Map<Tuple, Field> map;

        public Cartography() {
            map = new HashMap<>();
            position = new Tuple(0, 0);
            boundingBox = new BoundingBox(position);

            final Field startingField = new Field(position, FieldContent.EMPTY);
            addField(startingField);
            startingField.visit();
        }

        public void addSurrounding(GameTurn gameTurn) {
            final CartographyInputDto inputDto = new CartographyInputDto(gameTurn.getDirectionsAsString());
            for (Direction direction : inputDto.getMappedDirections()) {
                final Tuple neighborPosition = position.getNeighbor(direction);
                final FieldContent content = inputDto.getFieldContent(direction);
                final Field field = new Field(neighborPosition, content);
                verifyOrAddField(field);
            }
        }

        private void verifyOrAddField(Field field) {
            if (containsField(field)) {
                if (map.get(field.getPosition()) != field) {
                    throw new InputMismatchException();
                }
            } else {
                addField(field);
            }
        }

        public void visit(Tuple newPosition) {
            if (!position.isNeighbor(newPosition)) {
                throw new IllegalArgumentException(String.format("Position %s can not be reached in one step", newPosition));
            }
            position = newPosition;
            map.get(newPosition).visit();
        }

        public void addField(Field field) {
            map.put(field.getPosition(), field);
            boundingBox.extend(field.getPosition());
        }

        public boolean containsField(Field field) {
            return map.containsKey(field.getPosition());
        }

        public void print() {
            StringBuilder output = new StringBuilder();
            // iterate bounding box and get from map and print ' ', _, #
        }
    }

    private static class BoundingBox {
        private Tuple topLeft;
        private Tuple bottomRight;

        public BoundingBox(Tuple startingPosition) {
            this.topLeft = this.bottomRight = startingPosition;
        }

        public boolean isInside(Tuple position) {
            return topLeft.X <= position.X &&
                    topLeft.Y >= position.Y &&
                    bottomRight.X >= position.X &&
                    bottomRight.Y <= position.Y;
        }

        public void extend(Tuple position) {
            if (topLeft.X > position.X) {
                topLeft = new Tuple(position.X, topLeft.Y);
            }
            if (topLeft.Y < position.Y) {
                topLeft = new Tuple(topLeft.X, position.Y);
            }
            if (bottomRight.X < position.X) {
                bottomRight = new Tuple(position.X, bottomRight.Y);
            }
            if (bottomRight.Y > position.Y) {
                bottomRight = new Tuple(bottomRight.X, position.Y);
            }
        }
    }

    private static class Field {
        private final Tuple position;
        private final FieldContent content;
        private FieldState state;

        public Field(Tuple position, FieldContent content) {
            this.position = position;
            this.content = content;
            this.state = FieldState.UNVISITED;
        }

        public boolean canVisit() {
            return content.canVisit();
        }

        public boolean isFirstVisit() {
            return content.canVisit() && state == FieldState.UNVISITED;
        }

        public void visit() {
            state = FieldState.VISITED;
        }

        public Tuple getPosition() {
            return position;
        }

        public FieldContent getContent() {
            return content;
        }

        public FieldState getState() {
            return state;
        }
    }

    private enum FieldContent {
        EMPTY('_', true),
        WALL('#', false);

        private final char character;
        private final boolean canVisit;

        private FieldContent(char character, boolean canVisit) {
            this.character = character;
            this.canVisit = canVisit;
        }

        @Override
        public String toString() {
            return String.valueOf(character);
        }

        public char getCharacter() {
            return character;
        }

        public boolean canVisit() {
            return canVisit;
        }

        public static FieldContent getForCharacter(char character) {
            return Arrays.stream(FieldContent.values())
                    .filter(content -> content.character == character)
                    .findFirst()
                    .orElseThrow(FieldContentNotFoundException::new);
        }

    }

    private static class FieldContentNotFoundException extends IllegalArgumentException {
    }

    private enum FieldState {
        UNVISITED,
        VISITED;
    }

    private enum Direction {
        NORTH(0, new Tuple(+1, 0)), // todo fix this
        EAST(1, new Tuple(+1, 0)),
        SOUTH(2, new Tuple(+1, 0)),
        WEST(3, new Tuple(+1, 0));

        private final Integer inputOffset;
        private final Tuple directionalOffset;

        Direction(Integer inputOffset, Tuple directionalOffset) {
            this.inputOffset = inputOffset;
            this.directionalOffset = directionalOffset;
        }

        public Tuple getDirectionalOffset() {
            return directionalOffset;
        }

        public Integer getInputOffset() {
            return inputOffset;
        }
    }

    private static class CartographyInputDto {
        private final String inputString;
        private final Map<Direction, FieldContent> input;

        private CartographyInputDto(String inputString) {
            this.inputString = inputString;
            this.input = parse(inputString);
        }

        private Map<Direction, FieldContent> parse(String inputString) {
            final Map<Direction, FieldContent> result = new HashMap<>();
            for (Direction direction : Direction.values()) {
                final char inputCharacter = inputString.charAt(direction.getInputOffset());
                final FieldContent content = FieldContent.getForCharacter(inputCharacter);
                result.put(direction, content);
            }
            return result;
        }

        public Map<Direction, FieldContent> getInput() {
            return input;
        }

        public Set<Direction> getMappedDirections() {
            return input.keySet();
        }

        public FieldContent getFieldContent(Direction direction) {
            return input.get(direction);
        }
    }

}