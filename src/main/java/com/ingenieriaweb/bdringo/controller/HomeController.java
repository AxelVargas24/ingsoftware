package com.ingenieriaweb.bdringo.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ingenieriaweb.bdringo.model.DetalleOrden;
import com.ingenieriaweb.bdringo.model.Orden;
import com.ingenieriaweb.bdringo.model.Producto;
import com.ingenieriaweb.bdringo.model.Usuario;
import com.ingenieriaweb.bdringo.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

import com.ingenieriaweb.bdringo.service.IDetalleOrdenService;
import com.ingenieriaweb.bdringo.service.IOrdenService;
import com.ingenieriaweb.bdringo.service.IProductoService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model,HttpSession session) {
		log.info("session del ususario: {}",session.getAttribute("idusuario"));
		model.addAttribute("productos", productoService.findAll());
		//session
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		return "usuario/home";
	}

	@GetMapping("/productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model,HttpSession session) {
		log.info("Id producto enviado como parametro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productOptional = productoService.get(id);
		producto = productOptional.get();
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model,HttpSession session) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);
		
		//validar producto2 veces
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if(!ingresado) {
			detalles.add(detalleOrden);
		}

				
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}
	
	//quitar un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden:detalles) {
			if(detalleOrden.getProducto().getId()!=id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		//nueva lista
		detalles=ordenesNueva;
		double sumaTotal=0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model,HttpSession session) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		//sesion
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model,HttpSession session) {
		
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		return "usuario/resumenorden";
	}
	
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//Usuario
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		orden.setUsuario(usuario);
		ordenService.save(orden);
				
		//Detalles
		for (DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		//Limpiar
		orden = new Orden();
		detalles.clear();
		return "redirect:/";
	}
	
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre,Model model) {
		log.info("nombre del producto:{}",nombre);
		List<Producto> productos = productoService.findAll().stream().filter( p -> p.getNombre().toLowerCase().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos",productos);
		return "usuario/home";
	}
}























