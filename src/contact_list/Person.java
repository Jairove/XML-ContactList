package contact_list;

/**
 * Clase que representa a los contactos de tipo persona.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person implements Contact {
	private String surname, name, id;
	private List<String> mailList;
	Map<String,EnumKindOfPhone> phoneMap;

	/**
	 * Permite crear una persona sin especificar sus atributos
	 */
	public Person() {
		mailList = new ArrayList<>();
		phoneMap = new HashMap<String,EnumKindOfPhone>();
	}
	
	/**
	 * Constructor de personas dados sus atributos
	 * @param name Nombre
	 * @param id Identificador de la persona
	 * @param surname Apellidos
	 * @param mailList Lista de correos
	 * @param phoneMap Mapa de teléfonos
	 */
	public Person(String name, String id, String surname,
	String[] mailList,Map<String,EnumKindOfPhone> phoneMap) {
		this();
		this.surname = surname;
		this.name = name;
		this.id = id;
		this.phoneMap = phoneMap;
		for(String e : mailList) {
			this.mailList.add(e);
		}
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getID() {
		return id;
	}
	public void setID(String nickname) {
		id = nickname;
	}
	
	public List<String> getMailList() {
		return mailList;
	}
	
	/**
	 * Permite añadir un correo a la lista
	 * @param mail Correo a añadir
	 */
	public void addMail(String mail) {
		mailList.add(mail);
	}

	public Map<String,EnumKindOfPhone> getPhoneMap() {
		return phoneMap;
	}

	public void setPhoneMap(Map<String,EnumKindOfPhone> phoneMap) {
		this.phoneMap = phoneMap;
	}
	
	/**
	 * Permite añadir un teléfono al mapa
	 * @param key Número de teléfono
	 * @param value Etiqueta
	 */
	public void addPhone(String key, EnumKindOfPhone value) {
		phoneMap.put(key, value);
	}
	
}
