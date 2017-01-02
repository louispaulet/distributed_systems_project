package exercise1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public class Service {

	//contient la méthode de réplication
    private String replication;

    //dit si le service est master ou secondaire (backup)
    private Boolean master = false;

    //passage du service ?
    private Remote remote;
    
    //liste des copies distantes
    private List<Remote> copies;

    //getters & setters
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
    
    //constructeur
    public Service(String _replication, Boolean _master, Remote _remote) {
        replication = _replication;
        master = _master;
        remote = _remote;
    }
    //constructeur 2
    public Service(Remote _remote){
        replication = "None";
        remote = _remote;
    }
    //setter : implementation de l'interface
    public void setPassiveService() throws RemoteException {
        ((IPassive)remote).setService(this);
    }

}
