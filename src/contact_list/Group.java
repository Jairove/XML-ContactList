package contact_list;

/**
 * Clase que representa a los contactos de tipo grupo.
 */
import java.util.ArrayList;
import java.util.List;

public class Group implements Contact {
	private String id;
	private List <Contact> members;
	
	/**
	 * Permite crear un grupo sin especificar inicialmente sus miembros
	 */
	public Group() {
		members = new ArrayList<>();
	}
	
	public Group(String name, Contact[] contacts) {
		this();
		id = name;
		for(Contact c : contacts) {
			members.add(c);
		}
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String groupname) {
		id = groupname;
	}
	
	public List<Contact> getMembers() {
		return members;
	}
	
	/**
	 * Añade un contacto al grupo.
	 * @param member Contacto que se va a añadir
	 */
	public void addMember(Contact member) {
		members.add(member);
	}
	
	/**
	 * Eliminar un contacto del grupo.
	 * @param member Contacto a eliminar
	 */
	public void removeMember(Contact member) {
		members.remove(member);
	}
}
