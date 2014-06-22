package networking;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 */
public interface IRCSListener {
    public void onConnect();
    public void onDisconnect();
    public void onError(Exception e);
    public void dataReceived(String s);    
}