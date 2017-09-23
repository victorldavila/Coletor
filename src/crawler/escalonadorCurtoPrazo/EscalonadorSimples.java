package crawler.escalonadorCurtoPrazo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.trigonic.jrobotx.Record;
import com.trigonic.jrobotx.RobotExclusion;

import crawler.Servidor;
import crawler.URLAddress;

public class EscalonadorSimples implements Escalonador{

	private LinkedHashMap<Servidor, List<URLAddress>> fila ;

	private Map<String, Record> mapRobots;
	private Set<String> pagVisitada;
	private static int limDepth = 10;
	private int pageCount = 0;
	
	Record rEduardo;
    Record rLoraine;
    Record rVictor;
    Record rVinicius;
	
	Servidor server;
	RobotExclusion robotExlusion = new RobotExclusion();
	
	public EscalonadorSimples() {
		fila = new LinkedHashMap();
		mapRobots = new HashMap<>();
		pagVisitada = new HashSet<>();

		try {
			rEduardo = robotExlusion.get((new URLAddress("www.casasbahia.com.br/robots.txt", 1)),"daniBoot");
		    rLoraine = robotExlusion.get((new URLAddress("www.casasbahia.com.br/robots.txt", 1)), "daniBoot");
		    rVictor = robotExlusion.get((new URLAddress("www.bloomberg.com/robots.txt", 1)), "daniBoot");
		    rVinicius = robotExlusion.get((new URLAddress("economictimes.indiatimes.com/robots.txt", 1)), "daniBoot");
		} catch(MalformedURLException e) {
			
		}
	}

	@Override
	public synchronized URLAddress getURL() {
		while(!this.finalizouColeta()){
			for(Servidor s : fila.keySet()){

				List<URLAddress> filaDoServidor = fila.get(s);
				if((!fila.isEmpty() )&& (s.isAccessible())){
					if (filaDoServidor.isEmpty())
						continue;

					URLAddress element = filaDoServidor.remove(0);
					s.acessadoAgora();

					return element;
					
				} else
					try {
						this.wait(1000L);
					} catch (InterruptedException e) { }
			}
		}
		return null;
	}

	@Override
	public boolean adicionaNovaPagina(URLAddress urlAdd) {
		if(pagVisitada.contains(urlAdd.getAddress()) || (urlAdd.getDepth() > limDepth)){
			return false;
		} else {
			System.out.println("Nao contem pagina");
			server = new Servidor(urlAdd.getDomain());
			if(fila.containsKey(server)){
				System.out.println("pega server");
				List lista = fila.get(server);
				lista.add(urlAdd);
				pagVisitada.add(urlAdd.getAddress());
			
				return true;
			} else {
				List lista = new ArrayList<>();
				lista.add(urlAdd);
				pagVisitada.add(urlAdd.getAddress());
				fila.put(server, lista);

				return true;
			}
		}
	}


	@Override
	public Record getRecordAllowRobots(URLAddress url) {
		Record record = mapRobots.get(url.getDomain());
		return record;
	}

	@Override
	public void putRecorded(String domain, Record domainRec) {
		if(domainRec != null) {
			mapRobots.put(domain, domainRec);
		}
	
	}
	
	@Override
	public boolean finalizouColeta() {
		// TODO Auto-generated method stub
				
		if( this.pageCount > 1000) {
			return true;
		}
		
		return false;
	}

	@Override
	public void countFetchedPage() {
		// TODO Auto-generated method stub
		
		this.pageCount++;
	}

	
}
