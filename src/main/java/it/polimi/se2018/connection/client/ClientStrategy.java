package it.polimi.se2018.connection.client;

import it.polimi.se2018.model.PlayerMessage;

public interface ClientStrategy {

    void sendToServer(PlayerMessage playerMessage);
    void receive(PlayerMessage playerMessage);
    void close();
}
