package imbachain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;

import imbachain.infrastructure.BaseNode;
import imbachain.infrastructure.Node;
import imbachain.infrastructure.NodeInterface;

@SpringBootApplication
@EnableScheduling
@EnableIntegration
public class Application {

	private static int port = BaseNode.DEFAULT_PORT;
	private static String name=null;
	
	public static void main(String[] args) {
		HashMap<String, Object> props = new HashMap<>();
		try {
			for (int i = 0; i < args.length; i++)
				if (args[i].toLowerCase().equals("--port")) {
					port = Integer.parseInt(args[i + 1]);
				} else if (args[i].toLowerCase().equals("--name")) {
					name = args[i + 1];
				}
		} catch (Exception e) {
			System.out.println("There was a problem with the input parameters!");
		}
		System.out.println("Server starting with port:" + port);
		props.put("server.port", port);
		new SpringApplicationBuilder().sources(Application.class).properties(props).run(args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
		};
	}

	@Bean
	public NodeInterface getNode() {
		String address = "127.0.0.1";
		try {
			URL url = new URL("http://checkip.amazonaws.com/");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			address = br.readLine();
			System.out.println("Node created with IP address:" + address);
		} catch (Exception e) {
			System.out.println("Couldnt get your external IP address");
		}
		return new Node(address,name, port);
	}
	
	 @Bean
	 public UnicastReceivingChannelAdapter inbound() {
	        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(5555);
	        adapter.setOutputChannelName("udp-channel");
	        return adapter;
	  }




}