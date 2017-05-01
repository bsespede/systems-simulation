package simulation.force;

import java.util.List;

import math.Vector3d;
import simulation.particle.Particle;

public class Gravity {
	
	public final static double G = 6.74 * Math.pow(10, -11);

	public static Vector3d gravitationalForceBetween(final Particle p1, final Particle p2) {		
		final Vector3d r = p2.getPosition().substract(p1.getPosition());
		double module = G * p1.getMass() * p2.getMass() / Math.pow(r.module(), 2);
		return r.scale(module);
	}
	
	public static Vector3d gravitationalForceBetween(final Particle p1, final List<Particle> particles) {	
		Vector3d sumForces = new Vector3d(0, 0 , 0);
		for (Particle p2: particles) {
			if (!p1.equals(p2)) {
				sumForces = sumForces.add(Gravity.gravitationalForceBetween(p1, p2));
			}
		}
		return sumForces;
	}
}
