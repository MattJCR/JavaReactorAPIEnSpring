package com.springboot.reactor.app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.springboot.reactor.app.models.Comentarios;
import com.springboot.reactor.app.models.UsuarioComentarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{
	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//testFluxUsuariosFromString();
		//testFluxUsuariosFromList();
		//testFluxProgramTimeToExecute();
		//testFlatMapUsuariosFromList();
		//testFlatMapUsuariosFromListToString();
		//testFlatMapUsuariosFromListToCollectList();
		//testFlatMapUsuarioComentarios();
		//testZipWithBiFunctionUsuarioComentarios();
		//testZipWithMonoAndTupleUsuarioComentarios();
		//testZipWithAndRange();
		//testFluxAndInterval();
		//testFluxAndDelay();
		testFluxAndInfinityIntervalAndOperationRetryNTimes();
	}
	private void testFluxUsuariosFromString(){
		log.warn("testFluxUsuariosFromString");
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
	}
	private void testFluxUsuariosFromList(){
		log.warn("testFluxUsuariosFromList");
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
	}
	private void testFluxProgramTimeToExecute(){
		log.warn("testFluxProgramTimeToExecute");
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
							Thread.sleep(1000);
						} catch (InterruptedException e) {

						}
					}
					if(p == "p4") {
						try {
							Thread.sleep(1000);
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
	private void testFlatMapUsuariosFromList(){
		log.warn("testFlatMapUsuariosFromList");
		//Flux de Lista de String
		List<String> nombresList = new ArrayList<String>();
		nombresList.add("Mateo Cabello");
		nombresList.add("Pedro Garcia");
		nombresList.add("Carlos Sanchez");
		nombresList.add("Juan Mengano");
		nombresList.add("Maria Guzman");
		nombresList.add("Bruce Lee");
		nombresList.add("Bruce Willis");
		//Flux obtiene un stream reactivo de varios objectos
		Flux.fromIterable(nombresList)
				//Con map devolvemos un stream normal de objetos
				.map(nombre -> new Usuario(nombre.split(" ")[0],nombre.split(" ")[1]))
				//Con flatMat devolvemos un stream reactivo de objetos
				.flatMap(usuario -> {
					//Mono devuelve un objecto reactivo individual para concadenarlo
					//al stream actual de Flux. Es como un map normal pero con objetos reactivos.
					if(usuario.getNombre().equalsIgnoreCase("bruce")){
						return Mono.just(usuario);
					}
					/*
					  Mono.empty devuelve una instancia reactiva vacia por lo que no se añade al flujo.
					  No es como devolver un null dentro de un map ya que si está vacio no se concatena
					  al flujo actual
					*/
					return Mono.empty();
				})
				.map(usuario -> {
					usuario.setNombre(usuario.getNombre().toUpperCase());
					usuario.setApellidos(usuario.getApellidos().toUpperCase());
					return usuario;
				}).subscribe(u -> log.info(u.toString()));
	}
	private void testFlatMapUsuariosFromListToString(){
		log.warn("testFlatMapUsuariosFromListToString");
		//Ejemplo flatMat a la inversa de testFlatMapUsuariosFromList
		//Convertimos objetos Usuario a una cadena
		List<Usuario> usuariosList = new ArrayList<Usuario>();
		usuariosList.add(new Usuario("Mateo","Cabello"));
		usuariosList.add(new Usuario("Pedro","Garcia"));
		usuariosList.add(new Usuario("Carlos","Sanchez"));
		usuariosList.add(new Usuario("Juan","Mengano"));
		usuariosList.add(new Usuario("Maria","Guzman"));
		usuariosList.add(new Usuario("Bruce","Lee"));
		usuariosList.add(new Usuario("Bruce","Willis"));

		Flux.fromIterable(usuariosList)
				.map(usuario -> usuario.getNombre()
						   			   .concat(" ")
									   .concat(usuario.getApellidos())
				).flatMap(usuarioCadena -> {
					if(usuarioCadena.toLowerCase().contains("bruce")){
						return Mono.just(usuarioCadena);
					}
					return Mono.empty();
				}).map(String::toUpperCase).subscribe(u -> log.info(u.toString()));
	}
	private void testFlatMapUsuariosFromListToCollectList(){
		log.warn("testFlatMapUsuariosFromListToCollectList");
		//Ejemplo para convertir un Flux List de Usuario en un List de Usuario
		List<Usuario> usuariosList = new ArrayList<Usuario>();
		usuariosList.add(new Usuario("Mateo","Cabello"));
		usuariosList.add(new Usuario("Pedro","Garcia"));
		usuariosList.add(new Usuario("Carlos","Sanchez"));
		usuariosList.add(new Usuario("Juan","Mengano"));
		usuariosList.add(new Usuario("Maria","Guzman"));
		usuariosList.add(new Usuario("Bruce","Lee"));
		usuariosList.add(new Usuario("Bruce","Willis"));

		Flux.fromIterable(usuariosList)
				.collectList()
				.subscribe(lista -> {
					log.info(lista.toString());
					lista.forEach(u -> log.info(u.toString()));
				});
	}
	private Usuario crearUsuario(){
		//Metodo de ejemplo para testFlatMapUsuarioComentarios
		//Logica de creacion de usuario, consumo de Api, etc...
		return new Usuario("Mateo","Cabello");

	}
	private Comentarios crearComentarios(){
		//Metodo de ejemplo para testFlatMapUsuarioComentarios
		//Logica de creacion de comentarios para un usuario, consumo de Api, etc...
		Comentarios comentarios = new Comentarios();
		comentarios.addComentario("El usuario se agrega a la plataforma.");
		comentarios.addComentario("El usuario ya tiene un puesto asignado.");
		comentarios.addComentario("Este usuario es muy trabajador.");
		return comentarios;
	}
	private void testFlatMapUsuarioComentarios(){
		log.warn("testFlatMapUsuarioComentarios");
		//Ejemplo de combinar 2 flatMap en un solo objeto
		Mono<Usuario> monoUsuario = Mono.fromCallable(
				() -> crearUsuario()
		);
		Mono<Comentarios> monoComentarios = Mono.fromCallable(
				() -> crearComentarios()
		);
		//Se modifica el stream para convertir Usuario y Comentarios
		//oen un nuevo bjecto UsuarioComentarios
		monoUsuario.flatMap(
				usuario -> monoComentarios.map(
						comentarios -> new UsuarioComentarios(usuario,comentarios)
				)).subscribe(usuarioComentario -> log.info(usuarioComentario.toString()));
	}
	//Los ejemplos testZipWithBiFunctionUsuarioComentarios y testZipWithMonoAndTupleUsuarioComentarios
	//muestran como hacer una misma funcionalidad de 2 formas diferentes.
	private void testZipWithBiFunctionUsuarioComentarios(){
		log.warn("testZipWithBiFunctionUsuarioComentarios");
		//Ejemplo de combinar 2 flatMap en un solo objeto
		Mono<Usuario> monoUsuario = Mono.fromCallable(
				() -> crearUsuario()
		);
		Mono<Comentarios> monoComentarios = Mono.fromCallable(
				() -> crearComentarios()
		);
		//Se modifica el stream para convertir Usuario y Comentarios
		//en un nuevo objecto UsuarioComentarios utilizando una BiFunction
		Mono<UsuarioComentarios> uc = monoUsuario.zipWith(monoComentarios, (usuario,comentarios) ->
						new UsuarioComentarios(usuario,comentarios)
				);
		uc.subscribe(usuarioComentarios -> log.info(usuarioComentarios.toString()));
	}
	private void testZipWithMonoAndTupleUsuarioComentarios(){
		log.warn("testZipWithMonoAndTupleUsuarioComentarios");
		//Ejemplo de combinar 2 flatMap en un solo objeto
		Mono<Usuario> monoUsuario = Mono.fromCallable(
				() -> crearUsuario()
		);
		Mono<Comentarios> monoComentarios = Mono.fromCallable(
				() -> crearComentarios()
		);
		//Se modifica el stream para convertir Usuario y Comentarios
		//en un nuevo objecto UsuarioComentarios usando una tupla.
		Mono<UsuarioComentarios> uc = monoUsuario.zipWith(monoComentarios)
				//Con map extraemos de la tupla los objectos con getT(X)
				.map(tupla ->{
					Usuario usuario = tupla.getT1();
					Comentarios comentarios = tupla.getT2();
					return new UsuarioComentarios(usuario,comentarios);
				});
		uc.subscribe(usuarioComentarios -> log.info(usuarioComentarios.toString()));
	}
	private void testZipWithAndRange(){
		log.warn("testZipWithAndRange");
		//Flux inicial (fluxJust)
		Flux.just(1,2,3,4)
				.map(i -> (i*2))
				//Flux secundario (fluxRange)
				//Flux range obtiene 4 valores de 0 a 3 (lo usamos como indice en este ejemplo)
				.zipWith(Flux.range(0,4), (fluxJust,fluxRange) ->
						String.format("fluxRange: %d - fluxJust %d",fluxRange,fluxJust)
				).subscribe(result -> log.info(result));

	}
	private void testFluxAndInterval(){
		log.warn("testFluxAndInterval");
		//Ejecuta en paralelo durante un tiempo un conjuno de tareas (rango en este ejemplo)
		Flux<Integer> rangoValores = Flux.range(1,12);
		Flux<Long> tiempoEjecucion = Flux.interval(Duration.ofSeconds(1));
		rangoValores.zipWith(tiempoEjecucion,(rango,intervalo) -> rango)
				.doOnNext(rango -> log.info(rango.toString()))
				//BlockLast fuerza a esperar la ejecucion en segundo plano
				//En este ejemplo se está utilizando para analizar la ejecución
				//Sin esta opcion se imprimirian todos los valores del rango en un
				//hilo separado y no se mostraria el ejemplo de ejecución
				.blockLast();
	}
	private void testFluxAndDelay(){
		log.warn("testFluxAndDelay");
		//Ejecuta en paralelo durante un tiempo un conjuno de tareas (rango en este ejemplo)
		Flux<Integer> rangoValores = Flux.range(1,12)
										 .delayElements(Duration.ofSeconds(1))
				.doOnNext(rango -> log.info(rango.toString()));
		rangoValores.blockLast();

	}
	private void testFluxAndInfinityIntervalAndOperationRetryNTimes() throws InterruptedException {
		log.warn("testFluxAndInfinityInterval");
		//CountDownLatch es un contador que pone el hilo en espera
		//hasta que este llega a 0. En este caso se inicializa a 1
		CountDownLatch latch = new CountDownLatch(1);
		//El Flux está en loop infinito hasta terminar la tarea
		Flux.interval(Duration.ofSeconds(1))
				//Al terminar el Flux decrementamos el contador con doOnTerminate
				.doOnTerminate(() -> latch.countDown())

				//Con flatMap controlamos el bucle infinito para tener un limite de tiempo
				.flatMap(i -> {
					//Control de reintentos para la operacion.
					if(i >= 5){
						//Retornamos una excepcion para invocar el doOnTerminate
						return Flux.error(new InterruptedException("El tiempo de espera permitido se ha superado."));
					}
					//Continua intentando la operacion
					return Flux.just(i);
				})
				.map(i -> "Intervalo/Reintento -> ".concat(i.toString()))
				//Con retry podemos forzar a reintentar la operacion n veces
				//Por si esta falla y es necesario volver a lanzarla
				.retry(2)
				.subscribe(successMsg -> log.info(successMsg),exception -> log.error(exception.getMessage()));
		//Esperamos a que el contador del CountDownLatch llegue a 0
		//y mientras dejamos el hilo bloqueado/en espera.
		latch.await();


	}


	

}
