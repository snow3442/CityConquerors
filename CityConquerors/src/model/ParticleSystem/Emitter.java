
package model.ParticleSystem;

import java.util.List;
import javafx.geometry.Point2D;

public abstract class Emitter {
   public abstract List<Particle> emit(double x, double y, Point2D velocity); 
}
