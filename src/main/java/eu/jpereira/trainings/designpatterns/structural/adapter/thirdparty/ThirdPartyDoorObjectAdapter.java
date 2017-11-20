package eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty;

import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.CodeMismatchException;
import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.IncorrectDoorCodeException;
import eu.jpereira.trainings.designpatterns.structural.adapter.model.Door;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeCodeForUnlockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeStateOfLockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotUnlockDoorException;

import static eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.ThirdPartyDoor.DoorState.CLOSED;
import static eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.ThirdPartyDoor.DoorState.OPEN;

public class ThirdPartyDoorObjectAdapter implements Door {
    ThirdPartyDoor door = new ThirdPartyDoor();

    @Override
    public void open(String code) throws IncorrectDoorCodeException {
        if(!isOpen()){
            try {
                door.unlock(code);
                if(door.getLockStatus().equals(ThirdPartyDoor.LockStatus.UNLOCKED)) { //Duplicate
                    door.setState(OPEN);}
            } catch (CannotUnlockDoorException e) {
                throw new IncorrectDoorCodeException();
            } catch (CannotChangeStateOfLockedDoor e) {
                e.printStackTrace();
            }
        }    }

    @Override
    public void close() {
        if (isOpen()) {
            try {
                door.setState(CLOSED);
            } catch (CannotChangeStateOfLockedDoor e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean isOpen() {
        if(door.getState().equals(OPEN)){
            return true;
        }
        return false;
    }

    @Override
    public void changeCode(String oldCode, String newCode, String newCodeConfirmation) throws IncorrectDoorCodeException, CodeMismatchException {
        if (newCode.equals(newCodeConfirmation)) {
            try {
                open(oldCode);
                door.setNewLockCode(newCode);
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
            door.unlock(code);
            door.lock();
            return true;
        } catch (CannotUnlockDoorException e){
            return false;
        }
    }
}
