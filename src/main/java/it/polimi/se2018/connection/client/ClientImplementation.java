package it.polimi.se2018.connection.client;


import it.polimi.se2018.model.PlayerMessage;
import it.polimi.se2018.utils.Observable;

import java.rmi.RemoteException;


public class ClientImplementation implements ClientRemoteInterface {

    private Observable<PlayerMessage> obs;

     ClientImplementation(){
        obs = new Observable<>();
    }

     Observable<PlayerMessage> getObs() {
        return obs;
    }


    @Override
    public void receiveFromServer(PlayerMessage playerMessage) throws RemoteException {
        obs.notify(playerMessage);
    }


    @Override
    public void receiveLifeline() throws RemoteException {
        //crea thread di risposta
    }
}

