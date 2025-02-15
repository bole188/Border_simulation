package backenddev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import application.MainSceneController;

public class TerminalChecker extends Thread {
	public boolean changed[] = new boolean[5];
	public TerminalChecker() {
		for(int i = 0;i<5;i++) changed[i] = false;
	}
	public static boolean fileExists(String filePath) {
        return new java.io.File(filePath).exists();
    }

	private void checkTerminals(){
		String path = "C:\\Users\\Korisnik\\Desktop\\BoskoB\\javaProjekatEclipse\\BorderSimulation\\checkTerminals.txt";
		try {
			FileHandler fileHandler = new FileHandler("error.log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            ExceptionHandler.logger.addHandler(fileHandler);
            fileHandler.close();
		while(true) {
			sleep(100);
				if(!fileExists(path)) {
					sleep(1000);
					continue;
				}
				BufferedReader reader = new BufferedReader(new FileReader(path));
				String line = reader.readLine();
				reader.close();
				char ch;
				int i = 0;
				int selectedTerminal;
				for(;i<line.length();i++) {
					if(i%2 != 0) {
						continue;
					}
					ch = line.charAt(i);
					int value = Character.getNumericValue(ch);
					selectedTerminal = i/2;
					//System.out.println(selectedTerminal + " " + value);
					switch(selectedTerminal) {
					case 0:
						if(changed[selectedTerminal] && value==1) {
							Border.pV.PoliceSem4V.release();
							MainSceneController.firstPoliceTerminal = 1;
							changed[selectedTerminal] = false;
						}
						else if(value == 0 && !changed[selectedTerminal]) {
							Border.pV.PoliceSem4V.acquire();
							MainSceneController.firstPoliceTerminal = 0;
							changed[selectedTerminal] = true;
						}
						break;
					case 1:
						if(changed[selectedTerminal] && value==1) {
							Border.pV.PoliceSem4V.release();
							MainSceneController.secondPoliceTerminal = 1;
							changed[selectedTerminal] = false;
						}
						else if(value == 0 && !changed[selectedTerminal]) {
							Border.pV.PoliceSem4V.acquire();
							MainSceneController.secondPoliceTerminal = 0;
							changed[selectedTerminal] = true;
						}
						break;
					case 2:
						if(changed[selectedTerminal] && value==1) {
							Border.pT.PoliceSem4T.release();
							changed[selectedTerminal] = false;
						}
						else if(value == 0 && !changed[selectedTerminal]) {
							Border.pT.PoliceSem4T.acquire();
							changed[selectedTerminal] = true;
						}
						break;
					case 3:
						if(changed[selectedTerminal] && value==1) {
							Border.cV.CustomsSem4V.release();
							changed[selectedTerminal] = false;
						}
						else if(value == 0 && !changed[selectedTerminal]) {
							Border.cV.CustomsSem4V.acquire();
							changed[selectedTerminal] = true;
						}
						break;
					case 4:
						if(changed[selectedTerminal] && value==1) {
							Border.cT.CustomsSem4T.release();
							changed[selectedTerminal] = false;
						}
						else if(value == 0 && !changed[selectedTerminal]) {
							Border.cT.CustomsSem4T.acquire();
							changed[selectedTerminal] = true;
						}
						break;
					default:
						break;
				}
				}
		}
		}catch(IOException | InterruptedException e) {
			ExceptionHandler.logger.log(Level.SEVERE, e.fillInStackTrace().toString(),e);
		}
	}
	public void run() {
		checkTerminals();
	}
}
