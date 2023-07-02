package com.curso.ecommerce.controller;
import java.util.List;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.entity.Orden;
import com.curso.ecommerce.entity.Producto;
import com.curso.ecommerce.entity.Usuario;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.OrdenService;
import com.curso.ecommerce.service.ProductoService;



/**
 * The AdministradorController class in Java retrieves and displays information about products, users,
 * and orders for an administrator in a web application.
 */
@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	
	private Logger log= (Logger) LoggerFactory.getLogger(AdministradorController.class);

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private OrdenService ordenService;
	
	/**
	 * This Java function retrieves a list of products and adds them to a model before returning a view
	 * for an administrator's home page.
	 * 
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows the controller to add attributes to the model, which can then be
	 * accessed by the view to render the data. In this case, the "productos" attribute is added to the
	 * model,
	 * @return The method is returning a string "administrador/home".
	 */
	@GetMapping("")
	public String home(Model model) {
		
		List<Producto> productos= productoService.findAll();
		model.addAttribute("productos",productos);
		return "administrador/home";
	}
	
	/**
	 * This Java function returns a view that displays all users in the system for an administrator.
	 * 
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows the controller to add attributes to the model, which can then be
	 * accessed by the view to render the data. In this case, the "usuarios" attribute is added to the
	 * model with
	 * @return The method is returning a String with the value "administrador/usuarios".
	 */
	@GetMapping("/usuarios")
	public String mostrarUsuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}

	/**
	 * This Java function returns a view called "administrador/ordenes" with a model attribute "ordenes"
	 * that contains all orders retrieved from the ordenService.
	 * 
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows the controller to add attributes to the model, which can then be
	 * accessed by the view to render the data. In this case, the "ordenes" attribute is added to the
	 * model
	 * @return The method is returning a string "administrador/ordenes".
	 */
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordenService.findAll());
		return "administrador/ordenes";
	}
	
	/**
	 * This Java function retrieves the details of an order with a specific ID and adds them to a model
	 * for display in a web page.
	 * 
	 * @param id The id parameter is an integer value that is passed as a path variable in the URL. It is
	 * used to identify a specific order for which the details need to be displayed.
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can then be accessed in
	 * the view to render dynamic content. In this case, the "detalles" attribute is added to the model,
	 * which contains
	 * @return The method is returning a String which is the name of the view template
	 * "administrador/detalleorden".
	 */
	@GetMapping("/detalle/{id}")
	public String detalle(@PathVariable Integer id,Model model) {
		log.info("Id de la orden {}",id);
		Orden orden= ordenService.findById(id).get();
		model.addAttribute("detalles", orden.getDetalle());
		return "administrador/detalleorden";
	}
	
}
