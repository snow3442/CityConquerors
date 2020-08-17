
package model.ParticleSystem;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;


public class ExplosiveArrowEmitter extends Emitter {

   List<Particle> particles = new ArrayList<>();

    @Override
    public List<Particle> emit(double x, double y, Point2D velocity) {
        int numParticles = 15;
        for (int i = 0; i < numParticles; i++) {     
            Particle p = new Particle(x, y, velocity,
                    15, 1, Color.rgb(230, 40, 45), BlendMode.ADD);
            particles.add(p);            
        }
        return particles;
    }
    
}
