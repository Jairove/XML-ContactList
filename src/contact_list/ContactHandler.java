package contact_list;

/**
 * Implementación de un manejador todo en uno para el parser SAX
 * Esta clase posee los callbacks (el parser de SAX es push)
 */

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ContactHandler extends DefaultHandler {
	private List<Contact> contactList;
	private Contact contact;
	private EnumKindOfPhone tag;
	private boolean isName, isSurname, isMail, isPhone = false;
	
	@Override
	public void startDocument() throws SAXException {
		contactList = new ArrayList<>();
	};
	
	@Override
	/* Invocado por el evento de inicio de elemento
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
    public void startElement(String uri, String localName, String qName, Attributes atts)
            throws SAXException {

		if(qName.equalsIgnoreCase("persona")){
			contact = new Person();
			String nickname = atts.getValue("nickname");
			contact.setID(nickname);
		} else if(contact instanceof Person) {
			if(qName.equalsIgnoreCase("apellidos")){isSurname=true;}else
			if(qName.equalsIgnoreCase("nombre")){isName=true;}else
			if(qName.equalsIgnoreCase("mail")){isMail=true;}else
			if(qName.equalsIgnoreCase("telefono")){
				String et =  atts.getValue("etiqueta");
				if(et.equals("MovilPersonal"))	tag = EnumKindOfPhone.MovilPersonal;
				else if (et.equals("MovilTrabajo"))	tag = EnumKindOfPhone.MovilTrabajo;
				else if (et.equals("FijoCasa"))	tag = EnumKindOfPhone.FijoCasa;
				else if (et.equals("FijoTrabajo"))	tag = EnumKindOfPhone.FijoTrabajo;
				isPhone=true;
			}
		}
		if(qName.equalsIgnoreCase("grupo")){
			contact = new Group();
			String groupName = atts.getValue("groupname");
			contact.setID(groupName);
		}
		else if (qName.equalsIgnoreCase("miembro")){
			String memberID = atts.getValue("id");
			Contact member = getContactByID(memberID);
			if(contact!=null) {
				((Group) contact).addMember(member);
			}
		}
	 }
	
	 @Override
	 /* Invocado por el evento de fin de elemento
	  * (non-Javadoc)
	  * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	  */
	 public void endElement(String uri, String localName, String qName) throws SAXException {
		 if (qName.equalsIgnoreCase("persona")||qName.equalsIgnoreCase("grupo")) {
			 contactList.add(contact);
	     }
	 }
	 
	 /* Invocado por el evento de aparición de caracteres
	  * (non-Javadoc)
	  * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	  */
	 @Override 
	 public void characters(char ch[], int start, int length) throws SAXException {
		 
		 if(contact instanceof Person){
			 if (isSurname) {
				 ((Person) contact).setSurname(new String(ch, start, length));
				 isSurname = false;
			 }
			 if (isName) {
				 ((Person) contact).setName(new String(ch, start, length));
				 isName = false;
			 }
			 if (isPhone) {
				 ((Person) contact).addPhone((new String(ch, start, length)),tag);
				 isPhone = false;
			 }
			 if (isMail) {
				 ((Person) contact).addMail(new String(ch, start, length));
				 isMail = false;
			 }
		 }
	 }
	 
	 public void warning(SAXParseException e) throws SAXException {
		 System.err.println("Warning: " + e.getMessage());
		 throw new SAXException("Parsing Warning");
	    }
	 
	 public void error(SAXParseException e) throws SAXException {
		 System.err.println("Line " + e.getLineNumber() + ", Column " + e.getColumnNumber() +
		 ". Error: " + e.getMessage());
		 throw new SAXException("Parsing Error");
	 }
	    
	 public void fatalError(SAXParseException e) throws SAXException {
		 System.err.println("Faltal error:" + e.getMessage());
		 throw new SAXException("Parsing fatal error");
	 }
	 

	 /**
	  * Permite obtener un contacto de la lista a partir de un id.
	  * @param id del contacto a obtener
	  * @return contacto deseado
	  */
	 private Contact getContactByID(String id) {
		 for(Contact contact : contactList) {
			 if (contact.getID().equals(id)){
				 return contact;
			 }
		 }
		 throw new IllegalArgumentException("Bad id. "
		 		+ "Un grupo contiene un elemento inexistente o trata de contenerse a si mismo");
	 }

	 /**
	  * Devuelve la lista de contactos que se ha parseado
	  * @return Lista de contactos resultado de parsear
	  */
	 public List<Contact> getContactList() {
		return contactList;
	 }
	 
}
