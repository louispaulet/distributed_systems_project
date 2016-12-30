package exercise1;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.ServerCloneException;
import java.util.*;

/**
 * Created by Quentin on 12/12/2016.
 */


public class GlobalRegistry implements java.rmi.registry.Registry {
    //  CONSTANT
    //  GLOBAL REGISTRY PORT
    public static final int GLOBAL_REGISTRY_PORT = 1098;

    private Map<String, Map<String, Service>> remotes = new HashMap<>();
    private Map<String, Integer> roundIndex = new HashMap<>();

    @Override
    public Remote lookup(String name) throws RemoteException, NotBoundException, AccessException {
        int index = roundIndex.get(name) + 1;
        List<String> tags = new ArrayList(remotes.get(name).keySet());

        Service service = remotes.get(name).get(tags.get(0).toString());

        if(service.getReplication() == "Passive"){
            return service.getRemote();
        }else {
            if(index >= tags.size()) index = 0;

            roundIndex.put(name, index);

            System.out.println(index);
            System.out.println(tags.get(index).toString());

            return remotes.get(name).get(tags.get(index).toString()).getRemote();
        }
    }

    @Override
    public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
        List<String> keyTag = Arrays.asList(name.split("\\s*:\\s*"));
        Map<String, Service> tmp = remotes.get(keyTag.get(0));
        Service service;
        Boolean master = false;

        if(tmp == null) {
            tmp = new HashMap<>();
            master = true;
        }

        if(keyTag.size() == 3){
            if(keyTag.get(2) == "Passive"){
                for(int i =0; i < remotes.get(keyTag.get(0)).size();i++){
                    if(((Service)remotes.get(keyTag.get(0)).values().toArray()[i]).getMaster()){
                        ((Service)remotes.get(keyTag.get(0)).values().toArray()[i]).addCopy(obj);
                    }
                }
            }
            service = new Service(keyTag.get(2), master, obj);
            if (master) service.setPassiveService();
            tmp.put(keyTag.get(1), service);
            remotes.put(keyTag.get(0), tmp);
            roundIndex.put(keyTag.get(0), 0);
        }else {

            service = new Service(obj);
            tmp.put(keyTag.get(1), service);
            remotes.put(keyTag.get(0), tmp);
            roundIndex.put(keyTag.get(0), 0);
        }
    }

    @Override
    public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
        List<String> keyTag = Arrays.asList(name.split("\\s*:\\s*"));
        Map<String, Service> tmp = remotes.get(keyTag.get(0));
        if(tmp != null && !tmp.isEmpty()) {
            System.out.println(tmp.get(keyTag.get(1)).getClass().toString());
            if(tmp.get(keyTag.get(1)).getMaster()) {
                Service service = (Service) tmp.values().toArray()[0];
                service.setMaster(true);
                service.setCopies(tmp.get(keyTag.get(1)).getCopies());
                tmp.remove(keyTag.get(1));
                tmp.values().toArray()[0] = service;
                remotes.put(keyTag.get(0), tmp);
            }
            tmp.remove(keyTag.get(1));
            remotes.put(keyTag.get(0), tmp);
        }

    }

    @Override
    public void rebind(String name, Remote obj) throws RemoteException, AccessException {

        try {
            this.unbind(name);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        try {
            this.bind(name, obj);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] list() throws RemoteException, AccessException {
        return new String[0];
    }

    public static synchronized void main(String[] args) throws Exception {

        System.out
                .println("registry: running on host " + InetAddress.getLocalHost());

        // create the registry on the local machine, on the default port number
        LocateGlobalRegistry.createRegistry(GLOBAL_REGISTRY_PORT);
        System.out.println("global registry: listening on port " + GLOBAL_REGISTRY_PORT);

        // block forever
        GlobalRegistry.class.wait();
        System.out.println(" global registry: exiting (should not happen)");

    }
}
