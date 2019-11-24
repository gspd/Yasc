package yasc.motor;

import java.util.ArrayList;

public class Clock {
    private final double initialTimeClock; // Initial time clock
    private double clockTimeDuration; // Clock time duration
    private double frequency; // Clock time interval
    private double numberPulse; // Quantidade de pulsos 
    private ArrayList<Double> listPulse;
    
    public Clock(double clockTimeDuration, double frequency) {
        initialTimeClock = 0;
        this.clockTimeDuration = clockTimeDuration;
        this.frequency = frequency;
        setNumberPulse();
        setPulses();
    }
    
    private void setPulses() {
        listPulse = new ArrayList<>();
        double f = 0;
        for (int i = 0; i < numberPulse; i++) {
            f = f + frequency;
            listPulse.add(f);
        }
    }
    
    public ArrayList<Double> getListPulse() {
        return listPulse;
    }

    public double getInitialTimeClock() {
        return initialTimeClock;
    }

    public double getClockTimeDuration() {
        return clockTimeDuration;
    }
    
    private void setNumberPulse() {
        numberPulse = clockTimeDuration / frequency;
    }
    
    public double getNumberPulse() {
        return numberPulse;
    }

    /**
     * Get the frequency time
     * @return Float value of clock time interval
     */
    public double getFrequency() {
        return frequency;
    }
    
}
