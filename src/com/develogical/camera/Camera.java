package com.develogical.camera;

public class Camera implements WriteListener {

    private Sensor sensor;
    private MemoryCard memoryCard;
    private boolean isCameraOn = false;
    private boolean isDataBeingWritten = false;

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public Camera(Sensor sensor, MemoryCard memoryCard, boolean isCameraOn) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
        this.isCameraOn= isCameraOn;
    }

    public void pressShutter() {
        if(isCameraOn()){
            memoryCard.write(this.sensor.readData());
            isDataBeingWritten = true;
        }
    }

    public void powerOn() {
        isCameraOn = true;
        sensor.powerUp();
    }

    public void powerOff() {
        if(!this.isDataBeingWritten){
            isCameraOn = false;
            sensor.powerDown();
        }

    }

    @Override
    public void writeComplete() {
        isDataBeingWritten = false;
        sensor.powerDown();
    }

    public boolean isCameraOn() {
        return isCameraOn;
    }

}

