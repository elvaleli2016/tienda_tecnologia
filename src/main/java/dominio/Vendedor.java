package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
	public static final String EL_PRODUCTO_NO_CUENTA_CON_GARANTIA = "Este producto no cuenta con garantía extendida";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo, String cliente, Date fecha) {
		
		if(this.cantidadVocales(codigo) == 3){ // Si tiene 3 vocales el codigo retorna una exceptión
			throw new GarantiaExtendidaException(EL_PRODUCTO_NO_CUENTA_CON_GARANTIA);			
		}
		if(this.tieneGarantia(codigo)){ // Tiene garantia extendida
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);	
		}
		
		int cantDias = 100;
		Producto producto = repositorioProducto.obtenerPorCodigo(codigo); //Se obtiene el producto
		Double precioGarantia = producto.getPrecio();
		Date fechaFinGarantia = null;

		if(precioGarantia > 500000){
			precioGarantia += precioGarantia*0.2d;
			cantDias = 200;
		}else{
			precioGarantia += precioGarantia*0.1d;
		}

		fechaFinGarantia=calcularFechaFin(cantDias, fecha); //Calcular la fecha Fin
		GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, fecha, fechaFinGarantia, precioGarantia, cliente);
		
		repositorioGarantia.agregar(garantiaExtendida);
		
	}
	/**
	 * Metodo que valida si tiene garantia el producto
	 * @param codigo, de la garantia
	 * @return true si tiene garantia y false si no
	 */
	public boolean tieneGarantia(String codigo) {
		if (repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo) != null) { // si el producto es igual a null
			return true; 
		}
		return false;  
	}
	/**
	 * Metodo que cuenta cuantas vocales contiene el codigo
	 * @param codigo
	 * @return Integer de la cantidad de vocales
	 */
	private int cantidadVocales(String codigo){
		int cant=0; // Inicializa cantodiades de vocales en 0
		for(int i=0; i<codigo.length(); i++){
			// Comparar por vocal si existe alguma coincidencia
			if ((codigo.charAt(i)=='a') || (codigo.charAt(i)=='e') || (codigo.charAt(i)=='i') || (codigo.charAt(i)=='o') || (codigo.charAt(i)=='u')||
				(codigo.charAt(i)=='A') || (codigo.charAt(i)=='E') || (codigo.charAt(i)=='I') || (codigo.charAt(i)=='O') || (codigo.charAt(i)=='U')){
			    cant++;
			  }
		}
		return cant;
	}
	/**
	 * Metodo que retorna el calculo de la fecha limite de la garantia
	 * @param cantDias
	 * @param fechaIni
	 * @return 
	 */
	private Date calcularFechaFin(int cantDias, Date fechaIni){
		int diaSemana;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaIni);
		for(int i=0;i<cantDias; i++){
			diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
			if(diaSemana == Calendar.MONDAY){
				
				calendar.add(Calendar.DATE,2); 
			} else {
				calendar.add(Calendar.DATE,1); 
			}
		}
		diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		if(diaSemana == Calendar.SUNDAY){
			calendar.add(Calendar.DATE,2);
		}else if(diaSemana == calendar.MONDAY){
			calendar.add(Calendar.DATE,1);
		}
		return calendar.getTime();
	}
	
}
