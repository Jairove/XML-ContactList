package contact_list;

import java.nio.file.Path;
import java.util.Map;

public interface EContactSystemInterface {
	
	/**
	 * Parsea y carga el contenido de un fichero XML en memoria.
	 * @param pathToXML Ruta del fichero.
	 */
	void loadFrom(Path pathToXML);
	
	/**
	 * Vuelca el contenido en memoria a un fichero XML.
	 * @param pathToXML Ruta del fichero.
	 */
	void updateTo(Path pathToXML);
	
	/**
	 * Averigua si hay un fichero XML cargado
	 * @return true si lo está, false en caso contrario.
	 */
	boolean isXMLLoaded();
	
	/**
	 * Averigua si se ha modificado la libreta tras ser cargada.
	 * @return true si lo está, false en caso contrario.
	 */
	boolean isModifiedAfterLoaded();
	
	/**
	 * Crea un nuevo contacto de tipo persona.
	 * @param name Nombre
	 * @param nickname Identificador de la persona
	 * @param surName Apellidos
	 * @param emails Lista de correos
	 * @param phones Mapa de teléfonos
	 */
	void createNewPerson(String name, String nickname, String surName, String[] emails, Map<String,EnumKindOfPhone> phones);
	
	/**
	 * Crea un nuevo contacto de tipo grupo.
	 * @param name Nombre
	 * @param contacts Lista de miembros
	 */
	void createNewGroup(String name, Contact[] contacts); 
	
	/**
	 * Devuelve un contacto a partir de su id.
	 * @param id Identificador del contacto
	 * @return
	 */
	Contact getAnyContactById(String id);
	
	/**
	 * Devuelve un contacto de tipo persona a partir de su id.
	 * @param name Identificador de la persona
	 * @return
	 */
	Person getPersonByNickname(String name);
	
	/**
	 * Devuelve un contacto de tipo grupo a partir de su id.
	 * @param id Identificador del grupo
	 * @return
	 */
	Group getGroupByName(String name);
	
	/**
	 * Añade un contacto a un grupo.
	 * @param contact
	 * @param group
	 */
	void addContactToGroup(Contact contact, Group group);
	
	/**
	 * Elimina un contacto de un grupo.
	 * @param contact
	 * @param group
	 */
	void removeContactFromGroup(Contact contact, Group group); 
	
	/**
	 * Elimina un contacto del sistema.
	 * @param contact
	 */
	void removeContactFromSystem(Contact contact);
}