package exercise1;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IIterator extends IPassive {

    public void setValue(int _value) throws RemoteException;

    public void plus() throws RemoteException;

}
