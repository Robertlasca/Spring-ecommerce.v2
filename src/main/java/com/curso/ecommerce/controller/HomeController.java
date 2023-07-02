package com.curso.ecommerce.controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.entity.Producto;
import com.curso.ecommerce.entity.Usuario;
import com.curso.ecommerce.entity.Orden;
import com.curso.ecommerce.entity.DetalleOrden;
import com.curso.ecommerce.service.DetalleOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.OrdenService;
import com.curso.ecommerce.service.ProductoService;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;

/**
 * This is a Java class that serves as a controller for a web application's home page, product page,
 * shopping cart, and order summary page.
 */
@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger LOGGER= (Logger) LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private OrdenService ordenService;
	
	@Autowired
	private DetalleOrdenService detalleOrdenService;

	//Lista para almacenar los detalles de la orden
	List<DetalleOrden> detalles= new ArrayList<DetalleOrden>();
	
	//Datos de la orden
	Orden orden= new Orden();
	
	/**
	 * This Java function retrieves a list of products and checks if a user session exists, adding the
	 * session ID to the model if it does.
	 * 
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can be accessed in the
	 * view to render dynamic content. In this code, the model is used to add the list of products and the
	 * session attribute "
	 * @param session The "session" parameter is an object of the HttpSession class, which represents a
	 * user session in a web application. It allows the storage and retrieval of attributes associated
	 * with a particular user's session. In this code snippet, the session object is used to check if a
	 * user is logged in or not,
	 * @return The method is returning a String that represents the name of the view that will be
	 * displayed to the user. The name of the view depends on the value returned by the if-else statement.
	 * If the session attribute "idusuario" is not null, the method will return "usuario/home". Otherwise,
	 * it will return "usuario/home" after setting the session attribute "idusuario" to null.
	 */
	@GetMapping("user")
	public String home(Model model,HttpSession session) {
		
		List <Producto> productos= productoService.findAll();
		model.addAttribute("productos", productos);
		LOGGER.info("Si entre al metodo");
		//Session
		if(session.getAttribute("idusuario")!= null) {
			int id=Integer.parseInt(session.getAttribute("idusuario").toString());
			if(id > 1) {
				LOGGER.info("Si entre al metodo if");
				System.out.println("Si es mayor");
				model.addAttribute("sesion", session.getAttribute("idusuario"));
				return "usuario/home";
				
			}else {
				session.setAttribute("idusuario", null);
			    model.addAttribute("sesion", session.getAttribute("idusuario"));
				return "usuario/home";
			}
			
			
		}else {
			LOGGER.info("Si entre al metodo else");
			System.out.println("No es mayor");
		    session.setAttribute("idusuario", null);
		    model.addAttribute("sesion", session.getAttribute("idusuario"));
			return "usuario/home";
		}
		
		
	}
	
	/**
	 * This Java function retrieves a product by its ID and adds it to the model before returning a view.
	 * 
	 * @param id The id parameter is an integer value that is passed as a path variable in the URL. It is
	 * used to retrieve a specific product from the database and display its details on the product home
	 * page.
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can then be accessed in
	 * the view to render dynamic content. In this code snippet, the model is used to add the "producto"
	 * attribute, which
	 * @return The method is returning a String which is the name of the view that will be displayed to
	 * the user. In this case, it is "usuario/productohome".
	 */
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		Producto producto= new Producto();
		Optional<Producto> productoOptional=productoService.get(id);
		producto=productoOptional.get();
		LOGGER.info("Id producto enviado como parámetro {}",id);
		
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}

	/**
	 * This function adds a product to the cart and calculates the total price of all products in the
	 * cart.
	 * 
	 * @param id The ID of the product being added to the cart.
	 * @param cantidad The quantity of the product being added to the cart.
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can then be accessed in
	 * the view to render dynamic content. In this case, the model is used to pass the cart and order
	 * details to the view
	 * @return The method is returning a String, which is the name of the view that will be displayed to
	 * the user. In this case, it is "usuario/carrito".
	 */
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id,@RequestParam Integer cantidad,Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		
		double sumaTotal=0;
		
		Optional<Producto> optionalProducto= productoService.get(id);
		LOGGER.info("Producto añadido: {}",optionalProducto.get());
		LOGGER.info("Cantidad: {}",cantidad);
		producto= optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(cantidad*producto.getPrecio());
		detalleOrden.setProducto(producto);
		
		//Validad que el producto no se añada 2 veces
		Integer idProducto= producto.getId();
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if(!ingresado) {
			detalles.add(detalleOrden);
		}
		
		
		
		
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		
		return "usuario/carrito";
	}

	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id,Model model) {
			
		//Lista nueva de productos
		List<DetalleOrden> ordenesNueva= new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden : detalles) {
			if(detalleOrden.getProducto().getId()!=id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		
		//Actualizar la lista de productos
		detalles=ordenesNueva;
		
		double sumaTotal=0;
		
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		
		return "usuario/carrito";
	}

	/**
	 * This Java function retrieves the user's cart details and order information and adds them to the
	 * model for display in a web page.
	 * 
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can be accessed in the
	 * view to render dynamic content. In this case, the model is used to pass the session attribute
	 * "idusuario", the "
	 * @param session The session parameter is an object of the HttpSession class which represents a user
	 * session in a web application. It can be used to store and retrieve data that is specific to a
	 * particular user across multiple requests. In this code snippet, the session parameter is used to
	 * retrieve the idusuario attribute from the session and
	 * @return The method is returning a string that represents the path to the "carrito" view template
	 * for the "usuario" module.
	 */
	@GetMapping("/getCart")
	public String getCart(Model model,HttpSession session) {
		
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		
		
		return "/usuario/carrito";
	}

	/**
	 * This Java function retrieves order details and user information to display a summary of the order.
	 * 
	 * @param model The model parameter is an object that is used to pass data from the controller to the
	 * view. It allows the controller to add attributes to the model, which can then be accessed by the
	 * view to render the data.
	 * @param session The session parameter is an object of the HttpSession class which represents a user
	 * session in a web application. It allows the server to store and retrieve information about the user
	 * across multiple requests. In this code snippet, the session parameter is used to retrieve the
	 * idusuario attribute from the session and convert it to an
	 * @return The method is returning a string "usuario/resumenorden".
	 */
	@GetMapping("/order")
	public String order(Model model,HttpSession session) {
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()) ).get();
		
		model.addAttribute("cart",detalles);
		model.addAttribute("orden",orden);
		model.addAttribute("usuario", usuario);
		return "usuario/resumenorden";
	}

	/**
	 * This function saves an order with its details and assigns it to a user, then clears the order and
	 * details list.
	 * 
	 * @param session The "session" parameter is an object of the HttpSession class, which represents a
	 * user's session and allows the storage of data that can be accessed across multiple requests. In
	 * this method, the session is used to retrieve the ID of the currently logged-in user, which is then
	 * used to set the "
	 * @return The method is returning a string "redirect:user".
	 */
	@GetMapping("/saveOrder")
	public String saveORder(HttpSession session) {
		Date fechaActual = new Date();
		
		orden.setFechaCreacion(fechaActual);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//Usuario relacionado a esa orden
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()) ).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//Guardar detalles
		for(DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		
		}
		
		//Limpiar lista y orden
		orden = new Orden();
		detalles.clear();
		
		
		return "redirect:user";
	}

	/**
	 * This function searches for products by name, removes accents and filters the results using regular
	 * expressions.
	 * 
	 * @param nombre The parameter "nombre" is a String that represents the name of the product that the
	 * user is searching for. It is obtained through a request parameter annotation @RequestParam.
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can then be accessed in
	 * the view to render dynamic content. In this case, the "productos" attribute is added to the model,
	 * which contains a
	 * @return The method is returning a String with the value "usuario/home".
	 */
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre,Model model) {
		LOGGER.info("nombre del producto: {}",nombre);
		String nombreSinAcentos = removeAccents(nombre.toLowerCase());
		Pattern pattern = Pattern.compile(Pattern.quote(nombreSinAcentos), Pattern.CASE_INSENSITIVE);

		List<Producto> productos = productoService.findAll()
		    .stream()
		    .filter(p -> pattern.matcher(removeAccents(p.getNombre().toLowerCase())).find())
		    .collect(Collectors.toList());
		model.addAttribute("productos", productos);
		
		return "usuario/home";
	}
	
	
	/**
	 * The function removes accents from a given string by replacing them with their corresponding
	 * non-accented characters.
	 * 
	 * @param input The input string that needs to have its accents removed.
	 * @return The method `removeAccents` returns a `String` with all accented characters replaced by
	 * their non-accented equivalents.
	 */
	private String removeAccents(String input) {
	    return input.replaceAll("[áàäâã]", "a")
	                .replaceAll("[éèëê]", "e")
	                .replaceAll("[íìïî]", "i")
	                .replaceAll("[óòöôõ]", "o")
	                .replaceAll("[úùüû]", "u")
	                .replaceAll("[ñ]", "n");
	}
}

