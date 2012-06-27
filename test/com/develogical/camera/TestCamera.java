package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Member;

@RunWith(value = JMock.class)
public class TestCamera {

    Mockery context = new Mockery();
    final Sensor sensor = context.mock(Sensor.class);
    final MemoryCard memoryCard = context.mock(MemoryCard.class);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {

        Camera camera = new Camera(sensor, memoryCard);


        context.checking(new Expectations(){{
            oneOf(sensor).powerUp();
        }});

        camera.powerOn();

    }

    @Test
    public void switchingTheCameraOffPowersOffTheSensor() {


        Camera camera = new Camera(sensor, memoryCard);


        context.checking(new Expectations(){{
            oneOf(sensor).powerDown();
        }});

        camera.powerOff();

    }

    @Test
    public void pressingTheShutterPowerOffDoesNothing() {

        Camera camera = new Camera(sensor, memoryCard, false);

        context.checking(new Expectations(){{
            never(sensor);
        }});

        camera.pressShutter();

    }

    @Test
    public void pressingTheShutterPowerOnCopiesDataFromSensorToMemory() {

        Camera camera = new Camera(sensor, memoryCard, true);
        final byte[] data = new byte[]{1,2,3};

        context.checking(new Expectations(){{
            context.checking(new Expectations(){{
                oneOf(sensor).readData(); will(returnValue(data));
                oneOf(memoryCard).write(with(equal(data)));
            }});
        }});

        camera.pressShutter();

    }

    @Test
    public void whenDataIsBeingWrittenSwitchingTheCameraOffDoesNotPowersOffTheSensor() {

        Camera camera = new Camera(sensor, memoryCard, true);


        context.checking(new Expectations(){{
            context.checking(new Expectations(){{
                oneOf(sensor).readData();
                oneOf(memoryCard).write(with(any(byte[].class)));
                exactly(0).of(sensor).powerDown();

            }});
        }});

        camera.pressShutter();
        camera.powerOff();

    }

    @Test
    public void onceWritingTheDataHasCompletedTheCameraOffPowersOffTheSensor() {

        Camera camera = new Camera(sensor, memoryCard, true);


        context.checking(new Expectations(){{
            context.checking(new Expectations(){{
                oneOf(sensor).readData();
                oneOf(memoryCard).write(with(any(byte[].class)));
                oneOf(sensor).powerDown();
            }});
        }});

        camera.pressShutter();
        camera.writeComplete();

    }



}
