package cs4103.entryPoint;

import cs4103.GUI.SimulationGUI;
import cs4103.GUI.SimulationModel;

public class StartGUIMode {

	/**
	 * A class that is the entry point to the GUI interface, specified in
	 * {@link SimulationGUI}
	 */
	public static void main(String[] args) {
		new SimulationGUI(new SimulationModel());
	}

}
