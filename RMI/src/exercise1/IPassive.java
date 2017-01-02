package exercise1;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IPassive extends Remote{

    Service service = null;

    public void setService(Service service) throws RemoteException;

}
