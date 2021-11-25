package com.springboot.reactor.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{
	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.warn("Flux desde just String");		
		//Flux de String
		Flux<String> nombresFlux = Flux.just("Mateo Cabello","Pedro Garcia","Carlos Sanchez", "Juan Mengano", "Maria Guzman", "Bruce Lee", "Bruce Willis");
		//Suscribe de String
		nombresFlux.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage())
				,new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del Obserbable nombres con Exito!");		
					}
				}
		);
		log.warn("Flux desde nombresFlux a usuarios");		
		//Flux de Usuarios
		Flux<Usuario> usuariosFlux = nombresFlux.map(nombre -> new Usuario(nombre.split(" ")[0],nombre.split(" ")[1]))
				.filter(usuario -> usuario.getNombre().toLowerCase().equals("bruce"))
				.doOnNext(u -> {
					if(u == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					log.info(u.toString());	
				}).map(usuario -> {
						usuario.setNombre(usuario.getNombre().toUpperCase());
						usuario.setApellidos(usuario.getApellidos().toUpperCase());
						return usuario;
					});
		
		//Suscribe de Usuarios
		usuariosFlux.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage())
				,new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del Obserbable usuarios con Exito!");		
					}
				}
		);
		log.warn("Flux desde fromIterable nombresListFlux a usuarios");		
		//Flux de Lista de String
		List<String> nombresList = new ArrayList<String>();
		nombresList.add("Mateo Cabello");
		nombresList.add("Pedro Garcia");
		nombresList.add("Carlos Sanchez");
		nombresList.add("Juan Mengano");
		nombresList.add("Maria Guzman");
		nombresList.add("Bruce Lee");
		nombresList.add("Bruce Willis");
		Flux<String> nombresListFlux = Flux.fromIterable(nombresList);
		
		//Flux de Usuarios desde lista
		Flux<Usuario> usuariosFromListFlux = nombresListFlux.map(nombre -> new Usuario(nombre.split(" ")[0],nombre.split(" ")[1]))
				.filter(usuario -> usuario.getNombre().toLowerCase().equals("bruce"))
				.doOnNext(u -> {
					if(u == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					log.info(u.toString());	
				}).map(usuario -> {
						usuario.setNombre(usuario.getNombre().toUpperCase());
						usuario.setApellidos(usuario.getApellidos().toUpperCase());
						return usuario;
					});
		//Suscribe de Usuarios desde lista
		usuariosFromListFlux.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage())
				,new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del Obserbable usuarios desde lista con Exito!");		
					}
				}
		);
		
		//Flux de tiempos de ejecucion desde lista de "programs"
		List<String> program = new ArrayList<String>();
		program.add("p1");
		program.add("p2");
		program.add("p3");
		program.add("p4");
		Flux<String> programListFlux = Flux.fromIterable(program);
		log.info("Inicializa la ejecucion del Obserbable con Exito!: " + LocalDateTime.now().toString());
		Flux<String> programFromListFlux = programListFlux.
				doOnNext(p -> {
					if(p == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					if(p == "p2") {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							
						}
					}
					if(p == "p4") {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							
						}
					}
					log.info(p + " " + LocalDateTime.now().toString());	
				});
		//Subscribe de tiempos de ejecucion desde lista de "programs"
		//Se suman todos los tiempos y se realizan secuancialmente los hilos.
		programFromListFlux.subscribe(u -> log.info(u.toString()), error -> log.error(error.getMessage())
				,new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del Obserbable con Exito!: " + LocalDateTime.now().toString());		
					}
				}
		);
		
	}
	

}
