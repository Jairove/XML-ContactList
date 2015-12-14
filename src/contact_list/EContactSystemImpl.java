package contact_list;

/**
 * Implementación de la interfaz EContactSystemInterface.
 */

import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;

import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EContactSystemImpl implements EContactSystemInterface {
	private List<Contact> contactList;
	private Path dtdPath = Paths.get("src","libreta.dtd");
	private boolean isModified, isLoaded;
	
	public EContactSystemImpl() {
		
	}
	
	public void loadFrom(Path path){
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
	        factory.setNamespaceAware(true);
	        SAXParser parser = factory.newSAXParser();
	        contactList = new ArrayList<>();

			FileReader input = new FileReader(path.toString());
			InputSource source = new InputSource(input);
			ContactHandler handler = new ContactHandler();
			parser.parse(source,handler); 
			contactList = handler.getContactList();
			isModified = false;
			isLoaded = true;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			isLoaded = false;
		}
		catch(IOException e){
			e.printStackTrace();
			isLoaded = false;
		}
		catch(SAXException e){
			e.printStackTrace();
			isLoaded = false;
		} 
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			isLoaded = false;
		}
	}
	
	public void updateTo(Path pathToXML){
		StringWriter sw = new StringWriter();
		XMLOutputFactory xof = XMLOutputFactory.newInstance();

		try
		{
		  XMLStreamWriter writer = xof.createXMLStreamWriter(sw);
		  writer.writeStartDocument("UTF-8", "1.0");
		  writer.writeStartElement("libreta");
		  
		  for(Contact contact : contactList) {
			  if(contact instanceof Person){
				  
				  	writer.writeStartElement("persona");
					  	writer.writeAttribute("nickname", contact.getID());
						writer.writeStartElement("apellidos");
							writer.writeCharacters(((Person) contact).getSurname());
						writer.writeEndElement();
						writer.writeStartElement("nombre");
							writer.writeCharacters(((Person) contact).getName());
					writer.writeEndElement();
					
					List<String> mailList = ((Person) contact).getMailList();
					for(String mail : mailList) {
						writer.writeStartElement("mail");
							writer.writeCharacters(mail);
						writer.writeEndElement();
					}
					
					Map<String,EnumKindOfPhone> phone = ((Person) contact).getPhoneMap();
					for (Map.Entry<String,EnumKindOfPhone> p : phone.entrySet()) {
						writer.writeStartElement("telefono");
							writer.writeAttribute("etiqueta", p.getValue().toString());
							writer.writeCharacters(p.getKey());
						writer.writeEndElement();
					}
					
				writer.writeEndElement();
			  }
			  
			  if(contact instanceof Group) {
				  writer.writeStartElement("grupo");
				  	writer.writeAttribute("groupname", contact.getID());
				  	List <Contact> memberList = ((Group) contact).getMembers();
				  	for(Contact member : memberList) {
						writer.writeStartElement("miembro");
							writer.writeAttribute("id", member.getID());
						writer.writeEndElement();
					}
				 writer.writeEndElement();
			  }
		  }
			writer.writeEndElement();
			
			writer.flush();
			writer.close();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,dtdPath.toString());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    
		    transformer.transform(new StreamSource(new StringReader(sw.toString())), 
		    new StreamResult(new FileOutputStream(pathToXML.toString(),false)));

		}
		
		catch (XMLStreamException e)
		{
		  e.printStackTrace();
		}
		catch (IOException ie)
		{
		  ie.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isXMLLoaded(){
		return isLoaded;
	}
	
	public boolean isModifiedAfterLoaded(){
		return isModified;
	}
	
	public void createNewPerson(String name, String nickname, String surName, String[] emails, 
	Map<String,EnumKindOfPhone> phones) {
		Person person = new Person(name,nickname,surName,emails,phones);
		contactList.add(person);
		isModified = true;
	}
	
	public void createNewGroup(String name, Contact[] contacts) {
		Group group = new Group(name,contacts);
		contactList.add(group);
		isModified = true;
	} 
	
	public Contact getAnyContactById(String id){
		for(Contact c : contactList){
			if(c.getID().equals(id)) return c;
		}
		throw new IllegalArgumentException("bad id");
	}
	
	public Person getPersonByNickname(String name){
		Contact c = getAnyContactById(name);
		if (c instanceof Person) {
			Person p = (Person) c;
			return p;
		}
		throw new IllegalArgumentException("bad person name");
	}
	public Group getGroupByName(String name) {
		Contact c = getAnyContactById(name);
		if (c instanceof Group) {
			Group g = (Group) c;
			return g;
		}
		throw new IllegalArgumentException("bad group name");
	}
	public void addContactToGroup(Contact contact, Group group) {
		group.addMember(contact);
		isModified = true;
	}
	public void removeContactFromGroup(Contact contact, Group group) {
		if(group.getMembers().size()>1){
			group.removeMember(contact);
			isModified = true;
		}
		else{
			throw new IllegalArgumentException("Can't remove a member from a group when "
					+ "its the single contact in that group");
		}
		
	}
	public void removeContactFromSystem(Contact contact) {
		contactList.remove(contact);
		for(Contact c : contactList) {
			if(c instanceof Group) {
				List<Contact> members = ((Group) c).getMembers();
				for (int i=0;i<members.size();i++) {
					if(members.get(i)==contact) {
						((Group) c).removeMember(members.get(i));
					}
				}
			}
		}
		isModified = true;
	}
	
}
