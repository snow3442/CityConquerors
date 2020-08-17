package model.abilities.Animations;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static view.GameView.gamePane;

/*
Ability Notification animation
 */
public class Animator {

    private static Animator instance = null;
    private static final int ANIMATION_SECONDS = 2;
    private Random random = new Random();
    private NotiText[] texts = new NotiText[2];
    private int currentIndex = -1;
    private GraphicsContext g;
    private double time = 0;
    private List<Particle> particles = new ArrayList<>();
    private int maxParticles = 0;
    private ArrayList<Text> arr = new ArrayList<>();
    private AnimationTimer timer;
    private Canvas canvas;
    private boolean stopAnim = false;
    private final double CANVASH = 200;
    private final double CANVASW = 400;
    private final double CANVASX = 200;
    private final double CANVASY = 0;

    private Animator() {

    }

    public static synchronized Animator getInstance() {
        if (instance == null) {
            instance = new Animator();
        }

        return instance;
    }

    public void NotificationAnimation(String str) {
        canvas = new Canvas(CANVASW, CANVASH);
        arr.add(new Text(str));
        arr.add(new Text(""));
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLUE);
        gamePane.getChildren().add(canvas);
        canvas.setLayoutX(CANVASX);
        canvas.setLayoutY(CANVASY);
        populateDigits();
        populateParticles();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }

    private void onUpdate() {
        g.clearRect(0, 0, CANVASW, CANVASH);
        if ((time == 0 || time > ANIMATION_SECONDS) && !stopAnim) {
            currentIndex++;
            if (currentIndex == 2) {
                //currentIndex = 0;
                timer.stop();
                stopAnim = true;
                particles.clear();
                g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gamePane.getChildren().remove(canvas);
                currentIndex=-1;
            }

            particles.parallelStream().forEach(p -> {
                if (p.index < texts[currentIndex].positions.size()) {
                    Point2D point = texts[currentIndex].positions.get(p.index);

                    // offset to center of screen
                    p.nextX = point.getX()+100;
                    p.nextY = point.getY();
                } else {
                    if (random.nextBoolean()) {
                        // move horizontally
                        p.nextX = random.nextBoolean() ? CANVASW+5 : -5;
                        p.nextY = random.nextInt((int)CANVASH);
                    } else {
                        // move vertically
                        p.nextX = random.nextInt((int)CANVASW);
                        p.nextY = random.nextBoolean() ? CANVASH+5 : -5;
                    }
                }

                p.dx = p.nextX - p.x;
                p.dy = p.nextY - p.y;
                p.dx /= ANIMATION_SECONDS * 10 * random.nextDouble();
                p.dy /= ANIMATION_SECONDS * 10 * random.nextDouble();

            });

            time = 0;

        }

        for (Particle p : particles) {
            p.update();
            p.render(g);
        }

        time += 0.016;
    }

    private void populateDigits() {
        for (int i = 0; i < texts.length; i++) {
            texts[i] = new NotiText();
            Text text = arr.get(i);
            text.setFont(Font.font("Helvetica", 60));
            text.setFill(Color.BLACK);
            Image snapshot = text.snapshot(null, null);
            for (int y = 0; y < snapshot.getHeight(); y++) {
                for (int x = 0; x < snapshot.getWidth(); x++) {
                    if (snapshot.getPixelReader().getColor(x, y).equals(Color.BLACK)) {
                        texts[i].positions.add(new Point2D(x, y));
                    }
                }
            }
            //determining particle numbers to ensure we have enough particles to paint the text
            if (texts[i].positions.size() > maxParticles) {
                maxParticles = texts[i].positions.size();
            }
        }
    }

    private void populateParticles() {
        for (int i = 0; i < maxParticles; i++) {
            particles.add(new Particle(i));
        }
    }

    private static class NotiText {

        private List<Point2D> positions = new ArrayList<>();
    }

    private static class Particle {

        private double x, y;
        private double nextX, nextY;
        private double dx, dy;
        private int index;

        public Particle(int index) {
            this.index = index;
        }

        void update() {
            if (Math.abs(nextX - x) > Math.abs(dx)) {
                x += dx;
            } else {
                x = nextX;
            }

            if (Math.abs(nextY - y) > Math.abs(dy)) {
                y += dy;
            } else {
                y = nextY;
            }
        }

        void render(GraphicsContext g) {
            Point2D center = new Point2D(350, 200);
            double alpha = 1 - new Point2D(x, y).distance(center) / 500;
            g.setGlobalAlpha(alpha);
            g.fillOval(x, y, 2, 2);
        }
    }
}
