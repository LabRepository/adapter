package eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty;

import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.CodeMismatchException;
import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.IncorrectDoorCodeException;
import eu.jpereira.trainings.designpatterns.structural.adapter.model.Door;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeCodeForUnlockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeStateOfLockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotUnlockDoorException;

import static eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.ThirdPartyDoor.DoorState.CLOSED;
import static eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.ThirdPartyDoor.DoorState.OPEN;

public class ThirdPartyDoorAdaper extends ThirdPartyDoor implements Door {
    @Override
    public void open(String code) throws IncorrectDoorCodeException {
        if(!isOpen()){
            try {
                unlock(code);
                setState(OPEN);
            } catch (CannotUnlockDoorException e) {
                    throw new IncorrectDoorCodeException();
            } catch (CannotChangeStateOfLockedDoor e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        if(isOpen()) {
            try {
                setState(CLOSED);
            } catch (CannotChangeStateOfLockedDoor e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isOpen() {
        if(getState().equals(OPEN)){
            return true;
        } return false;
    }

    @Override
    public void changeCode(String oldCode, String newCode, String newCodeConfirmation) throws IncorrectDoorCodeException, CodeMismatchException {
        if (newCode.equals(newCodeConfirmation)) {
            try {
                open(oldCode);
                setNewLockCode(newCode);
            } catch (IncorrectDoorCodeException e){
                throw e;
            } catch (CannotChangeCodeForUnlockedDoor e) {
                e.printStackTrace();
            }
        } else {
            throw new CodeMismatchException();
        }
    }
    @Override
    public boolean testCode(String code) {
        try {
            unlock(code);
            lock();
            return true;
        } catch (CannotUnlockDoorException e){
            return false;
        }
    }
}
