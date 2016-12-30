package exercise1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Quentin on 27/12/2016.
 */
public class Service {

    private String replication;

    private Boolean master = false;

    private Remote remote;

    private List<Remote> copies;


    public Boolean getMaster() {
        return master;
    }

    public String getReplication() {
        return replication;
    }

    public Remote getRemote() {
        return remote;
    }

    public void setCopies(List<Remote> _copies){
        copies = _copies;
    }

    public void addCopy(Remote copy){
        copies.add(copy);
    }

    public List<Remote> getCopies(){
        return copies;
    }

    public void setMaster(Boolean _master) {
        master = _master;
    }

    public Service(String _replication, Boolean _master, Remote _remote) {
        replication = _replication;
        master = _master;
        remote = _remote;
    }

    public Service(Remote _remote){
        replication = "None";
        remote = _remote;
    }

    public void setPassiveService() throws RemoteException {
        ((IPassive)remote).setService(this);
    }

}
