package simulation.force;

import java.util.List;

import math.Vector2d;
import simulation.particle.Particle;
import terrain.Terrain;
import terrain.Wall;

public class Force {

	public static Vector2d getTotalForce(final Particle particle, final List<Particle> particles, final Terrain terrain) {
		Vector2d totalForce = Gravitational.getForce(particle);
		for (Particle otherParticle: particles) {
			if (!particle.equals(otherParticle)) {
				totalForce = totalForce.add(Granular.getForce(particle, otherParticle));
			}
		}
		for (Wall wall: terrain.getWalls()) {
			totalForce = totalForce.add(Granular.getForce(particle, wall));
		}
		return totalForce;
	}
	
}
