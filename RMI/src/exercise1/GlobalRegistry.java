package exercise1;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.ServerCloneException;
import java.util.*;


//creation de GlobalRegistry
public class GlobalRegistry implements java.rmi.registry.Registry {
    //  definition du port de ce registre
    public static final int GLOBAL_REGISTRY_PORT = 1098;

    //liste des services et leur nom
    private Map<String, Map<String, Service>> remotes = new HashMap<>();
    //liste de noms de services et leur priorité
    private Map<String, Integer> roundIndex = new HashMap<>();

    @Override
    //Methode 1  : passive : retour du service
    //Methode 2 : ? : retour d'un service répliqué en RoundRobin (indice circulaire)
    public Remote lookup(String name) throws RemoteException, NotBoundException, AccessException {
        // incrémentation de l'index RoundRobin
    	int index = roundIndex.get(name) + 1;
    	// liste des noms des repliques ?
        List<String> tags = new ArrayList(remotes.get(name).keySet());
        //service dont le nom est le premier de la liste
        Service service = remotes.get(name).get(tags.get(0).toString());
        
        //detection du mode de replication
        if(service.getReplication() == "Passive"){
        	//retourne le service en tête de liste
            return service.getRemote();
        }else {
        	//gestion de l'index circulaire
            if(index >= tags.size()) index = 0;
            
            //ajout de ?
            roundIndex.put(name, index);
            
            System.out.println(index);
            System.out.println(tags.get(index).toString());
            //retourne le service répliqué suivant
            return remotes.get(name).get(tags.get(index).toString()).getRemote();
        }
    }

    @Override
    //ajouter un service au registre global
    public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
        //liste des noms de service qui ont un nom similaire ?
    	List<String> keyTag = Arrays.asList(name.split("\\s*:\\s*"));
        //map des répliques d'un service en début de liste precedente
    	Map<String, Service> tmp = remotes.get(keyTag.get(0));
        Service service;
        Boolean master = false;
        
        //s'il n'y a pas de services répliqués ?
        if(tmp == null) {
        	//créer un nouvelle map
            tmp = new HashMap<>();
            //passer le service en mode principal 
            master = true;
        }
        
        //
        if(keyTag.size() == 3){
        	//si le mode de réplication est passif :
            if(keyTag.get(2) == "Passive"){
            	//pour toutes les répliques :
                for(int i =0; i < remotes.get(keyTag.get(0)).size();i++){
                	//chercher la réplique primaire
                    if(((Service)remotes.get(keyTag.get(0)).values().toArray()[i]).getMaster()){
                    	//ajout de la copie sur master ?
                        ((Service)remotes.get(keyTag.get(0)).values().toArray()[i]).addCopy(obj);
                    }
                }
            }
            service = new Service(keyTag.get(2), master, obj);
            if (master) service.setPassiveService();
            tmp.put(keyTag.get(1), service);
            //MAJ des attributs
            remotes.put(keyTag.get(0), tmp);
            roundIndex.put(keyTag.get(0), 0);
        }else {
    		//création d'un service
            service = new Service(obj);
            //ajout dans 
            tmp.put(keyTag.get(1), service);
            //MAJ des attributs
            remotes.put(keyTag.get(0), tmp);
            roundIndex.put(keyTag.get(0), 0);
        }
    }

    @Override
    public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
        List<String> keyTag = Arrays.asList(name.split("\\s*:\\s*"));
        //recherche des services associés
        Map<String, Service> tmp = remotes.get(keyTag.get(0));
        //s'il y en a :
        if(tmp != null && !tmp.isEmpty()) {
            System.out.println(tmp.get(keyTag.get(1)).getClass().toString());
            //si le service trouvé est primaire
            if(tmp.get(keyTag.get(1)).getMaster()) {
                Service service = (Service) tmp.values().toArray()[0];
                service.setMaster(true);
                service.setCopies(tmp.get(keyTag.get(1)).getCopies());
                tmp.remove(keyTag.get(1));
                tmp.values().toArray()[0] = service;
                //mise à jour de remotes
                remotes.put(keyTag.get(0), tmp);
            }
            //sinon : simple suppression de la replique secondaire
            tmp.remove(keyTag.get(1));
            //mise à jour de remotes
            remotes.put(keyTag.get(0), tmp);
        }

    }

    @Override
    //execution de unbind puis bind
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

        System.out.println("registry: running on host " + InetAddress.getLocalHost());

        // create the registry on the local machine, on the default port number
        LocateGlobalRegistry.createRegistry(GLOBAL_REGISTRY_PORT);
        System.out.println("global registry: listening on port " + GLOBAL_REGISTRY_PORT);

        // block forever
        GlobalRegistry.class.wait();
        System.out.println(" global registry: exiting (should not happen)");

    }
}
