package com.curso.ecommerce.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.entity.Orden;
import com.curso.ecommerce.entity.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.OrdenService;

import java.util.List;
import java.util.Optional;
import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;

/**
 * This is a Java class that handles user registration, login, and purchase history for an e-commerce
 * website.
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER= (Logger) LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private OrdenService ordenService;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;
	
	/**
	 * This function returns the view for user registration.
	 * 
	 * @return The method is returning a String "usuario/registro".
	 */
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}

	/**
	 * This Java function saves a new user in the system with a default user type and encrypted password.
	 * 
	 * @param usuario The parameter "usuario" is an object of the class "Usuario" which contains the
	 * information of a user being registered. This object is passed as a parameter to the "save" method
	 * of the controller.
	 * @return The method is returning a String "redirect:/user".
	 */
	@PostMapping("/save")
	public String save(Usuario usuario) {
		LOGGER.info(" Usuario registro : {}",usuario);
		usuario.setTipo("USER");
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuarioService.save(usuario);
		
		return "redirect:/user";
	}

	/**
	 * This function returns the view for the login page for a user.
	 * 
	 * @return The method is returning a String "usuario/login".
	 */
	@GetMapping("/login")
	public String login() {
		
		return "usuario/login";
	}

	/**
	 * This function checks if a user is present in the database and redirects them to the appropriate
	 * page based on their user type.
	 * 
	 * @param usuario An object of the class Usuario, which contains information about the user trying to
	 * access the system.
	 * @param session The "session" parameter is an object of the HttpSession class, which represents a
	 * user session in a web application. It allows the application to store and retrieve information
	 * specific to a particular user across multiple requests and responses. In this code, the "session"
	 * object is used to retrieve the "idusuario
	 * @return The method is returning a String that redirects the user to either "/administrador" or
	 * "/user" depending on the type of user. If the user is not present, it redirects to "/user".
	 */
	@GetMapping("/acceder")
	public String acceder(Usuario usuario,HttpSession session) {
		LOGGER.info("Accesos: {}",usuario);
		Optional <Usuario> user= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
		
		//LOGGER.info("Usuario de db: {}",user.get());
		
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			
			if(user.get().getTipo().equals("ADMIN")) {
				System.out.print("Si entre aqui");
				return "redirect:/administrador";
			}else {
				System.out.print("Tambien entre aqui");
				return "redirect:/user";
			}
		}else {
			session.setAttribute("idusuario", null);
			return "redirect:/user";
		}
		
		
	}

	/**
	 * This Java function retrieves a list of orders made by a user and displays them on a webpage.
	 * 
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can be accessed in the
	 * view to render dynamic content. In this case, the model is used to add the "sesion" and "ordenes
	 * @param session The session parameter is an object of the HttpSession class, which represents a user
	 * session in a web application. It is used to store and retrieve data across multiple requests from
	 * the same client. In this code snippet, the session parameter is used to retrieve the idusuario
	 * attribute from the session and pass it to
	 * @return The method is returning a string "usuario/compras".
	 */
	@GetMapping("/compras")
	public String obtenerCompras(Model model,HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		System.out.print("Este es mi id de usuario compras"+usuario.getId());
		List<Orden> ordenes= ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}

	/**
	 * This Java function retrieves the details of a purchase order with a given ID and adds them to a
	 * model for display in a user interface.
	 * 
	 * @param id The id parameter is an integer value that is passed as a path variable in the URL. It is
	 * used to identify a specific order for which the details need to be displayed.
	 * @param session The session parameter is an object of the HttpSession class in Java. It represents a
	 * way to store and retrieve information between multiple requests from the same client. In this
	 * specific code, the session parameter is used to retrieve the "idusuario" attribute that was
	 * previously set in the session, and then add it
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can then be accessed in
	 * the view to render dynamic content. In this case, the model is used to add the "detalles"
	 * attribute, which
	 * @return The method is returning a String that represents the name of the view that will be
	 * displayed to the user, in this case "usuario/detallecompra".
	 */
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id,HttpSession session,Model model) {
		LOGGER.info("Id de la orden: {}",id);
		Optional<Orden> orden=ordenService.findById(id);
		
		model.addAttribute("detalles",orden.get().getDetalle());
		//Session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/detallecompra";
	}
	
	/**
	 * This Java function logs out a user by setting their session attribute "idusuario" to 0 and
	 * redirects them to the "/user" page.
	 * 
	 * @param session The "session" parameter is an object of the HttpSession class, which represents a
	 * user's session and allows the storage of attributes that can be accessed across multiple requests.
	 * In this code snippet, the "cerrarSesion" method uses the session object to set the value of the
	 * "idusuario"
	 * @return The method is returning a redirect to the "/user" endpoint.
	 */
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.setAttribute("idusuario", null);
		
		return "redirect:/user";
	}
}

